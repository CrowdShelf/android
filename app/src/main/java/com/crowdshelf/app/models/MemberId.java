package com.crowdshelf.app.models;

import io.realm.RealmObject;

/**
 * Created by Torstein on 19.09.2015.
 */
// Needed to create list with member id's in a crowd as Realm does not support lists with Strings
public class MemberId extends RealmObject{
    private String id;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }
}
