package com.example.attendancetracker.Util;

import android.view.View;

import java.sql.Date;
import java.sql.Time;
import java.util.Calendar;
import java.util.Random;

public class MyUtil {

    public static final int SUNDAY = 1;
    public static final int MONDAY = 2;
    public static final int TUESDAY = 3;
    public static final int WEDNESDAY = 4;
    public static final int THURSDAY = 5;
    public static final int FRIDAY = 6;
    public static final int SATURDAY = 7;


    public static final  String NOTIFICATIONS_KEY ="notifications";

    public static final  String FEEDBACK_KEY ="feedback";

    public static final  String ABOUT_KEY ="about";

    public static final String  LOGIN_SHARED_PREF_FILE =
            "ATTENDANCE_TRACKER_LOGIN";

    public static final String  LOGIN_SHARED_PREF_ON_PAUSE_FILE =
            "ATTENDANCE_TRACKER_LOGIN_ON_PAUSE";

    public static final String  LOGIN_USERNAME_KEY =
            "LOGIN_USERNAME";

    public static final String  LOGIN_EMAIL_KEY =
            "LOGIN_EMAIL";

    public static final String  LOGIN_PASSWORD_KEY =
            "LOGIN_PASSWORD";

    public static final String  LOGIN_USERNAME_ON_PAUSE_KEY =
            "LOGIN_USERNAME_ON_PAUSE";

    public static final String  LOGIN_PASSWORD_ON_PAUSE_KEY =
            "LOGIN_PASSWORD_ON_PAUSE";

    public static final String  LOGIN_USERNAME_VALUE_DEFAULT =
            "NO_USERNAME";

    public static final String  LOGIN_PASSWORD_VALUE_DEFAULT =
            "NO_PASSWORD";

    public static final String  LOGIN_REMEMBER_ME_ON_PAUSE =
            "REMEMBER_ON_PAUSE";

    public static final String  LOGIN_REMEMBER_ME =
            "REMEMBER_ON";

    public static final String  LOGIN_ON_PAUSE_CALLED_KEY =
            "ON_PAUSE_CALLED";

    public static final String ALREADY_SIGNED_UP_KEY   =
            "ALREADY_SIGNED_UP";
    public static final boolean  ALREADY_SIGNED_UP_DEFAULT_VALUE =
            false;

    public static final boolean  LOGIN_ON_PAUSE_CALLED_VALUE =
            false;


    public static final String  LOGIN_REMEMBER_ME_ON_PAUSE_DEFAULT =
            "false";


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


    public static void loseOrGainFocus(View view, boolean gain) {
        view.setFocusable(gain);
        view.setFocusableInTouchMode(gain);

    }

    public static boolean checkPasswordValidity(String password) {
        return password.length() > 5;
    }



    public static final String APP_EMAIL = "attendancetrackerlord@gmail.com";

    public static final String APP_PASSWORD = "Thelordismystay1_";



    public static final String RESET_PASSCODE_KEY = "PASSCODE";


    public static int getPassCodeRandom(){
        Random random = new Random();
        return random.nextInt((90000 - 10000) +1) + 10000;
    }



}
