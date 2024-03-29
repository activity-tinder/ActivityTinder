package com.example.activtytinder.Fragments;

import android.content.Intent;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.activtytinder.CardUtils;
import com.example.activtytinder.MainActivity;
import com.example.activtytinder.Models.Event;
import com.example.activtytinder.R;
import com.example.activtytinder.SwipeEventCard;
import com.mindorks.placeholderview.SwipeDecor;
import com.mindorks.placeholderview.SwipePlaceHolderView;
import com.parse.FindCallback;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.example.activtytinder.Fragments.ProfileFragment.TAG;

/**
 * This class contains the card stack that shows the events users have created. Users are able to
 * swipe yes and no on cards in the stack, click the yes or no buttons
 */
public class CardFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private ImageButton btnAccept;
    private ImageButton btnReject;
    private ImageButton btnUndo;

    private Toolbar cardToolbar;
    //private ImageButton btnRefresh;

    //TODO -- add a spinner for filtering
    public String eventCategory;

    /**
     * Contains the objectIds of the events that the current user is planning to attend.
     */
    public ArrayList<String> userEventsAttending;
    public ArrayList<String> eventsRejected;


    //TODO-- Explain this viewholder
    public SwipePlaceHolderView mSwipePlaceHolderView;
    Point cardViewHolderSize;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_card, container, false);
    }

    /**
     * Finds buttons in xml by id and assign them programmatically. Initiates data structures used in this
     * fragment. Sets up SwipeCardViewHolder size and appearance. Initiates the button listeners. This
     * override method does not query the events in the card stack because the spinner automatically
     * queries the default card stack upon creation.
     * @param view
     * @param savedInstanceState
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnAccept = view.findViewById(R.id.btnAccept);
        btnReject = view.findViewById(R.id.btnReject);
        btnUndo = view.findViewById(R.id.btnUndo);

        userEventsAttending = new ArrayList<>();
        eventsRejected = new ArrayList<>();
        
        int bottomMargin = CardUtils.dpToPx(160);
        Point windowSize = CardUtils.getDisplaySize(getActivity().getWindowManager());
        cardViewHolderSize = new Point(windowSize.x, windowSize.y - bottomMargin);

        mSwipePlaceHolderView = view.findViewById(R.id.swipeView);

        btnListeners();
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_home_page, menu);

        MenuItem item = menu.findItem(R.id.realSpinner);
        Spinner spinner = (Spinner) item.getActionView();
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.filter_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.setting){
            MainActivity.openSettings();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Gets the events from the database and puts them into the SwipePlaceHolderView card stack.
     * Requires a call to Parse database for the Event object type and will get all of the events
     * and display them in the card stack. This function also contains a swipe listener for when the
     * card is swiped in, and the checkout menu for the card appears as an overlay.
     * Filters the queried events by events that have not occurred yet.
     * @param filter - A string that the query should use to filter the category of events shown in
     *                 the card stack.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void queryEvents(String filter) {

        ParseQuery<Event> eventQuery = new ParseQuery<Event>(Event.class);
        //Toast.makeText(getContext(), "got into queryEvents", Toast.LENGTH_SHORT).show();
        eventQuery.include(Event.KEY_CREATOR);
        eventQuery.orderByAscending("eventDate");
        /**
         * Queries into database of all events.
         */
        eventQuery.findInBackground(new FindCallback<Event>() {
            @Override
            public void done(List<Event> events, com.parse.ParseException e) {
                if (e != null) {
                    Log.d(TAG, "Error with Parse Query");
                    e.printStackTrace();
                    return;
                }
                //TODO make this a static method for any DateTime Format
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm a");
                LocalDateTime now = LocalDateTime.now();

                long currentMillis = CardFragment.this.getDateInMillis(dtf.format(now));

                ParseUser currentUser = ParseUser.getCurrentUser();


                ParseRelation<Event> rejectedEvents = currentUser.getRelation("swipedNo");
                ParseQuery<Event> queryReject = rejectedEvents.getQuery();

                queryReject.findInBackground(new FindCallback<Event>() {
                    @Override
                    public void done(List<Event> rejectedEvents, com.parse.ParseException e) {
                        for(int x = 0; x < rejectedEvents.size(); x++){
                            eventsRejected.add(rejectedEvents.get(x).getObjectId());
                        }
                    }
                });

                ParseRelation<Event> eventsAttending = currentUser.getRelation("willAttend");
                ParseQuery<Event> queryWillAttend = eventsAttending.getQuery();

                /**
                 * Queries into the list of events that the current user is already userEventsAttending.
                 */
                queryWillAttend.findInBackground(new FindCallback<Event>() {
                    @Override
                    public void done(List<Event> results, com.parse.ParseException attendingError) {

                        if (attendingError == null) {
                            /**
                             * Checks the events that the user is already attending in order for them
                             * not to show in the card stack.
                             */
                            for (int i = 0; i < results.size(); i++) {
                                userEventsAttending.add(results.get(i).getObjectId());
                            }

                            for (int i = 0; i < events.size(); i++) {

                                Event thisEvent = events.get(i);

                                String eventDateRaw = thisEvent.getKeyDate() + " " + thisEvent.getKeyStartTime();

                                long eventMillis = CardFragment.this.getDateInMillis(eventDateRaw);

                                if (filter.equals("Categories")) {
                                    if (!(userEventsAttending.contains(thisEvent.getObjectId())) && currentMillis < eventMillis && !(eventsRejected.contains(thisEvent.getObjectId()))) {
                                        // figure out if this call is safe or not
                                        SwipeEventCard card = new SwipeEventCard(CardFragment.this.getContext(), thisEvent, cardViewHolderSize);
                                        Event eventToSend = thisEvent;

                                        CardFragment.this.cardListeners(card, eventToSend);

                                        mSwipePlaceHolderView.addView(card);
                                    }
                                } else {
                                    if (!(userEventsAttending.contains(thisEvent.getObjectId())) && thisEvent.getCategory().equals(filter) && currentMillis < eventMillis && !(eventsRejected.contains(thisEvent.getObjectId()))) {
                                        // figure out if this call is safe or not
                                        SwipeEventCard card = new SwipeEventCard(CardFragment.this.getContext(), thisEvent, cardViewHolderSize);
                                        Event eventToSend = thisEvent;

                                        CardFragment.this.cardListeners(card, eventToSend);

                                        mSwipePlaceHolderView.addView(card);
                                    }
                                }
                            }
                        } else {
                            Log.d(TAG, "Error with getting user's currently userEventsAttending events.");
                            attendingError.printStackTrace();
                            return;
                        }
                    }
                });
            }
        });
        buildCardStack();
    }

    /**
     * Creates and formats card stack properties.
     */
    public void buildCardStack() {
        mSwipePlaceHolderView.getBuilder()
                .setDisplayViewCount(5)
                .setHeightSwipeDistFactor(10)
                .setWidthSwipeDistFactor(5)
                .setSwipeType(1)
                .setIsUndoEnabled(true)
                .setSwipeDecor(new SwipeDecor()
                        .setPaddingTop(15)
                        .setMarginTop(-120)
                        .setRelativeScale(0.01f));
    }

    /**
     * When an item in the spinner is selected for filtering
     * @param adapterView
     * @param view
     * @param i
     * @param l
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        adapterView.getItemAtPosition(i);
        eventCategory = adapterView.getItemAtPosition(i).toString();
        mSwipePlaceHolderView.removeAllViews();
        queryEvents(eventCategory);
    }

    //TODO -- explain this pls
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

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

    /**
     * Turns a date and time in the form of a string into milliseconds for accurate comparisons.
     * @param parsedString - A string that contains the date to be parsed and converted into a long.
     * @return A long that represents the time given in milliseconds relative to the UTC.
     */
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
    }
}
