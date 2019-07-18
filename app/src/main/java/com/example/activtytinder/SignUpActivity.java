package com.example.activtytinder;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.activtytinder.Models.User;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class SignUpActivity  extends AppCompatActivity {

    private EditText nameOfPerson;
    private EditText email;
    private EditText birthday;
    private EditText baseLocation;
    private EditText usernameInput;
    private EditText passwordInput;
    private Button createAccount;
    private DatePickerDialog picker;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        nameOfPerson = (EditText) findViewById(R.id.etName);
        email = (EditText) findViewById(R.id.etEmail);
        birthday = (EditText) findViewById(R.id.etBirthday);
        baseLocation = (EditText) findViewById(R.id.etBaseLocation);
        usernameInput = (EditText) findViewById(R.id.etUsername);
        passwordInput = (EditText) findViewById(R.id.etPassword);
        createAccount = (Button) findViewById(R.id.btnCreateUser);


        birthday.setInputType(InputType.TYPE_NULL);
        birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                picker = new DatePickerDialog(SignUpActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        birthday.setText((monthOfYear+1) + "/" + dayOfMonth + "/" + year);
                    }
                }, year, month, day);
                picker.show();
            }
        });

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Birthday = birthday.getText().toString();
                String deliveryDate = Birthday;
                SimpleDateFormat dateFormatprev = new SimpleDateFormat("MM/dd/yyyy");
                Date d = null;
                try {
                    d = dateFormatprev.parse(deliveryDate);
                } catch (java.text.ParseException e) {
                    e.printStackTrace();
                }
                final String Name = nameOfPerson.getText().toString();
                final String Email = email.getText().toString();
                //final String Birthday = birthday.getText().toString();
                final Date fbirthday = d;
                final String BaseLocation = baseLocation.getText().toString();
                final String Username = usernameInput.getText().toString();
                final String Password = passwordInput.getText().toString();
                makeAccount(Name, Email, fbirthday, Username, Password);
            }
        });
    }

    private void makeAccount(String Name, String Email, Date Birthday, String Username, String Password){

        ParseUser user = new ParseUser();
        // Set core properties
        user.setUsername(Username);
        user.setPassword(Password);
        user.setEmail(Email);
        // Set custom properties
        user.put("name", Name);
        //user.put("location", "Seattle");
        user.put("birthday", Birthday);
        // Invoke signUpInBackground
        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    Toast.makeText(getApplicationContext(), "Sign Up Successful!", Toast.LENGTH_SHORT).show();
                    final Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                    nameOfPerson.setText("");
                    email.setText("");
                    birthday.setText("");
                    baseLocation.setText("");
                    usernameInput.setText("");
                    passwordInput.setText("");
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Sign Up Failed!", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });


    }

    public String getRelativeTimeAgo(User user) {
        String twitterFormat = "dd MMM yyyy HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        long dateMillis = user.getBirthday().getTime();
        relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();

        return relativeDate;
    }
}
