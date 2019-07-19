package com.example.activtytinder.Fragments;

import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.activtytinder.Models.Event;
import com.example.activtytinder.R;
import com.example.activtytinder.SwipeEventCard;
import com.example.activtytinder.Utils;
import com.mindorks.placeholderview.SwipeDecor;
import com.mindorks.placeholderview.SwipePlaceHolderView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.List;

import static com.example.activtytinder.Fragments.ProfileFragment.TAG;

public class CardFragment extends Fragment {

    // TODO -- change btn names
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

        int bottomMargin = Utils.dpToPx(160);
        Point windowSize = Utils.getDisplaySize(getActivity().getWindowManager());
        cardViewHolderSize = new Point(windowSize.x, windowSize.y - bottomMargin);

        mSwipePlaceHolderView = view.findViewById(R.id.swipeView);



        queryEvents();

        mSwipePlaceHolderView.getBuilder()
                .setDisplayViewCount(3)
                .setHeightSwipeDistFactor(10)
                .setWidthSwipeDistFactor(5)
                .setSwipeDecor(new SwipeDecor()
                    .setPaddingTop(20)
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

    /*
    Gets the events from the database and puts them into the SwipePlaceHolderView card stack.

     */
    private void queryEvents() {
        ParseQuery<Event> eventQuery = new ParseQuery<Event>(Event.class);
        Toast.makeText(getContext(), "got into queryEvents", Toast.LENGTH_SHORT).show();
        eventQuery.include(Event.KEY_CREATOR);
        eventQuery.setLimit(5);
        eventQuery.findInBackground(new FindCallback<Event>() {
            @Override
            public void done(List<Event> objects, ParseException kms) {

                if (kms != null) {
                    Log.d(TAG, "Error with Parse Query");
                    kms.printStackTrace();
                    return;
                }

                for (int i = 0; i < objects.size(); i++) {

                  mSwipePlaceHolderView.addView(new SwipeEventCard(CardFragment.this.getContext(), objects.get(i), cardViewHolderSize));

                  Log.d(TAG, "Post: "
                          + objects.get(i).getKeyName()
                          + " Creator: "
                          + objects.get(i).getCreator().getUsername());
                }
            }
        });
    }
}
