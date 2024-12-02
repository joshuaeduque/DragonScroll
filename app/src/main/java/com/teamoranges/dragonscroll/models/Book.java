package com.teamoranges.dragonscroll.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Book is a java class that defines the model for a Book object in the app's Room database.
 * @author Joshua Duque
 * @author Mateo Garcia
 * @author Emiliano Garza
 * @author Samatha Poole
 * @author Alaine Liserio
 * UTSA CS 3443 - Team Oranges Project
 * Fall 2024
 */
@Entity
public class Book {

    // Primary key used to store books in the database
    @PrimaryKey(autoGenerate = true)
    private int id;

    // Title of the book
    @ColumnInfo(name = "title")
    private String title;

    // Author of the book
    @ColumnInfo(name = "author")
    private String author;

    // Rating of the book
    @ColumnInfo(name = "rating")
    private int rating;

    // URI string of the book's cover image
    @ColumnInfo(name = "cover_uri")
    private String coverUri;

    // Summary of the book
    @ColumnInfo(name = "summary")
    private String summary;

    // Notes on the book
    @ColumnInfo(name = "notes")
    private String notes;

    // Reading start date of the book
    @ColumnInfo(name = "start_date")
    private String startDate;

    // Reading end date of the book
    @ColumnInfo(name = "end_date")
    private String endDate;

    /**
     * Getter to return the ID of the Book.
     * @return int of the ID of the Book
     */
    public int getId() {
        return id;
    }

    /**
     * Setter to set the ID of the Book.
     * @param id ID of the Book (int)
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Getter to return the title of the Book.
     * @return String of the title of the Book
     */
    public String getTitle() {
        return title;
    }

    /**
     * Setter to set the title of the Book.
     * @param title Title of the Book (String)
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Getter to return the author of the Book.
     * @return String of the author of the Book
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Setter to set the author of the Book.
     * @param author Author of the Book (String)
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * Getter to return the rating of the Book.
     * @return int of the rating of book
     */
    public int getRating() {
        return rating;
    }

    /**
     * Setter to set the rating of the Book.
     * @param rating Rating of the Book (int)
     */
    public void setRating(int rating) {
        this.rating = rating;
    }

    /**
     * Getter to return the cover URI of the Book.
     * @return String of the cover URI of the Book
     */
    public String getCoverUri() {
        return coverUri;
    }

    /**
     * Setter to set the cover URI of the Book.
     * @param coverUri Cover URI of the Book (String)
     */
    public void setCoverUri(String coverUri) {
        this.coverUri = coverUri;
    }

    /**
     * Getter to return the summary of the Book.
     * @return String of the summary of the Book
     */
    public String getSummary() {
        return summary;
    }

    /**
     * Setter to set the summary of the Book.
     * @param summary Summary of the Book (String)
     */
    public void setSummary(String summary) {
        this.summary = summary;
    }

    /**
     * Getter to return the notes of the Book.
     * @return String of the notes of the Book
     */
    public String getNotes() {
        return notes;
    }

    /**
     * Setter to set the notes of the Book.
     * @param notes Notes of the Book (String)
     */
    public void setNotes(String notes) {
        this.notes = notes;
    }

    /**
     * Getter to return the reading start date of the Book.
     * @return String of the reading start date of the Book
     */
    public String getStartDate() {
        return startDate;
    }

    /**
     * Setter to set the reading start date of the Book.
     * @param startDate Reading start date of the Book (String)
     */
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    /**
     * Getter to return the reading end date of the Book.
     * @return String of the reading end date of the Book.
     */
    public String getEndDate() {
        return endDate;
    }

    /**
     * Setter to set the reading end date of the Book.
     * @param endDate Reading end date of the Book (String)
     */
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
