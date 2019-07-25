package com.example.activtytinder;

import android.content.Context;
import android.graphics.Point;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.activtytinder.Models.Event;
import com.mindorks.placeholderview.SwipeDirection;
import com.mindorks.placeholderview.annotations.Click;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;
import com.mindorks.placeholderview.annotations.swipe.SwipeCancelState;
import com.mindorks.placeholderview.annotations.swipe.SwipeIn;
import com.mindorks.placeholderview.annotations.swipe.SwipeInState;
import com.mindorks.placeholderview.annotations.swipe.SwipeOut;
import com.mindorks.placeholderview.annotations.swipe.SwipeOutState;
import com.mindorks.placeholderview.annotations.swipe.SwipeTouch;
import com.mindorks.placeholderview.annotations.swipe.SwipingDirection;

// this class binds the event information to the card swiping view
@Layout(R.layout.event_card_view)
public class SwipeEventCard {


    SwipeListener listener;
    onClickListener clickListener;


    // setting up layout variables
    @View(R.id.ivCardImage)
    protected ImageView ivCardImage;

    @View(R.id.tvCardEventName)
    protected TextView tvCardEventName;

    @View(R.id.tvCardEventLocation)
    protected TextView tvCardLocation;

    @View(R.id.tvCardEventCreator)
    protected TextView tvCardEventCreator;

    protected Event mEvent;
    protected Context mContext;
//    public SwipePlaceHolderView mSwipeView;
    public Point mCardViewHolderSize;

    public SwipeEventCard(Context context, Event event, Point cardViewHolderSize) {
        mContext = context;
        mEvent = event;
        mCardViewHolderSize = cardViewHolderSize;
    }

    public interface SwipeListener {
        void onSwiped();
    }

    public interface onClickListener{
        void onClick();
    }
    public void setOnSwipeListener(SwipeListener listener){
        this.listener = listener;
    }

    public void setOnClickListener(onClickListener clickListener) {this.clickListener = clickListener;}

    // loads information on cards
    @Resolve
    public void onResolved() {
//        ParseFile image = mEvent.getEventImage();
//        if (image != null) {
//            Log.d("DEBUG", "in setting image " + image.getUrl());
//            Glide.with(mContext).load(image.getUrl()).into(ivCardImage);
//        }

        tvCardEventName.setText(mEvent.getKeyName());

        if (mEvent.getLocation() == null) {
            tvCardLocation.setText("no address given");
        } else {
            tvCardLocation.setText(mEvent.getKeyAddress());
        }

        tvCardEventCreator.setText(mEvent.getCreator().getUsername());
    }

    // TODO -- write documentation for the structure of the fragment calling
    @SwipeIn
    public void onSwipeIn(){
        Log.d("EVENT", "onSwipedIn");
//        Log.d("EVENT", "this is the current user: " + getCurrentUser().getUsername());
//        ParseUser user = getCurrentUser();
//        addUserToEvent(user, mEvent);
//        swipedRight = true;
        // TODO -- document the workaround
        listener.onSwiped();
    }


    @Click(R.id.cvCards)
    public void onCardClick() {
        clickListener.onClick();
    }

    @SwipeInState
    public void onSwipeInState(){
        //Log.d("EVENT", "onSwipeInState");
    }

    @SwipeOutState
    public void onSwipeOutState(){
        Log.d("EVENT", "onSwipeOutState");
    }

    @SwipeOut
    public void onSwipedOut(){
        Log.d("EVENT", "onSwipedOut");
    }

    // when card is not swiped fully in a direction and swings back onto the deck
    @SwipeCancelState
    public void onSwipeCancelState(){
        Log.d("EVENT", "onSwipeCancelState");
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


}
