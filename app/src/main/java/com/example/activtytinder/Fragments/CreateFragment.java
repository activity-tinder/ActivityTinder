package com.example.activtytinder.Fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.activtytinder.Models.Event;
import com.example.activtytinder.R;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class CreateFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private EditText etEventName;
    private EditText etEventDescription;
    private EditText etEventDate;
    private EditText etEventStartTime;
    private EditText etEventEndTime;
    private EditText etEventAddress;
    private EditText etEventMaxPeople;
    private Button btnCreateEvent;
    private Button btnGetEventLocation;
    ParseGeoPoint gpEventCoordinates;
    private DatePickerDialog dpdPicker;
    private TimePickerDialog tpdClock;
    public String AM_PM;
    public String searchQuery;
    public static final String TAG = "Create Fragment";
    String API_KEY;
    RequestQueue requestQueue;
    String url ="https://www.mapquestapi.com/geocoding/v1/address?key=";
    String Location = "&location=";
    String eventCategory;





    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etEventName = view.findViewById(R.id.etEventName);
        etEventDescription = view.findViewById(R.id.etEventDescription);
        etEventDate = view.findViewById(R.id.etEventDate);
        etEventStartTime = view.findViewById(R.id.etStartTime);
        etEventEndTime = view.findViewById(R.id.etEndTime);
        etEventAddress = view.findViewById(R.id.etEventLocation);
        etEventMaxPeople = view.findViewById(R.id.etPeopleLimit);
        btnCreateEvent = view.findViewById(R.id.btnCreateEvent);
        btnGetEventLocation = view.findViewById(R.id.btnConfirmLocation);
        API_KEY = getActivity().getResources().getString(R.string.mapquest_api_key);
        requestQueue = Volley.newRequestQueue(getContext());
        Spinner spinner = (Spinner) view.findViewById(R.id.spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.categories_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);




        etEventDate.setInputType(InputType.TYPE_NULL);
        etEventDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);
                dpdPicker = new DatePickerDialog(getContext(), (view1, year1, monthOfYear, dayOfMonth)
                        -> etEventDate.setText((monthOfYear + 1) + "/" + dayOfMonth + "/" + year1), year, month, day);
                dpdPicker.show();
            }
        });

        etEventStartTime.setOnClickListener(view12 -> {
            final Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minutes = calendar.get(Calendar.MINUTE);
            tpdClock = new TimePickerDialog(getContext(),
                    (tp, sHour, sMinute) -> {
                        if (sHour > 12) {
                            sHour = sHour - 12;
                            AM_PM = "PM";
                        } else {
                            AM_PM = "AM";
                        }
                        etEventStartTime.setText(String.format("%02d:%02d", sHour, sMinute) + " " + AM_PM);
                    }, hour, minutes, true);
            tpdClock.show();
        });

        etEventEndTime.setOnClickListener(view13 -> {
            final Calendar cldr = Calendar.getInstance();
            int hour = cldr.get(Calendar.HOUR_OF_DAY);
            int minutes = cldr.get(Calendar.MINUTE);
            tpdClock = new TimePickerDialog(getContext(),
                    (tp, sHour, sMinute) -> {
                        if (sHour > 12) {
                            sHour = sHour - 12;
                            AM_PM = "PM";
                        } else {
                            AM_PM = "AM";
                        }
                        etEventEndTime.setText(String.format("%02d:%02d", sHour, sMinute) + " " + AM_PM);
                    }, hour, minutes, true);
            tpdClock.show();
        });

        btnGetEventLocation.setOnClickListener(view14 -> {
            searchQuery = etEventAddress.getText().toString();
            getEventAddress();
        });

        btnCreateEvent.setOnClickListener(view15 -> {
            final String EventName = etEventName.getText().toString();
            final String EventDescription = etEventDescription.getText().toString();
            final String EventDate = etEventDate.getText().toString();
            final String StartTime = etEventStartTime.getText().toString();
            final String EndTime = etEventEndTime.getText().toString();
            final String Address = etEventAddress.getText().toString();
            final String Category = eventCategory;
            final Integer PeopleLimit = Integer.parseInt(etEventMaxPeople.getText().toString());
            final ParseGeoPoint EventCoordinates = gpEventCoordinates;
            makeEvent(EventName, EventDescription, EventDate, StartTime, EndTime, Address, PeopleLimit, EventCoordinates, Category);
        });
    }

    private void makeEvent(String Name, String Description, String Date, String StartTime, String EndTime, String Address, Integer PeopleLimit, ParseGeoPoint EventCoordinates, String Category){
        Event event = new Event();
        event.setKeyCreator(ParseUser.getCurrentUser());
        event.setKeyName(Name);
        event.setKeyDescription(Description);
        event.setKeyDate(Date);
        event.setKeyStartTime(StartTime);
        event.setKeyEndTime(EndTime);
        event.setKeyAddress(Address);
        event.setKeyLimit(PeopleLimit);
        event.setKeyLocation(EventCoordinates);
        event.setKeyCategory(Category);
        JSONArray attending = new JSONArray();
        attending.put(ParseUser.getCurrentUser().getObjectId());
        event.put("usersAttending", attending);

        event.saveInBackground();

    }

    public void getEventAddress(){
        JsonObjectRequest addressRequest = new JsonObjectRequest(Request.Method.GET, url + API_KEY + Location + searchQuery, null,
                response -> {
                    Log.d(TAG, response.toString());
                    try {
                        JSONArray results = response.getJSONArray("results");
                        JSONArray locations = results.getJSONObject(0).getJSONArray("locations");
                        String city = locations.getJSONObject(0).get("adminArea5").toString();
                        String street = locations.getJSONObject(0).get("street").toString();
                        String state = locations.getJSONObject(0).get("adminArea3").toString();
                        JSONObject eventLatLng = locations.getJSONObject(0).getJSONObject("latLng");
                        double latitude = eventLatLng.getDouble("lat");
                        double longitude = eventLatLng.getDouble("lng");
                        gpEventCoordinates = new ParseGeoPoint(latitude, longitude);

                        etEventAddress.setText(street + ", " + city + ", " + state);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Please try a valid address!", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Log.e(TAG, "Error with response");
                    error.printStackTrace();
                });
        requestQueue.add(addressRequest);
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            adapterView.getItemAtPosition(i);
            eventCategory = adapterView.getItemAtPosition(i).toString();


    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
