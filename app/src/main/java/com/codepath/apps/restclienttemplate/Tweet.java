package com.codepath.apps.restclienttemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;


@Parcel
public class Tweet {

    String body;
    User user;
    public long id;

    public Tweet(){}


    public static Tweet fromJOBJtoTWEET(JSONObject jobj) throws JSONException {
        Tweet tweetItem = new Tweet();
        tweetItem.body= jobj.getString("text");
        tweetItem.user= User.fromJOBJtoUser(jobj.getJSONObject("user"));
        tweetItem.id= jobj.getLong("id");
        return tweetItem;
    };


    public static ArrayList<Tweet> generateTweetList(JSONArray jarr) throws JSONException {
        ArrayList<Tweet> collection =  new ArrayList<>();
        for(int i=0; i<jarr.length();i++){
            Tweet single = fromJOBJtoTWEET(jarr.getJSONObject(i));
            collection.add(single);
        }
        return collection;
    };
}
