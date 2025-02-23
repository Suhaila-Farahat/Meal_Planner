package com.example.mealplanner.auth.login.presenter;

public interface LoginPresenter {
    void login(String email, String password);
    void onDestroy();
}
