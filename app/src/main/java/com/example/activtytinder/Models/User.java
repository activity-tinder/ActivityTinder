package com.example.activtytinder.Models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("User")
public class User extends ParseObject{

    // TODO -- Brad's doing this
    //set variables to bring "post" information from Parse database
    public static final String KEY_NAME = "name";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";

    //get and set all variables
    public ParseUser getUser (){
        return getParseUser(KEY_USERNAME);
    }
    public void setUser(ParseUser user){
        put(KEY_USERNAME, user);
    }





}
