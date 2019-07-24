package com.example.attendancetracker.UI;


import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.SavedStateVMFactory;
import androidx.lifecycle.ViewModelProviders;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;

import android.os.PersistableBundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.attendancetracker.AddClassSession;
import com.example.attendancetracker.BuildConfig;
import com.example.attendancetracker.R;
import com.example.attendancetracker.Repository.SessionClassRepository.SessionModelRepository;
import com.example.attendancetracker.Repository.SessionClassRepository.SessionViewModel;
import com.example.attendancetracker.Services.AlertNotificationJobService;
import com.example.attendancetracker.Util.MyUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;


public class UserSettings extends PreferenceFragmentCompat
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    private MainMenuListeners mainMenuListeners;

    private NotificationManager mNotificationManager;

   private SwitchPreference switchPreferenceNotifications;

    private static final int NOTIFICATION_ID = 225;

    private Intent notifyIntent;
    private AlarmManager alarmManager;

    private PendingIntent notifyPendingIntent;

    private SessionViewModel sessionViewModel;

    private Calendar calendar;

    String sessionName;

    private JobScheduler schedulerNotification;

   private AlertSessionAlarmReceiver alertSessionAlarmReceiver;

    boolean enableNotification;

    public static final String NOTIFICATION_ACTION =
            BuildConfig.APPLICATION_ID +"ACTION_NOTIFICATION";

    private List<AddClassSession> mTodaySessionClasses;

    private static final String NOTIFICATION_PRIMARY_CHANNEL_ID =
            "notification_channel";

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.user_preference, rootKey);

        mNotificationManager = (NotificationManager) Objects.requireNonNull(getActivity()).
                getSystemService(Context.NOTIFICATION_SERVICE);

        notifyIntent = new Intent(getContext(), AlertSessionAlarmReceiver.class);


        String subject = "";

//        setNextSessionAlarm();

         switchPreferenceNotifications
                = findPreference(MyUtil.NOTIFICATIONS_KEY);

        Preference preferenceFeedback =
                findPreference(MyUtil.FEEDBACK_KEY);

        Objects.requireNonNull(preferenceFeedback).
                setOnPreferenceClickListener(preference -> {
                    Toast.makeText(getContext(),
                            "feedback is clicked ",
                            Toast.LENGTH_SHORT).show();

                    Intent sendFeedback = new Intent(Intent.ACTION_SENDTO);
                    sendFeedback.setData(Uri.parse("mailto:"));
                    sendFeedback.putExtra(Intent.EXTRA_EMAIL,
                            new String[]{"7hegifted@gmail.com"});
                    sendFeedback.putExtra(Intent.EXTRA_SUBJECT, subject);
                    startActivity(sendFeedback);

                    if (sendFeedback.resolveActivity(
                            Objects.requireNonNull(getActivity()).
                                    getPackageManager()) != null) {

                    }
                    return false;
                });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        sessionViewModel = ViewModelProviders.
                of(Objects.requireNonNull(getActivity()), new SavedStateVMFactory(this))
                .get(SessionViewModel.class);


    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceManager().
                getSharedPreferences().
                registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof MainMenuListeners) {
            mainMenuListeners = (MainMenuListeners) context;

        } else {
            throw new RuntimeException("must" +
                    " implement MainMenuListeners");
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        getPreferenceManager().
                getSharedPreferences().
                unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mainMenuListeners = null;
    }


    /**
     * Called when a shared preference is changed, added, or removed. This
     * may be called even if a preference is set to its existing value.
     *
     * <p>This callback will be run on your main thread.
     *
     * @param sharedPreferences The {@link SharedPreferences} that received
     *                          the change.
     * @param key               The key of the preference that was changed, added, or
     */
    @Override
    public void onSharedPreferenceChanged
    (SharedPreferences sharedPreferences, String key) {


        calendar = Calendar.getInstance();
        int dayOfTheWeek = calendar.get(Calendar.DAY_OF_WEEK);

        checkDay(dayOfTheWeek);
        scheduleNextNotificationJob(sharedPreferences, key);


    }

    private void scheduleNextNotificationJob(SharedPreferences sharedPreferences, String key) {
        long sessionStartTime = 0;
        Calendar calendar = Calendar.getInstance();
        int today = calendar.get(Calendar.DAY_OF_WEEK );
        enableNotification =
                sharedPreferences.getBoolean(key, false);

        if (enableNotification) {

            sessionStartTime = getDeadLineTime(today);

            if (sessionStartTime == 0){
                findNextAvailableSession();
            } else{

                ComponentName componentName = new ComponentName
                        (Objects.requireNonNull(getActivity()),
                                AlertNotificationJobService.class);

                PersistableBundle sessionNameExtra = new PersistableBundle();

                sessionNameExtra.putString("sessionName", sessionName);


                JobInfo jobInfo = new JobInfo.Builder(234,componentName)
                        .setRequiresCharging(false)
                        .setRequiresDeviceIdle(false)
                        .setMinimumLatency(sessionStartTime - 20000)
                        .setOverrideDeadline(sessionStartTime)
                        .setExtras(sessionNameExtra)
                        .build();

                schedulerNotification = (JobScheduler) getActivity().
                        getSystemService(Context.JOB_SCHEDULER_SERVICE);

                int resultCode = schedulerNotification.schedule(jobInfo);
                if (resultCode == JobScheduler.RESULT_SUCCESS){
                    findNextAvailableSession();
                }

            }




            Toast.makeText(getContext(),
                    "notification is turned on ",
                    Toast.LENGTH_SHORT).show();

        } else {
            schedulerNotification = (JobScheduler) Objects.requireNonNull(getActivity()).
                    getSystemService(Context.JOB_SCHEDULER_SERVICE);

            schedulerNotification.cancel(234);
            Toast.makeText(getContext(),
                    "notification is turned off ",
                    Toast.LENGTH_SHORT).show();

        }
    }

    private Long getDeadLineTime(int dayOfWeek) {

        long alarmTriggerTime = 0;

        AddClassSession addClassSession = getSessionUpNext(dayOfWeek);

        if (addClassSession!= null){
            sessionName = addClassSession.getClassname();
        }


        if (addClassSession!= null){

            Calendar nowTimeCalender = Calendar.getInstance();

            int hourOfTheDay = nowTimeCalender.get(Calendar.HOUR_OF_DAY);

            int minutesOfTheDay = nowTimeCalender.get(Calendar.MINUTE);

            String nowTimeString = hourOfTheDay + ":" +
                    ((minutesOfTheDay / 10 < 1) ? "0" + minutesOfTheDay :
                            String.valueOf(minutesOfTheDay)) + ":" + "00";

            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat simpleDateClassStartTIme = new SimpleDateFormat("HH:mm:ss");

            try {

                Date startDateTime = simpleDateClassStartTIme.parse(Objects.
                        requireNonNull(addClassSession).getRawTime());

                Date nowTimeDate = simpleDateClassStartTIme.parse(nowTimeString);

                alarmTriggerTime = startDateTime.getTime() - nowTimeDate.getTime();

                return alarmTriggerTime;


            } catch (ParseException e) {
                e.printStackTrace();
            }


            }
        return  alarmTriggerTime;

        }





    private void cancelAlarm() {
        boolean alarmUp = (PendingIntent.getBroadcast(getContext(), NOTIFICATION_ID,
                notifyIntent, PendingIntent.FLAG_NO_CREATE) != null);
        if (alarmManager != null & alarmUp) {
            alarmManager.cancel(notifyPendingIntent);

        }
    }


    private void checkDay(int day) {

        getSessionDay(day);
    }

    private void getSessionDay(int dayOfTheWeek) {

        SessionModelRepository.setTodayClassSessionListener(new TodayClassSessionListener() {
            @Override
            public void todayClassSession(LiveData<List<AddClassSession>> listLiveData) {

                listLiveData.observe(getViewLifecycleOwner(), new Observer<List<AddClassSession>>() {
                    @Override
                    public void onChanged(List<AddClassSession> addClassSessionList) {
                        mTodaySessionClasses = addClassSessionList;
                    }
                });
            }

            @Override
            public void deletedSession(String sessionDeleted) {

            }
        });
        switch (dayOfTheWeek) {
            case MyUtil.SUNDAY:
                if (sessionViewModel.getSundayScheduledClass() != null) {
                    sessionViewModel.getSundayScheduledClass().observe(
                            getViewLifecycleOwner(),
                            addClassSessionList -> {
                                mTodaySessionClasses = addClassSessionList;

                            });
                }
                return;

            case MyUtil.MONDAY:
                if (sessionViewModel.getMondayScheduledClass() != null) {
                    sessionViewModel.getMondayScheduledClass().
                            observe(getViewLifecycleOwner(),
                                    addClassSessionList -> {
                                        mTodaySessionClasses = addClassSessionList;

                                    });
                }

                return;

            case MyUtil.TUESDAY:
                if (sessionViewModel.getTuesdayScheduledClass() != null) {
                    sessionViewModel.getTuesdayScheduledClass().observe(
                            getViewLifecycleOwner(),
                            addClassSessionList -> {
                                mTodaySessionClasses = addClassSessionList;
                            });
                }

                return;

            case MyUtil.WEDNESDAY:
                if (sessionViewModel.getWednesdayScheduledClass() != null) {
                    sessionViewModel.getWednesdayScheduledClass().observe(
                            getViewLifecycleOwner(),
                            addClassSessionList -> {
                                mTodaySessionClasses = addClassSessionList;

                            });
                }

                return;

            case MyUtil.THURSDAY:
                if (sessionViewModel.getThursdayScheduledClass() != null) {
                    sessionViewModel.getThursdayScheduledClass().observe(
                            getViewLifecycleOwner(),
                            addClassSessionList -> {

                                mTodaySessionClasses = addClassSessionList;
                            });
                }
                return;

            case MyUtil.FRIDAY:
                if (sessionViewModel.getFridayScheduledClass() != null) {
                    sessionViewModel.getFridayScheduledClass().observe(
                            getViewLifecycleOwner(),
                            addClassSessionList -> {
                                mTodaySessionClasses = addClassSessionList;

                            });
                }

                return;

            case MyUtil.SATURDAY:
                if (sessionViewModel.getSaturdayScheduledClass() != null) {
                    sessionViewModel.getSaturdayScheduledClass().
                            observe(getViewLifecycleOwner(),
                                    addClassSessionList -> {
                                        mTodaySessionClasses = addClassSessionList;


                                    });
                }
        }
    }

    private AddClassSession getSessionUpNext(int dayOfWeek) {

        checkDay(dayOfWeek);


        AddClassSession classUpNext;

        int hourOfTheDay = calendar.get(Calendar.HOUR_OF_DAY);

        int minutesOfTheDay = calendar.get(Calendar.MINUTE);

        String rawTimeString = hourOfTheDay + ":" +
                ((minutesOfTheDay / 10 < 1) ? "0" + minutesOfTheDay :
                        String.valueOf(minutesOfTheDay)) + ":" + "00";


        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDateNowTime = new SimpleDateFormat("HH:mm:ss");

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDateClassStartTIme = new SimpleDateFormat("HH:mm:ss");


        if (mTodaySessionClasses != null) {
            for (int i = 0; mTodaySessionClasses.size() > i; i++) {

                try {
                    Date nowTime = simpleDateNowTime.parse(rawTimeString);
                    Date sessionStartTime = simpleDateClassStartTIme.parse(
                            mTodaySessionClasses.get(i).getRawTime());

                    if (nowTime.before(sessionStartTime)) {
                        classUpNext = mTodaySessionClasses.get(i);
                        return classUpNext;

                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        }

        return  null;
    }


    private void registerAlarmReceiver(){
       alertSessionAlarmReceiver =
                new AlertSessionAlarmReceiver();
        IntentFilter notificationIntentFilter = new IntentFilter();
        notificationIntentFilter.addAction(NOTIFICATION_ACTION);
        Objects.requireNonNull(getContext()).
                registerReceiver(alertSessionAlarmReceiver, notificationIntentFilter);
    }


    private void unregisterAlarmReceiver(){
        if (alertSessionAlarmReceiver!= null){
            Objects.requireNonNull(getContext()).
                    unregisterReceiver(alertSessionAlarmReceiver);
        }


    }


    private  void findNextAvailableSession(){

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK );

        AddClassSession nextSession;
           nextSession = getSessionUpNext(day);

           if (nextSession == null){
               int nextDay = 1;

               Calendar calendar1 = Calendar.getInstance();
               nextDay = calendar1.get(Calendar.DAY_OF_WEEK );

               for (int i = 0; i <=  7;i++) {

                   if (nextDay == 8){
                       nextDay =1;
                   }


                   getSessionDay(nextDay);

                   nextSession = getSessionUpNext(nextDay);

                   if (nextSession != null) {

                       scheduleNextNotificationJob(switchPreferenceNotifications.getSharedPreferences()
                               , MyUtil.NOTIFICATIONS_KEY);

                       break;

                   }

                   nextDay =nextDay + 1;



               }



           }

    }

    public void scheduleNotifications(){

    }


}
