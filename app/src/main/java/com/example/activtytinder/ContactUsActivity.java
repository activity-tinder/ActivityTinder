package com.example.activtytinder;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.activtytinder.Models.Feedback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

//TODO -- explain this activity
public class ContactUsActivity extends AppCompatActivity {

    private Button btnSubmit;
    private EditText etName;
    private EditText etEmail;
    private TextView tvComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        btnSubmit = findViewById(R.id.btn_submit);
        etName = findViewById(R.id.name_et);
        etEmail = findViewById(R.id.email_et);
        tvComment = findViewById(R.id.comment_et);


        //TODO -- move button to separate method
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Feedback feedback = new Feedback();
                final String description = tvComment.getText().toString();
                feedback.setKeySender(ParseUser.getCurrentUser().getUsername());
                feedback.setKeyDescription(description);
                feedback.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        Toast.makeText(ContactUsActivity.this, "Thank you for sending us feedback! We appreciate it!", Toast.LENGTH_LONG).show();
                        Log.d("ContactUs", "Thank you for sending us feedback! We appreciate it!");
                        etName.setText("");
                        etEmail.setText("");
                        tvComment.setText("");
                    }
                });


            }
        });

    }
}
