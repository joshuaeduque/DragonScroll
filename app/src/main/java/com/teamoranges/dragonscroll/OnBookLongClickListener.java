package com.teamoranges.dragonscroll;

import com.teamoranges.dragonscroll.models.Book;

/**
 * Interface used to define a book long click listener for the app's {@link BookAdapter}.
 */
public interface OnBookLongClickListener {
    boolean onBookLongClick(Book book, int position);
}
