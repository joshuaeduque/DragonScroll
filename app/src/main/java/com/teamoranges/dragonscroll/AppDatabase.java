package com.teamoranges.dragonscroll;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.teamoranges.dragonscroll.models.Book;

@Database(entities = {Book.class}, version = 4)
public abstract class AppDatabase extends RoomDatabase {
    public abstract BookDao bookDao();
}
