package com.teamoranges.dragonscroll;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.teamoranges.dragonscroll.models.Book;

import java.util.List;
import java.util.Random;

/**
 * HomeFragment is a java class that represents the view a user sees when the select the home item in the
 * bottom navigation view. It displays a clickable list of books that navigate to their own
 * BookFragment.
 * @author Joshua Duque
 * @author Mateo Garcia
 * @author Emiliano Garza
 * @author Samatha Poole
 * @author Alaine Liserio
 * UTSA CS 3443 - Team Oranges Project
 * Fall 2024
 */
public class HomeFragment extends Fragment {

    private Context context;

    private List<Book> bookList;
    private BookDao bookDao;
    private BookAdapter bookAdapter;
    private NavController navController;

    private TextView noBooksTextView;

    /**
     * Constructor for the HomeFragment
     */
    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Method that starts a new instance of the HomeFragment.
     * @return HomeFragment with populated data
     */
    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    /**
     * Method that runs when the HomeFragment is first created.
     * @param savedInstanceState If the fragment is being re-created from
     * a previous saved state, this is the state.
     */
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

    /**
     * Method that runs when a new view is created
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     * @return View that is created
     */
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

    /**
     * Method that runs the Floating Action Button is clicked.
     * @param view Current view (View)
     */
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

    /**
     * Method that runs when a Book is clicked.
     * @param book Book that is clicked (Book)
     * @param position Position of the Book in the table (int)
     */
    private void onBookClick(Book book, int position) {
        // Create a bundle for the fragment we're about to navigate to
        Bundle bundle = new Bundle();
        // Put the book ID in the bundle
        bundle.putInt("bookId", book.getId());
        // Navigate to BookFragment with the bundle
        navController.navigate(R.id.navigation_book, bundle);
    }

    /**
     * Method that runs when a Book is long clicked.
     * @param book Book that is long clicked (Book)
     * @param position Position of the Book in the table (int)
     * @return boolean to represent Book being deleted
     */
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

    /**
     * Method that determines if the Book table is empty or not and displays the appropriate TextView.
     */
    private void updateNoBooksTextViewVisibility() {
        // Set the visibility of the "No books" TextView depending on whether the
        // book list is empty.
        noBooksTextView.setVisibility(bookList.isEmpty() ? View.VISIBLE : View.GONE);
    }

    /**
     * Method to add a Book to the table.
     * @param book Book to be added (Book)
     */
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

    /**
     * Method that deletes a Book from the table.
     * @param book Book to be deleted (Book)
     * @param position Position of the Book in the table (int)
     */
    private void deleteBook(Book book, int position) {
        // Delete book from database
        bookDao.delete(book);

        // Update the data list
        bookList.remove(position);

        // Notify the adapter
        bookAdapter.notifyItemRemoved(position);
        bookAdapter.notifyItemRangeChanged(position, bookList.size());
    }

    /**
     * Method that generates a random title for a newly created Book.
     * @return String of the title of the Book.
     */
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