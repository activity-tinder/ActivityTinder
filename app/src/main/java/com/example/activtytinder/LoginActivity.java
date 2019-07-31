package com.example.activtytinder;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsernameInput;
    private EditText etPasswordInput;
    private Button btnLogin;
    private Button btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ParseUser currentUser = ParseUser.getCurrentUser();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        etUsernameInput = findViewById(R.id.etUsername);
        etPasswordInput = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnSignUp = findViewById(R.id.btnSignup);

        if (currentUser != null) {
            final Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        btnLogin.setOnClickListener(view -> {
            final String username = etUsernameInput.getText().toString();
            final String password = etPasswordInput.getText().toString();
            login(username, password);
        });

        btnSignUp.setOnClickListener(view -> {
            goToSignUpScreen();
        });
    }

    /**
     * Signs the user into their account by verifying the username and password supplied with the
     * Parse database information.
     * @param username - String for the user's username input
     * @param password - String for the user's password input
     */
    private void login(String username, String password){
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e == null){
                    Log.d("Login Activity", "Login successful!");
                    final Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Log.e("Login Activity", "Login failure!");
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Fires and intent to take the user from the login screen to the SignUp screen for them to set
     * up their new account.
     */
    public void goToSignUpScreen() {
        final Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
        startActivity(intent);
        finish();
    }
}
