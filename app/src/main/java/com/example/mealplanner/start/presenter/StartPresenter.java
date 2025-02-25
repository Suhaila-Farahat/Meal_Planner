package com.example.mealplanner.start.presenter;

public interface StartPresenter {
    void handleEmailSignUp();
    void handleGuestLogin();
    void handleGoogleSignIn();
    void handleGoogleSignInResult(int requestCode, int resultCode, android.content.Intent data);
}

