package com.example.activtytinder;

import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import com.example.activtytinder.Models.Event;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;

public class CardUtils {

    public static Point getDisplaySize(WindowManager windowManager) {
        try {
            if (Build.VERSION.SDK_INT > 16) {
                Display display = windowManager.getDefaultDisplay();
                DisplayMetrics displayMetrics = new DisplayMetrics();
                display.getMetrics(displayMetrics);
                return new Point(displayMetrics.widthPixels, displayMetrics.heightPixels);
            } else {
                return new Point(0, 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Point(0, 0);
        }
    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static void addUserToEvent(ParseUser user, Event event) {
        // add the current user to the list of people attending the event in the event
        // class

        JSONArray currentAttendees = event.getKeyAttendees();
        Log.d("DEBUG", "Users before: " + currentAttendees.toString());
        if (user != null) {
            currentAttendees.put(user.getObjectId());
            currentAttendees.put(user);
        }
        try {
            currentAttendees.getString(0);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        event.setKeyAttendees(currentAttendees);
        event.saveInBackground();
        JSONArray newAttendees = event.getKeyAttendees();
        Log.d("DEBUG", "Users after: " + newAttendees.toString());

        // TODO --  fix null pointer error
        // currently buggy
        // adding event id to user's willAttend list
        JSONArray eventsAttending = user.getJSONArray("willAttend");
        Log.d("DEBUG", "events before: " + eventsAttending.toString());
        if (event != null) {
            eventsAttending.put(event);
        }

        user.put("willAttend", eventsAttending);
        user.saveInBackground();

    }

}
