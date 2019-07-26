package com.example.activtytinder;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.activtytinder.Fragments.CardFragment;
import com.example.activtytinder.Fragments.CreateFragment;
import com.example.activtytinder.Fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    final Fragment cardFragment = new CardFragment();
    final Fragment createFragment = new CreateFragment();
    final Fragment profileFragment = new ProfileFragment();
    final FragmentManager fm = getSupportFragmentManager();
    Fragment active = cardFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fm.beginTransaction().add(R.id.flContainer, profileFragment, "3").hide(profileFragment).commit();
        fm.beginTransaction().add(R.id.flContainer, createFragment, "2").hide(createFragment).commit();
        fm.beginTransaction().add(R.id.flContainer, cardFragment, "1").commit();


        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case R.id.action_card:
                    fm.beginTransaction().hide(active).show(cardFragment).commit();
                    active = cardFragment;
                    break;
                case R.id.action_create:
                    fm.beginTransaction().hide(active).show(createFragment).commit();
                    active = createFragment;
                    break;
                case R.id.action_profile:
                default:
                    fm.beginTransaction().hide(active).show(profileFragment).commit();
                    active = profileFragment;
                    break;
            }

            return true;
        });

        // Set default selection
        bottomNavigationView.setSelectedItemId(R.id.action_card);
    }
}
