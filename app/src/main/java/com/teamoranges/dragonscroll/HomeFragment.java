package com.teamoranges.dragonscroll;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

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

    // Timer-related fields
    private TextView timerTextView;
    private Button startButton;
    private Handler handler;
    private Runnable timerRunnable;
    private long startTime;
    private boolean isTimerRunning = false;

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
            book.setTitle(getRandomTitle());
            book.setAuthor("No Author");

            // Add book and update view
            addBook(book);

            // Update noBooksTextView visibility
            updateNoBooksTextViewVisibility();
        });

        // Timer setup
        timerTextView = view.findViewById(R.id.timerTextView);
        startButton = view.findViewById(R.id.startButton);

        startButton.setOnClickListener(v -> toggleTimer());

        return view;
    }

    private void toggleTimer() {
        if (isTimerRunning) {
            // Stop the timer
            handler.removeCallbacks(timerRunnable);
            startButton.setText("Start");
        } else {
            // Start the timer
            startCounting();
            startButton.setText("Stop");
        }
        isTimerRunning = !isTimerRunning;
    }

    private void startCounting() {
        if (handler == null) {
            handler = new Handler();
        }

        // Record the start time
        startTime = System.currentTimeMillis();

        // Define the Runnable
        timerRunnable = new Runnable() {
            @Override
            public void run() {
                long elapsedMillis = System.currentTimeMillis() - startTime;
                long seconds = elapsedMillis / 1000 % 60;
                long minutes = elapsedMillis / 1000 / 60;

                // Update the TextView
                timerTextView.setText(String.format("%02d:%02d", minutes, seconds));

                // Schedule the next update
                handler.postDelayed(this, 1000);
            }
        };

        // Start the Runnable
        handler.post(timerRunnable);
    }

    private void updateNoBooksTextViewVisibility() {
        noBooksTextView.setVisibility(bookList.isEmpty() ? View.VISIBLE : View.GONE);
    }

    private void addBook(Book book) {
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // Stop the Runnable when the view is destroyed
        if (handler != null && timerRunnable != null) {
            handler.removeCallbacks(timerRunnable);
        }
    }

    private String getRandomTitle() {
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
