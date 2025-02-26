package com.example.mealplanner.start.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import com.example.mealplanner.MainActivity;
import com.example.mealplanner.R;
import com.example.mealplanner.SessionManager;
import com.example.mealplanner.auth.login.view.LoginActivity;
import com.example.mealplanner.start.presenter.StartPresenter;
import com.example.mealplanner.start.presenter.StartPresenterImpl;
import com.example.mealplanner.util.AuthUtils;

public class StartActivity extends AppCompatActivity implements StartView {

    private StartPresenter presenter;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_start);

        presenter = new StartPresenterImpl(this, this);
        sessionManager = new SessionManager(this);

        if (AuthUtils.isUserLoggedIn(this)) {
            navigateToMain();
        }

        Button btnSignUpEmail = findViewById(R.id.btn_signup_email);
        Button btnSignUpGoogle = findViewById(R.id.btn_signup_google);
        Button btnGuest = findViewById(R.id.btn_guest);

        btnSignUpEmail.setOnClickListener(view -> {
            sessionManager.setGuestMode(false);
            presenter.handleEmailSignUp();
        });

        btnSignUpGoogle.setOnClickListener(view -> {
            sessionManager.setGuestMode(false);
            presenter.handleGoogleSignIn();
        });

        btnGuest.setOnClickListener(view -> {
            sessionManager.setGuestMode(true);
            presenter.handleGuestLogin();
        });
    }

    @Override
    public void navigateToLogin() {
        sessionManager.setGuestMode(false);
        startActivity(new Intent(StartActivity.this, LoginActivity.class));
    }

    @Override
    public void navigateToMain() {
        Intent intent = new Intent(StartActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void showGoogleSignInError(String message) {
        Toast.makeText(this, "Google Sign-In Failed: " + message, Toast.LENGTH_SHORT).show();
    }
}
