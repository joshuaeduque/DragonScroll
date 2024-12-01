package com.teamoranges.dragonscroll;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.teamoranges.dragonscroll.models.Book;

import java.util.List;
import java.util.Random;

public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";

    private NavController navController;
    private Context context;
    private BookDao bookDao;
    private List<Book> bookList;
    private BookAdapter bookAdapter;
    private TextView noBooksTextView;
    private LinearLayout inputContainer;

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
        floatingActionButton.setOnClickListener(v -> showAddBookDialog());

        return view;
    }

    private void showAddBookDialog() {
        // Inflate the existing LinearLayout
        View dialogView = getView().findViewById(R.id.alertDialogCustomView);

        // Avoid IllegalStateException
        if (dialogView.getParent() != null) {
            ((ViewGroup) dialogView.getParent()).removeView(dialogView);
        }

        // Set the visibility of the input container to visible
        dialogView.setVisibility(View.VISIBLE);

        // Get the EditText fields
        EditText bookTitleEditText = dialogView.findViewById(R.id.dialogBookTitleText);
        EditText bookAuthorEditText = dialogView.findViewById(R.id.dialogBookAuthorText);

        // Create the AlertDialog
        AlertDialog.Builder alert = new AlertDialog.Builder(requireContext());
        alert.setTitle("Add Book")
                .setView(dialogView)
                .setPositiveButton("Add", (dialogInterface, i) -> {
                    String title = bookTitleEditText.getText().toString().trim();
                    String author = bookAuthorEditText.getText().toString().trim();

                    // Validate input
                    if (title.isEmpty() || author.isEmpty()) {
                        Toast.makeText(requireContext(), "Please fill out all fields", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Create a new Book object
                    Book book = new Book();
                    book.setTitle(title);
                    book.setAuthor(author);

                    // Add the book to the database or list
                    addBook(book);

                    // Notify the user
                    Toast.makeText(requireContext(), "Book added!", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", (dialogInterface, i) -> {
                    // Reset the visibility of the LinearLayout to GONE when the dialog is dismissed
                    dialogView.setVisibility(View.GONE);
                    dialogInterface.dismiss();
                })
                .show();
    }

    private void onBookClick(Book book, int position) {
        Bundle bundle = new Bundle();

        bundle.putInt("bookId", book.getId());

        // Navigate to BookFragment with book data
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
        noBooksTextView.setVisibility(bookList.isEmpty() ? View.VISIBLE : View.GONE);
    }

    private void addBook(Book book) {
        // Insert book into database
        // bookDao.insertAll(book);
        long insertId = bookDao.insert(book);

        // Update the data list
        book.setId((int)insertId);
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

}