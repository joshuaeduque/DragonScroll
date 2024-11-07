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

        // Create book list with fake data
        List<Book> bookList = new ArrayList<Book>();
        bookList.add(new Book("Book Title", "Book Author"));
        bookList.add(new Book("1984", "George Orwell"));
        bookList.add(new Book("Charlotte's Web", "E. B. White"));
        bookList.add(new Book("World War Z", "Max Brooks"));
        bookList.add(new Book("Animal Farm", "George Orwell"));
        bookList.add(new Book("Dracula", "Bram Stoker"));
        bookList.add(new Book("Iliad", "Homer"));
        bookList.add(new Book("Adventures of Huckleberry Finn", "Mark Twain"));
        bookList.add(new Book("Lord of the Flies", "William Golding"));
        bookList.add(new Book("The Cat in the Hat", "Dr. Seuss"));

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