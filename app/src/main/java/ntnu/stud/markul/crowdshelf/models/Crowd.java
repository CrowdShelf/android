package ntnu.stud.markul.crowdshelf.models;

import java.util.ArrayList;

/**
 * Created by Torstein on 01.09.2015.
 */
public class Crowd {
    private final String ID; // Unique, retrieved from server
    private String name;
    private ArrayList<User> members;
    private User owner;

    public Crowd(String ID, String name, ArrayList<User> members, User owner) {
        this.ID = ID;
        this.name = name;
        this.members = members;
        this.owner = owner;
    }

    public ArrayList<User> getMembers() {
        return members;
    }

    public void addMember(User user) {
        if (!members.contains(user)) {
            members.add(user);
        }
    }

    public void removeMember(User user) {
        members.remove(user);
    }

    public ArrayList<Shelf> getShelves() {
        ArrayList<Shelf> shelves = new ArrayList<Shelf>();
        for (User u: members) {
            shelves.add(u.getShelf());
        }
        return shelves;
    }
}
