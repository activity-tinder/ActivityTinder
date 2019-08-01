package com.example.activtytinder.Models;

//import com.parse.ParseClassName;

import android.os.Parcelable;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.parceler.Parcel;
//import com.parse.ParseQuery;

@Parcel(analyze = Event.class)
@ParseClassName("Event")

//TODO-- explain model
public class Event extends ParseObject implements Parcelable {

    public static final String KEY_EVENT_DATE = "eventDate";
    public static final String KEY_NAME= "name";
    public static final String KEY_CREATOR = "creator";
    public static final String KEY_LIMIT = "peopleLimit";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_LOCATION  = "location";
    public static final String KEY_CATEGORY = "category";
    public static final String KEY_EVENT_IMAGE = "eventPhoto";
    public static final String KEY_START_TIME = "startTime";
    public static final String KEY_END_TIME = "endTime";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_IMAGE = "eventPhoto";

    public Event() {}


    public String getKeyDate(){
        return getString(KEY_EVENT_DATE);
    }
    public void setKeyDate(String eventDate){ put(KEY_EVENT_DATE, eventDate); }

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

    public Integer getKeyLimit(){
        return getInt(KEY_LIMIT);
    }

    public void setKeyLimit(Integer limit){
        put(KEY_LIMIT, limit);
    }

    public ParseGeoPoint getLocation(){
        return getParseGeoPoint(KEY_LOCATION);
    }

    public String getCategory() {
        return getString(KEY_CATEGORY);
    }

    public void setKeyLocation(ParseGeoPoint location){
        put(KEY_LOCATION, location);
    }

    public ParseFile getEventImage() {
        return getParseFile(KEY_EVENT_IMAGE);
    }
    public String getKeyStartTime(){return getString(KEY_START_TIME);}

    public void setKeyStartTime(String eventStartTime){put(KEY_START_TIME, eventStartTime);}

    public String getKeyEndTime(){return  getString(KEY_END_TIME);}

    public void setKeyEndTime(String eventEndTime){put(KEY_END_TIME, eventEndTime);}

    public String getKeyCategory(){return getString(KEY_CATEGORY);}

    public void setKeyCategory(String category){put(KEY_CATEGORY, category);}

    public String getKeyAddress(){return getString(KEY_ADDRESS);}

    public void setKeyAddress(String address){put(KEY_ADDRESS, address);}

    public ParseFile getKeyImage (){ //data types should match with parse file data types
        return getParseFile(KEY_IMAGE);
    }
    public void setKeyImage (ParseFile image){
        put(KEY_IMAGE, image);
    }

    public static class Query extends ParseQuery<ParseUser>{
        public Query(Class<ParseUser> subclass) {
            super(subclass);
        }

        public Query withCreator(){
            include("creator");
            return this;
        }
    }
}
