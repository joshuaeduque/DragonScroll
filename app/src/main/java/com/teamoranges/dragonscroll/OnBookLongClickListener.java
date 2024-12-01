package com.teamoranges.dragonscroll;

import com.teamoranges.dragonscroll.models.Book;

/**
 * OnBookLongClickListener is an interface that defines a method for long book clicks.
 * It's used by the BookAdapter.
 */
public interface OnBookLongClickListener {
    boolean onBookLongClick(Book book, int position);
}
