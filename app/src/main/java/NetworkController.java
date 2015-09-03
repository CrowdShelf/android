import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import models.Book;
import models.Crowd;
import models.User;

/**
 * Created by Torstein on 01.09.2015.
 */
public class NetworkController {
    //todo find out if serializers are necessary
    private static Gson gson = new GsonBuilder()
            //.registerTypeAdapter(Book.class, new BookDeserializer())
            //.registerTypeAdapter(User.class, new UserDeserializer())
            //.registerTypeAdapter(Crowd.class, new CrowdDeserializer())
            .create();

    // add book to db or update existing one
    public static void addBook(Book book) {
        /* PUT /book
        data: book object
         */
        NetworkHelper.sendPutRequest("/book", new Gson().toJson(book, Book.class));
    }

    // Obsolete? when is this needed??
    @Deprecated
    public static void getBook(User owner, String isbn) {
        // GET /book/:isbn/:owner
        NetworkHelper.sendGetRequest("/book/" + isbn + "/" + owner.toString());
    }

    public static void addRenter(String isbn, String owner, String renter) {
        /* PUT /book/:isbn/:owner/addrenter
        data: username : String # username of renter
         */
        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty("username", renter);
        NetworkHelper.sendPutRequest("/book/" + isbn + "/" + owner + "/addrenter", new Gson().toJson(renter));
    }

    public static void removeRenter(String isbn, String owner, String renter) {
        /* PUT /book/:isbn/:owner/removerenter
        data: username : String # username of renter
         */
        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty("username", renter);
        NetworkHelper.sendPutRequest("/book/"+isbn+"/"+owner+"/removerenter", jsonObj.getAsString());
    }

    public static void createCrowd(Crowd crowd) {
        /* POST /crowd
        data: crowd object
        response: crowd object (for correct _id)
         */
        NetworkHelper.sendPostRequest("/crowd", gson.toJson(crowd, Crowd.class));
    }

    public static void getCrowd(String crowdID) {
        // GET /crowd/:crowdId
        NetworkHelper.sendGetRequest("/crowd/"+crowdID);
    }

    public static void addCrowdMember(String crowdId, String username) {
        /* PUT /crowd/:crowdId/addmember
        data: username : String
         */
        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty("username", username);
        NetworkHelper.sendPutRequest("/crowd/"+crowdId+"/addememeber", jsonObj.getAsString());
    }

    public static void removeCrowdMember(String crowdId, String username) {
        /* PUT /crowd/:crowdId/removemember
        data: String : username
         */
        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty("username", username);
        NetworkHelper.sendPutRequest("/crowd/"+crowdId+"/removemember", jsonObj.getAsString());
    }

    public static void getUser(String username){
        // GET /api/user/:username
        NetworkHelper.sendGetRequest("/user/"+username);
    }
}
