package com.example.attendancetracker.UI;

import androidx.lifecycle.LiveData;

import com.example.attendancetracker.AddClassSession;

public interface CheckClassnameListener {

    void checkClassname(LiveData<AddClassSession> addClassSessionLiveData);
}
