package com.codepath.apps.restclienttemplate;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import fragments.SearchTweetsFragment;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        final String q = getIntent().getStringExtra("q");

        SearchTweetsFragment fragment =  SearchTweetsFragment.newInstance(q);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        ft.replace(R.id.flContainer,fragment);

        ft.commit();


    }
}
