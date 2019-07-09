package com.example.attendancetracker.UI;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.SavedStateVMFactory;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.attendancetracker.Adapters.HistoryDetailAdapter;
import com.example.attendancetracker.Adapters.StudentsAdapter;
import com.example.attendancetracker.Model.History;
import com.example.attendancetracker.Model.Students;
import com.example.attendancetracker.R;
import com.example.attendancetracker.Repository.HistoryRepository.HistoryViewModel;
import com.example.attendancetracker.Repository.SessionClassRepository.SessionViewModel;
import com.example.attendancetracker.Util.MyUtil;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryDetailFragment extends Fragment {

    @BindView(R.id.studentHistoryRecyclerView)
    RecyclerView studentRecyclerView;

    @BindView(R.id.historyDetailClassName)
    TextView mClassname;

    @BindView(R.id.location)
    TextView mLocation;

    @BindView(R.id.time)
    TextView mTime;

    @BindView(R.id.date)
    TextView textViewDate;

    @BindView(R.id.dateTakenActual)
    TextView dateTaken;

    @BindView(R.id.timeTakenActual)
    TextView timeTaken;

    @BindView(R.id.sun)
    TextView sun;

    @BindView(R.id.mon)
    TextView mon;

    @BindView(R.id.tue)
    TextView tue;

    @BindView(R.id.wed)
    TextView wed;

    @BindView(R.id.thu)
    TextView thur;

    @BindView(R.id.fri)
    TextView fri;

    @BindView(R.id.sat)
    TextView sat;

    List<Students> studentsList;

    LinearLayoutManager mLinearLayoutManager;


    private HistoryDetailAdapter historyDetailAdapter;

    private History history;

    HistoryViewModel historyViewModel;


    @BindView(R.id.historyDetailToolbar)
    Toolbar toolbar;

    @BindView(R.id.historyDetailCollapsingToolbarLayout)
    CollapsingToolbarLayout collapsingToolbarLayout;

    MainMenuListeners mainMenuListeners;

    private Drawable background, colorTransparent;

    public HistoryDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.history_detail, container, false);

        ButterKnife.bind(this, view);
        background = view.getResources().getDrawable(R.drawable.ic_my_circle);
        colorTransparent = view.getResources().getDrawable(R.drawable.ic_mycircle_white);

        mLinearLayoutManager = new LinearLayoutManager(getContext());

        studentRecyclerView.setLayoutManager(mLinearLayoutManager);

        studentRecyclerView.setHasFixedSize(true);

        studentRecyclerView.addItemDecoration
                (new DividerItemDecoration(Objects.requireNonNull(getContext())
                        , DividerItemDecoration.VERTICAL));

        historyDetailAdapter = new HistoryDetailAdapter(getContext(), history);

        studentRecyclerView.setAdapter(historyDetailAdapter);

        if (toolbar != null) {
            ((AppCompatActivity) (Objects.requireNonNull(getActivity()))).
                    setSupportActionBar(toolbar);
            toolbar.setTitle(null);
            toolbar.setTitleTextColor(getResources().getColor(R.color.copperGold));

            toolbar.setNavigationOnClickListener((View v) -> {
                mainMenuListeners.goToHome();
            });

            collapsingToolbarLayout.setTitle("HistoryDetail");
            collapsingToolbarLayout.setCollapsedTitleGravity(Gravity.NO_GRAVITY);
            collapsingToolbarLayout.setExpandedTitleColor(
                    getResources().getColor(R.color.light_cyan));
            collapsingToolbarLayout.setCollapsedTitleTextColor(
                    getResources().getColor(R.color.copperGold));
            collapsingToolbarLayout.setExpandedTitleMarginBottom(32);
            collapsingToolbarLayout.setExpandedTitleMarginTop(0);
            collapsingToolbarLayout.setExpandedTitleMarginStart(32);

        }

        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        historyViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity()),
                new SavedStateVMFactory(getActivity())).
                get(HistoryViewModel.class);

        historyViewModel.getHistoryMutableLiveData().
                observe(getViewLifecycleOwner(), history -> {
                    if (history != null) {
                        setData(history);
                        historyDetailAdapter.updateHistoryAdapter(history);
                    }

                });

    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof MainMenuListeners) {
            mainMenuListeners = (MainMenuListeners) context;
        }
    }

    private void setData(History history) {

        String startEndTimeFormattedString = history.getClassSession().getStartTimeString() + " - " +
                history.getClassSession().getEndTimeString();

        String startEndDateString = history.getClassSession().getFormattedDateStart() + " - "
                + history.getClassSession().getFormattedDateEnd();

        String timeTakenString = MyUtil.getFormattedTime(history.getTimeTaken());

        String dateTakenString = MyUtil.getFormattedDate(history.getDateTaken());

        mClassname.setText(history.getClassSession().getClassname());

        mLocation.setText(history.getClassSession().getLocation());

        mTime.setText(startEndTimeFormattedString);

        textViewDate.setText(startEndDateString);

        dateTaken.setText(dateTakenString);

        timeTaken.setText(timeTakenString);

        sun.setBackground(history.getClassSession().getSunday() != null ? background : colorTransparent);
        mon.setBackground(history.getClassSession().getMonday() != null ? background : colorTransparent);
        tue.setBackground(history.getClassSession().getTuesday() != null ? background : colorTransparent);
        wed.setBackground(history.getClassSession().getWednesday() != null ? background : colorTransparent);
        thur.setBackground(history.getClassSession().getThursday() != null ? background : colorTransparent);
        fri.setBackground(history.getClassSession().getFriday() != null ? background : colorTransparent);
        sat.setBackground(history.getClassSession().getSaturday() != null ? background : colorTransparent);
    }

}
