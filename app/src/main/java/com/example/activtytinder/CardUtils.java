package com.example.activtytinder;

import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import com.example.activtytinder.Models.Event;
import com.parse.ParseRelation;
import com.parse.ParseUser;

//TODO -- explain class
public class CardUtils {


    //TODO -- explain this
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

    /**
     * Adds the user to the list of people attending the event and adds the event to the
     * user's list of events they are attending.
     * @param user - the current user, must be a ParseUser
     * @param event - event user wants to attend, must be an Event
     */
    public static void addUserToEvent(ParseUser user, Event event) {
        ParseRelation<ParseUser> userInEvent = event.getRelation("usersAttending");
        userInEvent.add(user);
        event.saveInBackground();

        ParseRelation<Event> eventInUser = user.getRelation("willAttend");
        eventInUser.add(event);
        user.saveInBackground(e -> {
            if (e == null) {
                Log.d("DEBUG", "Inputting user successful!");
            } else {
                Log.d("DEBUG", "Inputting user failed!");
                e.printStackTrace();
            }
        });
    }

    /**
     * Removes the user from the event's list of people attending and removes the event
     * from the user's list of events that they're attending.
     * @param user - user to remove, must be a ParseUser
     * @param event - event user wants to leave, must be an Event
     */
    public static void removeUserFromEvent(ParseUser user, Event event) {
        ParseRelation<ParseUser> userInEvent = event.getRelation("usersAttending");
        userInEvent.remove(user);
        event.saveInBackground();

        ParseRelation<Event> eventInUser = user.getRelation("willAttend");
        eventInUser.remove(event);
        user.saveInBackground();
    }

}
