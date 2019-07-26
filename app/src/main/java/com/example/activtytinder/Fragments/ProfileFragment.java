package com.example.activtytinder.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.activtytinder.LoginActivity;
import com.example.activtytinder.R;
import com.google.android.gms.location.LocationRequest;
import com.parse.ParseUser;

import java.text.SimpleDateFormat;

public class ProfileFragment extends Fragment{

    ParseUser user = ParseUser.getCurrentUser();
    public Button btnLogout;
    public Button btnGetLocation;
    public TextView tvName;
    public TextView tvUsername;
    public TextView tvEmail;
    public TextView tvScore;
    public TextView tvAge;
    public TextView tvHomeCity;
    public Button btnTakeImage;
    public Button btnUploadImage;
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
        tvName = view.findViewById(R.id.tvName);
        tvUsername = view.findViewById(R.id.tvUsername);
        tvEmail = view.findViewById(R.id.tvEmail);
        tvScore = view.findViewById(R.id.tvScore);
        tvAge = view.findViewById(R.id.tvAge);
        tvHomeCity = view.findViewById(R.id.tvHomeCity);
        btnTakeImage = view.findViewById(R.id.btnTakeImage);
        btnUploadImage = view.findViewById(R.id.btnUploadImage);

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
        tvEmail.setText(user.getEmail());
        tvScore.setText(user.getNumber("reliabilityScore").toString());
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        tvAge.setText(formatter.format(user.getDate("birthday")));
        tvHomeCity.setText(user.getString("homeCity"));
    }
}


