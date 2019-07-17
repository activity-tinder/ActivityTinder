package com.example.activtytinder.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.activtytinder.LoginActivity;
import com.example.activtytinder.R;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.parse.ParseUser;

import permissions.dispatcher.NeedsPermission;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

public class ProfileFragment extends Fragment {

    public Button logoutButton;
    public Button getLocationButton;
    public TextView tvName;
    public TextView tvUsername;
    public TextView tvEmail;
    public TextView tvScore;
    public TextView tvAge;
    public TextView tvBaseLocation;
    public static final String TAG = "ProfileFragment";
    private LocationRequest mLocationRequest;

    //onCreateView to inflate the view
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false); //returns appropriate view
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        logoutButton = view.findViewById(R.id.logout_btn);
        getLocationButton = view.findViewById(R.id.get_location_btn);
        tvName = view.findViewById(R.id.tvName);
        tvUsername = view.findViewById(R.id.tvUsername);
        tvEmail = view.findViewById(R.id.tvEmail);
        tvScore = view.findViewById(R.id.tvScore);
        tvAge = view.findViewById(R.id.tvAge);
        tvBaseLocation = view.findViewById(R.id.tvBaseLocation);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });
        getLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCurrentLocation();
            }
        });

    }

    private void logout() {
        ParseUser.logOut();
        ParseUser currentUser = ParseUser.getCurrentUser(); // this will now be null
        final Intent intent = new Intent(getActivity(), LoginActivity.class);
        //start the intent that switches to home activity to in turn go to the home activity
        startActivity(intent);
        getActivity().finish();
    }

    @SuppressLint({"MissingPermission", "NewApi"})
    @NeedsPermission({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    private void getCurrentLocation() {
        // Create the location request to start receiving updates
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        LocationSettingsRequest locationSettingsRequest = builder.build();

        SettingsClient settingsClient = LocationServices.getSettingsClient(getActivity());
        settingsClient.checkLocationSettings(locationSettingsRequest);

        if (getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION,
                            android.Manifest.permission.ACCESS_FINE_LOCATION}, 20);
        }else {
            Log.e(TAG, "PERMISSION GRANTED");
        }
        Log.e(TAG, String.valueOf(locationSettingsRequest));
        getFusedLocationProviderClient(getActivity()).requestLocationUpdates(mLocationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        //onLocationChanged(locationResult.getLastLocation());
                        String msg = "Location: " +
                                Double.toString(locationResult.getLastLocation().getLatitude()) + "," +
                                Double.toString(locationResult.getLastLocation().getLongitude());
                        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                    }
                },
                Looper.myLooper());
    }

}


