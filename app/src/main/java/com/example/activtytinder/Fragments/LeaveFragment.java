package com.example.activtytinder.Fragments;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.activtytinder.CardUtils;
import com.example.activtytinder.Models.Event;
import com.example.activtytinder.Models.Tools;
import com.example.activtytinder.R;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LeaveFragment extends DialogFragment {

    private Button btnYes;
    private Button btnNo;
    private Event event;
    private EditText etPasswordInput;
    public TextView tvScore;

    public LeaveFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
    }

    public static LeaveFragment newInstance(Event event){
        LeaveFragment fragment = new LeaveFragment();
        Bundle eventBundle = new Bundle();
        eventBundle.putParcelable("Event", Parcels.wrap(event));
        fragment.setArguments(eventBundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_leave, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnYes = view.findViewById(R.id.btnYes);
        btnNo = view.findViewById(R.id.btnNo);
        etPasswordInput = view.findViewById(R.id.etPasswordInput);
        tvScore = view.findViewById(R.id.tvScore);
        getDialog().setCanceledOnTouchOutside(false);
        Bundle eventBundle = this.getArguments();
        if(eventBundle != null){
            event = Parcels.unwrap(eventBundle.getParcelable("Event"));
        }


        btnYes.setOnClickListener(view1 -> {
            ParseUser user = ParseUser.getCurrentUser();
            user.logInInBackground(user.getUsername(), etPasswordInput.getText().toString(), new LogInCallback() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void done(ParseUser user, ParseException e) {
                    if (e == null) {
                        CardUtils.removeUserFromEvent(user, event);
                        String eventDateRaw = event.getKeyDate() + " " + event.getKeyStartTime();
                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm a");
                        LocalDateTime now = LocalDateTime.now();
                        long currentMillis = Tools.getDateInMillis(dtf.format(now));
                        long eventMillis = Tools.getDateInMillis(eventDateRaw);
                        if (eventMillis - currentMillis < 86400000 && (Integer)user.get("reliabilityScore") > 0) {
                            int currentScore = (Integer) user.get("reliabilityScore");
                            currentScore -= 10;
                            user.put("reliabilityScore", currentScore);
                            user.saveInBackground();
                        }
                        FragmentManager fragmentManager = getFragmentManager();
                        fragmentManager.popBackStackImmediate();
                        LeaveFragment.this.dismiss();
                        Fragment fragment = new ProfileFragment();
                        fragmentManager.beginTransaction().addToBackStack("Cards").replace(R.id.flContainer, fragment ).commit();
                    } else {
                        Toast.makeText(getContext(),"Incorrect Password! Try again or don't leave!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        });

        btnNo.setOnClickListener(view12 -> LeaveFragment.this.dismiss());
    }
}
