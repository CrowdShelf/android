package com.crowdshelf.app.jsonModels;

/**
 * Created by markuslund92 on 01.09.15.
 */
public class GoogleBooksVolumeInfo {
    private String title;
    private String subTitle;
    private String publisher;
    private String publishedDate;
    private String description;
    private GoogleBooksImageLinks imageLinks;
    private String[] authors;

    public String getTitle() {
        return title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public String getDescription() { return description;}

    public GoogleBooksImageLinks getImageLinks() {
        return imageLinks;
    }

    public String[] getAuthors() {
        return authors;
    }
}
