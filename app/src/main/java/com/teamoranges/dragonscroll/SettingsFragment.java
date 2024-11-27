package com.teamoranges.dragonscroll;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.preference.ListPreference;
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
        Preference clearPreferencesButton = findPreference(getString(R.string.clear_prefs_key));
        if (clearPreferencesButton != null) {
            // Set clear prefs button onclick
            clearPreferencesButton.setOnPreferenceClickListener((preference) -> {
                // Clear SharedPreferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();

                return true;
            });
        }

        // Setup nuke database button
        Preference nukeDatabaseButton = findPreference("nuke_db_preference");
        if (nukeDatabaseButton != null) {
            nukeDatabaseButton.setOnPreferenceClickListener(preference -> {
                BookDao bookDao = ((MainActivity) requireActivity()).getBookDao();
                bookDao.nukeTable();
                return true;
            });
        }

        // Setup themes list
        ListPreference themesList = findPreference(getString(R.string.themes_preference_key));
        if(themesList != null) {
            themesList.setOnPreferenceChangeListener(this::onThemesPreferenceChanged);
        }
    }

    private boolean onThemesPreferenceChanged(Preference preference, Object o) {
        // TODO actually validate theme selection
        requireActivity().recreate();
        return true;
    }
}