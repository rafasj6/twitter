package com.codepath.apps.restclienttemplate.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by rafasj6 on 6/26/17.
 */

public class User {
    //sttributes
    public String name;
    public long uid;
    public String sreenName;
    public String profileImageUrl;

    //deserialize Json

    public static User fromJSON(JSONObject json) throws JSONException{
        User user = new User();

        user.name = json.getString("name");
        user.uid = json.getLong("id");
        user.sreenName = json.getString("screen_name");
        user.profileImageUrl = json.getString("profile_image_url");

        return user;


    }

}
