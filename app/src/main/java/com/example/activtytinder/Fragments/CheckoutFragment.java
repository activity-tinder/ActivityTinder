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
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.parceler.Parcels;


public class CheckoutFragment extends DialogFragment {

    public BtnNoListener btnNoListener;
//    private TextView tvQuestion;
    private TextView tvEventDetails;
    private Event event;
    private Button btnYes;
    public Button btnNo;
    private String mName;
    private String mDate;
    private String mLocation;


    BottomNavigationView bottomNavigationView;


    public CheckoutFragment() {

        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
    }

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

    public void setOnBtnNoListener(BtnNoListener listener){
        this.btnNoListener = listener;
    }

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

        tvEventDetails = view.findViewById(R.id.tvEventDetails);
        btnYes = view.findViewById(R.id.btnYes);
        btnNo = view.findViewById(R.id.btnNo);

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

        ParseQuery<Event> query = ParseQuery.getQuery(Event.class);
        query.getInBackground(event.getObjectId(), (Event event, ParseException e) -> {
            if(e == null){
                mName = event.getKeyName();
                mDate = event.getKeyDate();
                mLocation = event.getKeyAddress();
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
        });

        btnYes.setOnClickListener(view1 -> {
            CardUtils.addUserToEvent(ParseUser.getCurrentUser(), event);
            showReceiptFragment(event);
            dismiss();

        });

        btnNo.setOnClickListener(view12 -> {
            btnNoListener.onNoClicked();
            dismiss();
        });


    }

}
