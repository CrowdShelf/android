package com.crowdshelf.app.models;

import android.media.Image;

/**
 * Created by Torstein on 01.09.2015.
 */
public class BookInfo {
    private String isbn;
    private String title;
    private String subtitle;
    private String author;
    private String publisher;
    private String pubDate;
    private Image artwork; // Maybe we need multiple sizes

    public BookInfo(String isbn, String title, String subtitle, String author, String publisher, String pubDate, Image artwork) {
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

    public Image getArtwork() {
        return artwork;
    }

}
