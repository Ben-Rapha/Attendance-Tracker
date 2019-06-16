package com.example.attendancetracker;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.provider.CalendarContract;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;
import java.util.Objects;

public class DatePicker extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    Boolean startDateSet = false, endDateSet = false;

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
        datePickerListener.setDate(date);

    }

    public interface DatePickerListener{
        void setDate(String date);
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
}
