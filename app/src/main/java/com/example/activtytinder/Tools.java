package com.example.activtytinder;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

//TODO -- explain tools class
public class Tools {

    /**
     * Calls for an Android calendar widget instance specifically for time. The widget is displayed
     * on the screen and awaits the user to input a time which will be stored in a visible edit text.
     * @param context - the context where this method is called
     * @param etTime - the edit text where the user wants to store the time in HH:MM AM/PM format
     */
    public static void getTime(Context context, EditText etTime) {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);
        final String[] AM_PM = new String[1];
        TimePickerDialog tpdClock = new TimePickerDialog(context,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                        if (sHour > 12) {
                            sHour = sHour - 12;
                            AM_PM[0] = "PM";
                        } else {
                            AM_PM[0] = "AM";
                        }
                        etTime.setText(String.format("%02d:%02d", sHour, sMinute) + " " + AM_PM[0]);
                    }
                }, hour, minutes, true);
        tpdClock.show();
    }


    /**
     * Calls for an Android calendar widget instance specifically for choosing a date. The widget is
     * displayed on the screen and awaits the user to input a date which will be stored in a visible
     * edit text.
     * @param context - the context where this method is called
     * @param etDate - the edit text where the user wants to store the date in MM/DD/YY format
     */
    public static void getDate(Context context, EditText etDate) {
        final Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        DatePickerDialog dpdPicker = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view1, int year1, int monthOfYear, int dayOfMonth) {
                //etDate.setText((monthOfYear + 1) + "/" + dayOfMonth + "/" + year1);
                int month = monthOfYear + 1;
                String currentDate = String.format("%02d/%02d/%02d", month, dayOfMonth, year1);
                etDate.setText(convertDate(currentDate));
            }
        }, year, month, day);

        dpdPicker.show();
    }

    /**
     * Rotates an image by 90 degrees by converting it to a bitmap image and then providing a
     * rotation using matrices and returning the final bitmap.
     * @param photoFilePath - the string of the file path of the photo the user wants rotated
     * @return - returns a Bitmap that now is in the proper orientation
     */
    public static Bitmap rotateBitmapOrientation(String photoFilePath) {
        BitmapFactory.Options bounds = new BitmapFactory.Options();
        bounds.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(photoFilePath, bounds);
        BitmapFactory.Options opts = new BitmapFactory.Options();
        Bitmap bm = BitmapFactory.decodeFile(photoFilePath, opts);
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(photoFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String orientString = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
        int orientation = orientString != null ? Integer.parseInt(orientString) : ExifInterface.ORIENTATION_NORMAL;
        int rotationAngle = 0;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_90) rotationAngle = 90;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_180) rotationAngle = 180;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_270) rotationAngle = 270;
        Matrix matrix = new Matrix();
        matrix.setRotate(rotationAngle, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
        Bitmap rotatedBitmap = Bitmap.createBitmap(bm, 0, 0, bounds.outWidth, bounds.outHeight, matrix, true);
        return rotatedBitmap;
    }

    /**
     * Turns a date and time in the form of a string into milliseconds for accurate comparisons.
     * @param parsedString - A string that contains the date to be parsed and converted into a long.
     * @return A long that represents the time given in milliseconds relative to the UTC.
     */
    public static long getDateInMillis(String parsedString) {

        Date currentDate = null;
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm a", Locale.US);
        try {
            currentDate = sdf.parse(parsedString);
        } catch (ParseException e1) {
            e1.printStackTrace();
        }
        long millis = currentDate.getTime();
        return millis;
    }

    public static String convertDate (String currentDate) {
        if (currentDate.charAt(0) == '0' || currentDate.charAt(0) == '1') {
            Date newDate = null;
            SimpleDateFormat spf = new SimpleDateFormat("MM/dd/yyyy");
            try {
                newDate = spf.parse(currentDate);
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }
            spf = new SimpleDateFormat("EEE, d MMM yyyy", Locale.US);
            currentDate = spf.format(newDate);
            return currentDate;
        }else{
            Date newDate = null;
            SimpleDateFormat spf = new SimpleDateFormat("EEE, d MMM yyyy");
            try {
                newDate = spf.parse(currentDate);
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }
            spf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
            currentDate = spf.format(newDate);
            return currentDate;
        }
    }
}
