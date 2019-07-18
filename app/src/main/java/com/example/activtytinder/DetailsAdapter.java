package com.example.activtytinder;

import android.content.Context;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.activtytinder.Models.Event;

import java.util.List;

public class DetailsAdapter {

    //this class allows the details from an event to be shown in the Receipt Fragment

    //setting up layout variables


    private TextView tvEventDetailsAdapter;

    private Event mEvent;
    protected List<Event> events;
    private Context mContext;
    private ScrollView mScrollViewDetails;

    public DetailsAdapter(Context mContext, Event event, ScrollView mScrollViewDetails) {
        this.mContext = mContext;
        this.mEvent = event;
        this.mScrollViewDetails = mScrollViewDetails;
    }


}
