package com.example.activtytinder.Fragments;

import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
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
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import org.json.JSONArray;
import org.parceler.Parcels;

import java.text.SimpleDateFormat;
import java.util.Date;

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
    private String mStartTime;
    private String mEndTime;
//    private Button btnHome;

//    BottomNavigationView bottomNavigationView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_receipt, container , false);
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
//        btnHome = view.findViewById(R.id.btnHome);
        Bundle eventBundle = this.getArguments();
        if(eventBundle != null){
            mEvent = Parcels.unwrap(eventBundle.getParcelable("Event"));
        }

//        bottomNavigationView = getActivity().findViewById(R.id.bottomNavigationView);

        ParseQuery<Event> query = ParseQuery.getQuery(Event.class);
        query.include(Event.KEY_CREATOR);



        //TODO --  call the event.getObjectID
        query.getInBackground(mEvent.getObjectId(), new GetCallback<Event>() {
            @Override
            public void done(Event event, ParseException e) {
                if(e == null){
                    // ParseRelation relation = event.getRelation("usersAttending");
//                    for(int x =0; x< relation.describeContents(); x++){
//                    }
//                    ParseRelation relation = event.getRelation("usersAttending");
//                    ParseQuery attendeesQuery = relation.getQuery();
//                    attendeesQuery.findInBackground(new FindCallback() {
//                        @Override
//                        public void done(List<Event> usersList, ParseException e) {
//                            if(e == null){
//                                for(int x =0; x < usersList.size(); x++){
//                                    mAttendees.add(usersList.get(x).getUsername());
//                                }
//                            }
//                        }
//
//                    });
//                    ParseQuery queryAttendees = relation.getQuery();
//                    queryAttendees.getInBackground();
                    mName = event.getKeyName();
                    mDate = event.getKeyDate();
                    mCreator = event.getCreator().getUsername();
                    mLocation = event.getKeyAddress();
                    mAttendees = event.getKeyAttendees();
                    mDescription = event.getKeyDescription();
                    mStartTime = event.getKeyStartTime();
                    mEndTime = event.getKeyEndTime();
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
                String rightTime = checkTime(mStartTime);
                String myDate = mDate + " " + rightTime + ":00";
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:MM:SS");
                Date startDate = null;
                try {
                    startDate = sdf.parse(myDate);
                } catch (java.text.ParseException e) {
                    e.printStackTrace();
                }
                long start = startDate.getTime();
                addEvent(mName, mLocation, start,  8000);
            }
        });

        btnDitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO -- give User a warning message/confirmation overlay. If they choose to leave, take User's name off of the users attending. Lower their score if it's 24 hours before event will occur.
            }
        });

//        btnHome.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                bottomNavigationView.setVisibility(View.VISIBLE);
//                FragmentManager fragmentManager = getFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.remove(ReceiptFragment.this);
//                fragmentTransaction.commit();
//                fragmentManager.popBackStack();
//
//            }
//        });
    }

    private void searchWeb(String query) {
        Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
        intent.putExtra(SearchManager.QUERY, query);
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public void addEvent(String title, String location, long begin, long end) {
        Intent intent = new Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.Events.TITLE, title)
                .putExtra(CalendarContract.Events.EVENT_LOCATION, location)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, begin)
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, end);
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public String checkTime (String time) {
        if (time.charAt(6) == 'A') {
            String finalTime = time.substring(0,5);
            return finalTime;
        }else
        return null;
    }





}
