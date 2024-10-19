package com.teamoranges.dragonscroll;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private Context context;
    private String profileName;
    private SharedPreferences sharedPrefs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        // Get Context
        context = requireContext();

        // Get SharedPrefs
        sharedPrefs = context.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        // Get user profile name from SharedPrefs or the default profile name
        String defaultProfileName = getString(R.string.profile_name_default);
        profileName = sharedPrefs.getString(getString(R.string.profile_name_key), defaultProfileName);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Get TextView with user profile name
        TextView profileTextView = view.findViewById(R.id.profileTextView);

        // Set profile name
        profileTextView.setText(profileName);
        profileTextView.setClickable(true);

        // Set profile name on click listener
        profileTextView.setOnClickListener(pv -> {
            // Create EditText for user input
            final EditText editText = new EditText(context);
            editText.setHint("Profile name");

            // Create AlertDialog
            AlertDialog.Builder alert = new AlertDialog.Builder(context)
                    .setMessage("Enter a new profile name")
                    .setView(editText);

            // Set AlertDialog positive button onclick
            alert.setPositiveButton("Save", (dialog, button) -> {
                // Get text in EditText
                String editedText = String.valueOf(editText.getText());

                // Set text of TextView with user profile name
                profileTextView.setText(editedText);

                // Save new profile name to SharedPrefs
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putString(getString(R.string.profile_name_key), editedText);
                editor.apply();
            });

            // Set AlertDialog negative button empty onclick
            alert.setNegativeButton("Cancel", (dialog, button) -> {
                // Do nothing
            });

            // Show the AlertDialog 
            alert.show();
        });

        return view;
    }
}