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

import java.util.List;

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

    public static void addUserToEvent(String userId, Event event) {
        JSONArray currentAttendees = event.getKeyAttendees();
        //Log.d("DEBUG", "Users before: " + currentAttendees.toString());
        if (userId != null) {
            Log.d("DEBUG", "user is not null " + userId);
            currentAttendees.put(userId);
        }

        Log.d("DEBUG", "Users: " + currentAttendees.toString());
        event.setKeyAttendees(currentAttendees);
    }

}
