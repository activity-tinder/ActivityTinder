package com.example.activtytinder;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.activtytinder.Fragments.CardFragment;
import com.example.activtytinder.Fragments.CreateFragment;
import com.example.activtytinder.Fragments.ProfileFragment;
import com.example.activtytinder.Fragments.ReceiptFragment;
import com.example.activtytinder.Fragments.SettingsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

//TODO -- explain this activity
public class MainActivity extends AppCompatActivity {

    public BottomNavigationView bottomNavigationView;

    private Toolbar toolbar;

    final static Fragment cardFragment = new CardFragment();
    final Fragment createFragment = new CreateFragment();
    public static Fragment profileFragment = new ProfileFragment();
    final Fragment receiptFragment = new ReceiptFragment();
    final static Fragment settingsFragment = new SettingsFragment();
    public static FragmentManager fragmentManager;



    //TODO -- explain all the fragment stuff
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction().add(R.id.flContainer, settingsFragment, "5").commit();
        fragmentManager.beginTransaction().add(R.id.flContainer, receiptFragment, "4").commit();
        fragmentManager.beginTransaction().add(R.id.flContainer, profileFragment, "3").commit();
        fragmentManager.beginTransaction().add(R.id.flContainer, createFragment, "2").commit();
        fragmentManager.beginTransaction().add(R.id.flContainer, cardFragment, "1").commit();

        fragmentManager.popBackStack();

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case R.id.action_card:
//                    fragmentManager.saveFragmentInstanceState(cardFragment);
                    fragmentManager.beginTransaction().addToBackStack("1").replace(R.id.flContainer, cardFragment).commit();
                    break;
                case R.id.action_create:
//                    fragmentManager.saveFragmentInstanceState(createFragment);
                    fragmentManager.beginTransaction().addToBackStack("2").replace(R.id.flContainer, createFragment).commit();
                    break;
                case R.id.action_profile:
//                    fragmentManager.saveFragmentInstanceState(profileFragment);
                    fragmentManager.beginTransaction().addToBackStack("3").replace(R.id.flContainer, profileFragment).commit();
                    break;
                 default:
                     //Added this to the fragment manager so that I can create it at the beginning
//                     fragmentManager.saveFragmentInstanceState(receiptFragment);
                     fragmentManager.beginTransaction().addToBackStack("4").replace(R.id.flContainer, receiptFragment).commit();
                     break;
            }

            return true;
        });

        // Set default selection
        bottomNavigationView.setSelectedItemId(R.id.action_card);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.setting){
            openSettings();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Back button only goes to card fragment to prevent tracking entire stack back.
     */
    @Override
    public void onBackPressed() {
        fragmentManager.beginTransaction().addToBackStack("1").replace(R.id.flContainer, cardFragment).commit();
    }

    //TODO -- explain this method
    public void contactUs(View item) {
        // first parameter is the context, second is the class of the activity to launch
        Intent i = new Intent(this, ContactUsActivity.class);
        startActivity(i);
    }
    public void openSettings(){
        Fragment fragment = new SettingsFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        fragmentManager.popBackStack();
        fragmentManager.beginTransaction().addToBackStack("Settings").replace(R.id.flContainer, fragment).commit();
    }
}
