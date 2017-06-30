package com.codepath.apps.restclienttemplate.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by rafasj6 on 6/26/17.
 */

public class Tweet implements Parcelable {
    private static JSONArray media_temp;
    public String body;
    public long uid; //database id for tweet
    public String createdAt;
    public User user;
    public boolean favorited;
    public boolean retweeted;
    public String media;

    //deserialize

    public static Tweet fromJSON(JSONObject jsonObject) throws JSONException{

        Tweet tweet = new Tweet();

        tweet.body = jsonObject.getString("text");
        tweet.uid = jsonObject.getLong("id");
        tweet.createdAt = jsonObject.getString("created_at");
        tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));
        tweet.favorited = jsonObject.getBoolean("favorited");
        tweet.retweeted = jsonObject.getBoolean("retweeted");
        media_temp = jsonObject.getJSONObject("entities").optJSONArray("media");
        tweet.media = (media_temp!= null) ? media_temp.getJSONObject(0).getString("media_url"): "";

        return tweet;

    }


    public Tweet() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.body);
        dest.writeLong(this.uid);
        dest.writeString(this.createdAt);
        dest.writeParcelable(this.user, flags);
        dest.writeByte(this.favorited ? (byte) 1 : (byte) 0);
        dest.writeByte(this.retweeted ? (byte) 1 : (byte) 0);
    }

    protected Tweet(Parcel in) {
        this.body = in.readString();
        this.uid = in.readLong();
        this.createdAt = in.readString();
        this.user = in.readParcelable(User.class.getClassLoader());
        this.favorited = in.readByte() != 0;
        this.retweeted = in.readByte() != 0;
    }

    public static final Creator<Tweet> CREATOR = new Creator<Tweet>() {
        @Override
        public Tweet createFromParcel(Parcel source) {
            return new Tweet(source);
        }

        @Override
        public Tweet[] newArray(int size) {
            return new Tweet[size];
        }
    };
}
