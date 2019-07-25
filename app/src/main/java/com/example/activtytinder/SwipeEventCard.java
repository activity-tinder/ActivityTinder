package com.example.activtytinder;

import android.content.Context;
import android.graphics.Point;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
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
import com.parse.ParseException;
import com.parse.ParseFile;

import java.io.File;
import java.net.URI;

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

    /**
     *
     * @param context
     * @param event
     * @param cardViewHolderSize
     */
    public SwipeEventCard(Context context, Event event, Point cardViewHolderSize) {
        mContext = context;
        mEvent = event;
        mCardViewHolderSize = cardViewHolderSize;
    }

    /**
     * Listener interface created to detect when the user swipes the card in affirmation.
     */
    public interface SwipeListener {
        void onSwiped();
    }


    public interface onClickListener{
        void onClick();
    }

    /**
     * Sets swipe listener on a SwipeEventCard object.
     * @param listener
     */
    public void setOnSwipeListener(SwipeListener listener){
        this.listener = listener;
    }

    public void setOnClickListener(onClickListener clickListener) {this.clickListener = clickListener;}

    // loads information on cards
    @Resolve
    public void onResolved() {
        ParseFile image = mEvent.getEventImage();
        if (image != null) {
            Uri imageUri = Uri.fromFile(new File(image.getUrl()));

            // TODO -- make nonsecure links secure without cutting strings

            /**
             * Alters image url from Parse to begin with https instead of http to pass
             * Android security requirements.
             */
            String security = "https";
            String url = image.getUrl().substring(4);

            Log.d("DEBUG", "in setting image " + security + url);
            Glide.with(mContext)
                    .load(security + url)
                    .centerCrop()
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .dontAnimate()
                    .into(ivCardImage);
        }

        tvCardEventName.setText(mEvent.getKeyName());

        if (mEvent.getLocation() == null) {
            tvCardLocation.setText("no address given");
        } else {
            tvCardLocation.setText(mEvent.getKeyAddress());
        }

        tvCardEventCreator.setText(mEvent.getCreator().getUsername());
    }

    /**
     * SwipePlaceHolderView library's listener for detecting if a card is swiped in (right/up)
     */
    @SwipeIn
    public void onSwipeIn(){
        Log.d("EVENT", "onSwipedIn");
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
