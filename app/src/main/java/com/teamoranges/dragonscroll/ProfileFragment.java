package com.teamoranges.dragonscroll;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Locale;

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

    private BookDao bookDao;

    private int booksRead;

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

    private String profileName;
    private SharedPreferences sharedPrefs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        // Get book DAO
        bookDao = ((MainActivity)requireActivity()).getBookDao();
        // Get books read
        booksRead = bookDao.getCount();

        // Get SharedPreferences
        sharedPrefs = requireContext().getSharedPreferences(
                getString(R.string.preference_file_key),
                Context.MODE_PRIVATE
        );

        // Get profile name from SharedPreferences or default
        profileName = sharedPrefs.getString(
                getString(R.string.profile_name_key),
                getString(R.string.profile_name_default)
        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Get profile name TextView
        TextView profileNameTextView = view.findViewById(R.id.nameTextView);

        // Set profile name
        profileNameTextView.setText(this.profileName);

        // Set profile name TextView on click listener
        profileNameTextView.setOnClickListener(this::onNameTextViewClicked);

        // Set books read TextView
        TextView booksReadTextView = view.findViewById(R.id.booksReadTextView);
        booksReadTextView.setText(String.format(Locale.getDefault(), "Books Read: %d", booksRead));

        return view;
    }

    private void onNameTextViewClicked(View view) {
        Context context = requireContext();

        // Create EditText for user input
        EditText editText = new EditText(context);
        editText.setHint("Profile name");

        // Create AlertDialog
        AlertDialog.Builder alert = new AlertDialog.Builder(context)
                .setMessage("Enter a new profile name")
                .setView(editText);

        // Set AlertDialog positive button on click listener
        alert.setPositiveButton("Save", (dialog, button) -> {
            // Get EditText text
            String editTextValue = String.valueOf(editText.getText());

            // Validate EditText text.
            // If it's empty, just return without doing anything.
            if (editTextValue.trim().isEmpty())
                return;

            // Set text of TextView with user profile name
            ((TextView) view).setText(editTextValue);

            // Save new profile name to SharedPreferences
            SharedPreferences.Editor editor = sharedPrefs.edit();
            editor.putString(
                    getString(R.string.profile_name_key),
                    editTextValue);
            editor.apply();
        });

        // Give AlertDialog negative button and empty on click listener
        alert.setNegativeButton("Cancel", (dialog, button) -> {
            // The negative button needs an empty on click listener
            // otherwise it won't show at all.
            // There's probably a better way to do this lmao.
        });

        // Show the AlertDialog
        alert.show();
    }
}