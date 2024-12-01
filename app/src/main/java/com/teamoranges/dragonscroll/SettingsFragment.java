package com.teamoranges.dragonscroll;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SeekBarPreference;

public class SettingsFragment extends PreferenceFragmentCompat {

    private SharedPreferences sharedPreferences;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);

        // Get Context
        Context context = requireContext();

        // Get SharedPreferences
        sharedPreferences = context.getSharedPreferences(
                getString(R.string.preference_file_key),
                Context.MODE_PRIVATE
        );

        // Clear Preferences Button
        Preference clearPreferencesButton = findPreference(getString(R.string.clear_prefs_key));
        if (clearPreferencesButton != null) {
            clearPreferencesButton.setOnPreferenceClickListener(preference -> {
                // Clear SharedPreferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();
                // Reset UI elements to their default values
                resetPreferencesUI();
                Toast.makeText(context, "Preferences cleared successfully", Toast.LENGTH_SHORT).show();
                return true;
            });
        }

        // Setup nuke database button
        Preference nukeDatabaseButton = findPreference("nuke_db_preference");
        if (nukeDatabaseButton != null) {
            nukeDatabaseButton.setOnPreferenceClickListener(preference -> {
                // Show a confirmation dialog
                new AlertDialog.Builder(requireContext())
                        .setTitle("Confirm Action")
                        .setMessage("Are you sure you want to nuke the database? This action cannot be undone!")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            // Nuke the database
                            BookDao bookDao = ((MainActivity) requireActivity()).getBookDao();
                            bookDao.nukeTable();
                            Toast.makeText(requireContext(), "Database nuked successfully", Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                        .show();
                return true;
            });
        }

        // Setup themes list
        ListPreference themesList = findPreference(getString(R.string.themes_preference_key));
        if(themesList != null) {
            themesList.setOnPreferenceChangeListener(this::onThemesPreferenceChanged);
        }

        // Setup text size seekbar
        SeekBarPreference textSizeSlider = findPreference(getString(R.string.text_size_preference_key));
        if (textSizeSlider != null) {
            textSizeSlider.setOnPreferenceChangeListener((preference, newValue) -> {
                int sliderValue = (Integer) newValue;
                float textSizeMultiplier = sliderValue / 100f;
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putFloat(getString(R.string.text_size_preference_key), textSizeMultiplier);
                editor.apply();

                // Restart the activity to apply font size
                requireActivity().recreate();
                return true;
            });
        }
        // Setup dark mode preference
        ListPreference darkModePreference = findPreference(getString(R.string.dark_mode_key));
        if(darkModePreference != null) {
            darkModePreference.setOnPreferenceChangeListener(this::onDarkModePreferenceChanged);
        }
    }

    private boolean onThemesPreferenceChanged(Preference preference, Object o) {
        // Why do I have to commit the preference manually?
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(getString(R.string.themes_preference_key), o.toString());
        editor.commit();

        // Recreate the activity to update the theme
        requireActivity().recreate();
        return true;
    }

    private boolean onDarkModePreferenceChanged(Preference preference, Object o) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(getString(R.string.dark_mode_key), o.toString());
        editor.commit();

        requireActivity().recreate();
        return true;
    }

    /**
     * Default all SharedPreferences entries
     */
    private void resetPreferencesUI() {
        SeekBarPreference textSizeSlider = findPreference(getString(R.string.text_size_preference_key));
        ListPreference themeList = findPreference(getString(R.string.themes_preference_key));
        ListPreference darkMode = findPreference(getString(R.string.dark_mode_key));
        // Reset SeekBarPreference for font size
        if (textSizeSlider != null) {
            textSizeSlider.setValue(100);
        }
        // Reset Themes list to default
        if (themeList != null) {
            themeList.setValue(getString(R.string.default_theme_value));
        }
        //Reset Dark Mode list to default
        if (darkMode != null) {
            darkMode.setValue(getString(R.string.default_dark_mode_value));
        }
        requireActivity().recreate();
    }
}