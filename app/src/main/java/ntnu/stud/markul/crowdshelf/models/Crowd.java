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
    private String owner;
    private ArrayList<String> members;

    public ArrayList<User> getMembers() {
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
}
