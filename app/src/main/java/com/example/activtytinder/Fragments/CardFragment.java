package com.example.activtytinder.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.activtytinder.Models.Event;
import com.example.activtytinder.R;
import com.mindorks.placeholderview.SwipeDecor;
import com.mindorks.placeholderview.SwipePlaceHolderView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import static com.example.activtytinder.Fragments.ProfileFragment.TAG;

public class CardFragment extends Fragment {

    // TODO -- change btn names
    ImageButton acceptBtn;
    ImageButton rejectBtn;

    SwipePlaceHolderView mSwipeView;
    List<Event> events;

    protected List<Event> mEvents;

    // TODO -- get rid of this?
    Context mContext;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_card, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        acceptBtn = view.findViewById(R.id.acceptBtn);
        rejectBtn = view.findViewById(R.id.rejectBtn);

        mSwipeView = view.findViewById(R.id.swipeView);
        mContext = getContext();
        events = new ArrayList<>();


        ParseQuery<Event> query = new ParseQuery<Event>(Event.class);
        query.include(Event.KEY_USER);
        query.setLimit(5);
        query.findInBackground(new FindCallback<Event>() {
            @Override
            public void done(List<Event> events, ParseException e) {
                if(e != null){
                    Log.d(TAG, "Error with Query");
                    e.printStackTrace();
                    return;
                }
                //mEvents.addAll(events);
                //EventAdapter.notify;
                for (int i = 0; i < events.size(); i++) {
                    Event event = events.get(i);
                    events.add(event);

                    Log.d(TAG, "Post: "
                            + event.getKeyName()
                            + " Creator: "
                            + events.get(i).getUser().getUsername());
                }
            }
        });

        mSwipeView.getBuilder()
                .setDisplayViewCount(3)
                .setHeightSwipeDistFactor(10)
                .setWidthSwipeDistFactor(5)
                .setSwipeDecor(new SwipeDecor()
                    .setPaddingTop(20)
                    .setRelativeScale(0.01f));

       for (Event event : events) {
            mSwipeView.addView(event);
        }

        //TODO -- figure out what v is
        acceptBtn.setOnClickListener(v -> {
            mSwipeView.doSwipe(true);
        });

        rejectBtn.setOnClickListener(v -> mSwipeView.doSwipe(false));

    }
}
