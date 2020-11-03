package com.codepath.apps.restclienttemplate;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

@Parcel
public class User {

    String name;
    String profile_url;
    String screen_name;

    public User(){}

    public static User fromJOBJtoUser(JSONObject jsonObject) throws JSONException {
        User individual = new User();
        individual.name= jsonObject.getString("name");
        individual.screen_name=jsonObject.getString("screen_name");
        individual.profile_url=jsonObject.getString("profile_image_url_https");

        return individual;
    }
}
