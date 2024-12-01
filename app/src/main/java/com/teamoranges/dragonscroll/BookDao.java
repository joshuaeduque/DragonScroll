package com.teamoranges.dragonscroll;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.teamoranges.dragonscroll.models.Book;

import java.util.List;

/**
 * BookDao is a direct access java object interface for the app's Room database.
 * It defines methods that translate to SQLite queries for reading and writing
 * {@link Book} data.
 * @author Joshua Duque
 * @author Mateo Garcia
 * @author Emiliano Garza
 * @author Samatha Poole
 * @author Alaine Liserio
 * UTSA CS 3443 - Team Oranges Project
 * Fall 2024
 */
@Dao
public interface BookDao {
    /**
     * Getter to return every Book in the table
     * @return List of all Books in the table
     */
    @Query("SELECT * FROM book")
    List<Book> getAll();

    /**
     * Getter to return the number of Books in the table.
     * @return int of the number of Books in the table.
     */
    @Query("SELECT COUNT(*) FROM book")
    int getCount();

    /**
     * Getter to return a Book from the table by ID.
     * @param id ID of the Book (int)
     * @return Book that matches the ID
     */
    @Query("SELECT * FROM book WHERE id = :id LIMIT 1")
    Book getBook(int id);

    /**
     * Setter to set a Book's title by ID.
     * @param id ID of the Book (int)
     * @param title Title of the Book (String)
     */
    @Query("UPDATE book SET title = :title WHERE id = :id")
    void setTitle(int id, String title);

    /**
     * Setter to set a Book's author by ID.
     * @param id ID of the Book (int)
     * @param author Author of the Book (String)
     */
    @Query("UPDATE book SET author = :author WHERE id = :id")
    void setAuthor(int id, String author);

    /**
     * Setter to set a Book's rating by ID.
     * @param id ID of the Book (int)
     * @param rating Rating of the Book (int)
     */
    @Query("UPDATE book SET rating = :rating WHERE id = :id")
    void setRating(int id, int rating);

    /**
     * Setter to set a Book's Cover URI by ID.
     * @param id ID of the Book (int)
     * @param coverUri Cover URI of the Book (String)
     */
    @Query("UPDATE book SET cover_uri = :coverUri WHERE id = :id")
    void setCoverUri(int id, String coverUri);

    /**
     * Setter to set a Book's summary by ID.
     * @param id ID of the Book (int)
     * @param summary Summary of the Book (String)
     */
    @Query("UPDATE book SET summary = :summary WHERE id =:id")
    void setSummary(int id, String summary);

    /**
     * Setter to set a Book's notes by ID.
     * @param id ID of the Book (int)
     * @param notes Notes of the Book (String)
     */
    @Query("UPDATE book SET notes = :notes WHERE id =:id")
    void setNotes(int id, String notes);

    /**
     * Setter to set a Book's reading start date by ID.
     * @param id ID of the Book (int)
     * @param startDate Reading start date of the Book (String)
     */
    @Query("UPDATE book SET start_date = :startDate WHERE id = :id")
    void setStartDate(int id, String startDate);

    /**
     * Setter to set a Book's reading end date by ID.
     * @param id ID of the Book (int)
     * @param endDate Reading end date of the Book (String)
     */
    @Query("UPDATE book SET end_date = :endDate WHERE id = :id")
    void setEndDate(int id, String endDate);

    /**
     * Method that inserts a Book or List of Books into the table.
     * @param books Book or List of Books to be inserted (Book)
     */
    @Insert
    void insertAll(Book... books);

    /**
     * Method that inserts a Book into the table and returns its ID.
     * @param book Inserted Book (Book)
     * @return int of the ID of the Book
     */
    @Insert
    long insert(Book book);

    /**
     * Method that deletes a Book from the table.
     * @param book Deleted Book (Book)
     */
    @Delete
    void delete(Book book);

    /**
     * Method that deletes every Book from the table.
     */
    @Query("DELETE FROM book")
    void nukeTable();
}
