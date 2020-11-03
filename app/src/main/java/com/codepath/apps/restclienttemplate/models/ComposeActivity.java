package com.codepath.apps.restclienttemplate.models;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.Tweet;
import com.codepath.apps.restclienttemplate.TwitterApplication;
import com.codepath.apps.restclienttemplate.TwitterClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONException;
import org.parceler.Parcels;

import java.util.ArrayList;

import okhttp3.Headers;

public class ComposeActivity extends AppCompatActivity {

    EditText etContent;
    Button btnCompose;
    TwitterClient composeClient;

    public static final int MAX_CHAR = 140;
    public static final String TAG="ComposeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        etContent=findViewById(R.id.etContent);
        btnCompose=findViewById(R.id.btnCompose);

        composeClient= TwitterApplication.getRestClient(this);



        btnCompose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String tweetContent= etContent.getText().toString();

                if (tweetContent.length()==0){
                    Toast.makeText(ComposeActivity.this, "Tweet Can't be empty", Toast.LENGTH_SHORT).show();
                }else if(tweetContent.length()>MAX_CHAR){
                    Toast.makeText(ComposeActivity.this, "Tweet is too long", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(ComposeActivity.this, "about to create a post!", Toast.LENGTH_SHORT).show();
                    composeClient.postTweet(tweetContent, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Headers headers, JSON json) {
                            Log.i(TAG, "onSuccess: posting tweet!");
                            try {
                                Tweet compose_tweets= Tweet.fromJOBJtoTWEET(json.jsonObject);
                                Intent intent= new Intent();
                                intent.putExtra("COMPOSEDTWEET", Parcels.wrap(compose_tweets));
                                setResult(RESULT_OK,intent);
                                finish();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                            Log.e(TAG, "onFailure: fail to post tweet",throwable);
                        }
                    });

                }
            }
        });
    }
}