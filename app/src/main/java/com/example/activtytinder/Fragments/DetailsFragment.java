package com.example.activtytinder.Fragments;

import android.app.Dialog;
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

import com.example.activtytinder.Models.Event;
import com.example.activtytinder.R;
import com.example.activtytinder.Tools;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class DetailsFragment extends BottomSheetDialogFragment {

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
        View v =  inflater.inflate(R.layout.fragment_details, container, false);
        v.setBackgroundResource(android.R.color.transparent);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    public void onResume() {
        // Get existing layout params for the window
//        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
//        // Assign window properties to fill the parent
//        params.width = WindowManager.LayoutParams.MATCH_PARENT;
//        params.height = 500;
//        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);

        super.onResume();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvEventDetailsFragment = view.findViewById(R.id.tvEventDetailsFragment);
        btnClose = view.findViewById(R.id.btnClose);
        //getDialog().getWindow().findViewById(R.id.clDetails).setBackgroundResource(android.R.color.transparent);


        Bundle eventBundle = this.getArguments();
        if(eventBundle != null){
            mEvent = Parcels.unwrap(eventBundle.getParcelable("Event"));
        }

        String eventDetails = getArguments().getString("Event Details", "Event Details");
        getDialog().setTitle(eventDetails);

        tvEventDetailsFragment.requestFocus();
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        getDialog().getWindow().setBackgroundDrawableResource(R.drawable.dialog_rounded_bg);

        mAttendees = new ArrayList();
        mName = mEvent.getKeyName();
        mDate = Tools.convertDate(mEvent.getKeyDate());
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
