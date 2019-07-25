package com.example.activtytinder.Fragments;

import android.os.Bundle;
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

import org.json.JSONArray;
import org.parceler.Parcels;

public class DetailsFragment extends DialogFragment {

    private TextView tvEventDetailsFragment;
    private Button btnClose;
    private Event mEvent;
    private String mName;
    private String mDate;
    private String mLocation;
    private String mCreator;
    private JSONArray mAttendees;
    private Integer mLimit;
    private String mDescription;
    private String mStartTime;
    private String mEndTime;

    public DetailsFragment() {

    }

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

        mName = mEvent.getKeyName();
        mDate = mEvent.getKeyDate();
        mLocation = mEvent.getKeyAddress();
        mCreator = mEvent.getCreator().getUsername();
        mAttendees = mEvent.getKeyAttendees();
        mLimit = mEvent.getKeyLimit();
        mDescription = mEvent.getKeyDescription();
        mStartTime = mEvent.getKeyStartTime();
        mEndTime = mEvent.getKeyEndTime();

        tvEventDetailsFragment.setText(
                "Name: "
                        + mName
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
                        + mAttendees
                        + "\n\nPeople Limit: "
                        + mLimit
                        + "\n\nDescription: "
                        + mDescription
                        + "\n\n"
        );
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }
}
