package com.example.activtytinder.Fragments;

import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.activtytinder.Models.Event;
import com.example.activtytinder.R;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class ReceiptFragment extends Fragment  {

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
    private ArrayList<String> mAttendees;
    private String mDescription;
    private Event mEvent;
//    private Button btnHome;

//    BottomNavigationView bottomNavigationView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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
        mAttendees = new ArrayList<>();
//        btnHome = view.findViewById(R.id.btnHome);
        Bundle eventBundle = this.getArguments();
        if(eventBundle != null){
            mEvent = Parcels.unwrap(eventBundle.getParcelable("Event"));
        }
        //Right now, this is stopping the code from crashing because i don't get an event
        //Main Activity creates this fragment at the beginning, but I'm still not able to click on the navigation bar
        else{
            tvEventDetails.setText("You have not selected an event to attend yet!");
            return;
        }

//        bottomNavigationView = getActivity().findViewById(R.id.bottomNavigationView);

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
                    mDescription = event.getKeyDescription();
                    ParseRelation<ParseUser> relation = event.getRelation("usersAttending");
                    ParseQuery<ParseUser> query = relation.getQuery();
                    query.include(Event.KEY_CREATOR);
                    query.findInBackground(new FindCallback<ParseUser>() {
                        @Override
                        public void done(List<ParseUser> users, ParseException e) {
                            if(e == null){
                                for(int x = 0; x < users.size(); x++){
                                    mAttendees.add(users.get(x).getUsername());
                                }
                                tvEventDetails.setText("Name: "
                                        + mName
                                        + "\n\nDate: "
                                        + mDate
                                        + "\n\nCreated by: "
                                        + mCreator
                                        +"\n\nLocation: "
                                        + mLocation
                                        + "\n\nAttendees: "
                                        + mAttendees.toString().substring(1, mAttendees.toString().length() -1 )
                                        + "\n\nDescription: "
                                        + mDescription
                                        + "\n\n"
                                );
                            }
                            else{
                                Log.e("ReceiptFragment", "There are no attendees");
                                e.printStackTrace();
                            }
                        }
                    });

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
                //searchWeb(mLocation);
                Uri gmmIntentUri = Uri.parse("geo:" + 0 +"," + 0 +"?q="+ mLocation);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);

            }
        });

        btnChat.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //TODO -- open internal chat option or if we're feeling bold open Facebook Messenger
                Intent intent= new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, "Hello");
                intent.setType("text/plain");
                intent.setPackage("com.facebook.orca");

                try
                {
                    startActivity(intent);
                }
                catch (ActivityNotFoundException ex)
                {
                    Toast.makeText(getContext(),
                            "Oups!Can't open Facebook messenger right now. Please try again later.",
                            Toast.LENGTH_SHORT).show();
                }
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

    private void searchWeb(String query) {
        Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
        intent.putExtra(SearchManager.QUERY, query);
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        }
    }





}
