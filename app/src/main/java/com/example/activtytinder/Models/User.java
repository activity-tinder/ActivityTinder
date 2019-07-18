package com.example.activtytinder.Models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.Date;

@ParseClassName("User")
public class User extends ParseObject{

    // TODO -- Brad's doing this
    //set variables to bring "post" information from Parse database
    public static final String KEY_NAME = "name";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_BIRTHDAY = "birthday";
    public static final String KEY_LOCATION = "location";

    //get and set all variables
    public ParseUser getUser (){
        return getParseUser(KEY_USERNAME);
    }
    public void setUser(ParseUser user){
        put(KEY_USERNAME, user);
    }
    public String getName(){
        return getString(KEY_NAME);
    }
    public void setName(String name){
        put(KEY_NAME, name);
    }
    public String getEmail(){
        return getString(KEY_EMAIL);
    }
    public void setEmail(String email){
        put(KEY_EMAIL, email);
    }
    public Date getBirthday(){
        return  getDate(KEY_BIRTHDAY);
    }
    public void setBirthday(String birthday){
        put(KEY_BIRTHDAY, birthday);
    }






}
