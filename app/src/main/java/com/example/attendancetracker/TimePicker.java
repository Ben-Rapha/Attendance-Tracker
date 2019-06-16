package com.example.attendancetracker;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TimePicker extends DialogFragment implements
        TimePickerDialog.OnTimeSetListener {

    public Boolean startTimeSet = false, endTimeSet = false;

    private TimePickerEndTimeListener timePickerEndTimeListener;

    private int chosenHour, chosenMinute;


    public TimePicker() {

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final Calendar mCalendar = Calendar.getInstance();

        int hour = mCalendar.get(Calendar.HOUR_OF_DAY);

        int minute = mCalendar.get(Calendar.MINUTE);
        return new TimePickerDialog(getActivity(),
                this, hour, minute, DateFormat.is24HourFormat(getActivity()));
    }



    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        super.onCancel(dialog);

        if (startTimeSet){
            startTimeSet = false;
        }
        if (endTimeSet){
            endTimeSet = false;
        }

        Log.v("timePicker",  " on Cancel fired");
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);

        if (startTimeSet){
            startTimeSet = false;
        }
        if (endTimeSet){
            endTimeSet = false;
        }
    }

    @Override
    public void onTimeSet(android.widget.TimePicker view,
                          int hourOfDay, int minute) {
        String timeFormat;
        String timeMode;
        chosenHour = hourOfDay;

        if (hourOfDay == 0){
            hourOfDay += 12;
            timeMode = "am";

        } else if (hourOfDay == 12){
            timeMode = "pm";
        }

        else if (hourOfDay > 12){
            timeMode = "pm";
            hourOfDay = hourOfDay - 12;

        } else{
            timeMode = "am";
        }

        timeFormat = hourOfDay + ":" +
                ((minute / 10 < 1) ? "0" + minute :
                        String.valueOf(minute)) + timeMode;

        timePickerEndTimeListener.updateTime(timeFormat);

    }




    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof TimePickerEndTimeListener) {
            timePickerEndTimeListener = (TimePickerEndTimeListener) context;
        } else {
            throw new RuntimeException("must implement TimePickerListener");
        }
    }

    public interface TimePickerEndTimeListener {
        void updateTime(String endTime);
    }

    public void setStartTimeSet(Boolean startTimeSet) {
        this.startTimeSet = startTimeSet;
    }

    public void setEndTimeSet(Boolean endTimeSet) {
        this.endTimeSet = endTimeSet;
    }

    public Boolean getEndTimeSet() {
        return endTimeSet;
    }

    public Boolean getStartTimeSet() {
        return startTimeSet;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        timePickerEndTimeListener = null;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        timePickerEndTimeListener = null;
    }
}
