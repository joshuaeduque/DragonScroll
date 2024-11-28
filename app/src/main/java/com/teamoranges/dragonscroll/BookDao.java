package com.teamoranges.dragonscroll;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.teamoranges.dragonscroll.models.Book;

import java.util.List;

@Dao
public interface BookDao {
    @Query("SELECT * FROM book")
    List<Book> getAll();

    @Query("SELECT COUNT(*) FROM book")
    int getCount();

    @Query("SELECT * FROM book WHERE id = :id LIMIT 1")
    Book getBook(int id);

    @Query("UPDATE book SET title = :title WHERE id = :id")
    void setTitle(int id, String title);

    @Query("UPDATE book SET author = :author WHERE id = :id")
    void setAuthor(int id, String author);

    @Query("UPDATE book SET rating = :rating WHERE id = :id")
    void setRating(int id, int rating);

    @Query("UPDATE book SET cover_uri = :coverUri WHERE id = :id")
    void setCoverUri(int id, String coverUri);

    @Query("UPDATE book SET summary = :summary WHERE id =:id")
    void setSummary(int id, String summary);

    @Query("UPDATE book SET notes = :notes WHERE id =:id")
    void setNotes(int id, String notes);

    @Query("UPDATE book SET start_date = :startDate WHERE id = :id")
    void setStartDate(int id, String startDate);

    @Query("UPDATE book SET end_date = :endDate WHERE id = :id")
    void setEndDate(int id, String endDate);

    @Insert
    void insertAll(Book... books);

    @Insert
    long insert(Book book);

    @Delete
    void delete(Book book);

    @Query("DELETE FROM book")
    void nukeTable();
}
