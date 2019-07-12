package com.example.attendancetracker.UI;

import androidx.lifecycle.LiveData;

import com.example.attendancetracker.AddClassSession;

import java.util.List;

public interface  TodayClassSessionListener{

    void todayClassSession(LiveData<List<AddClassSession>> listLiveData);
}
