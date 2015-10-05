package com.crowdshelf.app.models;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Torstein on 01.09.2015.
 */
public class Crowd extends RealmObject {
    @PrimaryKey
    @SerializedName("_id")
    private String id; // Unique identifier
    @Index
    private String name;
    private String owner; // _id of owner
    private RealmList<MemberId> members; // _id of member

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String userId) {
        this.owner = userId;
    }

    public RealmList<MemberId> getMembers() {
        return this.members;
    }

    public void setMembers(RealmList<MemberId> userIds) {
        this.members = userIds;
    }

    /* Does not work with realm
    public String toString() {
        return "_id: " + String.valueOf(id) +
                "\nname: " + String.valueOf(name) +
                "\nowner: " + String.valueOf(owner) +
                "\nmembers: " + String.valueOf(members);
    }

    // For JUnit testing
    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        } else {
            final Crowd crowd = (Crowd) obj;
            return this.id.equals(crowd.getId())
                    && this.name.equals(crowd.name)
                    && this.owner.equals(crowd.owner)
                    && this.members.equals(crowd.members);
        }
    }
    */
}
