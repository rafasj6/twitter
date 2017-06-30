package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

import static com.codepath.apps.restclienttemplate.ComposeActivity.TWEET_KEY;

/**
 * Created by rafasj6 on 6/26/17.
 */

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder>{

    //pass tweet array to constructor
    Context context;
    private List<Tweet> mTweets;
    public TweetAdapter(List<Tweet> tweets){
    mTweets = tweets;
    }
    TwitterClient client = TwitterApp.getRestClient();




    //infalte each row into viewholder

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View tweetView = inflater.inflate(R.layout.item_tweet,parent, false);

        ViewHolder viewHolder = new ViewHolder(tweetView);
        return viewHolder;
    }


    //bind the valies based on pos of elemt in list

    public static String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS, DateUtils.FORMAT_ABBREV_ALL).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }
    public void clear() {
        mTweets.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Tweet> list) {
        mTweets.addAll(list);
        notifyDataSetChanged();
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Tweet tweet = mTweets.get(position);


        holder.tvUsername.setText(tweet.user.name);
        holder.tvBody.setText(tweet.body);
        holder.tvHandle.setText("@" + tweet.user.sreenName);
        holder.tvTimeStamp.setText( getRelativeTimeAgo(tweet.createdAt));
        holder.ivFavorite.setSelected(tweet.favorited);

        Glide.with(context)
                .load(tweet.user.profileImageUrl)
                .into(holder.ivProfileImage);
    }

    @Override
    public int getItemCount() {
        return mTweets.size();
    }

//create viewholder class

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView ivProfileImage;
        public TextView tvUsername;
        public TextView tvBody;
        public TextView tvTimeStamp;
        public ImageView ibReply;
        public TextView tvHandle;
        public ImageView ivFavorite;
        public ImageView ivRetweet;

        public  ViewHolder (View itemView) {
            super(itemView);
            tvTimeStamp = (TextView) itemView.findViewById(R.id.tvTimeStamp);
            tvHandle = (TextView) itemView.findViewById(R.id.tvHandle);

            ivProfileImage = (ImageView) itemView.findViewById(R.id.ivProfileImage);
            tvUsername =  (TextView) itemView.findViewById(R.id.tvUserName);
            tvBody =  (TextView) itemView.findViewById(R.id.tvBody);
            ibReply = (ImageView) itemView.findViewById(R.id.ibReply);
            ivFavorite = (ImageView) itemView.findViewById(R.id.ivFavorite);
            ivRetweet = (ImageView) itemView.findViewById(R.id.ivRetweet);


            ibReply.setOnClickListener(this);
            itemView.setOnClickListener(this);
            ivFavorite.setOnClickListener(this);
            ivRetweet.setOnClickListener(this);

        }
        public void favorite(final Tweet tweet){
            if (tweet.favorited == false){
            client.sendFavorite(tweet.uid, new JsonHttpResponseHandler(){

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Log.d("TwitterClient", response.toString());

                    //tweet = Tweet.fromJSON(response);
                    //Log.d("COMPOSE.activity", tweet.body);
                    Toast.makeText(context, "favorited!", Toast.LENGTH_LONG);
                    ivFavorite.setSelected(true);
                    tweet.favorited = true;

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Log.d("TwitterClient", errorResponse.toString());
                    throwable.printStackTrace();
                }
            });}


            else{
                client.sendUnfavorite(tweet.uid, new JsonHttpResponseHandler(){

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Log.d("TwitterClient", response.toString());


                        //tweet = Tweet.fromJSON(response);
                        //Log.d("COMPOSE.activity", tweet.body);
                        Toast.makeText(context, "favorited!", Toast.LENGTH_LONG);
                        ivFavorite.setSelected(false);
                        tweet.favorited = false;


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

        public void retweet(final Tweet tweet) {
            // if (tweet.retweeted == false){
            client.sendRetweet(tweet.uid, new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Log.d("retweeted", response.toString());

                    ivRetweet.setSelected(true);
                    //tweet.retweeted = true;

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Log.d("TwitterClient", errorResponse.toString());
                    throwable.printStackTrace();
                }
            });//}
        }

          /*  else{
                client.sendUnfavorite(tweet.uid, new JsonHttpResponseHandler(){

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Log.d("TwitterClient", response.toString());


                        //tweet = Tweet.fromJSON(response);
                        //Log.d("COMPOSE.activity", tweet.body);
                        Toast.makeText(context, "favorited!", Toast.LENGTH_LONG);
                        ivFavorite.setSelected(false);
                        tweet.favorited = false;


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
            }*/





        @Override
        public void onClick(View view) {
            // gets item position
            Intent intent;
            int position = getAdapterPosition();
            // make sure the position is valid, i.e. actually exists in the view
            if (position != RecyclerView.NO_POSITION) {
                // get the movie at the position, this won't work if the class is static
                Tweet tweet = mTweets.get(position);

                // create intent for the new activity
                if (view == ibReply) {
                    intent = new Intent(context, ComposeActivity.class);
                    // serialize the movie using parceler, use its short name as a key
                    intent.putExtra("user", tweet.user.sreenName);
                    intent.putExtra("uid", String.valueOf(tweet.uid));

                }
                else if (view == itemView){
                    intent = new Intent(context, TweetDetailActivity.class);
                    intent.putExtra(TWEET_KEY, tweet);

                }

                else if (view == ivRetweet){
                    retweet(tweet);
                    return;
                }
                else{
                    favorite(tweet);
                    return;

                }

                // show the activity
                context.startActivity(intent);
            }
        }
    }
}
