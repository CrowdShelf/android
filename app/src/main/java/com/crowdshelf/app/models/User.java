package com.crowdshelf.app.models;

import java.util.ArrayList;
import java.util.List;

import com.crowdshelf.app.MainController;
import com.crowdshelf.app.network.NetworkController;

import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Torstein on 01.09.2015.
 */
public class User extends RealmObject{
    private String rev;
    @PrimaryKey
    private String id;
    @Index
    private String username;

    public String getRev() {
        return rev;
    }

    public void setRev(String rev) {
        this.rev = rev;
    }

    public String getId() {
        return id;
    }

    public void setId(String userId) {
        this.id = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    /* Does not work with realm
    public String toString() {
        return "username: " + String.valueOf(username);
    }

    // For JUnit testing
    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        } else {
            final User user = (User) obj;
            return this.id.equals(user.id) &&
                    this.username.equals(user.username);
        }
    }
    */
}
