package com.example.activtytinder.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.activtytinder.EventCard;
import com.example.activtytinder.Models.Event;
import com.example.activtytinder.R;
import com.mindorks.placeholderview.SwipeDecor;
import com.mindorks.placeholderview.SwipePlaceHolderView;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;

public class CardFragment extends Fragment {

    ImageButton acceptBtn;
    ImageButton rejectBtn;

    SwipePlaceHolderView mSwipeView;
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

        mSwipeView.getBuilder()
                .setSwipeType(SwipePlaceHolderView.SWIPE_TYPE_HORIZONTAL)
                .setDisplayViewCount(3)
                .setSwipeDecor(new SwipeDecor()
                    .setPaddingTop(20)
                    .setRelativeScale(0.01f));

        ParseUser user = ParseUser.getCurrentUser();
        Event event = new Event();

        event.setKeyName("Biking around Seattle");
        event.setKeyLocation(new ParseGeoPoint(32, -122));
        event.setKeyCreator(user);
        event.setKeyDescription("We're biking around Seattle.");

        mSwipeView.addView(new EventCard(mContext, event, mSwipeView));

        acceptBtn.setOnClickListener(v -> mSwipeView.doSwipe(true));

        rejectBtn.setOnClickListener(v -> mSwipeView.doSwipe(false));

    }
}
