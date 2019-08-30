package com.example.attendancetracker.LifeCycleObservers;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import com.example.attendancetracker.Util.LoggerUtils;

public class UserProfileLifeCycleObserver implements LifecycleObserver {

    private static final String LOADING_SCREEN_LIFECYCLE_TAG =
            UserProfileLifeCycleObserver.class.getSimpleName();

    public UserProfileLifeCycleObserver(){

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    void onCreateEvent(){
        LoggerUtils.onCreateLogger(LOADING_SCREEN_LIFECYCLE_TAG);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    void onStartEvent(){
        LoggerUtils.onStartLogger(LOADING_SCREEN_LIFECYCLE_TAG);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    void onResumeEvent(){
        LoggerUtils.onResumeLogger(LOADING_SCREEN_LIFECYCLE_TAG);
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    void onPauseEvent() {
        LoggerUtils.onPauseLogger(LOADING_SCREEN_LIFECYCLE_TAG);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    void onStopEvent(){
        LoggerUtils.onStopLogger(LOADING_SCREEN_LIFECYCLE_TAG);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    void onDestroyEvent(){
        LoggerUtils.onDestroyLogger(LOADING_SCREEN_LIFECYCLE_TAG);
    }
}
