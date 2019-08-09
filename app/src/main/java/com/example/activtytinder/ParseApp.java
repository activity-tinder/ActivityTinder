package com.example.activtytinder;

import android.app.Application;

import com.example.activtytinder.Models.Event;

import com.example.activtytinder.Models.Feedback;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseUser;


/**
 * This class connects the Parse database online to our java code. By registering the subclasses,
 * Parse is now fully configured and we can use Parse helper functions set up in the Event model.
 */
public class ParseApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        /**
         * Registering each subclass takes the classes in our Parse online database and configures
         * them to be used and matched with the appropriate class in our Java code.
         */
        ParseObject.registerSubclass(ParseUser.class);//must have this for Parse to be configured fully
        ParseObject.registerSubclass(Event.class);//must have this for Parse to be configured fully
        ParseObject.registerSubclass(Feedback.class);//must have this for Parse to be configured fully

        //set up Parse stuff, values straight from setup for the project on heroku website
        final Parse.Configuration configuration = new Parse.Configuration.Builder(this)
                .applicationId("not-tinder")
                .clientKey("platonic-only")
                .server("https://activity-tinder.herokuapp.com/parse")
                .build();
        Parse.initialize(configuration);
    }
}
