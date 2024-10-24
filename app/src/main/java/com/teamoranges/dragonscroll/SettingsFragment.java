package com.teamoranges.dragonscroll;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);

        // Get Context
        Context context = requireContext();

        // Get SharedPreferences
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                getString(R.string.preference_file_key),
                Context.MODE_PRIVATE
        );

        // Try getting clear preferences button
        Preference clearPrefsButton = findPreference(getString(R.string.clear_prefs_key));
        if (clearPrefsButton == null)
            return;

        // Set clear prefs button onclick
        clearPrefsButton.setOnPreferenceClickListener((preference) -> {
            // Clear SharedPreferences
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();

            return true;
        });
    }
}