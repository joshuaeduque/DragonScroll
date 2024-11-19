package com.teamoranges.dragonscroll;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

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

        // Setup rating TextView
        TextView ratingTextView = view.findViewById(R.id.ratingTextView);
        ratingTextView.setText(String.format("Rating: %d/5", book.getRating()));

        return view;
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
            if(text.trim().isEmpty()) {
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

    private void updateBookTitle(View view, String title) {
        // Update title in database
        // Update title in view
        TextView titleTextView = (TextView)view;
        titleTextView.setText(title);
    }

    private void onEditTextViewClicked(View view) {
        // TODO
    }
}