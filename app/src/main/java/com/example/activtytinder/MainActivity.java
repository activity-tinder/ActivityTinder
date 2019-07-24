package com.example.activtytinder;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.activtytinder.Fragments.CardFragment;
import com.example.activtytinder.Fragments.ProfileFragment;
import com.example.activtytinder.Fragments.ReceiptFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final FragmentManager fragmentManager = getSupportFragmentManager();

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment fragment;

            switch (item.getItemId()) {
                case R.id.action_card:
                    fragment = new CardFragment();
                    //Toast.makeText(MainActivity.this, "Home!", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.action_create:
                    //TODO -- change back to CreateFragment();
                    fragment = new ReceiptFragment();
                    //Toast.makeText(MainActivity.this, "Compose!", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.action_profile:
                default:
                    fragment = new ProfileFragment();
                    //Toast.makeText(MainActivity.this, "Profile!", Toast.LENGTH_SHORT).show();
                    break;
            }
            // TODO -- add if statement to check if fragment isn't null

            fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
            return true;
        });

        // Set default selection
        bottomNavigationView.setSelectedItemId(R.id.action_card);
    }
}
