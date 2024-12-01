package com.teamoranges.dragonscroll;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
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

    private AppDatabase database;
    private BookDao bookDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Setup the view with a theme before it's created
        configureTheme();

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

        // Get the activity's BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavView);

        // Get the activity's NavHostFragment
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.navHostFragment);

        // Create an AppBarConfiguration
        AppBarConfiguration appBarConfig = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_profile, R.id.navigation_settings
        ).build();

        // Set activity's ActionBar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Configure navigation
        if (navHostFragment != null) {
            // Get the activity's NavController
            NavController navController = navHostFragment.getNavController();
            // Setup NavigationUI with our AppBarConfiguration
            NavigationUI.setupActionBarWithNavController(this, navController, appBarConfig);
            // Setup NavigationUI with the BottomNavigationView and NavController
            NavigationUI.setupWithNavController(bottomNavigationView, navController, false);
        }

        // Initialize the database.
        // NOTE: Notice how we allow queries on the main thread. This is very bad but we have no
        // time to scaffold out something proper. We'll use movie magic to reduce load times in
        // the presentation.
        database = Room.databaseBuilder(
                getApplicationContext(), AppDatabase.class, "books-db"
        ).allowMainThreadQueries().fallbackToDestructiveMigration().build();

        // Initialize the book database data access objects
        bookDao = database.bookDao();
    }

    private void configureTheme() {
        // Get shared preferences
        String preferenceFileKey = getString(R.string.preference_file_key);
        SharedPreferences sharedPreferences = getSharedPreferences(preferenceFileKey, MODE_PRIVATE);

        // Check if we got shared preferences successfully
        if (sharedPreferences != null) {
            // Check shared preferences for current theme
            String themesPreferenceKey = getString(R.string.themes_preference_key);
            String theme = sharedPreferences.getString(themesPreferenceKey, null);

            // Set theme if it exists in shared preferences
            if (theme != null) {
                switch (theme) {
                    case "theme_red":
                        setTheme(R.style.Red_Theme_DragonScroll);
                        break;
                    case "theme_orange":
                        setTheme(R.style.Orange_Theme_DragonScroll);
                        break;
                    case "theme_yellow":
                        setTheme(R.style.Yellow_Theme_DragonScroll);
                        break;
                    case "theme_green":
                        setTheme(R.style.Green_Theme_DragonScroll);
                        break;
                    case "theme_blue":
                        setTheme(R.style.Blue_Theme_DragonScroll);
                        break;
                    case "theme_purple":
                        setTheme(R.style.Purple_Theme_DragonScroll);
                        break;
                    case "theme_pink":
                        setTheme(R.style.Pink_Theme_DragonScroll);
                        break;
                    case "theme_brown":
                        setTheme(R.style.Brown_Theme_DragonScroll);
                        break;
                    case "theme_gray":
                        setTheme(R.style.Gray_Theme_DragonScroll);
                        break;
                    default:
                        setTheme(R.style.Base_Theme_DragonScroll);
                        break;
                }
            }
            // Apply text size changes
            float textSizeMultiplier = sharedPreferences.getFloat(
                    getString(R.string.text_size_preference_key), 1.0f
            );
            Configuration configuration = getResources().getConfiguration();
            configuration.fontScale = textSizeMultiplier;
            getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());
            // Apply dark mode preference
            String darkModePreferenceKey = getString(R.string.dark_mode_key);
            String darkMode = sharedPreferences.getString(darkModePreferenceKey, "system");
            switch (darkMode) {
                case "light":
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    break;
                case "dark":
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    break;
                default:
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                    break;
            }
        }
    }

    public AppDatabase getDatabase() {
        return database;
    }

    public BookDao getBookDao() {
        return bookDao;
    }
}