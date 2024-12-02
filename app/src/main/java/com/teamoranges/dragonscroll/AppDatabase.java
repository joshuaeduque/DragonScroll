package com.teamoranges.dragonscroll;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.teamoranges.dragonscroll.models.Book;

/**
 * AppDatabase is a java Room database that exposes a {@link BookDao} to read and write {@link Book} data.
 * @author Joshua Duque
 * @author Mateo Garcia
 * @author Emiliano Garza
 * @author Samatha Poole
 * @author Alaine Liserio
 * UTSA CS 3443 - Team Oranges Project
 * Fall 2024
 */
@Database(entities = {Book.class}, version = 6)
public abstract class AppDatabase extends RoomDatabase {
    /**
     * Method that returns the Book direct access object.
     * @return the database's Book DAO
     */
    public abstract BookDao bookDao();
}
