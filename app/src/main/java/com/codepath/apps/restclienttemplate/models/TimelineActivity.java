package com.codepath.apps.restclienttemplate.models;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.EndlessRecyclerViewScrollListener;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.Tweet;
import com.codepath.apps.restclienttemplate.TweetAdapter;
import com.codepath.apps.restclienttemplate.TwitterApplication;
import com.codepath.apps.restclienttemplate.TwitterClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;

import okhttp3.Headers;

public class TimelineActivity extends AppCompatActivity {

    public static final String TAG= "MainActivity";
    public static final int COMPOSE_REQUEST_CODE= 13;

    TwitterClient tcInstance;
    ArrayList<Tweet> tweets;
    RecyclerView rvTimeline;
    TweetAdapter adapterInstance;
    SwipeRefreshLayout srlContainer;
    EndlessRecyclerViewScrollListener ervsl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);


        tcInstance = TwitterApplication.getRestClient(this);
        tweets = new ArrayList<>();
        populateHomeTimeline();

        rvTimeline= findViewById(R.id.rvTimeline);
        adapterInstance= new TweetAdapter(tweets,this);
        LinearLayoutManager llm= new LinearLayoutManager(this);
        rvTimeline.setLayoutManager(llm);
        rvTimeline.setAdapter(adapterInstance);

        srlContainer = findViewById(R.id.srlContainer);
        srlContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                populateHomeTimeline();
            }
        });

        ervsl = new EndlessRecyclerViewScrollListener(llm) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadNextDataFromApi();
                Log.i(TAG, "onLoadMore: page"+ page);
            }
        };
        rvTimeline.addOnScrollListener(ervsl);
    }

    private void loadNextDataFromApi() {
        tcInstance.getNextPage(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.i(TAG, "onSuccess: loading more data page");
                JSONArray jarray = json.jsonArray;
                try {
                    ArrayList<Tweet> tweets =Tweet.generateTweetList(jarray);
                    adapterInstance.addAll(tweets);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {

            }
        }, tweets.get(tweets.size()-1).id);
        // Send an API request to retrieve appropriate paginated data

        //  --> Send the request including an offset value (i.e `page`) as a query parameter.
        //  --> Deserialize and construct new model objects from the API response
        //  --> Append the new data objects to the existing set of items inside the array of items
        //  --> Notify the adapter of the new items made with `notifyItemRangeInserted()`
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.compose){
            Toast.makeText(this, "compose",Toast.LENGTH_SHORT).show();
            Intent i = new Intent(this,ComposeActivity.class);
            startActivityForResult(i,COMPOSE_REQUEST_CODE);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==COMPOSE_REQUEST_CODE && resultCode==RESULT_OK){
            Tweet returnedTweet= Parcels.unwrap(data.getParcelableExtra("COMPOSEDTWEET"));
            tweets.add(0,returnedTweet);
            adapterInstance.notifyItemInserted(0);
            rvTimeline.smoothScrollToPosition(0);
        }
    }

    private void populateHomeTimeline() {
        JsonHttpResponseHandler jhrh = new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.i(TAG, "onSuccess fetching json respond!!");
                JSONArray localarr= json.jsonArray;
                try {
                    adapterInstance.clear();
                    adapterInstance.addAll(Tweet.generateTweetList(localarr));
                    adapterInstance.notifyDataSetChanged();
                    srlContainer.setRefreshing(false);

                } catch (JSONException e) {
                    Log.w(TAG, "Fail to generate TweetList",e );
                }

            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {

            }
        };

        tcInstance.getInterestingnessList(jhrh);
    }

}