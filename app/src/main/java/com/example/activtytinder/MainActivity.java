package com.example.activtytinder;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.activtytinder.Fragments.CardFragment;
import com.example.activtytinder.Fragments.CreateFragment;
import com.example.activtytinder.Fragments.ProfileFragment;
import com.example.activtytinder.Fragments.ReceiptFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity {

    public BottomNavigationView bottomNavigationView;

    final Fragment cardFragment = new CardFragment();
    final Fragment createFragment = new CreateFragment();
    final Fragment profileFragment = new ProfileFragment();
    final Fragment receiptFragment = new ReceiptFragment();
    public static FragmentManager fragmentManager;
    Fragment active = cardFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction().add(R.id.flContainer, receiptFragment, "4").hide(receiptFragment).commit();
        fragmentManager.beginTransaction().add(R.id.flContainer, profileFragment, "3").hide(profileFragment).commit();
        fragmentManager.beginTransaction().add(R.id.flContainer, createFragment, "2").hide(createFragment).commit();
        fragmentManager.beginTransaction().add(R.id.flContainer, cardFragment, "1").commit();


        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case R.id.action_card:
                    fragmentManager.beginTransaction().hide(active).show(cardFragment).commit();
                    active = cardFragment;
                    break;
                case R.id.action_create:
                    fragmentManager.beginTransaction().hide(active).show(createFragment).commit();
                    active = createFragment;
                    break;
                case R.id.action_profile:
                    fragmentManager.beginTransaction().hide(active).show(profileFragment).commit();
                    active = profileFragment;
                    break;
                 default:
                     //Added this to the fragment manager so that I cna create it at the beginning
                     fragmentManager.beginTransaction().hide(active).show(receiptFragment).commit();
                     active = receiptFragment;
                     break;
            }

            return true;
        });

        // Set default selection
        bottomNavigationView.setSelectedItemId(R.id.action_card);
    }
}
