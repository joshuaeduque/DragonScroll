package com.teamoranges.dragonscroll;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.teamoranges.dragonscroll.models.Book;

import java.util.Calendar;
import java.util.Locale;

/**
 * BookFragment represents the view a user sees when they click an entry in the book list.
 * It allows them to view and edit the book's title, author, etc.
 */
public class BookFragment extends Fragment {

    // Key string used to pass in a book ID
    private static final String BOOK_ID_KEY = "bookId";

    // Book ID passed to the book fragment
    private int bookIdParam;

    // Book retrieved from database by ID
    private Book book;

    private BookDao bookDao;
    private ActivityResultLauncher<PickVisualMediaRequest> pickMedia;
    private SharedPreferences sharedPreferences;

    private ImageView coverImageView;
    private EditText summaryEditText;
    private EditText notesEditText;

    public BookFragment() {
        // Required empty public constructor
    }

    public static BookFragment newInstance(int bookId) {
        BookFragment fragment = new BookFragment();

        // Get arguments from bundle
        Bundle args = new Bundle();
        args.putInt(BOOK_ID_KEY, bookId);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            // Get book id from fragment bundle
            bookIdParam = getArguments().getInt(BOOK_ID_KEY);
        }

        // Get SharedPreferences
        sharedPreferences = requireContext().getSharedPreferences(
                getString(R.string.preference_file_key),
                Context.MODE_PRIVATE
        );

        // Get BookDao from MainActivity
        bookDao = ((MainActivity) requireActivity()).getBookDao();

        // Get Book by ID from database
        book = bookDao.getBook(bookIdParam);

        // Registers a photo picker activity launcher in single-select mode.
        pickMedia =
                registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                    // Callback is invoked after the user selects a media item or closes the
                    // photo picker.

                    // Check if a uri was selected from the picker
                    if (uri != null) {
                        Log.d("PhotoPicker", "Selected URI: " + uri);

                        // If the cover ImageView is null, don't do anything.
                        if (coverImageView == null) {
                            return;
                        }

                        // Configure uri permission since we store them persistently in the database.
                        try {
                            // Do some nonsense with permissions
                            Context context = requireContext();
                            context.getContentResolver().takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        } catch (Exception exception) {
                            Log.d("PhotoPicker", "Permissions failed ig");
                            return;
                        }

                        // Update book cover
                        updateBookCover(coverImageView, uri);
                    } else {
                        Log.d("PhotoPicker", "No media selected");
                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_book, container, false);

        // Get cover ImageView
        coverImageView = view.findViewById(R.id.profileImageView);
        if (book.getCoverUri() != null && !book.getCoverUri().isEmpty()) {
            // Set cover uri
            String uri = book.getCoverUri();
            coverImageView.setImageURI(Uri.parse(uri));
        }
        // Set cover on click listener
        coverImageView.setOnClickListener(this::onCoverImageViewClicked);

        // Get summary EditText
        summaryEditText = view.findViewById(R.id.summaryEditText);
        if (book.getSummary() != null && !book.getSummary().isEmpty()) {
            // Set summary text
            summaryEditText.setText(book.getSummary());
        }

        // Get notes EditText
        notesEditText = view.findViewById(R.id.notesEditText);
        if (book.getNotes() != null && !book.getNotes().isEmpty()) {
            // Set notes text
            notesEditText.setText(book.getNotes());
        }

        // Get title TextView
        TextView titleTextView = view.findViewById(R.id.titleTextView);
        if (book.getTitle() != null && !book.getTitle().isEmpty()) {
            // Set title text
            titleTextView.setText(book.getTitle());
        }
        // Set title on click listener
        titleTextView.setOnClickListener(this::onTitleTextViewClicked);

        // Get author TextView
        TextView authorTextView = view.findViewById(R.id.authorTextView);
        if (book.getAuthor() != null && !book.getAuthor().isEmpty()) {
            // Set author text
            authorTextView.setText(book.getAuthor());
        }
        // Set author on click listener
        authorTextView.setOnClickListener(this::onAuthorTextViewClicked);

        // Get rating TextView
        TextView ratingTextView = view.findViewById(R.id.ratingTextView);
        // Set rating text
        ratingTextView.setText(String.format(Locale.getDefault(), "Rating: %d/5", book.getRating()));
        // Set rating on click listener
        ratingTextView.setOnClickListener(this::onRatingTextViewClicked);

        // Get save summary Button
        Button saveSummaryButton = view.findViewById(R.id.saveSummaryButton);
        // Set save summary on click listener
        saveSummaryButton.setOnClickListener(this::onSaveSummaryButtonClicked);

        // Get save notes Button
        Button saveNotesButton = view.findViewById(R.id.saveNotesButton);
        // Set save notes on click listener
        saveNotesButton.setOnClickListener(this::onSaveNotesButtonClicked);

        // Get start date TextView
        TextView startDateTextView = view.findViewById(R.id.startDateTextView);
        if (book.getStartDate() != null && !book.getStartDate().isEmpty()) {
            // Set start date text
            startDateTextView.setText(String.format(Locale.getDefault(), "Start Date: %s", book.getStartDate()));
        }
        // Set start date on click listener
        startDateTextView.setOnClickListener(this::onStartTextViewClicked);

        // Get end date TextView
        TextView endDateTextView = view.findViewById(R.id.endDateTextView);
        if (book.getEndDate() != null && !book.getEndDate().isEmpty()) {
            // Set end date text
            endDateTextView.setText(String.format(Locale.getDefault(), "End Date: %s", book.getEndDate()));
        }
        // Set end date on click listener
        endDateTextView.setOnClickListener(this::onEndTextViewClicked);

        // Get favorite button
        Button favoriteButton = view.findViewById(R.id.favoritesButton);
        // Set favorite on click listener
        favoriteButton.setOnClickListener(this::onFavoriteButtonClicked);

        return view;
    }

    private void onFavoriteButtonClicked(View view) {
        // Get the key we use to store the favorite book in SharedPreferences
        String favoriteBookKey = getString(R.string.favorite_book_key);

        // Store the favorite boo title in SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(favoriteBookKey, book.getTitle());
        editor.apply();

        // Display a toast stating that we saved the favorite book
        Toast.makeText(requireContext(), "Set as favorite book", Toast.LENGTH_SHORT).show();
    }

    private void onEndTextViewClicked(View view) {
        // Get the calendar year, month, and day
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                // Set the end date year, month, and day
                updateEndDate(view, i, i1, i2);
            }
        }, year, month, day);

        // Show the DatePickerDialog
        datePickerDialog.show();
    }

    private void onStartTextViewClicked(View view) {
        // Get the calendar year, month, and day
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                // Set the end date year, month, and day
                updateStartDate(view, i, i1, i2);
            }
        }, year, month, day);

        // Show the DatePickerDialog
        datePickerDialog.show();
    }

    private void onSaveSummaryButtonClicked(View view) {
        // Get summary text from EditText
        String text = summaryEditText.getText().toString();
        // Write text to database
        bookDao.setSummary(bookIdParam, text);
        // Show confirmation toast
        Toast.makeText(getContext(), "Summary saved", Toast.LENGTH_SHORT).show();
    }

    private void onSaveNotesButtonClicked(View view) {
        // Get notes text from EditText
        String text = notesEditText.getText().toString();
        // Write text to database
        bookDao.setNotes(bookIdParam, text);
        // Show confirmation toast
        Toast.makeText(getContext(), "Notes saved", Toast.LENGTH_SHORT).show();
    }

    private void onCoverImageViewClicked(View view) {
        // Launch the photo picker and let the user choose only images.
        pickMedia.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                .build());
    }

    private void onRatingTextViewClicked(View view) {
        // Get Context
        Context context = this.getContext();

        //  Create EditText
        EditText editText = new EditText(context);
        editText.setHint(String.valueOf(book.getRating()));
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);

        // Create AlertDialog
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context)
                .setMessage("Enter rating")
                .setView(editText);

        // Set positive button
        alertDialog.setPositiveButton("Save", (dialogInterface, i) -> {
            // Get EditText text
            String text = editText.getText().toString();
            // Return if text is empty
            if (text.trim().isEmpty()) {
                return;
            }

            // Get the text as an int
            int rating = tryParseInt(text);
            // Check if the int value is 1 - 5 inclusive
            if (rating < 1 || rating > 5) {
                return;
            }

            // Update book author
            updateBookRating(view, rating);
        });

        // Set negative button
        alertDialog.setNegativeButton("Cancel", (dialogInterface, i) -> {
            // Empty lambda to make negative button show
        });

        // Show AlertDialog
        alertDialog.show();
    }

    private void onAuthorTextViewClicked(View view) {
        // Get Context
        Context context = this.getContext();

        //  Create EditText
        EditText editText = new EditText(context);
        editText.setHint(book.getAuthor());

        // Create AlertDialog
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context)
                .setMessage("Enter author")
                .setView(editText);

        // Set positive button
        alertDialog.setPositiveButton("Save", (dialogInterface, i) -> {
            // Get EditText text
            String text = editText.getText().toString();
            // Return if text is empty
            if (text.trim().isEmpty()) {
                return;
            }
            // Update book author
            updateBookAuthor(view, text);
        });

        // Set negative button
        alertDialog.setNegativeButton("Cancel", (dialogInterface, i) -> {
            // Empty lambda to make negative button show
        });

        // Show AlertDialog
        alertDialog.show();
    }

    private void onTitleTextViewClicked(View view) {
        // Get Context
        Context context = this.getContext();

        //  Create EditText
        EditText editText = new EditText(context);
        editText.setHint(book.getTitle());

        // Create AlertDialog
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context)
                .setMessage("Enter title")
                .setView(editText);

        // Set positive button
        alertDialog.setPositiveButton("Save", (dialogInterface, i) -> {
            // Get EditText text
            String text = editText.getText().toString();
            // Return if text is empty
            if (text.trim().isEmpty()) {
                return;
            }
            // Update book title
            updateBookTitle(view, text);
        });

        // Set negative button
        alertDialog.setNegativeButton("Cancel", (dialogInterface, i) -> {
            // Empty lambda to make negative button show
        });

        // Show AlertDialog
        alertDialog.show();
    }

    private void updateBookRating(View view, int rating) {
        // Update rating in database
        bookDao.setRating(bookIdParam, rating);
        // Update rating in view
        TextView ratingTextView = (TextView) view;
        ratingTextView.setText(String.format(Locale.getDefault(), "Rating: %d/5", rating));
        // Update local book variable
        book.setRating(rating);
    }

    private void updateBookAuthor(View view, String author) {
        // Update author in database
        bookDao.setAuthor(bookIdParam, author);
        // Update author in view
        TextView authorTextView = (TextView) view;
        authorTextView.setText(author);
        // Update local book variable
        book.setAuthor(author);
    }

    private void updateBookTitle(View view, String title) {
        // Update title in database
        bookDao.setTitle(bookIdParam, title);
        // Update title in view
        TextView titleTextView = (TextView) view;
        titleTextView.setText(title);
        // Update local book title
        book.setTitle(title);
    }

    private void updateBookCover(View view, Uri uri) {
        // Get the uri string
        String uriString = uri.toString();

        // Update uri in database
        bookDao.setCoverUri(bookIdParam, uriString);
        // Update uri in view
        ImageView coverImageView = (ImageView) view;
        coverImageView.setImageURI(uri);
        // Update local book uri
        book.setCoverUri(uriString);
    }

    private void updateStartDate(View view, int year, int month, int day) {
        // Get the date string
        String date = String.format(Locale.getDefault(), "%d/%d/%d", year, month, day);

        // Update date in database
        bookDao.setStartDate(bookIdParam, date);
        // Update date in view
        ((TextView) view).setText(String.format(Locale.getDefault(), "Start Date: %s", date));
        // Update local book date
        book.setStartDate(date);
    }

    private void updateEndDate(View view, int year, int month, int day) {
        // Get the date string
        String date = String.format(Locale.getDefault(), "%d/%d/%d", year, month, day);

        // Update date in database
        bookDao.setEndDate(bookIdParam, date);
        // Update date in view
        ((TextView) view).setText(String.format(Locale.getDefault(), "End Date: %s", date));
        // Update local book date
        book.setEndDate(date);
    }

    // This is so hacky but it works for now
    private int tryParseInt(String number) {
        // Try parsing an integer from a string.
        // If it fails, just return -1 as a default.
        try {
            return Integer.parseInt(number);
        } catch (Exception exception) {
            return -1;
        }
    }
}