package com.example.activtytinder.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.activtytinder.R;

public class CheckoutFragment extends DialogFragment {

    private TextView tvQuestion;
    private TextView tvEventDetails;
    private Button btnYes;
    private Button btnNo;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_checkout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvQuestion = view.findViewById(R.id.tvQuestion);
        tvEventDetails = view.findViewById(R.id.tvEventDetails);
        btnYes = view.findViewById(R.id.btnYes);
        btnNo = view.findViewById(R.id.btnNo);


        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO -- take them to the receipt fragment page
            }
        });

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO -- close overlay and take them back to the cards
            }
        });
    }
}
