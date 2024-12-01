package com.teamoranges.dragonscroll;

import com.teamoranges.dragonscroll.models.Book;

/**
 * OnBookLongClickListener is a java Interface used to define a book long click listener for the app's {@link BookAdapter}.
 * @author Joshua Duque
 * @author Mateo Garcia
 * @author Emiliano Garza
 * @author Samatha Poole
 * @author Alaine Liserio
 * UTSA CS 3443 - Team Oranges Project
 * Fall 2024
 */
public interface OnBookLongClickListener {
    /**
     * Method that runs when a Book is long clicked.
     * @param book Book that is long clicked (Book)
     * @param position Position of the Book in the table (int)
     * @return boolean to represent Book being deleted
     */
    boolean onBookLongClick(Book book, int position);
}
