package com.android.samiha.bookapp;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Samiha on 24/10/2017.
 */

public class BookArrayAdapter extends ArrayAdapter<Book> {

    private Context context;
    private int resource;
    private ArrayList<Book> books;

    public BookArrayAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<Book> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        books = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(resource, null);
        }

        TextView bookName = (TextView) convertView.findViewById(R.id.book_name);
        TextView authorName = (TextView) convertView.findViewById(R.id.author_name);
        TextView description = (TextView) convertView.findViewById(R.id.description);


        Book book = books.get(position);
        bookName.setText(book.getTitle());
        ArrayList<String> authorArrayList = book.getAuthor();
        String authors = "";
        for (int i = 0; i < authorArrayList.size(); i++) {
            authors += authorArrayList.get(i);
        }
        authorName.setText(authors);
        description.setText(book.getDescription());


        return convertView;
    }
}
