package com.crowdshelf.app.models;

import java.util.ArrayList;
import java.util.List;

import com.crowdshelf.app.MainController;
import com.crowdshelf.app.network.NetworkController;

/**
 * Created by Torstein on 01.09.2015.
 */
public class Crowd implements BookOwner{
    private String _id; // Unique identified
    private String name;
    private String owner;
    private List<User> members; // = new ArrayList<User>();

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

    public List<User> getMembers() {
        return this.members;
    }

    public void setMembers(List<User> members) {
        this.members = members;
    }

    public void addMember(User member) {
        String username = member.getUsername();
        for (User u : members) {
            if (u.getUsername().equals(username)) {
                members.remove(u);
            }
        }
        members.add(member);
        NetworkController.addCrowdMember(this._id, username);
    }

    public void removeMember(String username) {
        members.remove(username);
        NetworkController.removeCrowdMember(this._id, username);
    }

    public List<Book> getBooks() {
        List<Book> books = new ArrayList<Book>();
        for (User u : members) {
            books.addAll(u.getBooksOwned());
            books.addAll(u.getBooksRented());
        }
        return books;
    }

    public List<Book> getBooksOwned() {
        List<Book> books = new ArrayList<Book>();
        for (User u : members) {
            books.addAll(u.getBooksOwned());
        }
        return books;
    }

    public List<Book> getBooksRented() {
        List<Book> books = new ArrayList<Book>();
        for (User u : members) {
            books.addAll(u.getBooksRented());
        }
        return books;
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
