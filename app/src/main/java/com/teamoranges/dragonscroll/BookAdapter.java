package com.teamoranges.dragonscroll;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.teamoranges.dragonscroll.models.Book;

import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {

    private static final String TAG = "BookAdapter";

    private final List<Book> bookList;
    private final OnBookClickListener onClickListener;
    private final OnBookLongClickListener onLongClickListener;

    public BookAdapter(List<Book> bookList, OnBookClickListener onClickListener, OnBookLongClickListener onLongClickListener) {
        this.bookList = bookList;
        this.onClickListener = onClickListener;
        this.onLongClickListener = onLongClickListener;
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
        Book book = bookList.get(position);
        holder.title.setText(book.getTitle());
        holder.author.setText(book.getAuthor());

        holder.itemView.setOnClickListener(view -> {
            onClickListener.onBookClick(book, position);
        });

        holder.itemView.setOnLongClickListener(view -> {
            return onLongClickListener.onBookLongClick(book, position);
        });
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public static class BookViewHolder extends RecyclerView.ViewHolder {
        private final TextView title, author;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.titleTextView);
            author = itemView.findViewById(R.id.authorTextView);
        }
    }
}
