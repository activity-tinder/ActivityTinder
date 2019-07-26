package com.example.activtytinder.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.activtytinder.LoginActivity;
import com.example.activtytinder.Models.Event;
import com.example.activtytinder.ProfileAdapter;
import com.example.activtytinder.R;
import com.google.android.gms.location.LocationRequest;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment{

    ParseUser user = ParseUser.getCurrentUser();
    public Button btnLogout;
    public ProfileAdapter adapter;
    List<Event> mEvents;
    public RecyclerView rvProfile;
    public TextView tvName;
    public TextView tvUsername;
    public TextView tvScore;
    public TextView tvHomeCity;
    public static final String TAG = "ProfileFragment";
    private LocationRequest mLocationRequest;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false); //returns appropriate view
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        btnLogout = view.findViewById(R.id.logout_btn);
        rvProfile = view.findViewById(R.id.rvEvents);
        mEvents = new ArrayList<>();





        adapter = new ProfileAdapter(getContext(), mEvents);
        rvProfile.setLayoutManager(new LinearLayoutManager(getContext()));
        rvProfile.setAdapter(adapter);
        tvName = view.findViewById(R.id.tvName);
        tvUsername = view.findViewById(R.id.tvUsername);
        tvScore = view.findViewById(R.id.tvScore);
        tvHomeCity = view.findViewById(R.id.tvHomeCity);

        populateProfile();

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });

    }

    private void logout() {
        ParseUser.logOut();
        ParseUser currentUser = ParseUser.getCurrentUser(); // this will now be null
        final Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    public void populateProfile(){
        tvName.setText(user.getString("name"));
        tvUsername.setText(user.getUsername());
        tvScore.setText(user.getNumber("reliabilityScore").toString());
        tvHomeCity.setText(user.getString("homeCity"));
    }

    public void populateEventAdapter(){
        ParseQuery<ParseUser> query = ParseQuery.getQuery(ParseUser.class);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null){
                    JSONArray eventsToAttend = ParseUser.getCurrentUser().getJSONArray("willAttend");
                    for(int x = 0; x < eventsToAttend.length(); x++){
                        try {
                            Event event =(Event) eventsToAttend.get(x);
                            mEvents.add(event);
                            adapter.notifyItemInserted(mEvents.size() -1);
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                    }
                }else{
                    Log.e("PostsFragment", "Error with query");
                    e.printStackTrace();
                    return;
                }
            }
        });
    }


//        query.getInBackground(ParseUser.getCurrentUser().getObjectId(), (ParseUser parseUser, ParseException error) -> {
//            if(error == null){
//                JSONArray eventsToAttend = ParseUser.getCurrentUser().getJSONArray("willAttend");


}


