package com.example.activtytinder;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.activtytinder.Models.Event;
import com.mindorks.placeholderview.SwipePlaceHolderView;
import com.mindorks.placeholderview.annotations.Resolve;

import java.util.ArrayList;
import java.util.List;

// this class binds the event information to the card swiping view
public class CardAdapter {

    // setting up layout variables
    private ImageView ivCardImage;
    private TextView tvCardEventName;
    private TextView tvCardLocation;

    private Event mEvent;
    protected List<Event> events;
    private Context mContext;
    private SwipePlaceHolderView mSwipeView;

    public CardAdapter(Context context, Event event, SwipePlaceHolderView swipeView) {
        mContext = context;
        mEvent = event;
        mSwipeView = swipeView;
        events = new ArrayList<>();
    }

    @Resolve
    private void onResolved() {
        tvCardEventName.setText(mEvent.getKeyName());
        tvCardLocation.setText(mEvent.getLocation().toString());
    }

    private void onSwipedOut() {
        Log.d("EVENT", "onSwipedOut");
        mSwipeView.addView(this);
    }

    private void onSwipeCancelState(){
        Log.d("EVENT", "onSwipeCancelState");
    }

    private void onSwipeIn(){
        Log.d("EVENT", "onSwipedIn");
    }

    private void onSwipeInState(){
        Log.d("EVENT", "onSwipeInState");
    }

    private void onSwipeOutState(){
        Log.d("EVENT", "onSwipeOutState");
    }

}
