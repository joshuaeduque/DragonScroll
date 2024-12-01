package com.teamoranges.dragonscroll;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.teamoranges.dragonscroll.models.Book;

/**
 * This class extends Android's Room database and gives access to the app's
 * book direct access object.
 */
@Database(entities = {Book.class}, version = 6)
public abstract class AppDatabase extends RoomDatabase {
    public abstract BookDao bookDao();
}
