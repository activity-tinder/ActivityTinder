package com.example.activtytinder.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.activtytinder.R;

public class ReceiptFragment extends Fragment {



    private Button directionsButton;
    private Button chatButton;
    private Button calendarButton;
    private Button ditchButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_receipt, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        directionsButton = view.findViewById(R.id.btnMaps);
        chatButton = view.findViewById(R.id.btnChat);
        calendarButton = view.findViewById(R.id.btnCalendar);
        ditchButton = view.findViewById(R.id.btnDitchEvent);


        directionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO -- open google maps application and input the locaiton of the event into the destination textview
            }
        });

        chatButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //TODO -- open internal chat option or if we're feeling bold open Facebook Messenger
            }
        });

        calendarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO-- open overlay that gives a bunch of calendar options that one can export the event name, details, location, and time to the calendar app of their choice
            }
        });

        ditchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO -- give User a warning message/confirmation overlay. If they choose to leave, take User's name off of the users attending. Lower their score if it's 24 hours before event will occur.
            }
        });
    }

}
