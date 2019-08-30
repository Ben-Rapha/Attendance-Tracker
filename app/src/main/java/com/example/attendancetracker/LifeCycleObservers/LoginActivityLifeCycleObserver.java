package com.example.attendancetracker.LifeCycleObservers;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import com.example.attendancetracker.UI.LoginActivity;
import com.example.attendancetracker.Util.LoggerUtils;

public class LoginActivityLifeCycleObserver implements LifecycleObserver {

    private static final String LOGIN_ACTIVITY_LIFECYCLE_TAG =
            LoginActivityLifeCycleObserver.class.getSimpleName();

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    void onCreateEvent(){
        LoggerUtils.onCreateLogger(LOGIN_ACTIVITY_LIFECYCLE_TAG);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    void onStartEvent(){
        LoggerUtils.onStartLogger(LOGIN_ACTIVITY_LIFECYCLE_TAG);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    void onResumeEvent(){
        LoggerUtils.onResumeLogger(LOGIN_ACTIVITY_LIFECYCLE_TAG);
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    void onPauseEvent() {
        LoggerUtils.onPauseLogger(LOGIN_ACTIVITY_LIFECYCLE_TAG);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    void onStopEvent(){
        LoggerUtils.onStopLogger(LOGIN_ACTIVITY_LIFECYCLE_TAG);

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    void onDestroyEvent(){
        LoggerUtils.onDestroyLogger(LOGIN_ACTIVITY_LIFECYCLE_TAG);
    }
}
