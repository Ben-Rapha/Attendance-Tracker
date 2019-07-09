package com.example.attendancetracker.Repository.SessionClassRepository;

import android.app.Application;
import android.app.ListActivity;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import androidx.lifecycle.MutableLiveData;

import com.example.attendancetracker.AddClassSession;
import com.example.attendancetracker.UI.AddClassActivity;
import com.example.attendancetracker.UI.CheckClassnameListener;
import androidx.lifecycle.SavedStateHandle;

import java.util.List;

public class SessionViewModel extends AndroidViewModel {
    private SessionModelRepository mSessionModelRepository;
    private MutableLiveData<AddClassSession> mAddClassSessionData =
            new MutableLiveData<AddClassSession>();

    private  SessionViewModel sessionViewModel;

    private  SavedStateHandle savedStateHandle;

    private static final String NAME_KEY="classData";



    public SessionViewModel(@NonNull Application application,SavedStateHandle savedStateHandle) {
        super(application);

        mSessionModelRepository = new SessionModelRepository(application);

        this.savedStateHandle = savedStateHandle;

    }

    public void setAddClassSessionData(AddClassSession addClassSessionData){

        this.mAddClassSessionData.setValue(addClassSessionData);
//        savedStateHandle.set(NAME_KEY,addClassSessionData);
    }
    public LiveData<AddClassSession> getAddClassSessionData(){
        return mAddClassSessionData;
    }

    public LiveData<List<AddClassSession>>
    getAllClassSessionFromSessionRepository(){
        return mSessionModelRepository.getClassSessionList();
    }

    public void insertClassSessionIntoDatabase(AddClassSession addClassSession){
        mSessionModelRepository.insertClassSession(addClassSession);
    }

    public void deleteClassSessionFromDatabase(String sessionName){
        mSessionModelRepository.deleteClassSessionFromDatabase(sessionName);
    }

    public LiveData<List<AddClassSession>> getMondayScheduledClass(){
        return mSessionModelRepository.getMondayClasses();
    }
    public LiveData<AddClassSession> getSpecifiedClass(String classname){
        return mSessionModelRepository.getSpecifiedClass(classname);
    }

    public void setViewModelObject(SessionViewModel sessionViewModel){
        this.sessionViewModel = sessionViewModel;
    }

    public SessionViewModel getSessionViewModel() {
            return sessionViewModel;
    }
}
