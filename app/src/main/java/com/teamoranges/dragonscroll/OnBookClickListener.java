package com.teamoranges.dragonscroll;

import com.teamoranges.dragonscroll.models.Book;

/**
 * Interface used to define a book click listener for the app's {@link BookAdapter}.
 */
public interface OnBookClickListener {
    void onBookClick(Book book, int position);
}
