package com.example.activtytinder;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.activtytinder.Models.Event;
import com.mindorks.placeholderview.SwipePlaceHolderView;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.swipe.SwipeCancelState;
import com.mindorks.placeholderview.annotations.swipe.SwipeIn;
import com.mindorks.placeholderview.annotations.swipe.SwipeInState;
import com.mindorks.placeholderview.annotations.swipe.SwipeOut;
import com.mindorks.placeholderview.annotations.swipe.SwipeOutState;

import java.util.ArrayList;
import java.util.List;

// this class binds the event information to the card swiping view
public class EventCard {

    // setting up layout variables
    protected ImageView ivCardImage;
    protected TextView tvCardEventName;
    protected TextView tvCardLocation;

    protected Event mEvent;
    protected List<Event> events;
    protected Context mContext;
    public SwipePlaceHolderView mSwipeView;

    public EventCard(Context context, Event event, SwipePlaceHolderView swipeView) {
        mContext = context;
        mEvent = event;
        mSwipeView = swipeView;
        events = new ArrayList<>();
    }

    @Resolve
    public void onResolved() {
        tvCardEventName.setText(mEvent.getKeyName());
        tvCardLocation.setText(mEvent.getLocation().toString());
    }

    @SwipeOut
    public void onSwipedOut() {
        Log.d("EVENT", "onSwipedOut");
        mSwipeView.addView(this);
    }

    @SwipeCancelState
    public void onSwipeCancelState(){
        Log.d("EVENT", "onSwipeCancelState");
    }

    @SwipeIn
    public void onSwipeIn(){
        Log.d("EVENT", "onSwipedIn");
    }

    @SwipeInState
    public void onSwipeInState(){
        Log.d("EVENT", "onSwipeInState");
    }

    @SwipeOutState
    public void onSwipeOutState(){
        Log.d("EVENT", "onSwipeOutState");
    }

}
