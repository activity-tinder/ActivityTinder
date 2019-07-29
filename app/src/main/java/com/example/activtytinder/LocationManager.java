package com.example.activtytinder;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Looper;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.parse.ParseGeoPoint;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import permissions.dispatcher.NeedsPermission;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;


public class LocationManager extends Activity
{

    private static LocationManager mTools = null;

    private Context mContext;
    private Activity mActivity;

    private double mLongitude = -1000.0;
    private double mLatitude = -1000.0;

    private double eventLat = 50.0;
    private double eventLong = 50.0;

    private String API_KEY;
    public RequestQueue requestQueue;
    private String url ="https://www.mapquestapi.com/geocoding/v1/address?key=";
    private String Location = "&location=";
    private ParseGeoPoint gpEventCoordinates;



    /// the initialization function is a workaround...


    public  static LocationManager get() {
        if (mTools == null) {
            mTools = new LocationManager();
        }
        return mTools;
    }

    private void setCurrentLocation(double longitude, double latitude)
    {
        mLongitude = longitude;
        mLatitude = latitude;
    }

    public double[] getCurrentLocation()
    {
        double[] location = new double[2];
        location[0] = mLongitude;
        location[1] = mLatitude;
        return location;
    }

    public ParseGeoPoint getEventLocation(){
        ParseGeoPoint gps = new ParseGeoPoint(eventLat,eventLong);
        return gps;
    }

    private void setEventLocation(double lat, double lng){
        eventLat = lat;
        eventLong = lng;
    }

    private LocationManager()
    {

    }

    public void initialize(Context context, Activity activity)
    {
        if (mContext == null || mActivity == null)
        {
            mContext = context;
            mActivity = activity;

            setUp();
        }
    }

    @SuppressLint({"MissingPermission", "NewApi"})
    @NeedsPermission({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    private void setUp() {

        LocationRequest myLocationRequest = new LocationRequest();
        myLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(myLocationRequest);

        LocationSettingsRequest locationSettingsRequest = builder.build();
        SettingsClient settingsClient = LocationServices.getSettingsClient(mContext);
        settingsClient.checkLocationSettings(locationSettingsRequest);

        if (mContext.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(mActivity, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    android.Manifest.permission.ACCESS_FINE_LOCATION}, 20);
        }else {
            //Log.e(TAG, "PERMISSION GRANTED");
        }

        getFusedLocationProviderClient(mContext).requestLocationUpdates(myLocationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult)
                    {
                        setCurrentLocation(locationResult.getLastLocation().getLongitude(), locationResult.getLastLocation().getLatitude());

                        String msg = "Location: " +
                                (locationResult.getLastLocation().getLatitude()) + ", " +
                                (locationResult.getLastLocation().getLongitude());

                        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                    }
                },
                Looper.myLooper());
    }

    public void getEventAddress(String searchQuery, String key, EditText etEventAddress, Context context){
        requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest addressRequest = new JsonObjectRequest(Request.Method.GET, url + key + Location + searchQuery, null,
                new Response.Listener<JSONObject>() {


                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray results = response.getJSONArray("results");
                            JSONArray locations = results.getJSONObject(0).getJSONArray("locations");
                            String city = locations.getJSONObject(0).get("adminArea5").toString();
                            String street = locations.getJSONObject(0).get("street").toString();
                            String state = locations.getJSONObject(0).get("adminArea3").toString();
                            JSONObject eventLatLng = locations.getJSONObject(0).getJSONObject("latLng");
                            setEventLocation(eventLatLng.getDouble("lat"), eventLatLng.getDouble("lng"));
                            etEventAddress.setText(street + ", " + city + ", " + state);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(context, "Please try a valid address!", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                error -> {
                    Log.e("Create Fragment", "Error with response");
                    error.printStackTrace();
                });
        requestQueue.add(addressRequest);
    }

    public ParseGeoPoint getEventCoordinates(){
        return gpEventCoordinates;
    }


}
