package com.example.mealplanner.start.presenter;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mealplanner.R;
import com.example.mealplanner.start.view.StartView;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class StartPresenterImpl implements StartPresenter {
    private static final int RC_SIGN_IN = 100;
    private StartView view;
    private FirebaseAuth mAuth;
    private SignInClient oneTapClient;
    private BeginSignInRequest signInRequest;

    public StartPresenterImpl(StartView view, AppCompatActivity activity) {
        this.view = view;
        this.mAuth = FirebaseAuth.getInstance();

        oneTapClient = Identity.getSignInClient(activity);
        signInRequest = BeginSignInRequest.builder()
                .setGoogleIdTokenRequestOptions(
                        BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                                .setSupported(true)
                                .setServerClientId(activity.getString(R.string.default_web_client_id))
                                .setFilterByAuthorizedAccounts(false)
                                .build())
                .build();
    }

    @Override
    public void handleEmailSignUp() {
        view.navigateToLogin();
    }

    @Override
    public void handleGuestLogin() {
        view.navigateToMain();
    }

    @Override
    public void handleGoogleSignIn() {
        oneTapClient.beginSignIn(signInRequest)
                .addOnSuccessListener(result -> {
                    try {
                        ((AppCompatActivity) view).startIntentSenderForResult(
                                result.getPendingIntent().getIntentSender(), RC_SIGN_IN,
                                null, 0, 0, 0
                        );
                    } catch (Exception e) {
                        Log.e("GoogleSignIn", "Error starting intent", e);
                    }
                })
                .addOnFailureListener(e -> view.showGoogleSignInError(e.getMessage()));
    }

    @Override
    public void handleGoogleSignInResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_SIGN_IN) {
            try {
                SignInCredential credential = oneTapClient.getSignInCredentialFromIntent(data);
                String idToken = credential.getGoogleIdToken();
                if (idToken != null) {
                    firebaseAuthWithGoogle(idToken);
                }
            } catch (Exception e) {
                Log.e("GoogleSignIn", "Sign-in failed", e);
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText((AppCompatActivity) view, "Welcome " + user.getDisplayName(), Toast.LENGTH_SHORT).show();
                        view.navigateToMain();
                    } else {
                        view.showGoogleSignInError("Authentication Failed");
                    }
                });
    }
}
