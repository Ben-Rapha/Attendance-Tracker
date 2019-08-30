package com.example.attendancetracker.UI;

import androidx.lifecycle.LiveData;

public interface EmailSentListener {
    void dismissDialog(LiveData<Boolean> isEmailSentSuccessfully);
}


