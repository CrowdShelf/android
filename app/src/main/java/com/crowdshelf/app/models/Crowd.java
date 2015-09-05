package com.crowdshelf.app.models;

import java.util.ArrayList;

import com.crowdshelf.app.MainController;
import com.crowdshelf.app.NetworkController;

/**
 * Created by Torstein on 01.09.2015.
 */
public class Crowd {
    private String _id; // Unique identified
    private String name;
    private String owner;
    private ArrayList<String> members;

    public void set_id(String _id) {
        this._id = _id;
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

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void setMembers(ArrayList<String> members) {
        this.members = members;
    }

    public ArrayList<String> getMembers() {
        return this.members;
    }

    @Deprecated
    public ArrayList<User> getMembers2() {
        return MainController.getUsers(members);
    }


    public void addMember(User user) {
        if (!members.contains(user.getName())) {
            members.add(user.getName());
            NetworkController.addCrowdMember(this._id, user.getName());
        }
    }

    public void removeMember(User user) {
        members.remove(user.getName());
        NetworkController.removeCrowdMember(this._id, user.getName());
    }

    // todo rework
    public ArrayList<Shelf> getShelves() {
        ArrayList<Shelf> shelves = new ArrayList<Shelf>();
        //for (User u: members) {
            //shelves.add(u.getShelf());
        //}
        return shelves;
    }

    public String get_id() {
        return _id;
    }

    public String toString() {
        return "_id: " + _id + "\nname: " + name + "\nowner: " + owner;
    }
}