package com.crowdshelf.app.bookInfo;

import android.graphics.Bitmap;

import io.realm.RealmObject;

/**
 * Created by Torstein on 01.09.2015.
 */
public class BookInfo extends RealmObject{
    private String isbn;
    private String title;
    private String subtitle;
    private String author;
    private String publisher;
    private String pubDate;
    private Bitmap artwork; // Maybe we need multiple sizes

    public BookInfo(String isbn, String title, String subtitle, String author, String publisher, String pubDate, Bitmap artwork) {
        this.isbn = isbn;
        this.title = title;
        this.subtitle = subtitle;
        this.author = author;
        this.publisher = publisher;
        this.pubDate = pubDate;
        this.artwork = artwork;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getAuthor() {
        return author;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getPubDate() {
        return pubDate;
    }

    public Bitmap getArtwork() {
        return artwork;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public void setArtwork(Bitmap artwork) {
        this.artwork = artwork;
    }
}
