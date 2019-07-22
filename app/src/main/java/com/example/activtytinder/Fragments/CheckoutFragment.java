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

import com.example.activtytinder.R;

public class CheckoutFragment extends DialogFragment {

    private TextView tvQuestion;
    private TextView tvEventDetails;
    private Button btnYes;
    private Button btnNo;


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
