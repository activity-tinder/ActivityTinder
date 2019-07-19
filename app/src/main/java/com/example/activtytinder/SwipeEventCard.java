package com.example.activtytinder;

import android.content.Context;
import android.graphics.Point;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.activtytinder.Models.Event;
import com.mindorks.placeholderview.SwipeDirection;
import com.mindorks.placeholderview.SwipePlaceHolderView;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;
import com.mindorks.placeholderview.annotations.swipe.SwipeCancelState;
import com.mindorks.placeholderview.annotations.swipe.SwipeIn;
import com.mindorks.placeholderview.annotations.swipe.SwipeInDirectional;
import com.mindorks.placeholderview.annotations.swipe.SwipeInState;
import com.mindorks.placeholderview.annotations.swipe.SwipeOut;
import com.mindorks.placeholderview.annotations.swipe.SwipeOutDirectional;
import com.mindorks.placeholderview.annotations.swipe.SwipeOutState;
import com.mindorks.placeholderview.annotations.swipe.SwipeTouch;
import com.mindorks.placeholderview.annotations.swipe.SwipingDirection;

// this class binds the event information to the card swiping view
@Layout(R.layout.event_card_view)
public class SwipeEventCard {

    // setting up layout variables
    protected ImageView ivCardImage;

    @View(R.id.tvCardEventName)
    protected TextView tvCardEventName;

    @View(R.id.tvCardEventLocation)
    protected TextView tvCardLocation;

    protected Event mEvent;
    protected Context mContext;
//    public SwipePlaceHolderView mSwipeView;
    public Point mCardViewHolderSize;
    protected Callback mCallback;

    public SwipeEventCard(Context context, Event event, Point cardViewHolderSize, Callback callback) {
        mContext = context;
        mEvent = event;
        mCardViewHolderSize = cardViewHolderSize;
        mCallback = callback;
    }

    @Resolve
    public void onResolved() {
        tvCardEventName.setText(mEvent.getKeyName());
        tvCardLocation.setText(mEvent.getLocation().toString());
    }

    @SwipeOutDirectional
    public void onSwipeOutDirectional(SwipeDirection direction) {
        Log.d("DEBUG", "SwipeOutDirectional " + direction.name());
        if (direction.getDirection() == SwipeDirection.TOP.getDirection()) {
            mCallback.onSwipeUp();
        }
    }

    @SwipeCancelState
    public void onSwipeCancelState(){
        Log.d("EVENT", "onSwipeCancelState");
    }

    @SwipeInDirectional
    public void onSwipeInDirectional(SwipeDirection direction) {
        Log.d("DEBUG", "SwipeInDirectional " + direction.name());
    }

    @SwipingDirection
    public void onSwipingDirection(SwipeDirection direction) {
        Log.d("DEBUG", "SwipingDirection " + direction.name());
    }


    @SwipeTouch
    public void onSwipeTouch(float xStart, float yStart, float xCurrent, float yCurrent) {

        float cardHolderDiagonalLength =
                (float) Math.sqrt(Math.pow(mCardViewHolderSize.x, 2) + (Math.pow(mCardViewHolderSize.y, 2)));
        float distance = (float) Math.sqrt(Math.pow(xCurrent - xStart, 2) + (Math.pow(yCurrent - yStart, 2)));

        float alpha = 1 - distance / cardHolderDiagonalLength;

        Log.d("DEBUG", "onSwipeTouch "
                + " xStart : " + xStart
                + " yStart : " + yStart
                + " xCurrent : " + xCurrent
                + " yCurrent : " + yCurrent
                + " distance : " + distance
                + " TotalLength : " + cardHolderDiagonalLength
                + " alpha : " + alpha
        );
    }

    public interface Callback {
        void onSwipeUp();
    }
}
