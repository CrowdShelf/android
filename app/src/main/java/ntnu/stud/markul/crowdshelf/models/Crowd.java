package ntnu.stud.markul.crowdshelf.models;

import java.util.ArrayList;

import ntnu.stud.markul.crowdshelf.MainController;
import ntnu.stud.markul.crowdshelf.NetworkController;

/**
 * Created by Torstein on 01.09.2015.
 */
public class Crowd {
    private String _id; // Unique identified
    private String name;
    private User owner;
    private ArrayList<User> members;


    public Crowd(String _id, String name, User owner, ArrayList<User> members) {
        this._id = _id;
        this.name = name;
        this.owner = owner;
        this.members = members;
    }

    public ArrayList<User> getMembers() {
        return members;
    }

    public void addMember(User user) {
        if (!members.contains(user)) {
            members.add(user);
            NetworkController.addCrowdMember(this, user);
        }
    }

    public void removeMember(User user) {
        members.remove(user);
        NetworkController.removeCrowdMember(this, user);
    }

    // todo verify
    public ArrayList<Shelf> getShelves() {
        ArrayList<Shelf> shelves = new ArrayList<Shelf>();
        for (User u: members) {
            shelves.add(u.getShelf());
        }
        return shelves;
    }

    public String get_id() {
        return _id;
    }
}
