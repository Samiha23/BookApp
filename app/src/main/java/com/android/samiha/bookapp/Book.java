package com.android.samiha.bookapp;

import java.util.ArrayList;

/**
 * Created by Samiha on 24/10/2017.
 */

public class Book {
    private String title;
    private ArrayList<String> author;
    private String description;

    public Book(String title, ArrayList<String> author, String description) {
        this.title = title;
        this.author = author;
        this.description = description;
    }

    public String getTitle() {

        return title;
    }

    public ArrayList<String> getAuthor() {
        return author;
    }

    public String getDescription() {
        return description;
    }

}
