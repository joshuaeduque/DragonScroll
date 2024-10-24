package com.teamoranges.dragonscroll;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BookFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BookFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String BOOK_TITLE_KEY = "bookTitle";
    private static final String BOOK_AUTHOR_KEY = "bookAuthor";

    // TODO: Rename and change types of parameters
    private String bookTitleParam;
    private String bookAuthorParam;

    public BookFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param bookTitle Parameter 1.
     * @return A new instance of fragment BookFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BookFragment newInstance(String bookTitle, String bookAuthor) {
        BookFragment fragment = new BookFragment();

        Bundle args = new Bundle();
        args.putString(BOOK_TITLE_KEY, bookTitle);
        args.putString(BOOK_AUTHOR_KEY, bookAuthor);

        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            bookTitleParam = getArguments().getString(BOOK_TITLE_KEY);
            bookAuthorParam = getArguments().getString(BOOK_AUTHOR_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_book, container, false);

        if(bookTitleParam != null) {
            TextView bookTitleView = view.findViewById(R.id.bookTitle);
            bookTitleView.setText(bookTitleParam);
        }

        if(bookAuthorParam != null) {
            TextView bookAuthorView = view.findViewById(R.id.bookAuthor);
            bookAuthorView.setText(bookAuthorParam);
        }

        return view;
    }
}