package ntnu.stud.markul.crowdshelf.gsonHelpers;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import ntnu.stud.markul.crowdshelf.MainController;
import ntnu.stud.markul.crowdshelf.models.Book;
import ntnu.stud.markul.crowdshelf.models.Crowd;
import ntnu.stud.markul.crowdshelf.models.User;

/**
 * Created by Torstein on 02.09.2015.
 */
public class jsonHelper {
    public static ArrayList<User> usernamesToUsers(JsonArray usernames) {
        ArrayList<User> users = new ArrayList<User>();
        for (int i = 0, size = usernames.size(); i < size; i++)
        {
            String username = usernames.get(i).getAsString();
            users.add(MainController.getUser(username));
        }
        return users;
    }

    public static Crowd jsonEToCrowd(JsonElement json) {
        JsonObject jsonObject = json.getAsJsonObject();
        String _id = jsonObject.get("_id").getAsString();
        String name = jsonObject.get("name").getAsString();
        String username = jsonObject.get("creator").getAsString();
        User owner = MainController.getUser(username);
        ArrayList<User> members = jsonHelper.usernamesToUsers(jsonObject.get("members").getAsJsonArray());

        return new Crowd(_id, name, owner, members);
    }

    public static Book jsonEToBook(JsonElement json) {
        JsonObject jsonObject = json.getAsJsonObject();
        String _id = jsonObject.get("_id").getAsString();
        String isbn = jsonObject.get("isbn").getAsString();
        boolean availableForRent = jsonObject.get("availableForRent").getAsBoolean();
        String username = jsonObject.get("owner").getAsString();
        User owner = MainController.getUser(username);
        ArrayList<User> rentedTo = jsonHelper.usernamesToUsers(jsonObject.get("rentedTo").getAsJsonArray());
        int numberOfCopies = jsonObject.get("numberOfCopies").getAsInt();

        return new Book(_id, isbn, owner, rentedTo, numberOfCopies);
    }
}
