package com.teamoranges.dragonscroll;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.teamoranges.dragonscroll.models.Book;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";

    private NavController navController;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Try getting NavController from NavHostFragment
        NavHostFragment navHostFragment = (NavHostFragment) requireActivity()
                .getSupportFragmentManager()
                .findFragmentById(R.id.navHostFragment);

        if (navHostFragment == null) {
            Log.e(TAG, "Failed to get NavigationHostFragment");
            return;
        }

        navController = navHostFragment.getNavController();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Get Context
        Context context = requireContext();

        // Setup RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        // Get Activity BookDao
        BookDao bookDao = ((MainActivity)getActivity()).getBookDao();
        // Populate book list from BookDao
        List<Book> bookList = bookDao.getAll();

        // If no books in list, show empty text
        TextView noBooksTextView = view.findViewById(R.id.noBooksTextView);
        noBooksTextView.setVisibility(bookList.isEmpty() ? View.VISIBLE : View.GONE);

        // Setup BookAdapter with RecyclerView
        // Navigate to BookFragment with book data
        BookAdapter bookAdapter = new BookAdapter(bookList, (book, position) -> {
            Bundle bundle = new Bundle();
            bundle.putString("bookTitle", book.getTitle());
            bundle.putString("bookAuthor", book.getAuthor());

            // Navigate to BookFragment with book data
            navController.navigate(R.id.navigation_book, bundle);
        });

        // Setup RecyclerView with BookAdapter 
        recyclerView.setAdapter(bookAdapter);

        return view;
    }
}