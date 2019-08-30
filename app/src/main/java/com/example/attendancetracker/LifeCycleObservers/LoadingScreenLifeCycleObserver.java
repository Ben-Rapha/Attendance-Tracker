package com.example.attendancetracker.LifeCycleObservers;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import com.example.attendancetracker.Util.LoggerUtils;
import com.example.attendancetracker.Util.MyUtil;

import java.util.logging.Logger;

public class LoadingScreenLifeCycleObserver
        implements LifecycleObserver {

    private ObjectAnimator mObjectAnimator;

    private Activity activity;

    private static final String LOADING_SCREEN_LIFECYCLE_TAG =
            LoadingScreenLifeCycleObserver.class.getSimpleName();


    public LoadingScreenLifeCycleObserver(ObjectAnimator mObjectAnimator,Activity activity) {
        this.mObjectAnimator = mObjectAnimator;
        this.activity = activity;
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
        mObjectAnimator.removeAllListeners();
        mObjectAnimator.cancel();
        mObjectAnimator.end();
        LoggerUtils.onPauseLogger(LOADING_SCREEN_LIFECYCLE_TAG);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    void onStopEvent(){
        LoggerUtils.onStopLogger(LOADING_SCREEN_LIFECYCLE_TAG);
        activity.finish();
        activity = null;

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    void onDestroyEvent(){
        LoggerUtils.onDestroyLogger(LOADING_SCREEN_LIFECYCLE_TAG);
    }
}
