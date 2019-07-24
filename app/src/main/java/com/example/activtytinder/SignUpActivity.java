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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    RequestQueue requestQueue;
    ParseGeoPoint BaseLocationCoordinates;
    String url ="https://open.mapquestapi.com/geocoding/v1/reverse?key=";
    String Location = "&location=";
    String TAG = "Sign Up Activity";
    String API_KEY;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        nameOfPerson = (EditText) findViewById(R.id.etEventName);
        email = (EditText) findViewById(R.id.etEmail);
        birthday = (EditText) findViewById(R.id.etBirthday);
        baseLocation = (EditText) findViewById(R.id.etBaseLocation);
        usernameInput = (EditText) findViewById(R.id.etUsername);
        passwordInput = (EditText) findViewById(R.id.etPassword);
        createAccount = (Button) findViewById(R.id.btnCreateUser);
        getLocationButton = (Button) findViewById(R.id.get_location_btn);
        getLocationButton.setVisibility(View.INVISIBLE);
        requestQueue = Volley.newRequestQueue(SignUpActivity.this);
        API_KEY = getApplicationContext().getResources().getString(R.string.mapquest_api_key);



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

        baseLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLocationButton.setVisibility(View.VISIBLE);

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
                final Date finalBirthday = d;
                final String Name = nameOfPerson.getText().toString();
                final String Email = email.getText().toString();
                final String Username = usernameInput.getText().toString();
                final String Password = passwordInput.getText().toString();
                final ParseGeoPoint BaseCoordinates = BaseLocationCoordinates;
                final String HomeCity = baseLocation.getText().toString();
                makeAccount(Name, Email, finalBirthday, BaseCoordinates, Username, Password, HomeCity);
            }
        });

        getLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCurrentLocation();
                getLocationButton.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void makeAccount(String Name, String Email, Date Birthday, ParseGeoPoint BaseLocation, String Username, String Password, String HomeCity){
        ParseUser user = new ParseUser();
        user.setUsername(Username);
        user.setPassword(Password);
        user.setEmail(Email);
        user.put("name", Name);
        user.put("location", BaseLocation);
        user.put("birthday", Birthday);
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

    @SuppressLint({"MissingPermission", "NewApi"})
    @NeedsPermission({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    public void getCurrentLocation() {
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
                        String msg = "Location: " +
                                (locationResult.getLastLocation().getLatitude()) + "," +
                                (locationResult.getLastLocation().getLongitude());
                        Toast.makeText(SignUpActivity.this, msg, Toast.LENGTH_SHORT).show();
                        String coordinates = (locationResult.getLastLocation().getLatitude()+","
                                +locationResult.getLastLocation().getLongitude());
                        baseLat = locationResult.getLastLocation().getLatitude();
                        baseLong = locationResult.getLastLocation().getLongitude();
                        BaseLocationCoordinates = new ParseGeoPoint(baseLat,baseLong);
                        JsonObjectRequest cityRequest = new JsonObjectRequest(Request.Method.GET, url+API_KEY+Location+coordinates, null,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        Log.d(TAG, response.toString());
                                        try {
                                            JSONArray results = response.getJSONArray("results");
                                            JSONArray locations = results.getJSONObject(0).getJSONArray("locations");
                                            String city = locations.getJSONObject(0).get("adminArea5").toString();
                                            baseLocation.setText(city);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Log.e(TAG,"Error with response");
                                    }
                                });
                        requestQueue.add(cityRequest);
                    }
                }, Looper.myLooper());
    }



}
