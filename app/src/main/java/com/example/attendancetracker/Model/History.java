package com.example.attendancetracker.Model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.example.attendancetracker.AddClassSession;

import java.sql.Date;
import java.sql.Time;

@Entity(tableName = "history_class_db",
        indices = {@Index(value = {"classSession"}, unique = true)})

public class History {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "classSession")
    private AddClassSession mClassSession;

    @ColumnInfo(name = "timeTaken")
    private Time mTimeTaken;

    @ColumnInfo(name = "dateTaken")
    private Date mDateTaken;

    public History(@NonNull AddClassSession mClassSession, Time mTimeTaken, Date mDateTaken) {
        this.mClassSession = mClassSession;
        this.mTimeTaken = mTimeTaken;
        this.mDateTaken = mDateTaken;
    }

    public AddClassSession getClassSession() {
        return mClassSession;
    }

    public void setClassSession(AddClassSession mClassSession) {
        this.mClassSession = mClassSession;
    }

    public Time getTimeTaken() {
        return mTimeTaken;
    }

    public void setTimeTaken(Time mTimeTaken) {
        this.mTimeTaken = mTimeTaken;
    }

    public Date getDateTaken() {
        return mDateTaken;
    }

    public void setDateTaken(Date mDateTaken) {
        this.mDateTaken = mDateTaken;
    }
}
