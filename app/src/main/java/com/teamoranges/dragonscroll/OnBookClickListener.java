package com.teamoranges.dragonscroll;

import com.teamoranges.dragonscroll.models.Book;

/**
 * OnBookClickListener is a java Interface used to define a book click listener for the app's {@link BookAdapter}.
 * @author Joshua Duque
 * @author Mateo Garcia
 * @author Emiliano Garza
 * @author Samatha Poole
 * @author Alaine Liserio
 * UTSA CS 3443 - Team Oranges Project
 * Fall 2024
 */
public interface OnBookClickListener {
    /**
     * Method that runs when a Book is clicked.
     * @param book Book that is clicked (Book)
     * @param position Position of the Book in the table (int)
     */
    void onBookClick(Book book, int position);
}
