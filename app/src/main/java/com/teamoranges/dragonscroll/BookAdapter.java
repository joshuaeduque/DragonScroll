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
 * {@link RecyclerView.Adapter} used to display {@link Book} data.
 * It supports clicks and long clicks on items.
 */
public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {

    // List of book objects
    private final List<Book> bookList;
    // Adapter's book click listener
    private final OnBookClickListener onBookClickListener;
    // Adapter's book long click listener
    private final OnBookLongClickListener onBookLongClickListener;

    /**
     * Sole constructor for the {@link BookAdapter}.
     * @param bookList book list
     * @param onBookClickListener book item click listener
     * @param onBookLongClickListener book item long click listener
     */
    public BookAdapter(List<Book> bookList, OnBookClickListener onBookClickListener, OnBookLongClickListener onBookLongClickListener) {
        this.bookList = bookList;
        this.onBookClickListener = onBookClickListener;
        this.onBookLongClickListener = onBookLongClickListener;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.book_item, parent, false);

        return new BookViewHolder(view);
    }

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
     * @return the size of the book list
     */
    @Override
    public int getItemCount() {
        return bookList.size();
    }

    /**
     * BookViewHolder for book items in the adapter.
     */
    public static class BookViewHolder extends RecyclerView.ViewHolder {
        private final TextView title, author;
        private final ImageView cover;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);

            // Set holder views 
            title = itemView.findViewById(R.id.titleTextView);
            author = itemView.findViewById(R.id.authorTextView);
            cover = itemView.findViewById(R.id.profileImageView);
        }
    }
}
