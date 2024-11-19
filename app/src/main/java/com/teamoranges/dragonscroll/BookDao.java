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

    @Query("SELECT * FROM book WHERE id = :id LIMIT 1")
    Book getBook(int id);

    @Query("UPDATE book SET title = :title WHERE id = :id")
    void setTitle(int id, String title);

    @Query("UPDATE book SET author = :author WHERE id = :id")
    void setAuthor(int id, String author);

    @Query("UPDATE book SET rating = :rating WHERE id = :id")
    void setRating(int id, int rating);

    @Insert
    void insertAll(Book... books);

    @Delete
    void delete(Book book);
}
