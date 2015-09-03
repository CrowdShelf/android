package com.crowdshelf.app.jsonModels;

/**
 * Created by markuslund92 on 01.09.15.
 */
public class GoogleBooksItem {

    private String id;
    private String etag;
    private GoogleBooksVolumeInfo volumeInfo;

    public GoogleBooksVolumeInfo getVolumeInfo(){
        return volumeInfo;
    }

    @Override
    public String toString() {
        return "id: " + id + "\tetag: " + etag;
    }
}
