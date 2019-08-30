package com.example.attendancetracker.LifeCycleObservers;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import com.example.attendancetracker.UI.LoginFragment;
import com.example.attendancetracker.Util.LoggerUtils;
import com.example.attendancetracker.Util.MyUtil;

import java.util.Objects;

public class LoginFragmentLifeCycleObserver implements LifecycleObserver {

    private SharedPreferences mPreferencesOnPause;

    private Activity mActivity;

    private String usernameChosen,passwordChosen;

    private boolean rememberMe;



    private static final String LOGIN_ACTIVITY_LIFECYCLE_TAG =
            LoginFragmentLifeCycleObserver.class.getSimpleName();


    public LoginFragmentLifeCycleObserver(SharedPreferences mPreference, Activity activity,
                                          String usernameChosen,
                                          String passwordChosen,boolean rememberMe){
        this.mPreferencesOnPause = mPreference;
        this.mActivity = activity;
        this.rememberMe = rememberMe;
        this.passwordChosen = passwordChosen;
        this.usernameChosen = usernameChosen;
    }

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
        mPreferencesOnPause = Objects.requireNonNull(mActivity).getSharedPreferences(MyUtil.
                LOGIN_SHARED_PREF_ON_PAUSE_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mPreferencesOnPause.edit();
        editor.putString(MyUtil.LOGIN_USERNAME_ON_PAUSE_KEY, usernameChosen);
        editor.putString(MyUtil.LOGIN_PASSWORD_ON_PAUSE_KEY, passwordChosen);
        editor.putBoolean(MyUtil.LOGIN_REMEMBER_ME_ON_PAUSE, rememberMe);
        editor.putBoolean(MyUtil.LOGIN_ON_PAUSE_CALLED_KEY, true);
        editor.apply();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    void onStopEvent(){
        LoggerUtils.onStopLogger(LOGIN_ACTIVITY_LIFECYCLE_TAG);

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    void onDestroyEvent(){
        LoggerUtils.onDestroyLogger(LOGIN_ACTIVITY_LIFECYCLE_TAG);
    }

    public void setUsernameChosen(String usernameChosen){
        this.usernameChosen = usernameChosen;
    }

    public void setPasswordChosen(String passwordChosen){
        this.passwordChosen = passwordChosen;
    }

    public void setRememberMe(boolean rememberMe){
        this.rememberMe = rememberMe;
    }
}
