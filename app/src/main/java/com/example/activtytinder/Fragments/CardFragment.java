package com.example.activtytinder.Fragments;

import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
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

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

import static com.example.activtytinder.Fragments.ProfileFragment.TAG;

//TODO -- documentation for class
public class CardFragment extends Fragment {

    private ImageButton btnAccept;
    private ImageButton btnReject;
    private ImageButton btnUndo;
    private ImageButton btnRefresh;

    private ConstraintLayout clCardStack;

    public SwipePlaceHolderView mSwipePlaceHolderView;
    Point cardViewHolderSize;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_card, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        btnAccept = view.findViewById(R.id.btnAccept);
        btnReject = view.findViewById(R.id.btnReject);
        btnUndo = view.findViewById(R.id.btnUndo);
        btnRefresh = view.findViewById(R.id.btnRefresh);

        clCardStack = view.findViewById(R.id.clCardStack);

        int bottomMargin = CardUtils.dpToPx(160);
        Point windowSize = CardUtils.getDisplaySize(getActivity().getWindowManager());
        cardViewHolderSize = new Point(windowSize.x, windowSize.y - bottomMargin);

        mSwipePlaceHolderView = view.findViewById(R.id.swipeView);

        queryEvents();

        btnListeners();
    }

    /**
     * Gets the events from the database and puts them into the SwipePlaceHolderView card stack.
     * Requires a call to Parse database for the Event object type and will get all of the events
     * and display them in the card stack. This function also contains a swipe listener for when the
     * card is swiped in, and the checkout menu for the card appears as an overlay.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void queryEvents() {
        ParseQuery<Event> eventQuery = new ParseQuery<Event>(Event.class);
        //Toast.makeText(getContext(), "got into queryEvents", Toast.LENGTH_SHORT).show();
        eventQuery.include(Event.KEY_CREATOR);
        eventQuery.orderByAscending("eventDate");
        eventQuery.findInBackground((event, e) -> {
            if (e != null) {
                Log.d(TAG, "Error with Parse Query");
                e.printStackTrace();
                return;
            }

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm a");
            LocalDateTime now = LocalDateTime.now();

            long currentMillis = getDateInMillis(dtf.format(now));

            for (int i = 0; i < event.size(); i++) {

                String eventDateRaw = event.get(i).getKeyDate() + " " + event.get(i).getKeyStartTime();

                long eventMillis = getDateInMillis(eventDateRaw);

                if (currentMillis < eventMillis) {
                    // figure out if this call is safe or not
                    SwipeEventCard card = new SwipeEventCard(CardFragment.this.getContext(), event.get(i), cardViewHolderSize);
                    Event eventToSend = event.get(i);

                    // TODO -- figure out how to dynamically set colors
//                if (event.get(i).getCategory().equals("Active") && event.get(i).getCategory() != null) {
//                    clCardStack.setBackgroundColor(23163377);
//                }

                    cardListeners(card, eventToSend);

                    mSwipePlaceHolderView.addView(card);
                }
            }
        });

        buildCardStack();
    }

    /**
     * Creates and formats card stack properties.
     */
    public void buildCardStack() {
        mSwipePlaceHolderView.getBuilder()
                .setDisplayViewCount(4)
                .setHeightSwipeDistFactor(8)
                .setWidthSwipeDistFactor(5)
                .setIsUndoEnabled(true)
                .setSwipeDecor(new SwipeDecor()
                        .setPaddingTop(15)
                        .setRelativeScale(0.01f));
    }

    /**
     * Creates an overlaid checkout fragment instance
     * @param event - the event that the checkout fragment should contain the information of
     */
    public void showCheckoutDialog(Event event) {
        FragmentManager fragmentManager = getFragmentManager();
        CheckoutFragment checkoutDialogFragment = CheckoutFragment.newInstance("Event", event);
        checkoutDialogFragment.show(fragmentManager, "CheckoutFragment");

        /**
         * Undoes the user's last swipe and returns the card if the user clicks no in the
         * checkout fragment.
         */
        checkoutDialogFragment.setOnBtnNoListener(() -> mSwipePlaceHolderView.undoLastSwipe());
    }

    /**
     * Shows and overlaid fragment that contains more details about the event that the user clicked
     * on.
     * @param event - event that should be shown in the detail fragment
     */
    private void showDetailFragment(Event event) {
        FragmentManager fragmentManager = getFragmentManager();
        DetailsFragment detailsDialogFragment = DetailsFragment.newInstance("Event", event);
        detailsDialogFragment.show(fragmentManager, "CheckoutFragment");
    }

    private long getDateInMillis(String parsedString) {

        Date currentDate = null;
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm a", Locale.US);
        try {
            currentDate = sdf.parse(parsedString);
        } catch (ParseException e1) {
            e1.printStackTrace();
        }
        long millis = currentDate.getTime();
        return millis;
    }

    /**
     * Initializes card action listeners.
     * @param card - the specific card the listener is being created for
     * @param eventToSend - the event that the card is associated with
     */
    private void cardListeners(SwipeEventCard card, Event eventToSend) {
        /**
         * Listens for card being swiped affirmative and opens a checkout dialog overlay.
         */
        card.setOnSwipeListener(() -> {
            Bundle eventBundle = new Bundle();
            eventBundle.putParcelable("Event", Parcels.wrap(eventToSend));
            CardFragment.this.showCheckoutDialog(eventToSend);
        });

        /**
         * Listens for card being clicked and opens the card detail overlay.
         */
        card.setOnClickListener(() -> {
            Bundle eventBundle = new Bundle();
            eventBundle.putParcelable("Event", Parcels.wrap(eventToSend));
            showDetailFragment(eventToSend);
        });
    }

    /**
     * Initializes buttons in this fragment.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void btnListeners() {
        btnAccept.setOnClickListener(btn -> {
            //Log.d(TAG, "accept clicked!");
            mSwipePlaceHolderView.doSwipe(true);
        });

        btnReject.setOnClickListener(btn -> {
            //Log.d(TAG, "reject clicked!");
            mSwipePlaceHolderView.doSwipe(false);
        });

        btnUndo.setOnClickListener(btn -> {
            //Log.d(TAG, "undo clicked!");
            mSwipePlaceHolderView.undoLastSwipe();
        });

        btnRefresh.setOnClickListener(view -> {
            //Log.d(TAG, "refresh clicked");
            mSwipePlaceHolderView.removeAllViews();
            queryEvents();
        });
    }
}
