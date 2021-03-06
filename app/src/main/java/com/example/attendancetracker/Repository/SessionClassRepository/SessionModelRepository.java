package com.example.attendancetracker.Repository.SessionClassRepository;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.attendancetracker.AddClassSession;
import com.example.attendancetracker.Repository.SessionDatabase;
import com.example.attendancetracker.UI.CheckClassnameListener;

import java.util.List;

public class SessionModelRepository {

    static CheckClassnameListener checkClassnameListener;

    private static LiveData<List<AddClassSession>>mClassSessionList,sundayList,
            mondayList,tuesdayList,wednesdayList,thursdayList,fridayList,saturdayList;

    public static LiveData<AddClassSession> singleAddClassSession = new MutableLiveData<>();

    public static MutableLiveData<AddClassSession> singleAddClassSessionResult  = new MutableLiveData<>();

    private static MutableLiveData<Boolean> liveProgress = new MutableLiveData<>();

    public static LiveData<AddClassSession> dummy;


    private SessionDatabase db;

    private SessionDao sessionDao;

    public SessionModelRepository (Application application){
        db = SessionDatabase.getInstance(application);
        sessionDao = db.sessionDao();
        mClassSessionList = sessionDao.
                getAllSessionClassesByClassnameInAsc();
        mondayList = sessionDao.getMondaySessionOrderedByTime();


    }

    public LiveData<List<AddClassSession>> getClassSessionList() {
        return mClassSessionList;
    }

    public void insertClassSession(AddClassSession addClassSession){
        new insertClassSessionAsyncTask(sessionDao).execute(addClassSession);
    }

    public void deleteClassSessionFromDatabase(String classname){
        new deleteClassSessionAsyncTask(sessionDao).execute(classname);
    }

    public LiveData<List<AddClassSession>> getMondayClasses(){
         new getMondayClassesAsyncTask(sessionDao).execute();
         return mondayList ;
    }

    public LiveData<AddClassSession> getSpecifiedClass(String classname){
      new getSpecifiedClassAsyncTask(sessionDao).execute(classname);
       return singleAddClassSession;
    }

    public MutableLiveData<Boolean> getLiveProgress(){
        return liveProgress;
    }


    public static class insertClassSessionAsyncTask extends
            AsyncTask<AddClassSession,Void,Void>{
        SessionDao sessionDao;
        insertClassSessionAsyncTask(SessionDao dao){
            sessionDao = dao;
        }

        @Override
        protected Void doInBackground(AddClassSession... addClassSessions) {
            sessionDao.insertClass(addClassSessions[0]);
            return null;
        }
    }

    private static class deleteClassSessionAsyncTask
            extends AsyncTask<String,Void,Void>{
        SessionDao sessionDao;

        deleteClassSessionAsyncTask(SessionDao sessionDao){
            this.sessionDao = sessionDao;
        }

        @Override
        protected Void doInBackground(String... sessionName) {
            sessionDao.deleteSpecifiedSessionClass(sessionName[0]);
            return null;
        }
    }

    private static class getSundayClassesAsyncTask
            extends AsyncTask<Void,Void,LiveData<List<AddClassSession>>>{

        SessionDao sessionDao;

        getSundayClassesAsyncTask(SessionDao dao){
            sessionDao = dao;
        }
        @Override
        protected LiveData<List<AddClassSession>> doInBackground(Void... voids) {
            sundayList = sessionDao.getSundaySessionOrderedByTime();
            return sundayList;
        }
    }

    private static class getMondayClassesAsyncTask
            extends AsyncTask<Void,Void,LiveData<List<AddClassSession>>>{

        SessionDao sessionDao;

        getMondayClassesAsyncTask(SessionDao dao){
            sessionDao = dao;
        }
        @Override
        protected LiveData<List<AddClassSession>> doInBackground(Void... voids) {
            mondayList = sessionDao.getMondaySessionOrderedByTime();
            return mondayList;
        }
    }

    private static class getTuesdayClassesAsyncTask
            extends AsyncTask<Void,Void,LiveData<List<AddClassSession>>>{

        SessionDao sessionDao;

        getTuesdayClassesAsyncTask(SessionDao dao){
            sessionDao = dao;
        }
        @Override
        protected LiveData<List<AddClassSession>> doInBackground(Void... voids) {
            tuesdayList = sessionDao.getTuesdaySessionOrderedByTime();
            return tuesdayList;
        }
    }

    private static class getWednesdayClassesAsyncTask
            extends AsyncTask<Void,Void,LiveData<List<AddClassSession>>>{

        SessionDao sessionDao;

        getWednesdayClassesAsyncTask(SessionDao dao){
            sessionDao = dao;
        }
        @Override
        protected LiveData<List<AddClassSession>> doInBackground(Void... voids) {
            wednesdayList = sessionDao.getWednesdaySessionOrderedByTime();
            return wednesdayList;
        }
    }

    private static class getThursdayClassesAsyncTask
            extends AsyncTask<Void,Void,LiveData<List<AddClassSession>>>{

        SessionDao sessionDao;

        getThursdayClassesAsyncTask(SessionDao dao){
            sessionDao = dao;
        }
        @Override
        protected LiveData<List<AddClassSession>> doInBackground(Void... voids) {
            thursdayList = sessionDao.getThursdaySessionOrderedByTime();
            return thursdayList;
        }
    }


    private static class getFridayClassesAsyncTask
            extends AsyncTask<Void,Void,LiveData<List<AddClassSession>>>{

        SessionDao sessionDao;

        getFridayClassesAsyncTask(SessionDao dao){
            sessionDao = dao;
        }
        @Override
        protected LiveData<List<AddClassSession>> doInBackground(Void... voids) {
            fridayList = sessionDao.getFridaySessionOrderedByTime();
            return fridayList;
        }
    }

    private static class getSaturdayClassesAsyncTask
            extends AsyncTask<Void,Void,LiveData<List<AddClassSession>>>{

        SessionDao sessionDao;

        getSaturdayClassesAsyncTask(SessionDao dao){
            sessionDao = dao;
        }
        @Override
        protected LiveData<List<AddClassSession>> doInBackground(Void... voids) {
            saturdayList = sessionDao.getSaturdaySessionOrderedByTime();
            return saturdayList;
        }
    }


    public static  class getSpecifiedClassAsyncTask
            extends AsyncTask<String,Void,LiveData<AddClassSession>>{

        SessionDao sessionDao;


        getSpecifiedClassAsyncTask(SessionDao dao){
            this.sessionDao = dao;
        }
        @Override
        protected LiveData<AddClassSession> doInBackground(String... strings) {
          singleAddClassSession = sessionDao.getSpecifiedSessionClass(strings[0]);

          return singleAddClassSession;
        }


        @Override
        protected void onPostExecute(LiveData<AddClassSession> addClassSessionLiveData) {
            super.onPostExecute(addClassSessionLiveData);
            checkClassnameListener.checkClassname(addClassSessionLiveData);


        }
    }
   public  static void  setCheckClassnameListener(CheckClassnameListener listener){
        checkClassnameListener = listener;
    }



}
