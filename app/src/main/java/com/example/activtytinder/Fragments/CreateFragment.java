package com.example.activtytinder.Fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.activtytinder.CardUtils;
import com.example.activtytinder.LocationManager;
import com.example.activtytinder.Models.Event;
import com.example.activtytinder.R;
import com.example.activtytinder.Tools;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

public class CreateFragment extends Fragment implements AdapterView.OnItemSelectedListener{

    private EditText etEventName;
    private EditText etEventDescription;
    private EditText etEventDate;
    private EditText etEventStartTime;
    private EditText etEventEndTime;
    private EditText etEventAddress;
    private EditText etEventMaxPeople;
    private Button btnCreateEvent;
    private Button btnGetEventLocation;
    private ParseGeoPoint gpEventCoordinates;
    private Spinner spinner;
    private String searchQuery;
    private String API_KEY;
    private String eventCategory;
    private ImageView ivImage;
    private ParseFile eventImageFile;
    private Event myEvent;
    private String parseEventDate;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etEventName = view.findViewById(R.id.etEventName);
        etEventDescription = view.findViewById(R.id.etEventDescription);
        etEventDate = view.findViewById(R.id.etEventDate);
        etEventStartTime = view.findViewById(R.id.etStartTime);
        etEventEndTime = view.findViewById(R.id.etEndTime);
        etEventAddress = view.findViewById(R.id.etEventLocation);
        etEventMaxPeople = view.findViewById(R.id.etPeopleLimit);
        btnCreateEvent = view.findViewById(R.id.btnCreateEvent);
        btnGetEventLocation = view.findViewById(R.id.btnConfirmLocation);
        ivImage = view.findViewById(R.id.ivImage);

        spinner = view.findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.categories_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        API_KEY = getActivity().getResources().getString(R.string.mapquest_api_key);
        etEventDate.setInputType(InputType.TYPE_NULL);

        btnListeners();

        //TODO -- put button in separate method

    }

    /**
     * When called, takes all the values from the edit texts that user filled out and puts them on the Parse database after
     * verifying that all fields have an appropriate value in them. The method then clears all fields when done uploading
     * the data so that the user can create a new event if they wish to.
     * @param Name - String of the Event Name
     * @param Description - String of the Event Description
     * @param Date - String of the date of the event in MM/DD/YYYY format
     * @param StartTime - String of the start time of the event
     * @param EndTime - String of the end time of the event
     * @param Address - String of the event address in the format of Street Address, City, State
     * @param PeopleLimit - Integer of the maximum amount of people who can attend the event
     * @param EventCoordinates - ParseGeoPoint of the latitude and longitude of the event location
     * @param Category - String of the category of the event by which it is classified
     * @param EventPhoto -  ParseFile of the image used to be seen with the event
     * @return - Returns the event that has been created
     */
    @SuppressLint("NewApi")
    private Event makeEvent(String Name, String Description, String Date, String StartTime, String EndTime, String Address,
                            Integer PeopleLimit, ParseGeoPoint EventCoordinates, String Category, ParseFile EventPhoto)
    {
        if (Name.equals("") || Description.equals("") || Date.equals("") || StartTime.equals("") || EndTime.equals("")
                || Address.equals("") || Category.equals("")) {
            Toast.makeText(getContext(), "CANNOT LEAVE FIELD EMPTY!", Toast.LENGTH_SHORT).show();
            return  null;
        }else if(PeopleLimit == null || EventCoordinates == null || !Address.equals(LocationManager.get().getCorrectAddress())){
            Toast.makeText(getContext(), "ENTER VALID LOCATION!",Toast.LENGTH_SHORT).show();
            return  null;
        }else if (EventPhoto == null){
            Toast.makeText(getContext(), "MUST PROVIDE AN EVENT PHOTO!", Toast.LENGTH_SHORT).show();
            return  null;
        }else if (Category.equals("Choose Category")){
            Toast.makeText(getContext(),"MUST CHOOSE A CATEGORY!", Toast.LENGTH_SHORT).show();
            return  null;
        }
        Event event = new Event();
        event.setKeyCreator(ParseUser.getCurrentUser());
        event.setKeyName(Name);
        event.setKeyDescription(Description);
        event.setKeyDate(Date);
        event.setKeyStartTime(StartTime);
        event.setKeyEndTime(EndTime);
        event.setKeyAddress(Address);
        event.setKeyLimit(PeopleLimit);
        event.setKeyLocation(EventCoordinates);
        event.setKeyCategory(Category);
        event.setKeyImage(EventPhoto);

        event.saveInBackground(e -> {
            if (e != null) {
                Log.d("Create Fragment", "Error while saving");
                Toast.makeText(getContext(),"Error while Saving!", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
                return;
            }
            etEventAddress.setText("");
            etEventName.setText("");
            etEventDescription.setText("");
            etEventDate.setText("");
            etEventStartTime.setText("");
            etEventEndTime.setText("");
            etEventMaxPeople.setText("");
            ivImage.setImageResource(0);


            CardUtils.addUserToEvent(ParseUser.getCurrentUser(), myEvent);
            Toast.makeText(CreateFragment.this.getContext(), "Event Creation Successful!", Toast.LENGTH_SHORT).show();
        });
        return event;
    }

    /**
     * Allows user to access their media to select a photo. The method fires an intent that lets the
     * user choose their photo from anywhere in their devices's storage used in selecting profile
     * and event photos.
     */
    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(intent, 1);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            adapterView.getItemAtPosition(i);
            eventCategory = adapterView.getItemAtPosition(i).toString();
    }

    /**
     * Required method for use of the spinner to select event category.
     * @param adapterView - adapterView of the spinner in question
     */
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    /**
     * Method that awaits a result from getting a photo. If the appropriate data is received, the
     * image will be loaded into the ivImage to be displayed.
     * @param requestCode - requestCode for selecting an image from the gallery
     * @param resultCode - RESULT_OK for successful result
     * @param data - data retrieved from the photo selected
     */
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
            //TODO: Keep aspect ratio of image
            thumbnail.createScaledBitmap(thumbnail, 350, 350, true);
            thumbnail.compress(Bitmap.CompressFormat.JPEG, 70, outputStream);
            byte[] image = outputStream.toByteArray();
            ParseFile file = new ParseFile("EVENT_IMAGE", image);
            eventImageFile = file;
        }
    }

    /**
     * Initializes buttons in this fragment.
     */
    private void btnListeners() {
        etEventDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View btnEventDate) {
                Tools.getDate(CreateFragment.this.getContext(), etEventDate);

            }
        });

        etEventStartTime.setOnClickListener(btnEventStartTime -> Tools.getTime(getContext(), etEventStartTime));

        etEventEndTime.setOnClickListener(btnEventEndTime -> { Tools.getTime(getContext(),etEventEndTime);});

        ivImage.setOnClickListener(btnIvImage -> selectImage());

        btnGetEventLocation.setOnClickListener(btnEventLocation -> {
            searchQuery = etEventAddress.getText().toString();
            LocationManager.get().getLocationAddress(searchQuery, API_KEY, etEventAddress, getContext());
        });

        btnCreateEvent.setOnClickListener(btnCreateEvent -> {
            if (etEventMaxPeople.getText().toString().equals("")) {
                Toast.makeText(CreateFragment.this.getContext(), "Please enter valid amount of people!", Toast.LENGTH_SHORT).show();
                return;
            }
            String currentDate = etEventDate.getText().toString();
            Date newDate = null;
            SimpleDateFormat spf = new SimpleDateFormat("EEE, d MMM yyyy");
            try {
                newDate = spf.parse(currentDate);
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }
            spf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
            currentDate = spf.format(newDate);
            parseEventDate = currentDate;
            gpEventCoordinates = LocationManager.get().getLocationCoordinates();
            final String EventName = etEventName.getText().toString();
            final String EventDescription = etEventDescription.getText().toString();
            final String EventDate = parseEventDate;
            final String StartTime = etEventStartTime.getText().toString();
            final String EndTime = etEventEndTime.getText().toString();
            final String Address = etEventAddress.getText().toString();
            final String Category = eventCategory;
            final Integer PeopleLimit = Integer.parseInt(etEventMaxPeople.getText().toString());
            final ParseGeoPoint EventCoordinates = gpEventCoordinates;
            final ParseFile EventPhoto = eventImageFile;
            myEvent = CreateFragment.this.makeEvent(EventName, EventDescription, EventDate, StartTime, EndTime, Address, PeopleLimit, EventCoordinates, Category, EventPhoto);
        });
    }
}
