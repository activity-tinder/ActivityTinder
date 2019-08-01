package com.example.activtytinder.Models;


import com.parse.ParseClassName;
import com.parse.ParseObject;

//TODO -- explain model
@ParseClassName("Feedback")
public class Feedback extends ParseObject {

    public static final String KEY_SENDER = "feedbackSender";
    public static final String KEY_DESCRIPTION = "description";



    public Feedback(){}

    public String getKEY_SENDER() { return getString(KEY_SENDER);}

    public void setKeySender(String sender) {put(KEY_SENDER, sender);}

    public String getKEY_DESCRIPTION() { return getString(KEY_DESCRIPTION);}


    public void setKeyDescription(String description) { put(KEY_DESCRIPTION, description); }
}


