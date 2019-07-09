package com.example.attendancetracker;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.example.attendancetracker.Model.Students;
import com.example.attendancetracker.Model.WeekDays;

import java.sql.Time;
import java.sql.Date;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;


@Entity(tableName = "class_session_db",indices = {@Index(value = {"classname"},unique = true)})
public class AddClassSession implements Parcelable {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "classname")
    private String mClassname;

    @ColumnInfo(name = "location")
    private String mLocation;

    @ColumnInfo(name = "start_date")
    private Date mStartDate;

    @ColumnInfo(name = "end_date")
    private Date mEndDate;

    @ColumnInfo(name = "start_time")
    private Time mStartTime;

    @ColumnInfo(name = "end_time")
    private Time mEndTime;

    @ColumnInfo(name = "students")
    private List<Students> students;

    @Ignore
    @ColumnInfo(name = "weekdays")
    private List<WeekDays> weekDaysList;



    @Nullable
    private String sunday,monday,tuesday,wednesday,thursday,friday,saturday;

    @Ignore
    public AddClassSession(@NonNull String mClassname, @NonNull String mLocation,
                           Date mStartDate, Date mEndDate, Time mStartTime,
                           Time mEndTime, List<WeekDays> weekDaysList){
      this.mClassname = mClassname;
      this.mLocation = mLocation;
      this.mStartDate = mStartDate;
      this.mEndDate = mEndDate;
      this.mStartTime = mStartTime;
      this.mEndTime = mEndTime;
      this.weekDaysList = weekDaysList;

    }

    public AddClassSession(@NonNull String mClassname,
                           String mLocation, Date mStartDate,
                           Date mEndDate, Time mStartTime, Time mEndTime,
                           @Nullable String sunday, @Nullable String monday, @Nullable String tuesday,
                           @Nullable String wednesday,
                           @Nullable String thursday, @Nullable String friday,
                           @Nullable String saturday,@Nullable List<Students> students){
        this.mClassname = mClassname;
        this.mLocation = mLocation;
        this.mStartDate = mStartDate;
        this.mEndDate = mEndDate;
        this.mStartTime = mStartTime;
        this.mEndTime = mEndTime;
        this.sunday = sunday;
        this.monday = monday;
        this.tuesday = tuesday;
        this.wednesday = wednesday;
        this.thursday = thursday;
        this.friday = friday;
        this.saturday = saturday;
        this.students = students;

    }


    public void setClassname(String mClassname) {
        this.mClassname = mClassname;
    }

    public String getClassname() {
        return mClassname;
    }

    public void setLocation(String mLocation) {
        this.mLocation = mLocation;
    }

    public String getLocation() {
        return mLocation;
    }

    public void setStartDate(Date mStartDate) {
        this.mStartDate = mStartDate;
    }


    public Date getStartDate() {
        return mStartDate;
    }

    public void setEndDate(Date mEndDate) {
        this.mEndDate = mEndDate;
    }

    public Date getEndDate() {
        return mEndDate;
    }

    public String getFormattedDateStart(){
        return getFormattedDate(mStartDate);
    }

    public String getFormattedDateEnd(){
        return getFormattedDate(mEndDate);
    }

    public void setStartTime(Time mStartTime) {
        this.mStartTime = mStartTime;
    }

    public Time getStartTime() {
        return mStartTime;
    }
    public String getStartTimeString(){
        return getFormattedTime(mStartTime);
    }


    public void setEndTime(Time mEndTime) {
        this.mEndTime = mEndTime;
    }

    public Time getEndTime() {
        return mEndTime;
    }
    public String getEndTimeString(){
        return getFormattedTime(getEndTime());
    }

    public void setWeekDaysList(List<WeekDays> weekDaysList) {
        this.weekDaysList = weekDaysList;
    }

    public List<WeekDays> getWeekDaysList() {
        return weekDaysList;
    }

    @Nullable
    public String getMonday() {
        return monday;
    }

    @Nullable
    public String getTuesday() {
        return tuesday;
    }

    @Nullable
    public String getWednesday() {
        return wednesday;
    }

    @Nullable
    public String getThursday() {
        return thursday;
    }

    @Nullable
    public String getFriday() {
        return friday;
    }

    @Nullable
    public String getSaturday() {
        return saturday;
    }

    @Nullable
    public String getSunday() {
        return sunday;
    }

    public void setMonday(@Nullable String monday) {
        this.monday = monday;
    }

    public void setSunday(@Nullable String sunday) {
        this.sunday = sunday;
    }

    public void setTuesday(@Nullable String tuesday) {
        this.tuesday = tuesday;
    }

    public void setWednesday(@Nullable String wednesday) {
        this.wednesday = wednesday;
    }

    public void setThursday(@Nullable String thursday) {
        this.thursday = thursday;
    }

    public void setFriday(@Nullable String friday) {
        this.friday = friday;
    }

    public void setSaturday(@Nullable String saturday) {
        this.saturday = saturday;
    }

    public List<Students> getStudents() {
        return students;
    }

    public void setStudents(List<Students> students) {
        this.students = students;
    }

    private String getFormattedTime(Time time) {
        String timeFormat;
        String timeMode;

//            java.util.Date date = simpleDateFormat.parse(time);
            Calendar timeCalender = Calendar.getInstance();
            timeCalender.setTime(time);
//            Log.v("timeFormatted", timeCalender.get(Calendar.HOUR)+" ");
//            Log.v("timeFormatted", timeCalender.get(Calendar.MINUTE)+" ");
//            Log.v("timeFormatted", timeCalender.get(Calendar.SECOND)+" ");

            int hourOfDay = timeCalender.get(Calendar.HOUR_OF_DAY);
            int minutes = timeCalender.get(Calendar.MINUTE);

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
                ((minutes / 10 < 1) ? "0" + minutes :
                        String.valueOf(minutes)) + timeMode;
            return timeFormat;


    }

    private String getFormattedDate(Date date){

        String dateFormat;

        Calendar dateCalender = Calendar.getInstance();
        dateCalender.setTime(date);

        dateFormat = ( dateCalender.get(Calendar.MONTH)+1) +"/" + dateCalender.get(Calendar.DAY_OF_MONTH)
                +"/" + dateCalender.get(Calendar.YEAR);
        return dateFormat;
    }





    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {

        parcel.writeString(this.mClassname);
        parcel.writeString(this.mLocation);
        parcel.writeString(this.monday);
        parcel.writeString(this.tuesday);
        parcel.writeString(this.wednesday);
        parcel.writeString(this.thursday);
        parcel.writeString(this.friday);
        parcel.writeString(this.saturday);
        parcel.writeString(this.sunday);
        parcel.writeValue(this.mStartDate);
        parcel.writeValue(this.mEndDate);
        parcel.writeValue(this.mStartTime);
        parcel.writeValue(this.mEndTime);


    }

    private AddClassSession (Parcel in){
        this.mClassname = Objects.requireNonNull(in.readString());
        this.mLocation = in.readString();
        this.monday = in.readString();
        this.tuesday = in.readString();
        this.wednesday = in.readString();
        this.thursday = in.readString();
        this.friday = in.readString();
        this.saturday = in.readString();
        this.sunday = in.readString();
        this.mStartDate = (Date) in.readValue(Date.class.getClassLoader());
        this.mEndDate = (Date) in.readValue(Date.class.getClassLoader());
        this.mStartTime = (Time) in.readValue(Time.class.getClassLoader());
        this.mEndTime = (Time) in.readValue(Time.class.getClassLoader());
    }

    public static final Parcelable.ClassLoaderCreator<AddClassSession>
            CREATOR = new ClassLoaderCreator<AddClassSession>() {
        @Override
        public AddClassSession createFromParcel(Parcel source) {
            return new AddClassSession(source);
        }

        @Override
        public AddClassSession[] newArray(int size) {
            return new AddClassSession[size];
        }

        @Override
        public AddClassSession createFromParcel(Parcel source, ClassLoader loader) {
            return new  AddClassSession(source);
        }
    };
}
