package com.example.mealplanner.auth.signup.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mealplanner.MainActivity;
import com.example.mealplanner.R;
import com.example.mealplanner.auth.signup.presenter.SignUpPresenter;
import com.example.mealplanner.auth.signup.presenter.SignUpPresenterImpl;
import com.example.mealplanner.util.AuthUtils;

public class SignUpActivity extends AppCompatActivity implements SignUpView {

    private EditText email, username, password, confirmPassword;
    private Button signUp;
    private SignUpPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        email = findViewById(R.id.emailEditText);
        username = findViewById(R.id.userNameEditText);
        password = findViewById(R.id.passwordEditText);
        confirmPassword = findViewById(R.id.confirmPasswordEditText);
        signUp = findViewById(R.id.signUpButton);

        presenter = new SignUpPresenterImpl(this);

        signUp.setOnClickListener(v -> {
            String emailStr = email.getText().toString().trim();
            String userStr = username.getText().toString().trim();
            String passStr = password.getText().toString().trim();
            String confirmStr = confirmPassword.getText().toString().trim();

            presenter.signUp(emailStr, userStr, passStr, confirmStr);
        });
    }

    @Override
    public void showSignUpSuccess(String message) {
        String userId = "sample_user_id";

        AuthUtils.saveLoginState(this, true, userId);

        startActivity(new Intent(SignUpActivity.this, MainActivity.class));
        finish();
    }

    @Override
    public void showSignUpError(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }
}
