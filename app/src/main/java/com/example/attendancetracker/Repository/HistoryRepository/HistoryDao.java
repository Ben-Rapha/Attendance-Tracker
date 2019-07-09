package com.example.attendancetracker.Repository.HistoryRepository;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.attendancetracker.AddClassSession;
import com.example.attendancetracker.Model.History;

import java.util.List;

@Dao
public interface HistoryDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertClass(History history);

    @Query("SELECT * from history_class_db ORDER BY timeTaken ASC")
    LiveData<List<History>> getAllHistoryClassesByTimeTakenInAsc();


    @Query("SELECT * from history_class_db WHERE classSession = :classname")
    LiveData<History> getSpecifiedHistoryClass(AddClassSession classname);

    @Query("SELECT * from history_class_db WHERE timeTaken = :timeTaken")
    LiveData<History> getHistoryByTime(String timeTaken);

    @Query("DELETE FROM history_class_db WHERE classSession =:classSession")
    void deleteSpecifiedHistoryClass(AddClassSession classSession);

    @Update
    void updateHistoryClass(History histories);
}
