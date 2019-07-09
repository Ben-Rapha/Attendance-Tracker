package com.example.attendancetracker.Repository.HistoryRepository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.attendancetracker.AddClassSession;
import com.example.attendancetracker.Model.History;

import java.sql.Time;
import java.util.List;

public class HistoryRepository {

    private HistoryDao historyDao;

    private LiveData<List<History>> historyList;

    private static  LiveData<History> historyLiveData;
    private static  LiveData<History> historyByTimeLiveData;

    public HistoryRepository(Application application) {

        HistoryDatabase historyDatabase = HistoryDatabase.getInstance(application);

        historyDao = historyDatabase.historyDao();

        historyList = historyDao.getAllHistoryClassesByTimeTakenInAsc();

    }

    public LiveData<List<History>> getHistoryClasses(){
       return historyList;
    }

    public void runUpdateAsyncTask(History history){
        new UpdateAsyncTask(historyDao).execute(history);
    }

    public LiveData<History> runGetSpecifiedHistoryAsyncTask(AddClassSession addClassSession){
        new GetSpecificHistoryAsyncTask(historyDao).execute(addClassSession);

        return historyLiveData;

    }

    public LiveData<History> runSelectByTimeAsyncTask(String timeTaken){
        new SelectByTimeAsyncTask(historyDao).execute(timeTaken);
        return historyByTimeLiveData;
    }

    public void runDeleteAsyncTask(AddClassSession addClassSession){
        new DeleteAsyncTask(historyDao).execute(addClassSession);
    }

    public void runInsertAsyncTask(History history){
        new InsertAsyncTask(historyDao).execute(history);
    }



    private static class UpdateAsyncTask extends
            AsyncTask<History, Void, Void> {
        HistoryDao historyDao;

        UpdateAsyncTask(HistoryDao dao) {
            historyDao = dao;

        }

        @Override
        protected Void doInBackground(History... histories) {
            historyDao.updateHistoryClass(histories[0]);
            return null;
        }
    }

    private static class GetSpecificHistoryAsyncTask
            extends AsyncTask<AddClassSession,Void,Void>{

        HistoryDao historyDao;

        GetSpecificHistoryAsyncTask(HistoryDao dao){
            historyDao = dao;
        }

        @Override
        protected Void doInBackground(AddClassSession... addClassSessions) {
            historyLiveData = historyDao.getSpecifiedHistoryClass(addClassSessions[0]);
            return null;
        }
    }


    private static class SelectByTimeAsyncTask extends
            AsyncTask<String, Void, Void> {

        HistoryDao historyDao;

        SelectByTimeAsyncTask(HistoryDao dao) {

            historyDao = dao;
        }

        @Override
        protected Void doInBackground(String... times) {
            historyByTimeLiveData =  historyDao.getHistoryByTime(times[0]);
            return null;
        }
    }

    private static class DeleteAsyncTask extends
            AsyncTask<AddClassSession, Void, Void> {

        HistoryDao historyDao;

        DeleteAsyncTask(HistoryDao dao) {
            historyDao = dao;
        }

        @Override
        protected Void doInBackground(AddClassSession... addClassSessions) {
           historyDao.deleteSpecifiedHistoryClass(addClassSessions[0]);
            return null;
        }
    }

    private static class InsertAsyncTask extends
            AsyncTask<History, Void, Void> {

        HistoryDao historyDao;

        InsertAsyncTask(HistoryDao dao) {
            historyDao = dao;
        }

        @Override
        protected Void doInBackground(History... histories) {
            historyDao.insertClass(histories[0]);
            return null;
        }
    }

}
