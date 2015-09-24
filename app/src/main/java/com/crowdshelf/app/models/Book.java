package com.crowdshelf.app.models;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Torstein on 01.09.2015.
 */
public class Book extends RealmObject{
    @PrimaryKey
    @SerializedName("_id")
    private String id;
    @Index
    private String isbn;
    @Index
    private String owner; // user _id
    @Index
    private String rentedTo; // user _id
    private boolean availableForRent;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String userId) {
        this.owner = userId;
    }

    public String getRentedTo() {
        return rentedTo;
    }

    public void setRentedTo(String userId) {
        this.rentedTo = userId;
    }

    public boolean getAvailableForRent() {
        return availableForRent;
    }

    public void setAvailableForRent(boolean availableForRent) {
        this.availableForRent = availableForRent;
    }

    /* Does not work with realm:
    public String toString() {
        return "_id: " + String.valueOf(id) +
                "\nisbn: " + String.valueOf(isbn) +
                "\nowner: " + String.valueOf(owner) +
                "\nrentedTo: " + String.valueOf(rentedTo);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        } else {
            final Book book = (Book) obj;
            return this.isbn.equals(book.isbn)
                    && this.owner.equals(book.owner)
                    && this.rentedTo.equals(book.rentedTo);
        }
    }
    */
}
