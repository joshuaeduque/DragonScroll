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

        Preference clearCachePref = findPreference(getString(R.string.clear_cache_key));
        if (clearCachePref == null)
            return;

        Context context = requireContext();
        SharedPreferences sharedPreferences =
                context.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        clearCachePref.setOnPreferenceClickListener((preference) -> {
            Log.i("SettingsFragment", "Clear cache pressed");
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();
            
            return true;
        });
    }
}