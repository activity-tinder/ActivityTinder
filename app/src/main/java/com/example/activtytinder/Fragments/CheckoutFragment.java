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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.activtytinder.CardUtils;
import com.example.activtytinder.Models.Event;
import com.example.activtytinder.R;
import com.example.activtytinder.Tools;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.List;


public class CheckoutFragment extends DialogFragment {

    public BtnNoListener btnNoListener;
    private TextView tvEventDetails;
    private Event event;
    private Button btnYes;
    public Button btnNo;
    private String mName;
    private String mDate;
    private String mLocation;
    private String numberOfAttendees;
    public static Integer num;


    //TODO -- Explain constructor

    public CheckoutFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
    }

    //TODO-- Explain new instance
    public static CheckoutFragment newInstance(String eventDetails, Event event){
        CheckoutFragment fragment = new CheckoutFragment();
        //Event event = (Event) eventBundle.getSerializable("event");
        Bundle eventBundle = new Bundle();
        eventBundle.putString("Event Details", eventDetails);
        eventBundle.putParcelable("Event", Parcels.wrap(event));
        fragment.setArguments(eventBundle);
        return fragment;
    }

    /**
     * Listener interface for detecting if the no button in the checkout fragment is clicked.
     */
    public interface BtnNoListener {
        void onNoClicked();
    }

    /**
     * Sets the checkout fragment object's button listener.
     * @param listener
     */
    public void setOnBtnNoListener(BtnNoListener listener){
        this.btnNoListener = listener;
    }


    //TODO-- Explain method
    /**
     *
     * @param event
     */
    public void showReceiptFragment(Event event) {
        ReceiptFragment receiptFragment = new ReceiptFragment();
        Bundle eventBundle = new Bundle();
        eventBundle.putParcelable("Event", Parcels.wrap(event));
        receiptFragment.setArguments(eventBundle);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.flContainer, receiptFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_checkout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        num = this.getId();
        System.out.println(num);

        tvEventDetails = view.findViewById(R.id.etPasswordInput);
        btnYes = view.findViewById(R.id.btnYes);
        btnNo = view.findViewById(R.id.btnNo);

        getDialog().setCanceledOnTouchOutside(false);
        Bundle eventBundle = this.getArguments();
        if(eventBundle != null){
            event = Parcels.unwrap(eventBundle.getParcelable("Event"));
        }

        //Fetch arguments from bundle and set Title
        String eventDetails = getArguments().getString("Event Details", "Event Details");
        getDialog().setTitle(eventDetails);

        //Show soft keyboard automatically and request focus to field
        tvEventDetails.requestFocus();
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        getDialog().getWindow().setBackgroundDrawableResource(R.drawable.dialog_rounded_bg);


        //TODO -- Explain query
        ParseQuery<Event> query = ParseQuery.getQuery(Event.class);
        query.include(Event.KEY_CREATOR);
        query.getInBackground(event.getObjectId(), (Event event, ParseException e) -> {
            if(e == null){
                ParseRelation<ParseUser> relation = event.getRelation("usersAttending");
                ParseQuery<ParseUser> numberQuery = relation.getQuery();
                mName = event.getKeyName();
                mDate = Tools.convertDate(event.getKeyDate());
                mLocation = event.getKeyAddress();
                numberQuery.findInBackground(new FindCallback<ParseUser>() {
                    @Override
                    public void done(List<ParseUser> users, ParseException e) {
                        if(users.size() == 1){
                            numberOfAttendees = "There is "  + users.size() +" person attending.";
                            tvEventDetails.setText(
                                    mName
                                    + "\n\n"
                                    + mDate
                                    +"\n\n"
                                    + mLocation
                                    + "\n\n"
                                    + numberOfAttendees
                                    + "\n\n"
                            );
                        }
                        else{
                           numberOfAttendees = "There are "  + users.size() +" people attending.";
                            tvEventDetails.setText("Name: "
                                    + mName
                                    + "\n\nDate: "
                                    + mDate
                                    +"\n\nLocation: "
                                    + mLocation
                                    + "\n\nNumber of Attendees: "
                                    + numberOfAttendees
                                    + "\n\n"
                            );
                        }

                    }
                });

            }
            else{
                Log.e("ReceiptFragment", "Girl, you don goofed");
                e.printStackTrace();
            }
        });

        //TODO -- Create seperate method to instantiate buttons
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {
                CardUtils.addUserToEvent(ParseUser.getCurrentUser(), event);
                CheckoutFragment.this.showReceiptFragment(event);
                CheckoutFragment.this.dismiss();
                ReceiptFragment.Confetti = true;

            }
        });

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view12) {
                btnNoListener.onNoClicked();
                CheckoutFragment.this.dismiss();
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
}
