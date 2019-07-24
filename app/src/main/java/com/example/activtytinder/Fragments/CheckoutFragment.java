package com.example.activtytinder.Fragments;

import android.content.Context;
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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.activtytinder.Models.Event;
import com.example.activtytinder.R;
import com.mindorks.placeholderview.SwipePlaceHolderView;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;

import org.parceler.Parcels;

import java.util.Date;

public class CheckoutFragment extends DialogFragment {

    public BtnNoListener btnNoListener;
    private TextView tvQuestion;
    private TextView tvEventDetails;
    private Event event;
    private Button btnYes;
    private Button btnNo;
    private String mName;
    private String mDate;
    private ParseGeoPoint mLocation;
    Context context;


    public CheckoutFragment() {

        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
    }

    public static CheckoutFragment newInstance(String eventDetails, Event event){
        CheckoutFragment fragment = new CheckoutFragment();
        //Event event = (Event) eventBundle.getSerializable("event");
        Bundle args = new Bundle();
        args.putString("Event Details", eventDetails);
        args.putParcelable("Event", Parcels.wrap(event));
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

//        event = getArguments().getParcelable("Event");
        Bundle eventBundle = this.getArguments();
        if(eventBundle != null){
            event = Parcels.unwrap(eventBundle.getParcelable("Event"));
//            event = eventBundle.getParcelable("Event");
        }

        //Fetch arguments from bundle and set Title
        String eventDetails = getArguments().getString("Event Details", "Event Details");
        getDialog().setTitle(eventDetails);

        //Show soft keyboard automatically and request focus to field
        tvEventDetails.requestFocus();
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        ParseQuery<Event> query = ParseQuery.getQuery(Event.class);
        //TODO --  call the event.getObjectID
        query.getInBackground(event.getObjectId(), new GetCallback<Event>() {
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
                dismiss();
                ReceiptFragment receiptFragment = new ReceiptFragment();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.flContainer, receiptFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO -- close overlay and take them back to the cards
                //btnNoListener.onNoClicked();
                dismiss();
            }
        });

    }

    public interface BtnNoListener {
        void onNoClicked();
    }
}
