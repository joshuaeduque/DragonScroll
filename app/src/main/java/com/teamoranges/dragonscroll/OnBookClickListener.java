package com.teamoranges.dragonscroll;

import com.teamoranges.dragonscroll.models.Book;

/**
 * OnBookClickListener is an interface that defines a method for book clicks. It's used
 * by the BookAdapter.
 */
public interface OnBookClickListener {
    void onBookClick(Book book, int position);
}
