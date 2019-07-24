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
import com.parse.ParseQuery;

import org.json.JSONArray;
import org.parceler.Parcels;

public class ReceiptFragment extends Fragment {

    private Button btnDirections;
    private Button btnChat;
    private Button btnCalendar;
    private Button btnDitch;
    private ScrollView scDetails;
    private TextView tvEventDetails;
    private String mName;
    private String mCreator;
    private String mDate;
    private String mLocation;
    private JSONArray mAttendees;
    private String mDescription;
    private Event mEvent;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle eventBundle = this.getArguments();
        if(eventBundle != null){
            mEvent = Parcels.unwrap(eventBundle.getParcelable("Event"));
        }
        return inflater.inflate(R.layout.fragment_receipt, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnDirections = view.findViewById(R.id.btnMaps);
        btnChat = view.findViewById(R.id.btnChat);
        btnCalendar = view.findViewById(R.id.btnCalendar);
        btnDitch = view.findViewById(R.id.btnDitchEvent);
        scDetails = view.findViewById(R.id.scDetails);
        tvEventDetails = view.findViewById(R.id.eventDetails);
        ParseQuery<Event> query = ParseQuery.getQuery(Event.class);
        query.include(Event.KEY_CREATOR);



        //TODO --  call the event.getObjectID
        query.getInBackground(mEvent.getObjectId(), new GetCallback<Event>() {
            @Override
            public void done(Event event, ParseException e) {
                if(e == null){
                    mName = event.getKeyName();
                    mDate = event.getKeyDate();
                    mCreator = event.getCreator().getUsername();
                    mLocation = event.getKeyAddress();
                    mAttendees = event.getKeyAttendees();
                    mDescription = event.getKeyDescription();
//                    for (int x = 0; x <event.getKeyAttendees().length(); x++){
//                        try {
//                            String userId = event.getKeyAttendees().getString(x);
                    //Make mAttendees an arraylist
//                            mAttendees.add(userId);
//                        } catch (JSONException e1) {
//                            e1.printStackTrace();
//                        }
//
//                    }
                    tvEventDetails.setText("Name: "
                            + mName
                            + "\n\nDate: "
                            + mDate
                            + "\n\nCreated by: "
                            + mCreator
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





        btnDirections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO -- open google maps application and input the locaiton of the event into the destination textview
            }
        });

        btnChat.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //TODO -- open internal chat option or if we're feeling bold open Facebook Messenger
            }
        });

        btnCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO-- open overlay that gives a bunch of calendar options that one can export the event name, details, location, and time to the calendar app of their choice
            }
        });

        btnDitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO -- give User a warning message/confirmation overlay. If they choose to leave, take User's name off of the users attending. Lower their score if it's 24 hours before event will occur.
            }
        });
    }



}
