package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

import static com.codepath.apps.restclienttemplate.ComposeActivity.TWEET_KEY;

public class TweetDetailActivity extends AppCompatActivity {

    public Tweet tweet;
    public ImageView ivProfileImage;
    public TextView tvUsername;
    public TextView tvBody;
    public TextView tvTimeStamp;
    public ImageView ibReply;
    public ImageView ibFavorite;
    public TwitterClient client;
    public TextView tvHandle;
    public ImageView ivRetweet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_detail);
        tweet = getIntent().getParcelableExtra(TWEET_KEY);
        tvHandle = (TextView) findViewById(R.id.tvHandle);
        tvTimeStamp = (TextView) findViewById(R.id.tvTimeStamp);
        ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
        ivRetweet = (ImageView) findViewById(R.id.ivRetweet);
        tvUsername = (TextView) findViewById(R.id.tvUserName);
        tvBody = (TextView) findViewById(R.id.tvBody);
        ibReply = (ImageView) findViewById(R.id.ibReply);
        ibFavorite = (ImageView) findViewById(R.id.ibFavorite);
        ibFavorite.setSelected(tweet.favorited);
        ivRetweet.setSelected(tweet.retweeted);

        Log.d("Tweet detail", String.valueOf(tweet.favorited));
        client = TwitterApp.getRestClient();

        //ibReply.setOnClickListener((View.OnClickListener) this);


        tvUsername.setText(tweet.user.name);
        tvHandle.setText("@"+tweet.user.sreenName);
        tvBody.setText(tweet.body);
        tvTimeStamp.setText( TweetAdapter.getRelativeTimeAgo(tweet.createdAt));

        Glide.with(this)
                .load(tweet.user.profileImageUrl)
                .into(this.ivProfileImage);

    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, TimelineActivity.class);
        intent.putExtra("tweet", tweet);
        startActivity(intent);
    }

    public void reply(View v){

            Intent intent = new Intent(this, ComposeActivity.class);
            // serialize the movie using parceler, use its short name as a key
            intent.putExtra("user", tweet.user.sreenName);
            intent.putExtra("uid", String.valueOf(tweet.uid));
            Toast.makeText(this, "replied!", Toast.LENGTH_LONG);
            startActivity(intent);




    }


    public void retweet(View v){
            if (tweet.retweeted == false) {
                client.sendRetweet(tweet.uid, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Log.d("retweeted", response.toString());

                        ivRetweet.setSelected(true);
                        tweet.retweeted = true;

                    }


                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        Log.d("TwitterClient", errorResponse.toString());
                        throwable.printStackTrace();
                    }
                });//}
            }
            else {
                client.sendUnRetweet(tweet.uid, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Log.d("Untweeted", response.toString());

                        ivRetweet.setSelected(false);
                        tweet.retweeted = false;


                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        try {
                            Log.d("TwitterClient", errorResponse.toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        throwable.printStackTrace();
                    }

                });
            }
        }




    public void favorite(View v) {
        Log.d("TweetDetailActivity", String.valueOf(tweet.favorited));
        if (tweet.favorited == false) {
            client.sendFavorite(tweet.uid, new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Log.d("TwitterClient", response.toString());

                    //tweet = Tweet.fromJSON(response);
                    //Log.d("COMPOSE.activity", tweet.body);
                    ibFavorite.setSelected(!tweet.favorited);
                    Log.d("TweetDetailActivity", String.valueOf(tweet.favorited));
                    tweet.favorited = true;
                }


                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Log.d("TwitterClient obj", errorResponse.toString());
                    throwable.printStackTrace();
                }
            });
        }
        else{
            client.sendUnfavorite(tweet.uid, new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Log.d("TwitterClient", response.toString());

                    //tweet = Tweet.fromJSON(response);
                    //Log.d("COMPOSE.activity", tweet.body);
                    ibFavorite.setSelected(!tweet.favorited);
                    Log.d("TweetDetailActivity", String.valueOf(tweet.favorited));
                    tweet.favorited = false;


                }


                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Log.d("TwitterClient obj", errorResponse.toString());
                    throwable.printStackTrace();
                }
            });



        }


    }
}