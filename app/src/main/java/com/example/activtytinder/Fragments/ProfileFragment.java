package com.example.activtytinder.Fragments;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.activtytinder.Models.Event;
import com.example.activtytinder.ProfileAdapter;
import com.example.activtytinder.R;
import com.example.activtytinder.Tools;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment{

    private ParseUser user = ParseUser.getCurrentUser();
    private SwipeRefreshLayout swipeContainer;
    private ProfileAdapter adapter;
    private List<Event> mEvents;
    private RecyclerView rvProfile;
    private TextView tvName;
    private TextView tvUsername;
    public static TextView tvScore;
    private TextView tvHomeCity;
    private Button btnTakeImage;
    private Button btnUploadImage;
    private ImageView ivImage;
//    public SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
    private File photoFile;
    private final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    private String photoFileName = "photo.jpg";
    byte[] image;



    static final String TAG = "ProfileFragment";

    public ProfileFragment() {
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false); //returns appropriate view
    }

    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        rvProfile = view.findViewById(R.id.rvEvents);
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);

        mEvents = new ArrayList<>();
        swipeContainer.setOnRefreshListener(() -> fetchEventsAsync(0));

        //TODO -- change refresh colors to color scheme for app
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        adapter = new ProfileAdapter(getContext(), mEvents);
        rvProfile.setLayoutManager(new LinearLayoutManager(getContext()));
        rvProfile.setAdapter(adapter);
        tvName = view.findViewById(R.id.tvName);
        tvUsername = view.findViewById(R.id.tvUsername);
        tvScore = view.findViewById(R.id.tvScore);
        tvHomeCity = view.findViewById(R.id.tvHomeCity);
        btnTakeImage = view.findViewById(R.id.btnTakeImage);
        btnUploadImage = view.findViewById(R.id.btnUploadImage);
        ivImage = view.findViewById(R.id.ivProfilePicture);
        btnTakeImage.setFontFeatureSettings("smcp");

        populateProfile();
        populateEventAdapter();



        tvScore.setText(user.getNumber("reliabilityScore").toString());

        //TODO -- create separate method for buttons

        btnUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View btnUploadImage) {
                selectImage();
            }
        });

        btnTakeImage.setOnClickListener(btnTakeImage -> verifyCameraPermission());


    }

    //TODO -- explain this
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void fetchEventsAsync(int i) {
        adapter.clear();
        populateEventAdapter();
        swipeContainer.setRefreshing(false);
    }

    //TODO -- explain this
    private void populateProfile(){
        user = ParseUser.getCurrentUser();
        tvName.setText(user.getString("name"));
        tvUsername.setText(user.getUsername());
        tvScore.setText(user.getNumber("reliabilityScore").toString());
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
                .placeholder(R.drawable.ic_launcher_foreground)
                .apply(RequestOptions.circleCropTransform())
                .dontAnimate()
                .into(ivImage);
    }

    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(intent, 1);
        }
    }

    private void verifyCameraPermission() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        } else {
            Log.e(TAG, "PERMISSION GRANTED");
            launchCamera();
        }
    }

    private void launchCamera(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        photoFile = getPhotoFileUri(photoFileName);
        Uri fileProvider = FileProvider.getUriForFile(getContext(), "com.codepath.fileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);
        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    public File getPhotoFileUri(String fileName) {
        File mediaStorageDir = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(TAG, "failed to create directory");
        }
        File file = new File(mediaStorageDir.getPath() + File.separator + fileName);
        return file;
    }

    //TODO -- explain this
    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void populateEventAdapter(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm a");
        LocalDateTime now = LocalDateTime.now();

        long currentMillis = Tools.getDateInMillis(dtf.format(now));
        ParseRelation<Event> eventsToAttend = user.getRelation("willAttend");
        ParseQuery<Event> eventQuery = eventsToAttend.getQuery();
        eventQuery.orderByAscending("eventDate");
        eventQuery.findInBackground((events, e) -> {
            for(int x= 0; x < events.size(); x++){
                Event thisEvent = events.get(x);

                String eventDateRaw = thisEvent.getKeyDate() + " " + thisEvent.getKeyStartTime();

                long eventMillis = Tools.getDateInMillis(eventDateRaw);
                if (currentMillis < eventMillis) {
                    // figure out if this call is safe or not
                    mEvents.add(thisEvent);
                }
            }
            adapter.notifyDataSetChanged();
        });
    }

    //TODO -- explain this

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
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
            thumbnail.createScaledBitmap(thumbnail, 350, 350, true);
            thumbnail.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            image = outputStream.toByteArray();
            saveImage();
        }
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Bitmap takenImage = Tools.rotateBitmapOrientation(photoFile.getAbsolutePath());
                takenImage.createScaledBitmap(takenImage, 350, 350, true);
                ivImage.setImageBitmap(takenImage);
                user.put("profileImage", new ParseFile(photoFile));
                user.saveInBackground();
            } else { // Result was a failure
                Toast.makeText(getContext(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }



    public void saveImage(){
        ParseFile file = new ParseFile("EVENT_IMAGE", image);
        user.put("profileImage", file);
        user.saveInBackground();
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            launchCamera();
        }
    }
}
