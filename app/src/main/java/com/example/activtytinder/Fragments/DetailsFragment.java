package com.example.activtytinder.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.activtytinder.Models.Event;
import com.example.activtytinder.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class DetailsFragment extends DialogFragment {

    private TextView tvEventDetailsFragment;
    private Button btnClose;
    private Event mEvent;
    private String mName;
    private String mDate;
    private String mLocation;
    private String mCreator;
    private ArrayList mAttendees;
    private Integer mLimit;
    private String mDescription;
    private String mStartTime;
    private String mEndTime;

    //TODO -- explain this constructor
    public DetailsFragment() {

    }

    //TODO -- Explain new instance

    public static DetailsFragment newInstance(String eventDetails, Event event){
        DetailsFragment fragment = new DetailsFragment();
        Bundle eventBundle = new Bundle();
        eventBundle.putString("Event Details", eventDetails);
        eventBundle.putParcelable("Event", Parcels.wrap(event));
        fragment.setArguments(eventBundle);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvEventDetailsFragment = view.findViewById(R.id.tvEventDetailsFragment);
        btnClose = view.findViewById(R.id.btnClose);

        Bundle eventBundle = this.getArguments();
        if(eventBundle != null){
            mEvent = Parcels.unwrap(eventBundle.getParcelable("Event"));
        }

        String eventDetails = getArguments().getString("Event Details", "Event Details");
        getDialog().setTitle(eventDetails);

        tvEventDetailsFragment.requestFocus();
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        mAttendees = new ArrayList();
        mName = mEvent.getKeyName();
        mDate = mEvent.getKeyDate();
        mLocation = mEvent.getKeyAddress();
        mCreator = mEvent.getCreator().getUsername();
        mLimit = mEvent.getKeyLimit();
        mDescription = mEvent.getKeyDescription();
        mStartTime = mEvent.getKeyStartTime();
        mEndTime = mEvent.getKeyEndTime();

        //TODO -- explain this query
        ParseRelation<ParseUser> relation = mEvent.getRelation("usersAttending");
        ParseQuery<ParseUser> query = relation.getQuery();
        query.include(Event.KEY_CREATOR);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> users, ParseException e) {
                if(e == null){
                    for(int x = 0; x < users.size(); x++){
                        mAttendees.add(users.get(x).getUsername());
                    }
                    tvEventDetailsFragment.setText(
                            "Name: "
                                    + mName
                                    + "\n\nDescription: "
                                    + mDescription
                                    + "\n\nDate: "
                                    + mDate
                                    + "\n\nTime: "
                                    + mStartTime
                                    + " - "
                                    + mEndTime
                                    + "\n\nCreated by: "
                                    + mCreator
                                    +"\n\nLocation: "
                                    + mLocation
                                    + "\n\nAttendees: "
                                    + mAttendees.toString().substring(1, mAttendees.toString().length() -1 )
                                    + "\n\nPeople Limit: "
                                    + mLimit
                                    + "\n\n"
                    );
                }
                else{
                    Log.e("ReceiptFragment", "There are no attendees");
                    e.printStackTrace();
                }
            }
        });


        //TODO -- create seperat emethiod for button
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

}
