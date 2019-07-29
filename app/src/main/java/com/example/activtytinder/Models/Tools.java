package com.example.activtytinder.Models;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Calendar;

public class Tools {

    public static String time;
    private static Tools mTools = null;


    public  static Tools get() {
        if (mTools == null) {
            mTools = new Tools();
        }
        return mTools;
    }

    private Tools()
    {

    }

    public void getTime(Context context, EditText etTime){
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

    public void getDate(Context context, EditText etDate){
        final Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        DatePickerDialog dpdPicker = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view1, int year1, int monthOfYear, int dayOfMonth) {
                //etDate.setText((monthOfYear + 1) + "/" + dayOfMonth + "/" + year1);
                int month = monthOfYear+1;
                etDate.setText(String.format("%02d/%02d/%02d",month,dayOfMonth,year1));
            }
        }, year, month, day);

        dpdPicker.show();
    }





}
