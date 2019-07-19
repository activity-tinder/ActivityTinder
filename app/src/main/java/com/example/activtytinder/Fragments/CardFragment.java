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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.activtytinder.Fragments.ProfileFragment.TAG;

public class CardFragment extends Fragment {

    // TODO -- change btn names
    private ImageButton btnAccept;
    private ImageButton btnReject;

    SwipePlaceHolderView mSwipePlaceHolderView;
    List<Event> events;


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

        events = new ArrayList<>();

        mSwipePlaceHolderView = view.findViewById(R.id.swipeView);

        int bottomMargin = Utils.dpToPx(160);
        Point windowSize = Utils.getDisplaySize(getActivity().getWindowManager());
        Point cardViewHolderSize = new Point(windowSize.x, windowSize.y - bottomMargin);

        queryPosts(cardViewHolderSize);

        mSwipePlaceHolderView.getBuilder()
                .setDisplayViewCount(3)
                .setHeightSwipeDistFactor(10)
                .setWidthSwipeDistFactor(5)
                .setSwipeDecor(new SwipeDecor()
                    .setPaddingTop(20)
                    .setRelativeScale(0.01f));

//        Point cardViewHolderSize = new Point(windowSize.x, windowSize.y - bottomMargin);

//        System.out.println(events.toString() + "oof");
//
//        for (Event event : events) {
//            mSwipePlaceHolderView.addView(new SwipeEventCard(getContext(), event, cardViewHolderSize, (SwipeEventCard.Callback) this));
//        }

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

    private void queryPosts(Point cardViewHolderSize) {
        ParseQuery<Event> eventQuery = new ParseQuery<Event>(Event.class);
        Toast.makeText(getContext(), "got into queryPosts", Toast.LENGTH_SHORT).show();
        eventQuery.include(Event.KEY_CREATOR);
        eventQuery.setLimit(5);
        eventQuery.findInBackground(new FindCallback<Event>() {
            @Override
            public void done(List<Event> objects, ParseException kms) {
                Toast.makeText(getContext(), "got into done", Toast.LENGTH_SHORT).show();
                if (kms != null) {
                    Toast.makeText(getContext(), "got into check", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Error with Parse Query");
                    kms.printStackTrace();
                    return;
                }
                Toast.makeText(getContext(), "got over done", Toast.LENGTH_SHORT).show();
                events.addAll(objects);
                Log.d(TAG, "Posts: " + Arrays.toString(events.toArray()));

                for (int i = 0; i < objects.size(); i++) {
//                Event event = objects.get(i);
//                events.add(event)
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
