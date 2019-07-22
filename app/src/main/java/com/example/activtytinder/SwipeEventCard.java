package com.example.activtytinder;

import android.content.Context;
import android.graphics.Point;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.activtytinder.Fragments.CardFragment;
import com.example.activtytinder.Models.Event;
import com.mindorks.placeholderview.SwipeDirection;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;
import com.mindorks.placeholderview.annotations.swipe.SwipeCancelState;
import com.mindorks.placeholderview.annotations.swipe.SwipeInDirectional;
import com.mindorks.placeholderview.annotations.swipe.SwipeOutDirectional;
import com.mindorks.placeholderview.annotations.swipe.SwipeTouch;
import com.mindorks.placeholderview.annotations.swipe.SwipingDirection;
import com.parse.ParseFile;

// this class binds the event information to the card swiping view
@Layout(R.layout.event_card_view)
public class SwipeEventCard {

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

    // loads information on cards
    @Resolve
    public void onResolved() {
        ParseFile image = mEvent.getEventImage();
        if (image != null) {
            Log.d("DEBUG", "in setting image");
            Glide.with(mContext).load(image.getUrl()).into(ivCardImage);
        }

        tvCardEventName.setText(mEvent.getKeyName());

        if (mEvent.getLocation() == null) {
            tvCardLocation.setText("no address given");
        } else {
            tvCardLocation.setText(mEvent.getLocation().toString());
        }

        tvCardEventCreator.setText(mEvent.getCreator().getUsername());
    }

    // when card is rejected (left/down)
    @SwipeOutDirectional
    public void onSwipeOutDirectional(SwipeDirection direction) {
        Log.d("DEBUG", "Card swiped out");
    }

    // when card is not swiped fully in a direction and swings back onto the deck
    @SwipeCancelState
    public void onSwipeCancelState(){
        Log.d("EVENT", "onSwipeCancelState");
    }

    // when card is accepted (right/up)
    // leads to the event checkout fragment
    @SwipeInDirectional
    public void onSwipeInDirectional(SwipeDirection direction) {
        Log.d("DEBUG", "Going to checkout, SwipeInDirectional " + direction.name());
        // TODO -- connect to CheckoutFragement with correct event being displayed
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
