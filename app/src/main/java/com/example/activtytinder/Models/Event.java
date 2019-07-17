package com.example.activtytinder.Models;

//import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.json.JSONArray;
//import com.parse.ParseQuery;

public class Event extends ParseObject{

    private static final String KEY_DATE = "eventDate";
    private static final String KEY_NAME= "name";
//    //creator is an object ID
    private static final String KEY_CREATOR = "creator";
    //people attending is an arraylist
    private static final String KEY_ATTENDEES =  "peopleAttending";
    //Limit is an integer
    private static final String KEY_LIMIT = "limit";
    private static final String KEY_DESCRIPTION = "description";
    //Location is a geopoint
    private static final String KEY_LOCATION  = "location";



    public String getKeyDate(){
        return getString(KEY_DATE);
    }
    public void setKeyDate(String eventDate){
        put(KEY_DATE, eventDate);
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

    public String getCreator(){
        return getString(KEY_CREATOR);
    }
    public void setKeyCreator(Object creator){
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

