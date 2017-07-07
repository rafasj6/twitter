package com.codepath.apps.restclienttemplate;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import fragments.UserTimelineFragment;

public class ProfileActivity extends AppCompatActivity {

    TwitterClient client;
    String self_screen_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        final String screen_name = getIntent().getStringExtra("screen_name");

        UserTimelineFragment fragment =  UserTimelineFragment.newInstance(screen_name);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        ft.replace(R.id.flContainer,fragment);

        ft.commit();

        client = TwitterApp.getRestClient();
        client.getUserInfo(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    User user = User.fromJSON(response);
                    self_screen_name = user.sreenName.toString();
                    if (screen_name == null) {
                        getSupportActionBar().setTitle("@" + user.sreenName.toString());
                        populateUserHeadline(user);
                    }

                    else{
                        client.getUserProfile(self_screen_name, screen_name,new JsonHttpResponseHandler(){
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                try {
                                    User user = User.fromJSON(response);
                                    getSupportActionBar().setTitle("@" + user.sreenName.toString());
                                    populateUserHeadline(user);
                                }
                                catch(JSONException e){
                                    e.printStackTrace();
                                }
                            }


                        });


                    }

                }
                catch (JSONException e){
                    e.printStackTrace();
                }
            }

        });

    }

    public void populateUserHeadline(User user){
        TextView tvName = (TextView) findViewById(R.id.tvName);
        TextView tvTagline = (TextView) findViewById(R.id.tvTagline);
        TextView tvFollowers = (TextView) findViewById(R.id.tvFollowers);
        TextView tvFollowing = (TextView) findViewById(R.id.tvFollowing);
        ImageView ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);

        tvName.setText(user.name.toString());
        tvTagline.setText(user.tagline.toString());
        tvFollowers.setText(String.valueOf(user.followersCount)+ " followers");
        tvFollowing.setText(String.valueOf(user.followingCount) + " following");

        Glide.with(this)
                .load(user.profileImageUrl)
                .into(ivProfileImage);


    }


}
