package com.example.attendancetracker.Repository.HistoryRepository;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.attendancetracker.Model.History;
import com.example.attendancetracker.R;
import com.example.attendancetracker.converters.Converters;

@Database(entities = {History.class}, version = 1, exportSchema = false)
@TypeConverters(Converters.class)
public abstract class HistoryDatabase extends RoomDatabase {

    public abstract HistoryDao historyDao();

    private static HistoryDatabase INSTANCE;

    public static HistoryDatabase getInstance(final Context context) {

        if (INSTANCE == null) {
            synchronized (HistoryDatabase.class) {

                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            HistoryDatabase.class, context.getString(R.string.history_database)).build();
                }
            }
        }

        return INSTANCE;
    }

}
