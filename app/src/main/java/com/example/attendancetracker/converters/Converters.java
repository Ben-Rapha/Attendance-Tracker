package com.example.attendancetracker.converters;

import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.example.attendancetracker.AddClassSession;
import com.example.attendancetracker.Model.History;
import com.example.attendancetracker.Model.Students;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

public class Converters {

    @TypeConverter
    public static Date fromLongToDate(Long value){

       return value == null ? null : new Date(value);

    }

    @TypeConverter
    public static Long fromDateToLong(Date date){

        return date == null? null : date.getTime();
    }


    @TypeConverter
    public static Time fromLongToTime(Long value){

        return value == null? null : new Time(value);
    }

    @TypeConverter
    public static Long fromTimeToLong(Time time){

        return time == null ? null : time.getTime();
    }


    @TypeConverter
    public static String fromStudentToJson(List<Students> students){
        Gson gson = new Gson();
        return gson.toJson(students);

    }

    @TypeConverter
    public static List<Students> fromJsonToStudentList(String students){
        Gson gson = new Gson();
        TypeToken<List<Students>> listTypeToken = new TypeToken<List<Students>>(){};
        return gson.fromJson(students,listTypeToken.getType());
    }

    @TypeConverter
    public static String fromHistoryToJson(AddClassSession addClassSession){
        Gson gson = new Gson();
        return gson.toJson(addClassSession);
    }

    @TypeConverter
    public static AddClassSession fromJsonToAddClassSession(String session){

        Gson gson = new Gson();

        TypeToken<AddClassSession> sessionTypeToken = new TypeToken<AddClassSession>(){};

        return gson.fromJson(session,sessionTypeToken.getType());

    }
}
