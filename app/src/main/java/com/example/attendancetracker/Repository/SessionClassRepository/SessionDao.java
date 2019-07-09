package com.example.attendancetracker.Repository.SessionClassRepository;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.attendancetracker.AddClassSession;

import java.util.List;

@Dao
public interface SessionDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertClass(AddClassSession addClassSession);

    @Query("SELECT * from class_session_db ORDER BY classname ASC")
    LiveData<List<AddClassSession>> getAllSessionClassesByClassnameInAsc();


    @Query("SELECT * from class_session_db WHERE classname = :classname")
    LiveData<AddClassSession> getSpecifiedSessionClass(String classname);

    @Query("SELECT * from class_session_db WHERE start_time = :startTime")
    AddClassSession getClassThatConflicts(String startTime);

    @Query("DELETE FROM class_session_db WHERE classname =:classname")
    void deleteSpecifiedSessionClass(String classname);


    @Query("SELECT * from class_session_db WHERE sunday='sunday' ORDER BY start_time ASC ")
    LiveData<List<AddClassSession>> getSundaySessionOrderedByTime();

    @Query("SELECT * from class_session_db WHERE monday='monday' ORDER BY start_time ASC ")
    LiveData<List<AddClassSession>> getMondaySessionOrderedByTime();

    @Query("SELECT * from class_session_db WHERE tuesday='tuesday' ORDER BY start_time ASC ")
    LiveData<List<AddClassSession>> getTuesdaySessionOrderedByTime();

    @Query("SELECT * from class_session_db WHERE wednesday='wednesday' ORDER BY start_time ASC ")
    LiveData<List<AddClassSession>> getWednesdaySessionOrderedByTime();

    @Query("SELECT * from class_session_db WHERE thursday='thursday' ORDER BY start_time ASC ")
    LiveData<List<AddClassSession>> getThursdaySessionOrderedByTime();

    @Query("SELECT * from class_session_db WHERE friday='friday' ORDER BY start_time ASC ")
    LiveData<List<AddClassSession>> getFridaySessionOrderedByTime();

    @Query("SELECT * from class_session_db WHERE saturday='saturday' ORDER BY start_time ASC ")
    LiveData<List<AddClassSession>> getSaturdaySessionOrderedByTime();

}
