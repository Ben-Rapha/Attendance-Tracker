package com.example.attendancetracker.UI.Dialogs;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.CalendarContract;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;
import java.util.Objects;

public class DatePicker extends DialogFragment implements
        DatePickerDialog.OnDateSetListener {

    private Boolean startDateSet = false, endDateSet = false;

    private DatePickerListener datePickerListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
         super.onCreateDialog(savedInstanceState);

        Calendar dateCalender = Calendar.getInstance();

        int day = dateCalender.get(Calendar.DAY_OF_MONTH);

        int month = dateCalender.get(Calendar.MONTH);

        int year = dateCalender.get(Calendar.YEAR);

        return new DatePickerDialog(Objects.requireNonNull(getActivity())
                ,this,year,month,day);

    }

    @Override
    public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
        String date = (month +1) +"/" + dayOfMonth  +"/" + year;
        String sqlDateFormat  = (year+"-"+(((month) /10 < 1)?"0"+(month+1):String.valueOf(month+1))+
                "-"+ ((dayOfMonth /10 < 1)? "0"+dayOfMonth:String.valueOf
                (dayOfMonth)));
        datePickerListener.setDate(date,sqlDateFormat);

    }

    public interface DatePickerListener{
        void setDate(String date,String sqlDateFormat);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof DatePickerListener){
            datePickerListener = (DatePickerListener) context;
        } else{
            throw new  RuntimeException("must implement DatePickerListener");
        }
    }


    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        if (endDateSet){
            endDateSet = false;
        }
        if (startDateSet){
            startDateSet = false;
        }
    }

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        super.onCancel(dialog);

        if (endDateSet){
            endDateSet = false;
        }
        if (startDateSet){
            startDateSet = false;
        }

    }

    public void setEndDateSet(Boolean endDateSet) {
        this.endDateSet = endDateSet;
    }

    public void setStartDateSet(Boolean startDateSet) {
        this.startDateSet = startDateSet;
    }

    public Boolean getEndDateSet() {
        return endDateSet;
    }

    public Boolean getStartDateSet() {
        return startDateSet;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        datePickerListener = null;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        datePickerListener = null;
    }
}
