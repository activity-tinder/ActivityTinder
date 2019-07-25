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
import com.parse.ParseQuery;

import org.parceler.Parcels;

import static com.example.activtytinder.Fragments.ProfileFragment.TAG;

//TODO -- documentation for class
public class CardFragment extends Fragment {

    private ImageButton btnAccept;
    private ImageButton btnReject;


    public SwipePlaceHolderView mSwipePlaceHolderView;
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
                    .setDisplayViewCount(4)
                    .setHeightSwipeDistFactor(8)
                    .setWidthSwipeDistFactor(5)
                    .setIsUndoEnabled(true)
                    .setSwipeDecor(new SwipeDecor()
                        .setPaddingTop(15)
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

    /**
     * Creates an overlaid checkout fragment instance
     * @param event - the event that the checkout fragment should contain the information of
     */
    public void showCheckoutDialog(Event event) {
        FragmentManager fragmentManager = getFragmentManager();
        CheckoutFragment checkoutDialogFragment = CheckoutFragment.newInstance("Event", event);
        checkoutDialogFragment.show(fragmentManager, "CheckoutFragment");

        checkoutDialogFragment.setOnBtnNoListener(new CheckoutFragment.BtnNoListener() {
            @Override
            public void onNoClicked() {
                mSwipePlaceHolderView.undoLastSwipe();
            }
        });
    }

    private void showDetailFragment(Event event) {
        FragmentManager fragmentManager = getFragmentManager();
        DetailsFragment detailsDialogFragment = DetailsFragment.newInstance("Event", event);
        detailsDialogFragment.show(fragmentManager, "CheckoutFragment");

    }
    //TODO -- documentation for the method
   public void onCancelCheckoutClicked() {
        mSwipePlaceHolderView.undoLastSwipe();
    }


    /**
     * Gets the events from the database and puts them into the SwipePlaceHolderView card stack.
     * Requires a call to Parse database for the Event object type and will get all of the events
     * and display them in the card stack. This function also contains a swipe listener for when the
     * card is swiped in, and the checkout menu for the card appears as an overlay.
     */
    private void queryEvents() {
        ParseQuery<Event> eventQuery = new ParseQuery<Event>(Event.class);
        //Toast.makeText(getContext(), "got into queryEvents", Toast.LENGTH_SHORT).show();
        eventQuery.include(Event.KEY_CREATOR);
        eventQuery.findInBackground((event, e) -> {

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

                /**
                 * Listens for card being swiped affirmative and opens a checkout dialog overlay.
                 */
                card.setOnSwipeListener(new SwipeEventCard.SwipeListener() {
                    @Override
                    public void onSwiped() {
                        Bundle eventBundle = new Bundle();
                        eventBundle.putParcelable("Event", Parcels.wrap(eventToSend));
                        CardFragment.this.showCheckoutDialog(eventToSend);
                    }
                });

                /**
                 * Listens for card being clicked and opens the card detail overlay.
                 */
                card.setOnClickListener(new SwipeEventCard.onClickListener() {
                    @Override
                    public void onClick() {
                        Bundle eventBundle = new Bundle();
                        eventBundle.putParcelable("Event", Parcels.wrap(eventToSend));
                        showDetailFragment(eventToSend);
                    }
                });
              mSwipePlaceHolderView.addView(card);
            }
        });
    }



}
