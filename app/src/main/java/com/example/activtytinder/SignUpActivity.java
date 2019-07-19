package com.example.activtytinder;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Looper;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import permissions.dispatcher.NeedsPermission;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

public class SignUpActivity  extends AppCompatActivity {

    private EditText nameOfPerson;
    private EditText email;
    private EditText birthday;
    private EditText baseLocation;
    private EditText usernameInput;
    private EditText passwordInput;
    private Button createAccount;
    public Button getLocationButton;
    private DatePickerDialog picker;
    private LocationRequest mLocationRequest;
    double baseLat;
    double baseLong;
//    double[] mLocation;
    String TAG = "Sign Up Activity";

    //TODO Investigate activity destruction/activity clearing

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        nameOfPerson = (EditText) findViewById(R.id.etName);
        email = (EditText) findViewById(R.id.etEmail);
        birthday = (EditText) findViewById(R.id.etBirthday);
        baseLocation = (EditText) findViewById(R.id.etBaseLocation);
        usernameInput = (EditText) findViewById(R.id.etUsername);
        passwordInput = (EditText) findViewById(R.id.etPassword);
        createAccount = (Button) findViewById(R.id.btnCreateUser);
        getLocationButton = (Button) findViewById(R.id.get_location_btn);

        //TODO test with a breakpoint, if we keep hitting it, we do not need to clear the edit texts


        birthday.setInputType(InputType.TYPE_NULL);
        birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                picker = new DatePickerDialog(SignUpActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        birthday.setText((monthOfYear+1) + "/" + dayOfMonth + "/" + year);
                    }
                }, year, month, day);
                picker.show();
            }
        });

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Birthday = birthday.getText().toString();
                String deliveryDate = Birthday;
                SimpleDateFormat dateFormatprev = new SimpleDateFormat("MM/dd/yyyy");
                Date d = null;
                try {
                    d = dateFormatprev.parse(deliveryDate);
                } catch (java.text.ParseException e) {
                    e.printStackTrace();
                }
                final String Name = nameOfPerson.getText().toString();
                final String Email = email.getText().toString();
                final Date finalBirthday = d;
                //final String BaseLocation = baseLocation.getText().toString();
                baseLocation.setText(baseLat +", " + baseLong);


                ParseGeoPoint gpBaseLocation = new ParseGeoPoint(baseLat,baseLong);
                final String Username = usernameInput.getText().toString();
                final String Password = passwordInput.getText().toString();
                makeAccount(Name, Email, finalBirthday, gpBaseLocation, Username, Password);
            }
        });

        getLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCurrentLocation();
                //baseLocation.setText(baseLat +", " + baseLong);
            }
        });
    }

    private void makeAccount(String Name, String Email, Date Birthday, ParseGeoPoint BaseLocation, String Username, String Password){
        ParseUser user = new ParseUser();
        user.setUsername(Username);
        user.setPassword(Password);
        user.setEmail(Email);
        user.put("name", Name);
        user.put("location", BaseLocation);
        user.put("birthday", Birthday);
        user.put("reliabilityScore", 100);
        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    Toast.makeText(getApplicationContext(), "Sign Up Successful!", Toast.LENGTH_SHORT).show();
                    final Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                    nameOfPerson.setText("");
                    email.setText("");
                    birthday.setText("");
                    baseLocation.setText("");
                    usernameInput.setText("");
                    passwordInput.setText("");
                    baseLocation.setText("");
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Sign Up Failed!", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
    }

    //TODO Clean up this function, do not have it in 2 places
    @SuppressLint({"MissingPermission", "NewApi"})
    @NeedsPermission({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    public void getCurrentLocation() {
        // Create the location request to start receiving updates
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        LocationSettingsRequest locationSettingsRequest = builder.build();

        SettingsClient settingsClient = LocationServices.getSettingsClient(SignUpActivity.this);
        settingsClient.checkLocationSettings(locationSettingsRequest);

        if (SignUpActivity.this.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(SignUpActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    android.Manifest.permission.ACCESS_FINE_LOCATION}, 20);
        }else {
            Log.e(TAG, "PERMISSION GRANTED");
        }
        Log.e(TAG, String.valueOf(locationSettingsRequest));
        getFusedLocationProviderClient(SignUpActivity.this).requestLocationUpdates(mLocationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        //onLocationChanged(locationResult.getLastLocation());
                        String msg = "Location: " +
                                Double.toString(locationResult.getLastLocation().getLatitude()) + "," +
                                Double.toString(locationResult.getLastLocation().getLongitude());
                        Toast.makeText(SignUpActivity.this, msg, Toast.LENGTH_SHORT).show();
                        baseLocation.setText(Double.toString(locationResult.getLastLocation().getLatitude())+", "
                                +Double.toString(locationResult.getLastLocation().getLongitude()));
                        baseLat = locationResult.getLastLocation().getLatitude();
                        baseLong = locationResult.getLastLocation().getLongitude();
                    }
                },
                Looper.myLooper());
    }
}
