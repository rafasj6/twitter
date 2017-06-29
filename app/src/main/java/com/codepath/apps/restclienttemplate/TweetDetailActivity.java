package com.codepath.apps.restclienttemplate;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

import static com.codepath.apps.restclienttemplate.ComposeActivity.TWEET_KEY;

public class TweetDetailActivity extends AppCompatActivity {

    public Tweet tweet;
    public ImageView ivProfileImage;
    public TextView tvUsername;
    public TextView tvBody;
    public TextView tvTimeStamp;
    public ImageButton ibReply;
    public TwitterClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_detail);
        tweet = getIntent().getParcelableExtra(TWEET_KEY);

        tvTimeStamp = (TextView) findViewById(R.id.tvTimeStamp);
        ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
        tvUsername = (TextView) findViewById(R.id.tvUserName);
        tvBody = (TextView) findViewById(R.id.tvBody);
        ibReply = (ImageButton) findViewById(R.id.ibReply);
        client = TwitterApp.getRestClient();

        //ibReply.setOnClickListener((View.OnClickListener) this);


        tvUsername.setText(tweet.user.name);
        tvBody.setText(tweet.body);
        tvTimeStamp.setText( TweetAdapter.getRelativeTimeAgo(tweet.createdAt));

        Glide.with(this)
                .load(tweet.user.profileImageUrl)
                .into(this.ivProfileImage);

    }

    public void favorite(View v){
        client.sendFavorite(tweet.uid, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("TwitterClient", response.toString());
                Tweet tweet;

                    //tweet = Tweet.fromJSON(response);
                    //Log.d("COMPOSE.activity", tweet.body);
                    Toast.makeText(TweetDetailActivity.this, "favorited!", Toast.LENGTH_LONG);




            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.d("TwitterClient", response.toString());


            }


            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("TwitterClient", errorResponse.toString());
                throwable.printStackTrace();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.d("TwitterClient", errorResponse.toString());
                throwable.printStackTrace();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("TwitterClient", responseString);
                throwable.printStackTrace();            }
        });

    }
}