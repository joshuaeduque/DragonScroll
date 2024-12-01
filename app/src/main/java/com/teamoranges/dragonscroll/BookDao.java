package com.teamoranges.dragonscroll;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.teamoranges.dragonscroll.models.Book;

import java.util.List;

/**
 * Direct access object interface for the app's Room database.
 * It defines methods that translate to SQLite queries for reading and writing
 * {@link Book} data.
 */
@Dao
public interface BookDao {

    // Get every book in the table
    @Query("SELECT * FROM book")
    List<Book> getAll();

    // Get the number of books in the table
    @Query("SELECT COUNT(*) FROM book")
    int getCount();

    // Get a book from the table by id
    @Query("SELECT * FROM book WHERE id = :id LIMIT 1")
    Book getBook(int id);

    // Set a book's title by id
    @Query("UPDATE book SET title = :title WHERE id = :id")
    void setTitle(int id, String title);

    // Set a book's author by id
    @Query("UPDATE book SET author = :author WHERE id = :id")
    void setAuthor(int id, String author);

    // Set a book's rating by id
    @Query("UPDATE book SET rating = :rating WHERE id = :id")
    void setRating(int id, int rating);

    // Set a book's cover uri string by id
    @Query("UPDATE book SET cover_uri = :coverUri WHERE id = :id")
    void setCoverUri(int id, String coverUri);

    // Set a book's summary by id
    @Query("UPDATE book SET summary = :summary WHERE id =:id")
    void setSummary(int id, String summary);

    // Set a book's notes by id
    @Query("UPDATE book SET notes = :notes WHERE id =:id")
    void setNotes(int id, String notes);

    // Set a book's reading start date by id
    @Query("UPDATE book SET start_date = :startDate WHERE id = :id")
    void setStartDate(int id, String startDate);

    // Set a book's reading end date by id
    @Query("UPDATE book SET end_date = :endDate WHERE id = :id")
    void setEndDate(int id, String endDate);

    // Insert a book or list of books into the table
    @Insert
    void insertAll(Book... books);

    // Insert a book into the table and return it's id
    @Insert
    long insert(Book book);

    // Delete a book from the table
    @Delete
    void delete(Book book);

    // Remove every book from the table
    @Query("DELETE FROM book")
    void nukeTable();
}
