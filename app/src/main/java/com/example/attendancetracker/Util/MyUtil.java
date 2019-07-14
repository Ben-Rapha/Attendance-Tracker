package com.example.attendancetracker.Util;

import java.sql.Date;
import java.sql.Time;
import java.util.Calendar;

public class MyUtil {

    public static final int SUNDAY = 1;
    public static final int MONDAY = 2;
    public static final int TUESDAY = 3;
    public static final int WEDNESDAY = 4;
    public static final int THURSDAY = 5;
    public static final int FRIDAY = 6;
    public static final int SATURDAY = 7;

    public static boolean checkInputValidity(String text) {
        return text.length() > 1;
    }

    public static boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static String getFormattedTime(Time time) {
        String timeFormat;
        String timeMode;
        Calendar timeCalender = Calendar.getInstance();
        timeCalender.setTime(time);
        int hourOfDay = timeCalender.get(Calendar.HOUR_OF_DAY);
        int minutes = timeCalender.get(Calendar.MINUTE);

        if (hourOfDay == 0) {
            hourOfDay += 12;
            timeMode = "am";

        } else if (hourOfDay == 12) {
            timeMode = "pm";
        } else if (hourOfDay > 12) {
            timeMode = "pm";
            hourOfDay = hourOfDay - 12;

        } else {
            timeMode = "am";
        }

        timeFormat = hourOfDay + ":" +
                ((minutes / 10 < 1) ? "0" + minutes :
                        String.valueOf(minutes)) + timeMode;
        return timeFormat;


    }


    public static String getFormattedDate(Date date){

        String dateFormat;

        Calendar dateCalender = Calendar.getInstance();
        dateCalender.setTime(date);

        dateFormat = ( dateCalender.get(Calendar.MONTH)+1) +"/" + dateCalender.get(Calendar.DAY_OF_MONTH)
                +"/" + dateCalender.get(Calendar.YEAR);
        return dateFormat;
    }



}
