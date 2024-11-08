package com.teamoranges.dragonscroll;

import android.app.AlertDialog;
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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.teamoranges.dragonscroll.models.Book;

import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";

    private NavController navController;
    private Context context;
    private BookDao bookDao;
    private List<Book> bookList;
    private BookAdapter bookAdapter;
    private TextView noBooksTextView;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        return new HomeFragment();
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
        context = requireContext();

        // Setup RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        // Get Activity BookDao
        bookDao = ((MainActivity) requireActivity()).getBookDao();
        // Populate book list from BookDao
        bookList = bookDao.getAll();

        // If no books in list, show empty text
        noBooksTextView = view.findViewById(R.id.noBooksTextView);
        updateNoBooksTextViewVisibility();

        // Setup BookAdapter with RecyclerView
        // Navigate to BookFragment with book data
        bookAdapter = new BookAdapter(bookList, this::onBookClick, this::onBookLongClick);

        // Setup RecyclerView with BookAdapter 
        recyclerView.setAdapter(bookAdapter);

        // Set FloatingActionButton on click listener
        FloatingActionButton floatingActionButton = view.findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(v -> {
            // Create new book
            Book book = new Book();
            book.setTitle(randomTitle());
            book.setAuthor("Book Author");

            // Add book and update view
            addBook(book);

            // Update noBooksTextView visibility
            updateNoBooksTextViewVisibility();
        });

        return view;
    }

    private void onBookClick(Book book, int position) {
        Bundle bundle = new Bundle();
        bundle.putString("bookTitle", book.getTitle());
        bundle.putString("bookAuthor", book.getAuthor());

        // Navigate to BookFragment with book data
        navController.navigate(R.id.navigation_book, bundle);
    }

    private boolean onBookLongClick(Book book, int position) {
        // Create delete AlertDialog
        AlertDialog.Builder alert = new AlertDialog.Builder(context)
                .setMessage(String.format("Delete %s?", book.getTitle()));

        // Set AlertDialog positive button
        alert.setPositiveButton("Delete", (dialogInterface, i) -> {
            deleteBook(book);
            updateNoBooksTextViewVisibility();
        });

        // Set AlertDialog negative button
        alert.setNegativeButton("Cancel", (dialogInterface, i) -> {
            // Empty lambda lol
        });

        // Show AlertDialog
        alert.show();

        return false;
    }

    private void updateNoBooksTextViewVisibility() {
        noBooksTextView.setVisibility(bookList.isEmpty() ? View.VISIBLE : View.GONE);
    }

    private void addBook(Book book) {
        bookDao.insertAll(book);
        bookList.add(book);
        // Bad
        bookAdapter.notifyDataSetChanged();
    }

    private void deleteBook(Book book) {
        bookDao.delete(book);
        bookList.remove(book);
        // Bad bad bad
        bookAdapter.notifyDataSetChanged();
    }

    private String randomTitle() {
        String[] adjectives = {"Lost", "Hidden", "Secret", "Dark", "Silent", "Ancient", "Haunted"};
        String[] nouns = {"Kingdom", "Garden", "Dreams", "Echoes", "Stars", "Journey", "Legacy"};

        Random random = new Random();

        return String.format(
                "%s %s",
                adjectives[random.nextInt(adjectives.length)],
                nouns[random.nextInt(nouns.length)]
        );
    }
}