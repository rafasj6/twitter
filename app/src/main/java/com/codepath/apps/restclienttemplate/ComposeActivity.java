package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by rafasj6 on 6/26/17.
 */

public class ComposeActivity extends AppCompatActivity {
    TwitterClient client;
    String tweetText;
    public static String TWEET_KEY = "tweet";
    private TextView sms_count;
    EditText text;
    TextView tvCharactersLeft;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        text = (EditText) findViewById(R.id.etTweetText);
        tvCharactersLeft =  (TextView) findViewById(R.id.tvCharactersLeft);

        client = TwitterApp.getRestClient();
        final TextWatcher mTextEditorWatcher = new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //This sets a textview to the current length
                tvCharactersLeft.setText("Characters Left:" + String.valueOf(140-s.length()));
            }
            public void afterTextChanged(Editable s) {
            }


        };
        text.addTextChangedListener(mTextEditorWatcher);
    }

    public void post(View v){

        tweetText = text.getText().toString();
            client.sendTweet(tweetText, new JsonHttpResponseHandler(){

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Log.d("TwitterClient", response.toString());
                    Tweet tweet;
                    tweet = null;
                    try {
                         tweet = Tweet.fromJSON(response);
                        Log.d("COMPOSE.activity", tweet.body);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Intent data = new Intent(ComposeActivity.this, TimelineActivity.class);
                    // Pass relevant data back as a result
                    data.putExtra(TWEET_KEY, tweet);
                    // Activity finished ok, return the data
                    setResult(RESULT_OK, data); // set result code and bundle data for response
                    finish(); // closes the activity, pass data to parent
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


