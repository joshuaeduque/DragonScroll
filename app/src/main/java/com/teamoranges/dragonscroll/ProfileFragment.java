package com.teamoranges.dragonscroll;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
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

    private ActivityResultLauncher<PickVisualMediaRequest> pickMedia;

    private ImageView profileImageView;

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
    private String profileImageUri;
    private String favoriteBook;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        // Get book DAO
        bookDao = ((MainActivity) requireActivity()).getBookDao();
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

        profileImageUri = sharedPrefs.getString(
                getString(R.string.profile_uri_key),
                null);

        favoriteBook = sharedPrefs.getString(
                getString(R.string.favorite_book_key),
                null);

        // Registers a photo picker activity launcher in single-select mode.
        pickMedia =
                registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                    // Callback is invoked after the user selects a media item or closes the
                    // photo picker.
                    if (uri != null) {
                        Log.d("PhotoPicker", "Selected URI: " + uri);

                        try {
                            // Do some nonsense with permissions
                            Context context = requireContext();
                            context.getContentResolver().takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        } catch (Exception exception) {
                            Log.d("PhotoPicker", "Permissions failed ig");
                            return;
                        }

                        // Update book cover
                        // updateBookCover(coverImageView, uri);
                        updateProfilePicture(uri);
                    } else {
                        Log.d("PhotoPicker", "No media selected");
                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Get profile name TextView
        TextView profileNameTextView = view.findViewById(R.id.nameTextView);

        // Setup profile name
        profileNameTextView.setText(this.profileName);
        profileNameTextView.setOnClickListener(this::onNameTextViewClicked);

        // Set books read TextView
        TextView booksReadTextView = view.findViewById(R.id.booksReadTextView);
        booksReadTextView.setText(String.format(Locale.getDefault(), "Books Read: %d", booksRead));

        // Setup profile picture
        profileImageView = view.findViewById(R.id.profileImageView);
        if (profileImageUri != null) {
            Uri uri = Uri.parse(profileImageUri);
            profileImageView.setImageURI(uri);
        }
        profileImageView.setOnClickListener(this::onProfileImageViewClicked);

        // Setup favorite book
        TextView favoriteBookTextView = view.findViewById(R.id.favoriteBookTextView);
        if (favoriteBook != null) {
            String favoriteBookText = String.format(Locale.getDefault(), "Favorite Book: %s", favoriteBook);
            favoriteBookTextView.setText(favoriteBookText);
        }

        return view;
    }

    private void updateProfilePicture(Uri uri) {
        String uriString = uri.toString();

        profileImageView.setImageURI(uri);

        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(
                getString(R.string.profile_uri_key),
                uriString);
        editor.apply();
    }

    private void onProfileImageViewClicked(View view) {
        // Launch the photo picker and let the user choose only images.
        pickMedia.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                .build());
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