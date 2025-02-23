package com.example.mealplanner.auth.signup.presenter;

import android.text.TextUtils;

import com.example.mealplanner.auth.signup.view.SignUpView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;

public class SignUpPresenterImpl implements SignUpPresenter {

    private SignUpView view;
    private FirebaseAuth mAuth;
    private CompositeDisposable disposables = new CompositeDisposable();

    public SignUpPresenterImpl(SignUpView view) {
        this.view = view;
        this.mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void signUp(String email, String username, String password, String confirmPassword) {
        if (view == null) return;

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(username)
                || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
            view.showSignUpError("Please fill all fields");
            return;
        }
        if (!password.equals(confirmPassword)) {
            view.showSignUpError("Passwords do not match");
            return;
        }


        disposables.add(
                createUser(email, password)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                user -> {
                                    view.showSignUpSuccess("Sign up successful: " + user.getEmail());
                                },
                                throwable -> {
                                    view.showSignUpError(throwable.getMessage());
                                }
                        )
        );
    }

    private Single<FirebaseUser> createUser(String email, String password) {
        return Single.create(emitter ->
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                if (user != null) {
                                    emitter.onSuccess(user);
                                } else {
                                    emitter.onError(new Exception("Sign up failed: User is null"));
                                }
                            } else {
                                emitter.onError(task.getException());
                            }
                        })
        );
    }

    @Override
    public void onDestroy() {
        disposables.clear();
        view = null;
    }
}
