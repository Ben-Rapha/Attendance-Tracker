package com.example.attendancetracker.Util;

import android.util.Log;

public class LoggerUtils {

    private static final String ON_CREATE_TAG = "on create";

    private static final String ON_START_TAG = "on start";

    private static final String ON_RESUME_TAG = "on resume";

    private static final String ON_PAUSE_TAG = "on pause";

    private static final String ON_STOP_TAG = "on stop";

    private static final String ON_DESTROY_TAG = "on destroy";


    public static void onCreateLogger(String classname) {
        Log.v(classname, ON_CREATE_TAG);
    }

    public static void onStartLogger(String classname) {
        Log.v(classname, ON_START_TAG);
    }

    public static void onResumeLogger(String classname) {
        Log.v(classname, ON_RESUME_TAG);
    }

    public static void onPauseLogger(String classname) {
        Log.v(classname, ON_PAUSE_TAG);
    }

    public static void onStopLogger(String classname) {
        Log.v(classname, ON_STOP_TAG);
    }

    public static void onDestroyLogger(String classname) {
        Log.v(classname, ON_DESTROY_TAG);
    }

    public static void logger(String classname,String message){
        Log.v(classname, message);
    }


}
