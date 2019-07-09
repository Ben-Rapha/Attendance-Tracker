package com.example.attendancetracker.Repository;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.example.attendancetracker.AddClassSession;
import com.example.attendancetracker.Repository.SessionClassRepository.SessionDao;
import com.example.attendancetracker.converters.Converters;


@Database(entities = {AddClassSession.class},version = 1,exportSchema = false)
@TypeConverters({Converters.class})
public abstract class SessionDatabase extends RoomDatabase {

    public abstract SessionDao sessionDao();
    private static SessionDatabase INSTANCE;

    public static SessionDatabase getInstance(final Context context){
        if (INSTANCE == null){
            synchronized (SessionDatabase.class){
                if (INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            SessionDatabase.class,"class_session_db").build();
                }
            }
        }
        return INSTANCE;
    }
}
