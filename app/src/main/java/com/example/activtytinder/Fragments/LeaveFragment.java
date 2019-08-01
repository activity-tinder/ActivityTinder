package com.example.activtytinder.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.activtytinder.CardUtils;
import com.example.activtytinder.Models.Event;
import com.example.activtytinder.R;
import com.parse.ParseUser;

import org.parceler.Parcels;

public class LeaveFragment extends DialogFragment {

    private Button btnYes;
    private Button btnNo;
    private Event event;
    private BtnNoListener btnNoListener;
    private EditText etPasswordInput;

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

    /**
     * Listener interface for detecting if the no button in the checkout fragment is clicked.
     */
    public interface BtnNoListener {
        void onNoClicked();
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
        getDialog().setCanceledOnTouchOutside(false);
        Bundle eventBundle = this.getArguments();
        if(eventBundle != null){
            event = Parcels.unwrap(eventBundle.getParcelable("Event"));
        }


        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {
                ParseUser user = ParseUser.getCurrentUser();
                if(etPasswordInput.getText().toString().equals(user.get("password"))) {
                    CardUtils.removeUserFromEvent(user, event);
                    LeaveFragment.this.dismiss();
                }else{
                    Toast.makeText(getContext(),"Incorrect Password! Try again or don't leave!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view12) {
                LeaveFragment.this.dismiss();
            }
        });




    }


}
