package com.example.mealplanner.auth.signup.presenter;

public interface SignUpPresenter {
    void signUp(String email, String username, String password, String confirmPassword);
    void onDestroy();
}