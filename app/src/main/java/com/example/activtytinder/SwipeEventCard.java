package com.example.activtytinder;

import android.content.Context;
import android.graphics.Point;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.activtytinder.Models.Event;
import com.mindorks.placeholderview.SwipeDirection;
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


    public static boolean swipedRight = false;
    MyListener listener;

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

    public void setOnSwipeListener(MyListener listener){
        this.listener = listener;
    }

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
            tvCardLocation.setText(mEvent.getLocation().toString());
        }

        tvCardEventCreator.setText(mEvent.getCreator().getUsername());
    }

    @SwipeIn
    public void onSwipeIn(){
        Log.d("EVENT", "onSwipedIn");
//        Log.d("EVENT", "this is the current user: " + getCurrentUser().getUsername());
//        ParseUser user = getCurrentUser();
//        addUserToEvent(user, mEvent);
        swipedRight = true;
        listener.onSwiped();
        //TODO -- set up our own callback
//        DialogFragment dialog = CheckoutFragment.instantiate(getParentFragment(), "help");
//        CheckoutFragment fragment = new CheckoutFragment();
//        fragment.show(getFragmentManager(), "CheckoutFragment");
//        fragment.show(getChildFragmentManager(), "send help");
//
//        new CheckoutFragment().show(getFragmentManager(), "CheckoutFragment");
//
//        FragmentManager fm = getFragmentManager();
//        CheckoutFragment dialog = CheckoutFragment.instantiate(getActivity(), "Hello world");
//        dialog.show(getFragmentManager(), "dialog");
//        FragmentTransaction fragmentTransaction = fm.beginTransaction();
//        fragmentTransaction.replace(R.layout.event_card_view, R.layout.fragment_checkout)
//        CheckoutFragment editNameDialogFragment = CheckoutFragment.newInstance("Some Title");
//        editNameDialogFragment.show(fm, "CheckoutFragment");

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

    // when card is accepted (right/up)
    // leads to the event checkout fragment
//    @SwipeInDirectional
//    public void onSwipeInDirectional(SwipeDirection direction) {
//        Log.d("DEBUG", "Going to checkout, SwipeInDirectional " + direction.name());
//        // TODO -- connect to CheckoutFragement with correct event being displayed
//        addUserToEvent(getCurrentUser(), mEvent);
//    }



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

    public interface MyListener {
        void onSwiped();
    }
}
