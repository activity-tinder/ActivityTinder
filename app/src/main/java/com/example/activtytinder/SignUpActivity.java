package com.example.activtytinder;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.oob.SignUp;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This class contains screen and functionality for the user to create and register their account
 * for the app with the Parse data base. The user supplies their information through the edit texts
 * which are wired to the appropriate xml file and then the button listeners await for the user
 * to submit certain date ultimately leading to the makeAccount function which inputs the user's
 * data into the Parse data base and fires an intent to the home card fragment inside the main
 * activity.
 */
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

        btnListeners();
    }

    @Override
    public void onBackPressed() {
        final Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * When called, takes all the values from the edit texts that user filled out and puts them on the Parse database after
     * verifying that all fields have an appropriate value in them. The method then fires an intent to take the newly registered
     * user to the MainActivity where they will then see the cards in the CardFragment.
     * @param Name - String of the user's name
     * @param Email - String of the user's email
     * @param Birthday - Date of the user's birthday
     * @param BaseLocation - ParseGeoPoint that gives the latitude and longitude coordinates of the user's base location
     * @param Username - String for the user's selected username
     * @param Password - String for the user to input their password
     * @param HomeCity - String for the user's home city that will be displayed in their profile not with their address
     */
    private void makeAccount(String Name, String Email, Date Birthday, ParseGeoPoint BaseLocation, String Username, String Password, String HomeCity){
        if (Name.equals("") || Email.equals("") || Birthday == null || BaseLocation == null || Username.equals("")
                || Password.equals("") || !HomeCity.equals(LocationManager.get().getCorrectAddress()))
        {
            Toast.makeText(SignUpActivity.this, "ERROR IN REQUIRED FIELD! REVIEW YOUR PROFILE!",Toast.LENGTH_SHORT).show();
            return;
        }
        ParseUser user = new ParseUser();
        user.setUsername(Username);
        user.setPassword(Password);
        user.setEmail(Email);
        user.put("name", Name);
        user.put("location", BaseLocation);
        user.put("birthday", Birthday);
        user.put("reliabilityScore", 100);
        user.put("homeCity", LocationManager.get().getCity());
        user.signUpInBackground(e -> {
            if (e == null) {
                Toast.makeText(getApplicationContext(), "Sign Up Successful!", Toast.LENGTH_SHORT).show();
                final Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "Sign Up Failed!", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        });
    }

    /**
     * Initializes buttons in this activity.
     */
    private void btnListeners(){
        btnGetLocation.setOnClickListener(btnGetLocation -> {
            String search = etBaseLocation.getText().toString();
            LocationManager.get().getLocationAddress(search, API_KEY, etBaseLocation, SignUpActivity.this);
        });

        btnCreateAccount.setOnClickListener(btnCreateAccount -> {
            if (LocationManager.get().getCity() == null) {
                Toast.makeText(SignUpActivity.this, "Please enter valid Address", Toast.LENGTH_SHORT).show();
                return;
            }
            String deliveryDate = etBirthday.getText().toString();
            String convertedBirthday = Tools.convertDate(deliveryDate);
            @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
            Date birthday = null;
            try {
                birthday = dateFormat.parse(convertedBirthday);
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }

            final String Name = etFullName.getText().toString();
            final String Email = etEmail.getText().toString();
            final String Username = etUsernameInput.getText().toString();
            final String Password = etPasswordInput.getText().toString();
            final ParseGeoPoint BaseCoordinates = LocationManager.get().getLocationCoordinates();
            final String HomeCity = etBaseLocation.getText().toString();
            makeAccount(Name, Email, birthday, BaseCoordinates, Username, Password, HomeCity);
        });
    }
}
