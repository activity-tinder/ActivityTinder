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

    final Fragment fragment1 = new CardFragment();
    final Fragment fragment2 = new CreateFragment();
    final Fragment fragment3 = new ProfileFragment();
    final FragmentManager fm = getSupportFragmentManager();
    Fragment active = fragment1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fm.beginTransaction().add(R.id.flContainer, fragment3, "3").hide(fragment3).commit();
        fm.beginTransaction().add(R.id.flContainer, fragment2, "2").hide(fragment2).commit();
        fm.beginTransaction().add(R.id.flContainer, fragment1, "1").commit();


        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case R.id.action_card:
                    fm.beginTransaction().hide(active).show(fragment1).commit();
                    active = fragment1;
                    break;
                case R.id.action_create:
                    fm.beginTransaction().hide(active).show(fragment2).commit();
                    active = fragment2;
                    break;
                case R.id.action_profile:
                default:
                    fm.beginTransaction().hide(active).show(fragment3).commit();
                    active = fragment3;
                    break;
            }

            return true;
        });

        // Set default selection
        bottomNavigationView.setSelectedItemId(R.id.action_card);
    }
}
