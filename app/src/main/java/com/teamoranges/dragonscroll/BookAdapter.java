package com.teamoranges.dragonscroll;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.teamoranges.dragonscroll.models.Book;

import java.util.List;

/**
 * BookAdapter is a java class that extends {@link RecyclerView.Adapter} used to display {@link Book} data.
 * It supports clicks and long clicks on items.
 * @author Joshua Duque
 * @author Mateo Garcia
 * @author Emiliano Garza
 * @author Samatha Poole
 * @author Alaine Liserio
 * UTSA CS 3443 - Team Oranges Project
 * Fall 2024
 */
public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {

    // List of book objects
    private final List<Book> bookList;
    // Adapter's book click listener
    private final OnBookClickListener onBookClickListener;
    // Adapter's book long click listener
    private final OnBookLongClickListener onBookLongClickListener;

    /**
     * Constructor to initialize the {@link BookAdapter} object.
     * @param bookList List of Books (List of type Book)
     * @param onBookClickListener Book item click listener (OnBookClickListener)
     * @param onBookLongClickListener Book item long click listener (OnBookLongClickListener)
     */
    public BookAdapter(List<Book> bookList, OnBookClickListener onBookClickListener, OnBookLongClickListener onBookLongClickListener) {
        this.bookList = bookList;
        this.onBookClickListener = onBookClickListener;
        this.onBookLongClickListener = onBookLongClickListener;
    }
    /**
     * Method that sets up a new BookViewHolder.
     * @param parent The ViewGroup into which the new View will be added after it is bound to
     *               an adapter position.
     * @param viewType The view type of the new View.
     * @return New BookViewHolder
     */
    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.book_item, parent, false);

        return new BookViewHolder(view);
    }
    /**
     * Method that populates a Book object into a BookViewHolder
     * @param holder The ViewHolder which should be updated to represent the contents of the
     *        item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        // Get book at position
        Book book = bookList.get(position);

        // Set holder title and author from book
        holder.title.setText(book.getTitle());
        holder.author.setText(book.getAuthor());

        // Set holder cover if book has a uri for it
        if (book.getCoverUri() != null && !book.getCoverUri().isEmpty()) {
            holder.cover.setImageURI(Uri.parse(book.getCoverUri()));
        }

        // Set holder on book click listener
        holder.itemView.setOnClickListener(view -> {
            onBookClickListener.onBookClick(book, position);
        });

        // Set holder on book long click listener
        holder.itemView.setOnLongClickListener(view -> {
            return onBookLongClickListener.onBookLongClick(book, position);
        });
    }

    /**
     * Getter to return the size of the bookList.
     * @return int of the size of the bookList
     */
    @Override
    public int getItemCount() {
        return bookList.size();
    }

    /**
     * BookViewHolder is a java class for book items in the adapter.
     */
    public static class BookViewHolder extends RecyclerView.ViewHolder {
        private final TextView title, author;
        private final ImageView cover;

        /**
         * Constructor to initialize the BookViewHolder object.
         * @param itemView Interface components for the BookViewHolder (View)
         */
        public BookViewHolder(@NonNull View itemView) {
            super(itemView);

            // Set holder views 
            title = itemView.findViewById(R.id.titleTextView);
            author = itemView.findViewById(R.id.authorTextView);
            cover = itemView.findViewById(R.id.profileImageView);
        }
    }
}
