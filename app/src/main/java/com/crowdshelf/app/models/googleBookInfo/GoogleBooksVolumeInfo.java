package com.crowdshelf.app.models.googleBookInfo;

import java.util.ArrayList;
import java.util.List;

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
    private ArrayList<GoogleBooksIndustryIdentifier> industryIdentifiers;

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

    public List<GoogleBooksIndustryIdentifier> getIndustryIdentifiers() {
        return industryIdentifiers;
    }

    public String getIsbn10() {
        String isbn = "";
        if (getIndustryIdentifiers() != null) {
            for (GoogleBooksIndustryIdentifier industryIdentifier : getIndustryIdentifiers()) {
                if (industryIdentifier.getType().equals("ISBN_10")) {
                    isbn = industryIdentifier.getIdentifier();
                }
            }
        }
        return isbn;
    }

    public String getIsbn13() {
        String isbn = "";
        if (getIndustryIdentifiers() != null) {
            for (GoogleBooksIndustryIdentifier industryIdentifier : getIndustryIdentifiers()) {
                if (industryIdentifier.getType().equals("ISBN_13")) {
                    isbn = industryIdentifier.getIdentifier();
                }
            }
        }
        return isbn;
    }
}
