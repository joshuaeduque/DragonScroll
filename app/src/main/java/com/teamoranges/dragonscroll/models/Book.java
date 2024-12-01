package com.teamoranges.dragonscroll.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Defines the model for a book object in the app's Room database.
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
     * @return ID of book
     */
    public int getId() {
        return id;
    }

    /**
     * @param id ID of book
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return title of book
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title title of book
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return author of book
     */
    public String getAuthor() {
        return author;
    }

    /**
     * @param author author of book
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * @return rating of book
     */
    public int getRating() {
        return rating;
    }

    /**
     * @param rating rating of book
     */
    public void setRating(int rating) {
        this.rating = rating;
    }

    /**
     * @return cover URI string of book
     */
    public String getCoverUri() {
        return coverUri;
    }

    /**
     * @param coverUri cover URI string of book
     */
    public void setCoverUri(String coverUri) {
        this.coverUri = coverUri;
    }

    /**
     * @return summary of book
     */
    public String getSummary() {
        return summary;
    }

    /**
     * @param summary summary of book
     */
    public void setSummary(String summary) {
        this.summary = summary;
    }

    /**
     * @return notes on book
     */
    public String getNotes() {
        return notes;
    }

    /**
     * @param notes notes on book
     */
    public void setNotes(String notes) {
        this.notes = notes;
    }

    /**
     * @return reading start date of book
     */
    public String getStartDate() {
        return startDate;
    }

    /**
     * @param startDate reading start date of book
     */
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    /**
     * @return reading end date of book
     */
    public String getEndDate() {
        return endDate;
    }

    /**
     * @param endDate reading end date of book
     */
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
