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

    @Insert
    void insertAll(Book... books);

    @Delete
    void delete(Book book);
}
