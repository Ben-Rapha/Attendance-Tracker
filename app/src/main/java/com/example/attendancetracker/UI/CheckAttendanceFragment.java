package com.example.attendancetracker.UI;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.SavedStateVMFactory;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.attendancetracker.Adapters.CheckAttendanceAdapter;
import com.example.attendancetracker.Adapters.StudentsAdapter;
import com.example.attendancetracker.AddClassSession;
import com.example.attendancetracker.Model.History;
import com.example.attendancetracker.Model.Students;
import com.example.attendancetracker.R;
import com.example.attendancetracker.Repository.HistoryRepository.HistoryViewModel;
import com.example.attendancetracker.Repository.SessionClassRepository.SessionViewModel;

import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

import static androidx.navigation.Navigation.findNavController;

/**
 * A simple {@link Fragment} subclass.
 */
public class CheckAttendanceFragment extends Fragment {

    private MainMenuListeners mMainMenuListeners;

    private CheckAttendanceAdapter checkAttendanceAdapter;

    private LinearLayoutManager mLinearLayoutManager;

    @BindView(R.id.checkAttendanceRecyclerView)
    RecyclerView checkAttendanceRecyclerView;

    @BindView(R.id.checkAttendanceToolbar)
    Toolbar toolbar;

    @BindView(R.id.done_Button)
    TextView doneButtonTextView;

    SessionViewModel sessionViewModel;

    HistoryViewModel historyViewModel;

    private List<Students> studentsList, studentsListFromAdapter;

    AddClassSession addClassSession;


    public CheckAttendanceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.check_attendance_fragment,
                container, false);

        ButterKnife.bind(this,view);

        if (toolbar != null){
            ((AppCompatActivity) (Objects.requireNonNull(getActivity()))).
                    setSupportActionBar(toolbar);
            toolbar.setTitle(null);
            toolbar.setTitleTextColor(getResources().getColor(R.color.copperGold));

            toolbar.setNavigationOnClickListener((View v) ->{
                final NavController navController = Navigation.findNavController(view);
                navController.popBackStack();
            });

        }

        studentsList = new ArrayList<>();

        mLinearLayoutManager = new LinearLayoutManager(getContext());
        checkAttendanceRecyclerView.setLayoutManager(mLinearLayoutManager);
       checkAttendanceRecyclerView.setHasFixedSize(true);

       checkAttendanceRecyclerView.addItemDecoration
                (new DividerItemDecoration(Objects.requireNonNull(getContext())
                        ,DividerItemDecoration.VERTICAL));

        checkAttendanceAdapter = new CheckAttendanceAdapter(getContext(),studentsList);

        checkAttendanceRecyclerView.setAdapter(checkAttendanceAdapter);

        doneButtonTextView.setOnClickListener(v -> {
          studentsListFromAdapter  = checkAttendanceAdapter.getHistoryList();
            History history = new History(addClassSession,getTime(),getDate());
          historyViewModel.insertHistoryIntoDatabase(history);
            findNavController(Objects.requireNonNull(getActivity()),
                    R.id.mainActivityNavigationHost)
                    .navigate(R.id.home2);
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        sessionViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity()),
                new SavedStateVMFactory(getActivity())).
                get(SessionViewModel.class);

        historyViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity()),
                new SavedStateVMFactory(getActivity())).
                get(HistoryViewModel.class);

        sessionViewModel.getAddClassSessionData().observe(getViewLifecycleOwner(), this::setData);




    }

    private  void setData(AddClassSession addClassSession){
        checkAttendanceAdapter.setStudentsList(addClassSession.getStudents());
        checkAttendanceAdapter.setAddClassSession(addClassSession);
        this.addClassSession = addClassSession;


    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof MainMenuListeners){
            mMainMenuListeners = (MainMenuListeners) context;
        }
    }


    private Time getTime(){

        Calendar calendar = Calendar.getInstance();

        int hour = calendar.get(Calendar.HOUR_OF_DAY);

        int minute = calendar.get(Calendar.MINUTE);

        int seconds = calendar.get(Calendar.SECOND);

        String rawTimeString =  hour + ":" +
                ((minute / 10 < 1) ? "0" + minute :
                        String.valueOf(minute)) + ":"+ seconds;

        return getFormattedTime(rawTimeString);
    }

    private Date getDate(){

        Calendar dateCalender = Calendar.getInstance();

        int day = dateCalender.get(Calendar.DAY_OF_MONTH);

        int month = dateCalender.get(Calendar.MONTH);

        int year = dateCalender.get(Calendar.YEAR);

        String sqlDateFormat  = (year+"-"+(((month) /10 < 1)?"0"+(month+1):String.valueOf(month+1))+
                "-"+ ((day /10 < 1)? "0"+day:String.valueOf
                (day)));

        return getFormattedSqlDate(sqlDateFormat);
    }


    private java.sql.Date getFormattedSqlDate(String date) {

        java.sql.Date sqlFormattedDate;

        sqlFormattedDate = java.sql.Date.valueOf(date);

        Log.v("dateFormatted", sqlFormattedDate.toString());
        return sqlFormattedDate;
    }


    private Time getFormattedTime(String time) {
        Time formattedTime;
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm:ss");

        try {
            java.util.Date date = simpleDateFormat.parse(time);
            Calendar timeCalender = Calendar.getInstance();
            timeCalender.setTime(date);
            formattedTime = Time.valueOf(time);
            Log.v("timeFormatted", formattedTime.toString());
            return formattedTime;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }



}
