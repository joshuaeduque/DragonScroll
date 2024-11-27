package com.teamoranges.dragonscroll;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.room.Room;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "MainActivity";

    private AppDatabase database;
    private BookDao bookDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Get shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences(
                getString(R.string.preference_file_key), MODE_PRIVATE);
        if (sharedPreferences != null) {
            // Change theme before view is created
            String themesPreferenceKey = getString(R.string.themes_preference_key);
            String theme = sharedPreferences.getString(themesPreferenceKey, null);
            if (theme != null) {
                if (theme.equals("theme_blue")) {
                    setTheme(R.style.Blue_Theme_DragonScroll);
                }
            }
        }

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            // Using systemBars.bottom adds unnecessary padding to the BottomNavigationView.
            // I don't know why it does, but we're removing it for now.
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });

        // Get the BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavView);

        // Try getting NavHostFragment
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.navHostFragment);
        if (navHostFragment == null)
            return;

        // Create an AppBarConfiguration
        AppBarConfiguration appBarConfig = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_profile, R.id.navigation_settings
        ).build();

        // Set activity toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Get the NavController from the NavHostFragment
        NavController navController = navHostFragment.getNavController();

        // Setup NavigationUI with our AppBarConfiguration
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfig);
        // Setup NavigationUI with the BottomNavigationView and NavController
        NavigationUI.setupWithNavController(bottomNavigationView, navController, false);

        // Create books database
        // Notice how we're allowed queries on the main thread
        // That's normally a big no-no but there's no time to scaffold
        // something proper. Too bad!
        database = Room.databaseBuilder(
                getApplicationContext(), AppDatabase.class, "books-db"
        ).allowMainThreadQueries().fallbackToDestructiveMigration().build();
        bookDao = database.bookDao();
    }

    public AppDatabase getDatabase() {
        return database;
    }

    public BookDao getBookDao() {
        return bookDao;
    }
}