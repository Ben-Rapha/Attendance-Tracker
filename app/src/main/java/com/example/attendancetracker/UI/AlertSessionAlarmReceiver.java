package com.example.attendancetracker.UI;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.attendancetracker.R;

import java.util.Objects;

public class AlertSessionAlarmReceiver extends BroadcastReceiver {

    private NotificationManager mNotificationManager;

    private static final int NOTIFICATION_ID = 0;

    public static final String BOOTED_COMPLETED =
            "android.intent.action.BOOT_COMPLETED";

    private static final String NOTIFICATION_PRIMARY_CHANNEL_ID =
            "notification_channel";

    private ScheduleNextClassSessionAlarm scheduleNextClassSessionAlarm;

    Context context;

    @Override
    public void onReceive(Context context, Intent intent) {

        this.context = context;

        scheduleNextClassSessionAlarm = (ScheduleNextClassSessionAlarm) context;

        mNotificationManager = (NotificationManager) Objects.requireNonNull(context)
                .getSystemService(Context.NOTIFICATION_SERVICE);

        createNotificationChannel(context);
        deliverNotification(context);
//        scheduleNextClassSessionAlarm.scheduleNextSessionAlarm();

//        String intentAction = intent.getAction();
//
//        if (intentAction != null){
//            switch (intentAction){
//
//                case UserSettings.NOTIFICATION_ACTION:
//                    createNotificationChannel(context);
//                    deliverNotification(context);
//                    scheduleNextClassSessionAlarm.scheduleNextSessionAlarm();
//                    return;
//
//                case BOOTED_COMPLETED:
//
//
//            }
//        }



    }




    private void deliverNotification(Context context) {
        Intent contentIntent = new Intent(context, MainActivity.class);
        PendingIntent contentPendingIntent = PendingIntent.
                getActivity(context,
                        NOTIFICATION_ID, contentIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notificationBuilder = new
                NotificationCompat.Builder(context, NOTIFICATION_PRIMARY_CHANNEL_ID)
                .setSmallIcon(R.drawable.app_logo)
                .setContentTitle("Session Start Alert")
                .setContentText("Math 101 session just started")
                .setContentIntent(contentPendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL);

        mNotificationManager.notify(NOTIFICATION_ID,
                notificationBuilder.build());


    }



    private void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >=
                Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel =
                    new NotificationChannel(NOTIFICATION_PRIMARY_CHANNEL_ID,
                            "Session start notification",
                            NotificationManager.IMPORTANCE_HIGH);

            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.YELLOW);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription
                    ("Notifies class in session");
            mNotificationManager.createNotificationChannel(notificationChannel);

        }

    }


    interface  ScheduleNextClassSessionAlarm{
        void scheduleNextSessionAlarm();
    }

    public void setScheduleNextClassSessionAlarm(ScheduleNextClassSessionAlarm nextClassSessionAlarm) {
        this.scheduleNextClassSessionAlarm = (ScheduleNextClassSessionAlarm) context;
    }
}
