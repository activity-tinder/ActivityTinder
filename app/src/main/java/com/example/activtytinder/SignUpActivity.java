package com.example.activtytinder;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.activtytinder.Models.Tools;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SignUpActivity  extends AppCompatActivity {

    private EditText etFullName;
    private EditText etEmail;
    private EditText etBirthday;
    private EditText etBaseLocation;
    private EditText etUsernameInput;
    private EditText etPasswordInput;
    private Button btnCreateAccount;
    public Button btnGetLocation;
    RequestQueue requestQueue;
    String API_KEY;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        etFullName = findViewById(R.id.etEventName);
        etEmail = findViewById(R.id.etEmail);
        etBirthday = findViewById(R.id.etBirthday);
        etBaseLocation = findViewById(R.id.etBaseLocation);
        etUsernameInput = findViewById(R.id.etUsername);
        etPasswordInput = findViewById(R.id.etPassword);
        btnCreateAccount = findViewById(R.id.btnCreateUser);
        btnGetLocation = findViewById(R.id.get_location_btn);
        requestQueue = Volley.newRequestQueue(SignUpActivity.this);
        API_KEY = getApplicationContext().getResources().getString(R.string.mapquest_api_key);

        etBirthday.setInputType(InputType.TYPE_NULL);
        etBirthday.setOnClickListener(btnBirthday -> Tools.getDate(SignUpActivity.this, etBirthday));

        btnGetLocation.setOnClickListener(btnGetLocation -> {
            String search = etBaseLocation.getText().toString();
            LocationManager.get().getLocationAddress(search, API_KEY, etBaseLocation, SignUpActivity.this);
        });

        btnCreateAccount.setOnClickListener(btnCreateAccount -> {
            String Birthday = etBirthday.getText().toString();
            String deliveryDate = Birthday;
            SimpleDateFormat dateFormatprev = new SimpleDateFormat("MM/dd/yyyy");
            Date birthday = null;
            try {
                birthday = dateFormatprev.parse(deliveryDate);
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }
            final Date finalBirthday = birthday;
            final String Name = etFullName.getText().toString();
            final String Email = etEmail.getText().toString();
            final String Username = etUsernameInput.getText().toString();
            final String Password = etPasswordInput.getText().toString();
            final ParseGeoPoint BaseCoordinates = LocationManager.get().getLocationCoordinates();
            final String HomeCity = LocationManager.get().getCity();
            makeAccount(Name, Email, finalBirthday, BaseCoordinates, Username, Password, HomeCity);
        });
    }

    private void makeAccount(String Name, String Email, Date Birthday, ParseGeoPoint BaseLocation, String Username, String Password, String HomeCity){
        ParseUser user = new ParseUser();
        user.setUsername(Username);
        user.setPassword(Password);
        user.setEmail(Email);
        user.put("name", Name);
        user.put("location", BaseLocation);
        user.put("etBirthday", Birthday);
        user.put("reliabilityScore", 100);
        user.put("homeCity", HomeCity);
        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    Toast.makeText(getApplicationContext(), "Sign Up Successful!", Toast.LENGTH_SHORT).show();
                    final Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Sign Up Failed!", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
    }
}
