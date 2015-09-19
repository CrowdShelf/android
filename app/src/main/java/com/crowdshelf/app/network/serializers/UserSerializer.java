package com.crowdshelf.app.network.serializers;

import com.crowdshelf.app.models.User;
import com.google.gson.Gson;
import com.google.gson.JsonObject;


/**
 * Created by Torstein on 19.09.2015.
 */
@Deprecated
public class UserSerializer {
        public JsonObject toJson(User user){
            Gson gson = new Gson();
            String json = gson.toJson(user, User.class);
            return null;
        }
}
