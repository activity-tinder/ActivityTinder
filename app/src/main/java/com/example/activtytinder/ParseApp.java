package com.example.activtytinder;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //ParseObject.registerSubclass(Post.class);//must have this for Parse to be configured fully
        //set up Parse stuff, values straight from setup for the project on heroku website
        final Parse.Configuration configuration = new Parse.Configuration.Builder(this)
                .applicationId("not-tinder")
                .clientKey("platonic-only")
                .server("http://activity-tinder.herokuapp.com/parse")
                .build();
        Parse.initialize(configuration);
    }


}
