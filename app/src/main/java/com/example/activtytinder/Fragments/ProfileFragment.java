package com.example.activtytinder.Fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.activtytinder.LoginActivity;
import com.example.activtytinder.R;
import com.google.android.gms.location.LocationRequest;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;

import static android.app.Activity.RESULT_OK;

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
    public ImageView ivImage;
    private static final int REQUEST_IMAGE_GET = 1;
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
        ivImage = view.findViewById(R.id.ivImage);

        populateProfile();

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });

        btnUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
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

        ParseFile image = user.getParseFile("profileImage");
        String url;

        String security = "https";
        try {
            url = image.getUrl().substring(4);
        }
        catch (Exception e){
            return;
        }

        Log.d("DEBUG", "in setting image " + security + url);
        Glide.with(getContext())
                .load(security + url)
                .centerCrop()
                .placeholder(R.drawable.ic_launcher_foreground)
                .dontAnimate()
                .into(ivImage);
    }

    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_GET);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_GET && resultCode == RESULT_OK) {
            Uri photoUri = data.getData();
            Glide.with(getContext()).load(photoUri).into(ivImage);
            InputStream inputStream = null;
            try {
                inputStream = getActivity().getContentResolver().openInputStream(photoUri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Bitmap thumbnail = BitmapFactory.decodeStream(inputStream);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            thumbnail.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            byte[] image = outputStream.toByteArray();
            ParseFile file = new ParseFile("EVENT_IMAGE", image);
            user.put("profileImage", file);
            user.saveInBackground();
        }
    }
}


