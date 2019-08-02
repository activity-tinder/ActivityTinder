package com.example.activtytinder.Fragments;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.example.activtytinder.R;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootkey){
        setPreferencesFromResource(R.xml.settings_screen, rootkey);
    }
}
