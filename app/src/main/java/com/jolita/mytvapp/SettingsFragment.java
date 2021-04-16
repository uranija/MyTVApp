package com.jolita.mytvapp;

import android.os.Bundle;
import androidx.preference.PreferenceFragmentCompat;

public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle bundle, String s) {

        // Add movie preferences, defined in the XML file in res->xml->movie_preference
        addPreferencesFromResource(R.xml.tv_preferences);
    }
}
