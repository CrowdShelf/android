package com.crowdshelf.app.models;

import java.util.ArrayList;

import com.crowdshelf.app.MainController;
import com.crowdshelf.app.network.NetworkController;

/**
 * Created by Torstein on 01.09.2015.
 */
public class Crowd {
    private String _id; // Unique identified
    private String name;
    private String owner;
    private ArrayList<String> members;

    public void setId(String _id) {
        this._id = _id;
    }

    public String getId() {
        return _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwnerName() {
        return owner;
    }

    public User getOwner() {
        return MainController.getUser(owner);
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public ArrayList<String> getMembersNames() {
        return this.members;
    }

    public ArrayList<User> getMembers() {
        return MainController.getUsers(members);
    }

    public void setMembers(ArrayList<String> members) {
        this.members = members;
    }

    public void addMember(String username) {
        if (!members.contains(username)) {
            members.add(username);
            NetworkController.addCrowdMember(this._id, username);
        }
    }

    public void removeMember(String username) {
        members.remove(username);
        NetworkController.removeCrowdMember(this._id, username);
    }

    public String toString() {
        return "_id: " + String.valueOf(_id) +
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
            return this._id.equals(crowd.getId())
                    && this.name.equals(crowd.getName())
                    && this.owner.equals(crowd.getMembers())
                    && this.members.equals(crowd.getMembers());
        }
    }
}
