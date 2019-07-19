package com.example.activtytinder;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ContactUsActivity extends AppCompatActivity {

    private Button btnsubmit;
    private TextView tvName;
    private TextView tvEmail;
    private ScrollView svCommentDetails;
    private TextView tvComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        btnsubmit = findViewById(R.id.btn_submit);
        tvName = findViewById(R.id.name_et);
        tvEmail = findViewById(R.id.email_et);
        svCommentDetails = findViewById(R.id.scComment);
        tvComment = findViewById(R.id.comment_et);



        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO -- find a way to get the info to us
            }
        });

    }
}
