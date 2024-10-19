package com.teamoranges.dragonscroll.models;

public class BookModel {
    private final String title, author;

    public BookModel(String title, String author) {
        this.title = title;
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }
}
