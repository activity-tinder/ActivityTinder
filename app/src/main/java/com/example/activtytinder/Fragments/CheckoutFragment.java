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
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;

public class CheckoutFragment extends DialogFragment {

    private TextView tvQuestion;
    private TextView tvEventDetails;
    private Button btnYes;
    private Button btnNo;
    private String mName;
    private String mDate;
    private ParseGeoPoint mLocation;


    public CheckoutFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
    }

    public static CheckoutFragment newInstance(String eventDetails){
        CheckoutFragment fragment = new CheckoutFragment();
        Bundle args = new Bundle();
        args.putString("Event Details", eventDetails);
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_checkout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvQuestion = view.findViewById(R.id.tvQuestion);
        tvEventDetails = view.findViewById(R.id.tvEventDetails);
        btnYes = view.findViewById(R.id.btnYes);
        btnNo = view.findViewById(R.id.btnNo);

        //Fetch arguments from bundle and set Title
        String eventDetails = getArguments().getString("Event Details", "Event Detsils");
        getDialog().setTitle(eventDetails);

        //Show soft keyboard automatically and request focus to field
        tvEventDetails.requestFocus();
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        ParseQuery<Event> query = ParseQuery.getQuery(Event.class);
        //TODO --  call the event.getObjectID
        query.getInBackground("JOeUTzZOXJ", new GetCallback<Event>() {
            @Override
            public void done(Event event, ParseException e) {
                if(e == null){
                    mName = event.getKeyName();
                    mDate = event.getKeyDate();
                    mLocation = event.getLocation();
                    tvEventDetails.setText("Name: "
                            + mName
                            + "\n\nDate: "
                            + mDate
                            +"\n\nLocation: "
                            + mLocation
                            + "\n\n"
                    );
                }
                else{
                    Log.e("ReceiptFragment", "Girl, you don goofed");
                    e.printStackTrace();
                }
            }
        });


        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO -- take them to the receipt fragment page
            }
        });

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO -- close overlay and take them back to the cards
                dismiss();
            }
        });
    }
}
