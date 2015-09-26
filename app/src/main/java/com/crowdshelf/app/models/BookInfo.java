package com.crowdshelf.app.models;

import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Torstein on 01.09.2015.
 */
public class BookInfo extends RealmObject{
    @PrimaryKey
    private String isbn;
    private String title;
    private String subtitle;
    private String author;
    private String publisher;
    private String pubDate;
    private String description;
    private byte[] artworkByteArray; // Maybe we need multiple sizes?

    public BookInfo(){
        //Realm demands empty constructor
    }

    public BookInfo(String isbn, String title, String subtitle, String author, String publisher, String pubDate, byte[] artworkByteArray, String description) {
        this.isbn = isbn;
        this.title = title;
        this.subtitle = subtitle;
        this.author = author;
        this.publisher = publisher;
        this.pubDate = pubDate;
        this.artworkByteArray = artworkByteArray;
        this.description = description;
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

    public byte[] getArtworkByteArray() {
        return artworkByteArray;
    }

    public String getDescription() {
        return description;
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

    public void setArtworkByteArray(byte[] artwork) {
        this.artworkByteArray = artwork;
    }
    
    public void setDescription(String description){
        this.description = description;
    }
}
