package com.teamoranges.dragonscroll;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.teamoranges.dragonscroll.models.Book;

/**
 * Room database that exposes a {@link BookDao} to read and write {@link Book} data.
 */
@Database(entities = {Book.class}, version = 6)
public abstract class AppDatabase extends RoomDatabase {
    public abstract BookDao bookDao();
}
