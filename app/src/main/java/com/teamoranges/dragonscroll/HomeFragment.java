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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.teamoranges.dragonscroll.models.BookModel;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    private final String TAG = "HomeFragment";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private RecyclerView recyclerView;
    private BookAdapter bookAdapter;
    private List<BookModel> bookList;
    private NavController navController;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

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
        Context context = requireContext();

        // Setup RecyclerView
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        // Setup List of BookModel with fake data
        bookList = new ArrayList<BookModel>();
        bookList.add(new BookModel("Book Title", "Book Author"));
        bookList.add(new BookModel("1984", "George Orwell"));
        bookList.add(new BookModel("Charlotte's Web", "E. B. White"));
        bookList.add(new BookModel("World War Z", "Max Brooks"));
        bookList.add(new BookModel("Animal Farm", "George Orwell"));
        bookList.add(new BookModel("Dracula", "Bram Stoker"));
        bookList.add(new BookModel("Iliad", "Homer"));
        bookList.add(new BookModel("Adventures of Huckleberry Finn", "Mark Twain"));
        bookList.add(new BookModel("Lord of the Flies", "William Golding"));
        bookList.add(new BookModel("The Cat in the Hat", "Dr. Seuss"));

        // Setup BookAdapter with RecyclerView
        bookAdapter = new BookAdapter(bookList, (book, position) -> {
            Log.i("HomeFragment", String.format("%s at pos %d", book.getTitle(), position));
            navController.navigate(R.id.navigation_book);
        });
        recyclerView.setAdapter(bookAdapter);

        return view;
    }
}