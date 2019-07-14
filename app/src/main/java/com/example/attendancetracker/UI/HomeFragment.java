package com.example.attendancetracker.UI;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.SavedStateVMFactory;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.attendancetracker.AddClassSession;
import com.example.attendancetracker.HandleMenuDropDownListener;
import com.example.attendancetracker.R;
import com.example.attendancetracker.Repository.SessionClassRepository.SessionModelRepository;
import com.example.attendancetracker.Repository.SessionClassRepository.SessionViewModel;
import com.example.attendancetracker.Util.MyUtil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;


public class HomeFragment extends Fragment {

    @BindView(R.id.backDrop_foreground)
    public ConstraintLayout constraintLayout;

    @BindView(R.id.menuListLinearLayout)
    public LinearLayout menuListContainer;

    @BindView(R.id.todayTextView)
    public TextView mTodayTextView;

    @BindView(R.id.classesTextView)
    public TextView mClassesTextView;

    @BindView(R.id.profileTextView)
    public TextView mProfileTextView;

    @BindView(R.id.historyTextView)
    public TextView mHistoryTextVIew;

    @BindView(R.id.settingTextView)
    public TextView mSettingTextView;

    @BindView(R.id.addNewClassFab)
    public FloatingActionButton mAddNewClassFloatingActionButton;

    private MainMenuListeners mMainMenuListeners;

    @BindView(R.id.mainActivityToolbar)
    Toolbar toolbar;

    @BindView(R.id.classNameUpNext)
    TextView mClassnameUpNext;

    @BindView(R.id.locationUpNext)
    TextView mLocationUpNext;

    @BindView(R.id.classDurationUpNext)
    TextView mDurationUpNext;

    @BindView(R.id.upComingClassName)
    TextView mUpComingClassName;

    @BindView(R.id.upComingClassRoomName)
    TextView mUpComingLocation;

    @BindView(R.id.upComingClassDuration)
    TextView mUpComingDuration;

    @BindView(R.id.upComingClassName2)
    TextView mUpComingClassname2;

    @BindView(R.id.upComingClassRoomName2)
    TextView mUpcomingLocation2;

    @BindView(R.id.upComingClassDuration2)
    TextView mUpComingDuration2;

    Calendar calendar;





    HandleMenuDropDownListener handleMenuDropDownListener;


    HandleMenuDropDownListener.BackdropListener backdropListener;

    SessionViewModel sessionViewModel;

    List<AddClassSession> mTodaySessionClasses, sortedTodayClasses;

    AddClassSession firstSession, secondSession,thirdSession;

    public HomeFragment() {
        // Required empty public constructor
    }


    private LiveData<List<AddClassSession>> addClassSessionLiveData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v("resume","on  create called");
    }



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        ButterKnife.bind(this, view);

        mAddNewClassFloatingActionButton.setColorFilter(
                getResources().getColor(R.color.light_cyan));


        mTodayTextView.setVisibility(View.GONE);
        mClassesTextView.setVisibility(View.GONE);
        mHistoryTextVIew.setVisibility(View.GONE);
        mSettingTextView.setVisibility(View.GONE);
        mProfileTextView.setVisibility(View.GONE);

        if (toolbar != null) {
            toolbar.setTitle("");
            ((AppCompatActivity) Objects.requireNonNull(getActivity())).
                    setSupportActionBar(toolbar);

            handleMenuDropDownListener = new HandleMenuDropDownListener(getContext(), constraintLayout,
                    Objects.requireNonNull(getContext()).getDrawable(R.drawable.ic_openedmenusvg),
                    getContext().getDrawable(R.drawable.ic_closed_menu),
                    new AccelerateDecelerateInterpolator(), new OvershootInterpolator(),
                    menuListContainer, mAddNewClassFloatingActionButton);

            toolbar.setNavigationOnClickListener(handleMenuDropDownListener);
        }

        mClassesTextView.setOnClickListener((View v) -> {
            enableActiveButton(mClassesTextView);
            mMainMenuListeners.goToClasses();
        });

        mHistoryTextVIew.setOnClickListener((View v) -> {
            enableActiveButton(mHistoryTextVIew);
            mMainMenuListeners.goToHistory();
        });

        mSettingTextView.setOnClickListener((View v) -> {
            enableActiveButton(mSettingTextView);
            mMainMenuListeners.goToSetting();
        });

        mProfileTextView.setOnClickListener((View v) -> {
            enableActiveButton(mProfileTextView);
            mMainMenuListeners.goToProfile();
        });

        mAddNewClassFloatingActionButton.setOnClickListener((View v) -> {
            mMainMenuListeners.goToAddNewClass();
        });

        mTodayTextView.setOnClickListener(v -> {
            handleMenuDropDownListener.performAnimation();

        });

        HandleMenuDropDownListener.setBackdropListener(
                isDown -> {
                    if (isDown) {

                        mTodayTextView.setVisibility(View.VISIBLE);
                        mClassesTextView.setVisibility(View.VISIBLE);
                        mHistoryTextVIew.setVisibility(View.VISIBLE);
                        mSettingTextView.setVisibility(View.VISIBLE);
                        mProfileTextView.setVisibility(View.VISIBLE);

                    } else {

                        mTodayTextView.setVisibility(View.GONE);
                        mClassesTextView.setVisibility(View.GONE);
                        mHistoryTextVIew.setVisibility(View.GONE);
                        mSettingTextView.setVisibility(View.GONE);
                        mProfileTextView.setVisibility(View.GONE);
                    }
                });

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof MainMenuListeners) {
            mMainMenuListeners = (MainMenuListeners) context;
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        sessionViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity()),
                new SavedStateVMFactory(getActivity())).
                get(SessionViewModel.class);
    }

    private void setUpClasses(Calendar calendar) {
        sortedTodayClasses = new ArrayList<>();

        int hourOfTheDay = calendar.get(Calendar.HOUR_OF_DAY);

        int minutesOfTheDay = calendar.get(Calendar.MINUTE);

        String rawTimeString =  hourOfTheDay + ":" +
                ((minutesOfTheDay / 10 < 1) ? "0" + minutesOfTheDay :
                        String.valueOf(minutesOfTheDay)) + ":"+"00";


        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDateNowTime = new SimpleDateFormat("hh:mm:ss");

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDateClassStartTIme = new SimpleDateFormat("hh:mm:ss");


        if (mTodaySessionClasses != null){
            for (int i = 0 ; mTodaySessionClasses.size() > i; i++){

                try {
                    Date nowTime = simpleDateNowTime.parse(rawTimeString);
                    Date sessionStartTime = simpleDateClassStartTIme.parse(
                            mTodaySessionClasses.get(i).getRawTime());

                    if (nowTime.before(sessionStartTime)) {

                        sortedTodayClasses.add(mTodaySessionClasses.get(i));

                    }
                    if (nowTime.equals(sessionStartTime)) {
                        sortedTodayClasses.add(mTodaySessionClasses.get(i));
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }

        }

        if (sortedTodayClasses.size() >  0){
                String durationTime = sortedTodayClasses.get(0).getStartTimeString() + "- "
                        +sortedTodayClasses.get(0).getEndTimeString();

                mClassnameUpNext.setText(sortedTodayClasses.get(0).getClassname());
                mLocationUpNext.setText(sortedTodayClasses.get(0).getLocation());
                mDurationUpNext.setText(durationTime);
        }

        if (sortedTodayClasses.size() >  1){

            String durationTime = sortedTodayClasses.get(1).getStartTimeString() + "- "
                    +sortedTodayClasses.get(1).getEndTimeString();

            mUpComingClassName.setText(sortedTodayClasses.get(1).getClassname());
            mUpComingLocation.setText(sortedTodayClasses.get(1).getLocation());
            mUpComingDuration.setText(durationTime);

        }

        if (sortedTodayClasses.size() >  2){

            String durationTime = sortedTodayClasses.get(2).getStartTimeString() + "- "
                    +sortedTodayClasses.get(2).getEndTimeString();
            mUpComingClassname2.setText(sortedTodayClasses.get(2).getClassname());
            mUpcomingLocation2.setText(sortedTodayClasses.get(2).getLocation());
            mUpComingDuration2.setText(durationTime);
        }
    }

    private void enableActiveButton(TextView backgroundChange) {
        backgroundChange.setBackground(getResources()
                .getDrawable(R.drawable.menu_text_background_shape));
        mTodayTextView.setBackground(null);

    }


    @Override
    public void onResume() {
        super.onResume();
        String today;
        Log.v("resume","on resume called");
        SessionModelRepository.setTodayClassSessionListener(listLiveData ->
                listLiveData.observe(getViewLifecycleOwner(), addClassSessionList -> {
                    mTodaySessionClasses = addClassSessionList;
                    setUpClasses(calendar);
                    Log.v("resume","listener called");
                }));

        calendar = Calendar.getInstance();

        int dayOfTheWeek = calendar.get(Calendar.DAY_OF_WEEK);

        switch (dayOfTheWeek) {
            case MyUtil.SUNDAY:
                if (sessionViewModel.getSundayScheduledClass()!= null){
                    sessionViewModel.getSundayScheduledClass().observe(
                            getViewLifecycleOwner(),
                            addClassSessionList -> {

                                setUpClasses(calendar);
                            });
                }
                return;

            case MyUtil.MONDAY:
                if (sessionViewModel.getMondayScheduledClass() != null){
                    sessionViewModel.getMondayScheduledClass().
                            observe(getViewLifecycleOwner(),
                                    addClassSessionList -> {
                                        mTodaySessionClasses = addClassSessionList;
                                        setUpClasses(calendar);

                                    });
                }

                return;

            case MyUtil.TUESDAY:
                if (sessionViewModel.getTuesdayScheduledClass() != null){
                    sessionViewModel.getTuesdayScheduledClass().observe(
                            getViewLifecycleOwner(),
                            addClassSessionList -> {
                                setUpClasses(calendar);

                            });
                }

                return;

            case MyUtil.WEDNESDAY:
                if (sessionViewModel.getWednesdayScheduledClass() != null){
                    sessionViewModel.getWednesdayScheduledClass().observe(
                            getViewLifecycleOwner(),
                            addClassSessionList -> {
                                mTodaySessionClasses = addClassSessionList;
                                setUpClasses(calendar);

                            });
                }

                return;

            case MyUtil.THURSDAY:
                if (sessionViewModel.getThursdayScheduledClass() != null){
                    sessionViewModel.getThursdayScheduledClass().observe(
                            getViewLifecycleOwner(),
                            addClassSessionList -> {
//                                mTodaySessionClasses = addClassSessionList;
                                setUpClasses(calendar);
                            });
                }
                return;

            case MyUtil.FRIDAY:
                if(sessionViewModel.getFridayScheduledClass() != null){
                    sessionViewModel.getFridayScheduledClass().observe(
                            getViewLifecycleOwner(),
                            addClassSessionList -> {
                            mTodaySessionClasses = addClassSessionList;
                                setUpClasses(calendar);

                            });
                }

                return;

            case MyUtil.SATURDAY:
                if (sessionViewModel.getSaturdayScheduledClass()!=null){
                    sessionViewModel.getSaturdayScheduledClass().
                            observe(getViewLifecycleOwner(),
                                    addClassSessionList -> {
                                        mTodaySessionClasses = addClassSessionList;
                                        setUpClasses(calendar);

                                    });
                }
        }
    }
}
