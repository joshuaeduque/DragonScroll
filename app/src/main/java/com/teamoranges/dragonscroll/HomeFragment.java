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
import java.util.Random;

/**
 * HomeFragment represents the view a user sees when the select the home item in the
 * bottom navigation view. It displays a clickable list of books that navigate to their own
 * BookFragment.
 */
public class HomeFragment extends Fragment {

    private Context context;

    private List<Book> bookList;
    private BookDao bookDao;
    private BookAdapter bookAdapter;
    private NavController navController;

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

        // Get the NavHostFragment from the activity(?)
        NavHostFragment navHostFragment = (NavHostFragment) requireActivity()
                .getSupportFragmentManager()
                .findFragmentById(R.id.navHostFragment);

        assert navHostFragment != null;

        // Get the NavController from the NavHostFragment
        navController = navHostFragment.getNavController();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Get fragment's Context
        context = requireContext();

        // Create RecyclerView for displaying books
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        // Get the BookDao from the Activity
        bookDao = ((MainActivity) requireActivity()).getBookDao();

        // Populate the book list with items from the BookDao
        bookList = bookDao.getAll();

        // Get the no books TextView from the View
        noBooksTextView = view.findViewById(R.id.noBooksTextView);
        // Update its visibility based on whether there are books in the book list
        updateNoBooksTextViewVisibility();

        // Initialize the BookAdapter with the book list and click listeners
        bookAdapter = new BookAdapter(bookList, this::onBookClick, this::onBookLongClick);

        // Configure the RecyclerView with the BookAdapter
        recyclerView.setAdapter(bookAdapter);

        // Set FloatingActionButton on click listener
        FloatingActionButton floatingActionButton = view.findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(this::onFloatingActionButtonClicked);

        return view;
    }

    private void onFloatingActionButtonClicked(View view) {
        // Create new book
        Book book = new Book();
        book.setTitle(getRandomTitle());
        book.setAuthor("No Author");

        // Add book and update view
        addBook(book);

        // Update noBooksTextView visibility
        updateNoBooksTextViewVisibility();
    }

    private void onBookClick(Book book, int position) {
        // Create a bundle for the fragment we're about to navigate to
        Bundle bundle = new Bundle();
        // Put the book ID in the bundle
        bundle.putInt("bookId", book.getId());
        // Navigate to BookFragment with the bundle
        navController.navigate(R.id.navigation_book, bundle);
    }

    private boolean onBookLongClick(Book book, int position) {
        // Create delete AlertDialog
        AlertDialog.Builder alert = new AlertDialog.Builder(context)
                .setMessage(String.format("Delete %s?", book.getTitle()));

        // Set AlertDialog positive button
        alert.setPositiveButton("Delete", (dialogInterface, i) -> {
            deleteBook(book, position);
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
        // Set the visibility of the "No books" TextView depending on whether the
        // book list is empty.
        noBooksTextView.setVisibility(bookList.isEmpty() ? View.VISIBLE : View.GONE);
    }

    private void addBook(Book book) {
        // NOTE: There's some serious mishandling going on in regards to the book list and
        // recycler view. I don't have time to fix it right now. It's good enough for the
        // presentation.

        // Insert book into database
        long insertId = bookDao.insert(book);

        // Update the data list
        book.setId((int) insertId);
        bookList.add(book);

        // Notify the adapter
        bookAdapter.notifyItemInserted(bookList.size() - 1);
    }

    private void deleteBook(Book book, int position) {
        // Delete book from database
        bookDao.delete(book);

        // Update the data list
        bookList.remove(position);

        // Notify the adapter
        bookAdapter.notifyItemRemoved(position);
        bookAdapter.notifyItemRangeChanged(position, bookList.size());
    }

    private String getRandomTitle() {
        // Create a list of random adjectives and nouns
        // This should really be a static array.
        String[] adjectives = {"Lost", "Hidden", "Secret", "Dark", "Silent", "Ancient", "Haunted"};
        String[] nouns = {"Kingdom", "Garden", "Dreams", "Echoes", "Stars", "Journey", "Legacy"};

        // Return a string with random adjective and noun indices
        Random random = new Random();
        return String.format(
                "%s %s",
                adjectives[random.nextInt(adjectives.length)],
                nouns[random.nextInt(nouns.length)]
        );
    }
}