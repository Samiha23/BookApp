package com.android.samiha.bookapp;

import java.util.ArrayList;

/**
 * Created by Samiha on 24/10/2017.
 */

public class BookSearchedList {
    private int totalItems;
    private ArrayList<Book> books;

    public BookSearchedList(int totalItems, ArrayList<Book> books) {
        this.totalItems = totalItems;
        this.books = books;
    }

    public ArrayList<Book> getBooks() {
        return books;
    }

}
