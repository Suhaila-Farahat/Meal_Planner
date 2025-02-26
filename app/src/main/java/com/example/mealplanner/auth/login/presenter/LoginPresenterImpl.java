package com.example.mealplanner.auth.login.presenter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;

import com.example.mealplanner.BackupManager;
import com.example.mealplanner.auth.login.view.LoginView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;

public class LoginPresenterImpl implements LoginPresenter {

    private LoginView view;
    private FirebaseAuth mAuth;
    private Context context;

    public LoginPresenterImpl(LoginView view ,Context context) {
        this.view = view;
        this.mAuth = FirebaseAuth.getInstance();
        this.context =context;
    }

    @SuppressLint("CheckResult")
    @Override
    public void login(String email, String password) {
        if (view == null) return;

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            view.showLoginError("Please fill all fields");
            return;
        }

        signIn(email, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        user -> {
                            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                            if (currentUser != null) {
                                String uid = currentUser.getUid();
                                BackupManager backupManager = new BackupManager(context);
                                backupManager.restoreData(uid);

                            }
                            view.showLoginSuccess("Welcome back, " + user.getEmail());
                        },
                        throwable -> {
                            view.showLoginError(throwable.getMessage());
                        }
                );
    }

    private Single<FirebaseUser> signIn(String email, String password) {
        return Single.create(emitter ->
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                if (user != null) {
                                    emitter.onSuccess(user);
                                } else {
                                    emitter.onError(new Exception("Login failed: User is null"));
                                }
                            } else {
                                emitter.onError(task.getException());
                            }
                        })
        );
    }

    @Override
    public void onDestroy() {

        view = null;
    }
}
