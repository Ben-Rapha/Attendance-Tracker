package com.example.attendancetracker.Services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.PersistableBundle;

import androidx.core.app.NotificationCompat;

import com.example.attendancetracker.Model.History;
import com.example.attendancetracker.R;
import com.example.attendancetracker.Repository.HistoryRepository.HistoryDao;
import com.example.attendancetracker.UI.MainActivity;

import java.util.Objects;

public class AlertNotificationJobService extends JobService {

    boolean isWorking = false;
    boolean jobCancelled = false;

    boolean needRescheduling;

    Context context;

    private static NotificationManager mNotificationManager;


    private static final int NOTIFICATION_ID = 0;

    public static final String BOOTED_COMPLETED =
            "android.intent.action.BOOT_COMPLETED";

    private static final String NOTIFICATION_PRIMARY_CHANNEL_ID =
            "notification_channel";

    /**
     * Called to indicate that the job has begun executing.  Override this method with the
     * logic for your job.  Like all other component lifecycle callbacks, this method executes
     * on your application's main thread.
     * <p>
     * Return {@code true} from this method if your job needs to continue running.  If you
     * do this, the job remains active until you call
     * {@link #jobFinished(JobParameters, boolean)} to tell the system that it has completed
     * its work, or until the job's required constraints are no longer satisfied.  For
     * example, if the job was scheduled using
     * {@link JobInfo.Builder#setRequiresCharging(boolean) setRequiresCharging(true)},
     * it will be immediately halted by the system if the user unplugs the device from power,
     * the job's {@link #onStopJob(JobParameters)} callback will be invoked, and the app
     * will be expected to shut down all ongoing work connected with that job.
     * <p>
     * The system holds a wakelock on behalf of your app as long as your job is executing.
     * This wakelock is acquired before this method is invoked, and is not released until either
     * you call {@link #jobFinished(JobParameters, boolean)}, or after the system invokes
     * {@link #onStopJob(JobParameters)} to notify your job that it is being shut down
     * prematurely.
     * <p>
     * Returning {@code false} from this method means your job is already finished.  The
     * system's wakelock for the job will be released, and {@link #onStopJob(JobParameters)}
     * will not be invoked.
     *
     * @param params Parameters specifying info about this job, including the optional
     *               extras configured with {@link JobInfo.Builder#setExtras(PersistableBundle).
     *               This object serves to identify this specific running job instance when calling
     *               {@link #jobFinished(JobParameters, boolean)}.
     * @return {@code true} if your service will continue running, using a separate thread
     * when appropriate.  {@code false} means that this job has completed its work.
     */

    @Override
    public boolean onStartJob(JobParameters params) {

        isWorking = true;

        startWorkOnNewThread(params);

        return isWorking;
    }

    /**
     * This method is called if the system has determined that you must stop execution of your job
     * even before you've had a chance to call {@link #jobFinished(JobParameters, boolean)}.
     *
     * <p>This will happen if the requirements specified at schedule time are no longer met. For
     * example you may have requested WiFi with
     * {@link JobInfo.Builder#setRequiredNetworkType(int)}, yet while your
     * job was executing the user toggled WiFi. Another example is if you had specified
     * {@link JobInfo.Builder#setRequiresDeviceIdle(boolean)}, and the phone left its
     * idle maintenance window. You are solely responsible for the behavior of your application
     * upon receipt of this message; your app will likely start to misbehave if you ignore it.
     * <p>
     * Once this method returns, the system releases the wakelock that it is holding on
     * behalf of the job.</p>
     *
     * @param params The parameters identifying this job, as supplied to
     *               the job in the {@link #onStartJob(JobParameters)} callback.
     * @return {@code true} to indicate to the JobManager whether you'd like to reschedule
     * this job based on the retry criteria provided at job creation-time; or {@code false}
     * to end the job entirely.  Regardless of the value returned, your job must stop executing.
     */

    @Override
    public boolean onStopJob(JobParameters params) {
        jobCancelled = true;
        needRescheduling = isWorking;
        jobFinished(params,needRescheduling);
        return needRescheduling;
    }



    private void deliverNotification(Context context,String sessionName) {


        Intent contentIntent = new Intent(context, MainActivity.class);
        PendingIntent contentPendingIntent = PendingIntent.
                getActivity(context,
                        NOTIFICATION_ID, contentIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notificationBuilder = new
                NotificationCompat.Builder(context, NOTIFICATION_PRIMARY_CHANNEL_ID)
                .setSmallIcon(R.drawable.app_logo)
                .setContentTitle(   sessionName+ " Session")
                .setContentText(sessionName + " session start's now")
                .setContentIntent(contentPendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL);

        mNotificationManager.notify(NOTIFICATION_ID,
                notificationBuilder.build());


    }



    private void createNotificationChannel(Context context) {
        mNotificationManager = (NotificationManager) Objects.requireNonNull(context)
                .getSystemService(Context.NOTIFICATION_SERVICE);
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


    private void startWorkOnNewThread(JobParameters jobParameters){
        String classname = jobParameters.getExtras().getString("sessionName");
        new Thread(new Runnable() {
            @Override
            public void run() {
                createNotificationChannel(getApplicationContext());
                deliverNotification(getApplicationContext(),classname);
                isWorking =false;
                needRescheduling = isWorking;
                jobFinished(jobParameters, needRescheduling);

            }
        }).start();
    }
}
