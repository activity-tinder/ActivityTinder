package com.example.activtytinder;

import android.content.Context;
import android.graphics.Point;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
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
import com.parse.ParseFile;


//TODO -- explain this class
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

    @View(R.id.tvCardEventDate)
    protected TextView tvCardEventDate;

    @View(R.id.tvCardEventTime)
    protected TextView tvCardEventTime;

    @View(R.id.tvCardEventLocation)
    protected TextView tvCardLocation;

    @View(R.id.tvCardEventCreator)
    protected TextView tvCardEventCreator;

    @View(R.id.clCardStack)
    protected ConstraintLayout mConstraintLayout;

    protected Event mEvent;
    protected Context mContext;
//    public SwipePlaceHolderView mSwipeView;
    public Point mCardViewHolderSize;

    /**
     * Creates a single SwipeEventCard to put on the card deck.
     * @param context - Context passed in from CardFragment
     * @param event - Event associated with the card created
     * @param cardViewHolderSize - Controls the size of the card created
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

    /**
     * Listener interface created to detect when the user clicks on a card.
     */
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

    //TODO -- explain this
    // loads information on cards
    @Resolve
    public void onResolved() {
        ParseFile image = mEvent.getEventImage();
        if (image != null) {
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

        tvCardEventDate.setText(mEvent.getKeyDate());

        String eventTime = mEvent.getKeyStartTime() + " - " + mEvent.getKeyEndTime();
        tvCardEventTime.setText(eventTime);

        if (mEvent.getLocation() == null) {
            tvCardLocation.setText("no address given");
        } else {
            tvCardLocation.setText(mEvent.getKeyAddress());
        }

        tvCardEventCreator.setText(mEvent.getCreator().getUsername());

        //TODO: Properly Select Colors for Categories

        if (mEvent.getCategory().equals("Active")) {
            mConstraintLayout.setBackgroundColor(ContextCompat.getColor(mContext, R.color.magenta));
        }else if (mEvent.getCategory().equals("Nature")){
            mConstraintLayout.setBackgroundColor(ContextCompat.getColor(mContext, R.color.bright_green));
        }else if (mEvent.getCategory().equals("Food")){
            mConstraintLayout.setBackgroundColor(ContextCompat.getColor(mContext, R.color.orange));
        }else if (mEvent.getCategory().equals("Social")){
            mConstraintLayout.setBackgroundColor(ContextCompat.getColor(mContext, R.color.blue));
        }else{
            mConstraintLayout.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorPrimaryDark));
        }
    }

    /**
     * SwipePlaceHolderView library's listener for detecting if a card is swiped in (right/up)
     */
    @SwipeIn
    public void onSwipeIn(){
        Log.d("EVENT", "onSwipedIn");
        listener.onSwiped();
    }

    //TODO -- finish this
    /**
     * When card is regularly clicked
     */
    @Click(R.id.cvCards)
    public void onCardClick() {
        clickListener.onClick();
    }

    //TODO -- explain this
    @SwipeInState
    public void onSwipeInState(){
        //Log.d("EVENT", "onSwipeInState");
    }

    //TODO -- explain this
    @SwipeOutState
    public void onSwipeOutState(){
        Log.d("EVENT", "onSwipeOutState");
    }

    //TODO -- explain this
    @SwipeOut
    public void onSwipedOut(){
        Log.d("EVENT", "onSwipedOut");
    }

    /**
     * When card is not swiped fully in a direction and swings back onto the deck
     */
    @SwipeCancelState
    public void onSwipeCancelState(){
        Log.d("EVENT", "onSwipeCancelState");
    }

    //TODO -- epxlain this
    @SwipingDirection
    public void onSwipingDirection(SwipeDirection direction) {
        Log.d("DEBUG", "SwipingDirection " + direction.name());
    }

    //TODO -- explain this
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
