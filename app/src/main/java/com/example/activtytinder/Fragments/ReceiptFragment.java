package com.example.activtytinder.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.activtytinder.Models.Event;
import com.example.activtytinder.R;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;

import org.json.JSONArray;

import java.util.Date;

public class ReceiptFragment extends Fragment {



    private Button directionsButton;
    private Button chatButton;
    private Button calendarButton;
    private Button ditchButton;
    protected ScrollView scDetails;
    protected TextView tvEventDetails;
    private String mName;
    private Date mDate;
    private ParseGeoPoint mLocation;
    private JSONArray mAttendees;
    private String mDescription;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_receipt, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        directionsButton = view.findViewById(R.id.btnMaps);
        chatButton = view.findViewById(R.id.btnChat);
        calendarButton = view.findViewById(R.id.btnCalendar);
        ditchButton = view.findViewById(R.id.btnDitchEvent);
        scDetails = view.findViewById(R.id.scDetails);
        tvEventDetails = view.findViewById(R.id.eventDetails);
        ParseQuery<Event> query = ParseQuery.getQuery(Event.class);
        //TODO --  call the event.getObjectID
        query.getInBackground("JOeUTzZOXJ", new GetCallback<Event>() {
            @Override
            public void done(Event event, ParseException e) {
                if(e == null){
                    mName = event.getKeyName();
                    mDate = event.getKeyDate();
                    mLocation = event.getLocation();
                    mAttendees = event.getKeyAttendees();
                    mDescription = event.getKeyDescription();
//                    Log.d("ReceiptFragment", "\nName: "
//                            + mName
//                            + "\nDate: "
//                            + mDate
//                            +"\nLocation: "
//                            + mLocation
//                            + "\nAttendees: "
//                            + mAttendees
//                            + "\nDescription: "
//                            + mDescription);
                    tvEventDetails.setText("Name: "
                            + mName
                            + "\n\nDate: "
                            + mDate
                            +"\n\nLocation: "
                            + mLocation
                            + "\n\nAttendees: "
                            + mAttendees
                            + "\n\nDescription: "
                            + mDescription
                            + "\n\n"
                    );
                }
                else{
                    Log.e("ReceiptFragment", "Girl, you don goofed");
                    e.printStackTrace();
                }
            }
        });





        directionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO -- open google maps application and input the locaiton of the event into the destination textview
            }
        });

        chatButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //TODO -- open internal chat option or if we're feeling bold open Facebook Messenger
            }
        });

        calendarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO-- open overlay that gives a bunch of calendar options that one can export the event name, details, location, and time to the calendar app of their choice
            }
        });

        ditchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO -- give User a warning message/confirmation overlay. If they choose to leave, take User's name off of the users attending. Lower their score if it's 24 hours before event will occur.
            }
        });
    }



}
