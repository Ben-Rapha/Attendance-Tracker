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
import com.example.attendancetracker.UI.EmailSentListener;

import androidx.lifecycle.SavedStateHandle;

import java.util.List;

public class SessionViewModel extends AndroidViewModel {
    private SessionModelRepository mSessionModelRepository;
    private MutableLiveData<AddClassSession> mAddClassSessionData =
            new MutableLiveData<AddClassSession>();

    public MutableLiveData<Boolean> isEmailSent = new MutableLiveData<>();

    private LiveData<List<AddClassSession>> mondayList, tuesdayList, wedList, thursList, friList,
            satList, sunList = new MutableLiveData<>();

    private SessionViewModel sessionViewModel;

    private static final String NAME_KEY = "classData";


    public SessionViewModel(@NonNull Application application) {
        super(application);

        mSessionModelRepository = new SessionModelRepository(application);

    }



    public void setAddClassSessionData(AddClassSession addClassSessionData) {

        this.mAddClassSessionData.setValue(addClassSessionData);
//        savedStateHandle.set(NAME_KEY,addClassSessionData);
    }

    public LiveData<AddClassSession> getAddClassSessionData() {
        return mAddClassSessionData;
    }

    public LiveData<List<AddClassSession>>
    getAllClassSessionFromSessionRepository() {
        return mSessionModelRepository.getClassSessionList();
    }

    public void insertClassSessionIntoDatabase(AddClassSession addClassSession) {
        mSessionModelRepository.insertClassSession(addClassSession);
    }

    public void deleteClassSessionFromDatabase(String sessionName) {
        mSessionModelRepository.deleteClassSessionFromDatabase(sessionName);
    }

    public LiveData<List<AddClassSession>> getMondayScheduledClass() {
        return mSessionModelRepository.getMondayClasses();
    }

    public LiveData<List<AddClassSession>> getSundayScheduledClass() {
        return mSessionModelRepository.getSundayClasses();
    }

    public LiveData<List<AddClassSession>> getTuesdayScheduledClass() {
        return mSessionModelRepository.getTuesdayClasses();
    }

    public LiveData<List<AddClassSession>> getWednesdayScheduledClass() {
        return mSessionModelRepository.getWednesdayClasses();
    }

    public LiveData<List<AddClassSession>> getThursdayScheduledClass() {
        thursList = mSessionModelRepository.getThursdayClasses();

        return thursList;
    }

    public LiveData<List<AddClassSession>> getFridayScheduledClass() {
        return mSessionModelRepository.getFridayClasses();
    }

    public LiveData<List<AddClassSession>> getSaturdayScheduledClass() {
        return mSessionModelRepository.getSaturdayClasses();
    }

    public LiveData<AddClassSession> getSpecifiedClass(String classname) {
        return mSessionModelRepository.getSpecifiedClass(classname);
    }

    public void setViewModelObject(SessionViewModel sessionViewModel) {
        this.sessionViewModel = sessionViewModel;
    }

    public SessionViewModel getSessionViewModel() {
        return sessionViewModel;
    }

    public void sendUserPasscode(String passcode,String email){
        mSessionModelRepository.sendUserEmailPasscode(passcode,email);
    }


}
