package com.codepath.apps.restclienttemplate.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by rafasj6 on 6/26/17.
 */

public class Tweet {
    public String body;
    public long uid; //database id for tweet
    public String createdAt;
    public User user;

    //deserialize

    public static Tweet fromJSON(JSONObject jsonObject) throws JSONException{

        Tweet tweet = new Tweet();

        tweet.body = jsonObject.getString("text");
        tweet.uid = jsonObject.getLong("id");
        tweet.createdAt = jsonObject.getString("created_at");
        tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));
        return tweet;

    }


}
