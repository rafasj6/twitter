package com.codepath.apps.restclienttemplate;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;

import static com.codepath.apps.restclienttemplate.ComposeActivity.TWEET_KEY;

public class TweetDetailActivity extends AppCompatActivity {
    public Tweet tweet;
    public ImageView ivProfileImage;
    public TextView tvUsername;
    public TextView tvBody;
    public TextView tvTimeStamp;
    public ImageButton ibReply;

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

        //ibReply.setOnClickListener((View.OnClickListener) this);


        tvUsername.setText(tweet.user.name);
        tvBody.setText(tweet.body);
        tvTimeStamp.setText( TweetAdapter.getRelativeTimeAgo(tweet.createdAt));

        Glide.with(this)
                .load(tweet.user.profileImageUrl)
                .into(this.ivProfileImage);

    }
}