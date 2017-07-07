package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.models.Tweet;

import fragments.HomeTimelineFragment;
import fragments.TweetsPagerAdapter;

public class TimelineActivity extends AppCompatActivity {



    public SwipeRefreshLayout swipeContainer;
    MenuItem miActionProgressItem;
    MenuItem searchItem;
    SearchView searchView;
    Context context;
    ViewPager viewPager;
    TweetsPagerAdapter pagerAdapter;


    private final int REQUEST_CODE = 20;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        context = this;

        viewPager= (ViewPager) findViewById(R.id.viewpager);
        pagerAdapter = new TweetsPagerAdapter(getSupportFragmentManager(), this);
        viewPager.setAdapter(pagerAdapter);

        new HomeTimelineFragment();

        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);




        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                fetchTimelineAsync(0);

            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

    }

    public void fetchTimelineAsync(int page) {
        // Send the network request to fetch the updated data
        // `client` here is an instance of Android Async HTTP
        // getHomeTimeline is an example endpoint.
        showProgressBar();

        if (viewPager.getCurrentItem() == 0) {

            pagerAdapter.homeTimelineFragment.tweetAdapter.mTweets.clear();
            pagerAdapter.homeTimelineFragment.tweetAdapter.notifyDataSetChanged();
            pagerAdapter.homeTimelineFragment.populateTimeline();

            //following wont let the code continue until the update is over
            while(pagerAdapter.homeTimelineFragment.updated = false);
        }
        else{
            pagerAdapter.mentionsTimelineFragment.tweetAdapter.mTweets.clear();
            pagerAdapter.mentionsTimelineFragment.tweetAdapter.notifyDataSetChanged();
            pagerAdapter.mentionsTimelineFragment.populateTimeline();

            //following wont let the code continue until the update is over

            while(pagerAdapter.mentionsTimelineFragment.updated = false);

        }
        //sets the refresh off
        swipeContainer.setRefreshing(false);
        hideProgressBar();

    }




    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // Store instance of the menu item containing progress
        miActionProgressItem = menu.findItem(R.id.miActionProgress);
        // Extract the action-view from the menu item
        ProgressBar v =  (ProgressBar) MenuItemCompat.getActionView(miActionProgressItem);

        searchItem = menu.findItem(R.id.app_bar_search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        // sets the query listener
        searchView.setOnQueryTextListener( new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent i = new Intent(context, SearchActivity.class);

                i.putExtra("q", query);

                startActivityForResult(i, REQUEST_CODE);
                return true;
            }

            //just because you must:
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });



        // Return to finish
        return super.onPrepareOptionsMenu(menu);

    }

    public void query(MenuItem view){
        //makes the search bar big
        searchItem.expandActionView();
        searchView.requestFocus();

    }

    public void showProgressBar() {
        // Show progress item

        miActionProgressItem.setVisible(true);
    }

    public void hideProgressBar() {
        // Hide progress item
        miActionProgressItem.setVisible(false);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);


        return true;
    }



    public void onComposeAction(MenuItem mi) {
            // handle click here
        Intent i = new Intent(this, ComposeActivity.class);
        startActivityForResult(i, REQUEST_CODE);

        }



    public void onProfileView(MenuItem item) {
        Intent i = new Intent(this, ProfileActivity.class);
        startActivity(i);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // REQUEST_CODE is defined above
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            // Extract name value from result extras
            Tweet tweet = data.getParcelableExtra(ComposeActivity.TWEET_KEY);
            Log.d("onActivityResult, ", "tweet is null?" + String.valueOf( tweet== null));


            ((HomeTimelineFragment) pagerAdapter.getItem(viewPager.getCurrentItem())).addTweet(tweet);
            // Toast the name to display temporarily on screen
            Toast.makeText(this, "Tweet Posted", Toast.LENGTH_SHORT).show();
        }
    }

}
