package com.example.attendancetracker.Services;

import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.PersistableBundle;

public class RemindNotificationJobService  extends JobService {

    @Override
    public boolean onStartJob(JobParameters params) {
        return false;
    }


    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}
