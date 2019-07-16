package com.example.activtytinder;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.activtytinder.Fragments.CardFragment;
import com.example.activtytinder.Fragments.CreateFragment;
import com.example.activtytinder.Fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFFFFFFFF));


        final FragmentManager fragmentManager = getSupportFragmentManager();

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;

                switch (item.getItemId()) {
                    case R.id.action_card:
                        fragment = new CardFragment();
                        Toast.makeText(MainActivity.this, "Home!", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_create:
                        fragment = new CreateFragment();
                        Toast.makeText(MainActivity.this, "Compose!", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_profile:
                    default:
                        fragment = new ProfileFragment();
                        Toast.makeText(MainActivity.this, "Profile!", Toast.LENGTH_SHORT).show();

                        break;
                }
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                return true;
            }
        });

        // Set default selection
        bottomNavigationView.setSelectedItemId(R.id.action_card);
    }
}
