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

    private EditText usernameInput;
    private EditText passwordInput;
    private Button loginBtn;
    private Button signUpBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ParseUser currentUser = ParseUser.getCurrentUser();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        usernameInput = findViewById(R.id.username_et);
        passwordInput = findViewById(R.id.password_et);
        loginBtn = findViewById(R.id.login_btn);
        signUpBtn = findViewById(R.id.signup_btn);

        // account persistence
        if (currentUser != null) {
            final Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            //start the intent that switches to home activity to in turn go to the home activity
            startActivity(intent);
            finish();
        }


        loginBtn.setOnClickListener(view -> {
            final String username = usernameInput.getText().toString();
            final String password = passwordInput.getText().toString();

            login(username, password);

        });

        signUpBtn.setOnClickListener(view -> {
            signUpScreen();
        });
    }

    private void login(String username, String password){
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e == null){
                    Log.d("Login Activity", "Login successful!");
                    //intent to switch over to the home activity
                    final Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    //start the intent that switches to home activity to in turn go to the home activity
                    startActivity(intent);
                    finish();//prevents user from hitting back to log out, we don't want logout with back
                } else {
                    Log.e("Login Activity", "Login failure!");
                    e.printStackTrace();
                }
            }
        });
    }

    public void signUpScreen() {
        // Create the ParseUser
        //ParseUser user = new ParseUser();
        //final String username = usernameInput.getText().toString();
        //final String password = passwordInput.getText().toString();
        // Set core properties
        //user.setUsername(username);
        //user.setPassword(password);
        // Invoke signUpInBackground
//        user.signUpInBackground(e -> {
//            if (e == null) {
//                // Hooray! Let them use the app now.
//                Log.d("Login Activity", "Sign up successful!");
//                //intent to switch over to the home activity
                final Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                //start the intent that switches to home activity to in turn go to the home activity
                startActivity(intent);
               //prevents user from hitting back to log out, we don't want logout with back
//            } else {
//                // Sign up didn't succeed. Look at the ParseException
//                // to figure out what went wrong
//                Log.e("Login Activity", "Sign up failure!");
//                e.printStackTrace();
//            }
//        });
    }
}
