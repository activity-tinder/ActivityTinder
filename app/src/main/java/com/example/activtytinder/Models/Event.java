package com.example.activtytinder.Models;

//import com.parse.ParseClassName;

import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;

import java.util.Date;
//import com.parse.ParseQuery;

@ParseClassName("Event")
public class Event extends ParseObject{

    public static final String KEY_EVENT_DATE = "eventDate";
    public static final String KEY_NAME= "name";
    public static final String KEY_CREATOR = "creator";
    public static final String KEY_ATTENDEES =  "usersAttending";
    public static final String KEY_LIMIT = "peopleLimit";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_LOCATION  = "location";
    public static final String KEY_CATERGORY = "category";


    public Date getKeyDate(){
        return getDate(KEY_EVENT_DATE);
    }
    public void setKeyDate(String eventDate){
        put(KEY_EVENT_DATE, eventDate);
    }

    public String getKeyName(){
        return getString(KEY_NAME);
    }
    public void setKeyName(String name){
        put(KEY_NAME, name);
    }

    public String getKeyDescription(){
        return getString(KEY_DESCRIPTION);
    }
    public void setKeyDescription(String description){
        put(KEY_DESCRIPTION, description);
    }

    public ParseUser getCreator(){
        return getParseUser(KEY_CREATOR);
    }
    public void setKeyCreator(ParseUser creator){
        put(KEY_CREATOR, creator);
    }

    public JSONArray getKeyAttendees(){
        return getJSONArray(KEY_ATTENDEES);
    }

    public void setKeyAttendees(JSONArray attendees){
        put(KEY_ATTENDEES, attendees);
    }

    public Integer getKeyLimit(){
        return getInt(KEY_LIMIT);
    }

    public void setKeyLimit(Integer limit){
        put(KEY_LIMIT, limit);
    }

    public ParseGeoPoint getLocation(){
        return getParseGeoPoint(KEY_LOCATION);
    }

    public void setKeyLocation(ParseGeoPoint location){
        put(KEY_LOCATION, location);
    }

    public static class Query extends ParseQuery<User>{
        public Query(Class<User> subclass) {
            super(subclass);
        }

        public Query withCreator(){
            include("creator");
            return this;
        }
    }
}

