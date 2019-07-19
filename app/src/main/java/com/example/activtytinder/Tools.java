package com.example.activtytinder;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Looper;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;

import permissions.dispatcher.NeedsPermission;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

public class Tools{



    @SuppressLint({"MissingPermission", "NewApi"})
    @NeedsPermission({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    public static double[] getCurrentLocation(LocationRequest myLocationRequest, Context context, Activity activity) {
        myLocationRequest = new LocationRequest();
        myLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(myLocationRequest);
        LocationSettingsRequest locationSettingsRequest = builder.build();
        SettingsClient settingsClient = LocationServices.getSettingsClient(context);
        settingsClient.checkLocationSettings(locationSettingsRequest);

        if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    android.Manifest.permission.ACCESS_FINE_LOCATION}, 20);
        }else {
            //Log.e(TAG, "PERMISSION GRANTED");
        }
        //Log.e(TAG, String.valueOf(locationSettingsRequest));
        double[] location = new double[2];
        getFusedLocationProviderClient(context).requestLocationUpdates(myLocationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        //onLocationChanged(locationResult.getLastLocation());
                        String msg = "Location: " +
                                (locationResult.getLastLocation().getLatitude()) + ", " +
                                (locationResult.getLastLocation().getLongitude());
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                       // latitude = locationResult.getLastLocation().getLatitude();
                        //longitude = locationResult.getLastLocation().getLongitude();
                    }
                },
                Looper.myLooper());
        return location;
    }
}
