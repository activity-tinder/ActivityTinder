package com.example.activtytinder.Fragments;

import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.activtytinder.CardUtils;
import com.example.activtytinder.Models.Event;
import com.example.activtytinder.R;
import com.example.activtytinder.SwipeEventCard;
import com.mindorks.placeholderview.SwipeDecor;
import com.mindorks.placeholderview.SwipePlaceHolderView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import org.parceler.Parcels;

import java.util.List;

import static com.example.activtytinder.Fragments.ProfileFragment.TAG;

public class CardFragment extends Fragment {

    private ImageButton btnAccept;
    private ImageButton btnReject;


    SwipePlaceHolderView mSwipePlaceHolderView;
    Point cardViewHolderSize;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_card, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnAccept = view.findViewById(R.id.acceptBtn);
        btnReject = view.findViewById(R.id.rejectBtn);

        int bottomMargin = CardUtils.dpToPx(160);
        Point windowSize = CardUtils.getDisplaySize(getActivity().getWindowManager());
        cardViewHolderSize = new Point(windowSize.x, windowSize.y - bottomMargin);

        mSwipePlaceHolderView = view.findViewById(R.id.swipeView);

        queryEvents();

        mSwipePlaceHolderView.getBuilder()
                .setDisplayViewCount(3)
                .setHeightSwipeDistFactor(10)
                .setWidthSwipeDistFactor(5)
                .setSwipeDecor(new SwipeDecor()
                    .setPaddingTop(10)
                    .setRelativeScale(0.01f));

        //TODO -- figure out what v is
        btnAccept.setOnClickListener(v -> {
            Log.d(TAG, "accept clicked!");
            mSwipePlaceHolderView.doSwipe(true);
        });

        btnReject.setOnClickListener(v -> {
            Log.d(TAG, "reject clicked!");
            mSwipePlaceHolderView.doSwipe(false);
        });

    }
    public void showCheckoutDialog(Event event) {
        FragmentManager fm = getFragmentManager();
        CheckoutFragment editNameDialogFragment = CheckoutFragment.newInstance("Event", event);
        editNameDialogFragment.show(fm, "CheckoutFragment");
    }
   public void onCancelCheckoutClicked() {
        mSwipePlaceHolderView.undoLastSwipe();
    }


    /*
    Gets the events from the database and puts them into the SwipePlaceHolderView card stack.
    Requires a call to Parse database for the Event object type and will get all of the events
    and display them in the card stack.
     */
    private void queryEvents() {
        ParseQuery<Event> eventQuery = new ParseQuery<Event>(Event.class);
        //Toast.makeText(getContext(), "got into queryEvents", Toast.LENGTH_SHORT).show();
        eventQuery.include(Event.KEY_CREATOR);
        eventQuery.setLimit(5);
        eventQuery.findInBackground(new FindCallback<Event>() {
            @Override
            public void done(List<Event> event, ParseException e) {

                if (e != null) {
                    Log.d(TAG, "Error with Parse Query");
                    e.printStackTrace();
                    return;
                }

                for (int i = 0; i < event.size(); i++) {

                    // TODO -- call adding and removing views in a multithreading way, synchronized
                    // figure out if this call is safe or not
                    SwipeEventCard card = new SwipeEventCard(CardFragment.this.getContext(), event.get(i), cardViewHolderSize);
                    Event eventToSend = event.get(i);
                    card.setOnSwipeListener(new SwipeEventCard.SwipeListener() {
                        @Override
                        public void onSwiped() {
                            Bundle eventBundle = new Bundle();
                            eventBundle.putParcelable("Event", Parcels.wrap(eventToSend));
                            showCheckoutDialog(eventToSend);
                        }
                    });
                  mSwipePlaceHolderView.addView(card);

                  Log.d(TAG, "Post: "
                          + event.get(i).getKeyName()
                          + " Creator: "
                          + event.get(i).getCreator().getUsername());
                }
            }
        });
    }
}
