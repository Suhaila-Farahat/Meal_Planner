package com.example.mealplanner.auth.login.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.mealplanner.MainActivity;
import com.example.mealplanner.R;
import com.example.mealplanner.auth.login.presenter.LoginPresenter;
import com.example.mealplanner.auth.login.presenter.LoginPresenterImpl;
import com.example.mealplanner.auth.signup.view.SignUpActivity;
import com.example.mealplanner.util.AuthUtils;

public class LoginActivity extends AppCompatActivity implements LoginView {

    private EditText email, password;
    private Button loginBtn, signUpTextButton;
    private LoginPresenter presenter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.emailEditTextLogin);
        password = findViewById(R.id.passwordEditTextLogin);
        loginBtn = findViewById(R.id.loginButton);
        signUpTextButton = findViewById(R.id.signUpTextButtonLogin);

        presenter = new LoginPresenterImpl(this, this);

        loginBtn.setOnClickListener(v -> {
            String emailStr = email.getText().toString().trim();
            String passStr = password.getText().toString().trim();
            presenter.login(emailStr, passStr);
        });

        signUpTextButton.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void showLoginSuccess(String message) {
        String userId = "sample_user_id";

        AuthUtils.saveLoginState(this, true, userId);

        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    public void showLoginError(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }
}
