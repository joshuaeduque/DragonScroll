package com.teamoranges.dragonscroll;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        bookDao = ((MainActivity)requireActivity()).getBookDao();

        // Get Book by ID from database
        book = bookDao.getBook(bookIdParam);

        TextView titleTextView = view.findViewById(R.id.titleTextView);
        TextView authorTextView = view.findViewById(R.id.authorTextView);
        TextView ratingTextView = view.findViewById(R.id.ratingTextView);

        titleTextView.setText(book.getTitle());
        authorTextView.setText(book.getAuthor());
        ratingTextView.setText(String.format("Rating: %d/5", book.getRating()));

        return view;
    }
}