package com.example.attendancetracker.Repository.HistoryRepository;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.attendancetracker.AddClassSession;
import com.example.attendancetracker.Model.History;

import java.util.List;

public class HistoryViewModel extends AndroidViewModel {
    private HistoryRepository historyRepository;

    private LiveData<History> historyByTimeLiveData;

    private LiveData<History> historyLiveData;

    private MutableLiveData<History> historyMutableLiveData = new MutableLiveData<History>();


    private LiveData<List<History>> historyListLiveData;

    public HistoryViewModel(@NonNull Application application) {
        super(application);

        historyRepository = new HistoryRepository(application);
        historyListLiveData = historyRepository.getHistoryClasses();

    }

    public void updateSpecificHistory(History history){
        historyRepository.runUpdateAsyncTask(history);
    }

    public LiveData<History> getSpecifiedHistory(AddClassSession addClassSession){

        historyLiveData =  historyRepository.
                runGetSpecifiedHistoryAsyncTask(addClassSession);

       return historyLiveData;
    }

    public LiveData<List<History>>getHistoryListLiveData(){
        return historyListLiveData;
    }

    public void deleteHistory(AddClassSession addClassSession){
        historyRepository.runDeleteAsyncTask(addClassSession);
    }

    public void insertHistoryIntoDatabase(History history){
        historyRepository.runInsertAsyncTask(history);
    }

    public LiveData<History> getHistoryByTimeFromDatabase(String timeTaken){
        historyByTimeLiveData = historyRepository.runSelectByTimeAsyncTask(timeTaken);

       return historyByTimeLiveData;
    }

    public void setHistoryData(History historyData){
        historyMutableLiveData.setValue(historyData);
    }


    public LiveData<History> getHistoryMutableLiveData(){
        return historyMutableLiveData;
    }

}
