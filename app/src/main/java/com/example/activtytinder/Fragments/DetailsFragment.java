package com.example.activtytinder.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.activtytinder.Models.Event;

import org.parceler.Parcels;

public class DetailsFragment extends DialogFragment {

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

    Event mEvent;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle eventBundle = this.getArguments();
        if(eventBundle != null){
            mEvent = Parcels.unwrap(eventBundle.getParcelable("Event"));
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        String eventDetails = getArguments().getString("Event Details", "Event Details");
        getDialog().setTitle(eventDetails);

        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }
}
