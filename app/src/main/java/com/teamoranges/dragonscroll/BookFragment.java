package com.teamoranges.dragonscroll;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.teamoranges.dragonscroll.models.Book;

public class BookFragment extends Fragment {

    private static final String BOOK_ID_KEY = "bookId";

    private int bookIdParam;

    private BookDao bookDao;
    private Book book;

    public BookFragment() {
        // Required empty public constructor
    }

    public static BookFragment newInstance(int bookId) {
        BookFragment fragment = new BookFragment();

        Bundle args = new Bundle();
        args.putInt(BOOK_ID_KEY, bookId);

        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            bookIdParam = getArguments().getInt(BOOK_ID_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_book, container, false);

        // Get BookDAO from MainActivity
        bookDao = ((MainActivity) requireActivity()).getBookDao();

        // Get Book by ID from database
        book = bookDao.getBook(bookIdParam);

        // Setup edit TextView
        TextView editTextView = view.findViewById(R.id.editTextView);
        editTextView.setOnClickListener(this::onEditTextViewClicked);

        // Setup title TextView
        TextView titleTextView = view.findViewById(R.id.titleTextView);
        titleTextView.setText(book.getTitle());
        titleTextView.setOnClickListener(this::onTitleTextViewClicked);

        // Setup author TextView
        TextView authorTextView = view.findViewById(R.id.authorTextView);
        authorTextView.setText(book.getAuthor());
        authorTextView.setOnClickListener(this::onAuthorTextViewClicked);

        // Setup rating TextView
        TextView ratingTextView = view.findViewById(R.id.ratingTextView);
        ratingTextView.setText(String.format("Rating: %d/5", book.getRating()));
        ratingTextView.setOnClickListener(this::onRatingTextViewClicked);

        return view;
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

    // This is so hacky but it works for now
    private int tryParseInt(String number) {
        try {
            return Integer.parseInt(number);
        } catch (Exception exception) {
            return -1;
        }
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
        ratingTextView.setText(String.format("Rating: %d/5", rating));
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

    private void onEditTextViewClicked(View view) {
        // TODO
    }
}