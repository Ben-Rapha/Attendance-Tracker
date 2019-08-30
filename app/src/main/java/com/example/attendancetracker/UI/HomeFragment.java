package com.example.attendancetracker.UI;


import android.annotation.SuppressLint;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.SavedStateViewModelFactory;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.PersistableBundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.attendancetracker.AddClassSession;
import com.example.attendancetracker.HandleMenuDropDownListener;
import com.example.attendancetracker.R;
import com.example.attendancetracker.Repository.SessionClassRepository.SessionModelRepository;
import com.example.attendancetracker.Repository.SessionClassRepository.SessionViewModel;
import com.example.attendancetracker.Services.AlertNotificationJobService;
import com.example.attendancetracker.UI.Dialogs.QuitAppDialog;
import com.example.attendancetracker.Util.MyUtil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.switchmaterial.SwitchMaterial;

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

    @BindView(R.id.upNextConstraint)
    ConstraintLayout upNextConstraintLayout;


    @BindView(R.id.upComingConstraint)
    ConstraintLayout upComingConstraintLayout;

    @BindView(R.id.upComingConstraint2)
    ConstraintLayout upComingConstraint2;

    @BindView(R.id.divider)
    View dividerView;

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
    public FloatingActionButton mAddNewClassSessionFloatingActionButton;

    private MainMenuListeners mMainMenuListeners;

    @BindView(R.id.mainActivityToolbar)
    Toolbar mHomeToolbar;

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


    @BindView(R.id.empty_schedule)
    TextView emptyScheduleTextView;

    @BindView(R.id.upNext)
    TextView mUpNextLabelTextView;

    @BindView(R.id.upComingClass)
    TextView upComingClassLabelTextView;


    @BindView(R.id.notifyMe)
    SwitchMaterial notifyMeSwitchButton;

    private Calendar calendar;

    private JobScheduler schedulerNotification;


    private AddClassSession addClassSessionNotify;


    private HandleMenuDropDownListener handleMenuDropDownListener;


    HandleMenuDropDownListener.BackdropListener backdropListener;

    SessionViewModel sessionViewModel;

    private List<AddClassSession> mTodayClassSessionList, mSortedTodayClassSessionList;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.v("berry", "onCreated called");
        View view = inflater.inflate(R.layout.home_fragment, container, false);

        ButterKnife.bind(this, view);


        setUpHomeToolbar();

        //this makes menu buttons not responsive to clicks when not needed
        clearMenuListVisibility();


        //change FAB background color
        mAddNewClassSessionFloatingActionButton.setColorFilter(
                getResources().getColor(R.color.light_cyan));

        mTodayClassSessionList = new ArrayList<>();


        //set up menu buttons listeners to navigate to the right destination
        setUpMenuListListeners();

        // listener to control when menu buttons to be responsive to user clicks
        setUpBackDropListener();


        // button control by user to alert them when a session starts
        setNotifyMeSwitchButton();


        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

                QuitAppDialog quitAppDialog = new QuitAppDialog();

                if (getFragmentManager() != null) {
                    quitAppDialog.show(getFragmentManager(), "quitApp");

                    quitAppDialog.setQuitAppListener(new QuitAppDialog.QuitAppListener() {
                        @Override
                        public void quitAttendanceTracker() {
                            Objects.requireNonNull(getActivity()).finish();
                        }
                    });
                }


            }
        };

        requireActivity().getOnBackPressedDispatcher().
                addCallback(getViewLifecycleOwner(), callback);

        return view;
    }

    private void setUpBackDropListener() {
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
    }

    private void setUpMenuListListeners() {
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
            constraintLayout.setVisibility(View.INVISIBLE);
            handleMenuDropDownListener.performAnimation();

        });

        mProfileTextView.setOnClickListener((View v) -> {
            enableActiveButton(mProfileTextView);
            mMainMenuListeners.goToProfile();
        });

        mAddNewClassSessionFloatingActionButton.setOnClickListener((View v) -> {
            mMainMenuListeners.goToAddNewClass();
        });

        mTodayTextView.setOnClickListener(v -> {
            handleMenuDropDownListener.performAnimation();

        });
    }

    private void setUpHomeToolbar() {
        if (mHomeToolbar != null) {
            mHomeToolbar.setTitle("");
            ((AppCompatActivity) Objects.requireNonNull(getActivity())).
                    setSupportActionBar(mHomeToolbar);

            handleMenuDropDownListener = new HandleMenuDropDownListener(getContext(), constraintLayout,
                    Objects.requireNonNull(getContext()).getDrawable(R.drawable.ic_openedmenusvg),
                    getContext().getDrawable(R.drawable.ic_closed_menu),
                    new AccelerateDecelerateInterpolator(), new OvershootInterpolator(),
                    menuListContainer, mAddNewClassSessionFloatingActionButton);

            mHomeToolbar.setNavigationOnClickListener(handleMenuDropDownListener);
        }
    }

    private void clearMenuListVisibility() {
        mTodayTextView.setVisibility(View.GONE);
        mClassesTextView.setVisibility(View.GONE);
        mHistoryTextVIew.setVisibility(View.GONE);
        mSettingTextView.setVisibility(View.GONE);
        mProfileTextView.setVisibility(View.GONE);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof MainMenuListeners) {
            mMainMenuListeners = (MainMenuListeners) context;
        }

        Log.v("berry", "onAttach called");
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.v("berry", "onActivity created called");
//        sessionViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity()),
//                new SavedStateVMFactory(getActivity())).
//                get(SessionViewModel.class);

        sessionViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).
                get(SessionViewModel.class);
    }

    private void setUpClasses(Calendar calendar) {

        mSortedTodayClassSessionList = new ArrayList<>();

        int hourOfTheDay = calendar.get(Calendar.HOUR_OF_DAY);

        int minutesOfTheDay = calendar.get(Calendar.MINUTE);


        String timeOfTheDay = hourOfTheDay + ":" +
                ((minutesOfTheDay / 10 < 1) ? "0" + minutesOfTheDay :
                        String.valueOf(minutesOfTheDay)) + ":" + "00";


        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDateNowTime = new SimpleDateFormat("HH:mm:ss");


        //sort mTodayClassSessionList according to starting time
        sortSessionAccordingToStartingTime(timeOfTheDay, simpleDateNowTime);

        //shows available sessions
        displayUpComingSessions();
    }

    private void displayUpComingSessions() {
        if (mSortedTodayClassSessionList.size() > 0) {
            mUpNextLabelTextView.setVisibility(View.VISIBLE);
            upNextConstraintLayout.setVisibility(View.VISIBLE);
            emptyScheduleTextView.setVisibility(View.GONE);
            addClassSessionNotify = mSortedTodayClassSessionList.get(0);
            String durationTime = mSortedTodayClassSessionList.get(0).getStartTimeString() + "- "
                    + mSortedTodayClassSessionList.get(0).getEndTimeString();

            mClassnameUpNext.setText(mSortedTodayClassSessionList.get(0).getClassname());
            mLocationUpNext.setText(mSortedTodayClassSessionList.get(0).getLocation());
            mDurationUpNext.setText(durationTime);
        } else {

            mUpNextLabelTextView.setVisibility(View.GONE);
            upNextConstraintLayout.setVisibility(View.GONE);
        }

        if (mSortedTodayClassSessionList.size() > 1) {

            dividerView.setVisibility(View.VISIBLE);

            upComingClassLabelTextView.setVisibility(View.VISIBLE);

            upComingConstraintLayout.setVisibility(View.VISIBLE);

            String durationTime = mSortedTodayClassSessionList.get(1).getStartTimeString() + "- "
                    + mSortedTodayClassSessionList.get(1).getEndTimeString();

            mUpComingClassName.setText(mSortedTodayClassSessionList.get(1).getClassname());
            mUpComingLocation.setText(mSortedTodayClassSessionList.get(1).getLocation());
            mUpComingDuration.setText(durationTime);

        } else {
            dividerView.setVisibility(View.GONE);

            upComingClassLabelTextView.setVisibility(View.GONE);

            upComingConstraintLayout.setVisibility(View.GONE);
        }

        if (mSortedTodayClassSessionList.size() > 2) {

            upComingConstraint2.setVisibility(View.VISIBLE);

            String durationTime = mSortedTodayClassSessionList.get(2).getStartTimeString() + "- "
                    + mSortedTodayClassSessionList.get(2).getEndTimeString();
            mUpComingClassname2.setText(mSortedTodayClassSessionList.get(2).getClassname());
            mUpcomingLocation2.setText(mSortedTodayClassSessionList.get(2).getLocation());
            mUpComingDuration2.setText(durationTime);
        } else {

            upComingConstraint2.setVisibility(View.GONE);
        }
    }

    private void sortSessionAccordingToStartingTime(String timeOfTheDay,
                                                    SimpleDateFormat simpleDateNowTime) {
        if (mTodayClassSessionList != null) {
            for (int i = 0; mTodayClassSessionList.size() > i; i++) {

                try {
                    Date nowTime = simpleDateNowTime.parse(timeOfTheDay);
                    Date sessionStartTime = simpleDateNowTime.parse(
                            mTodayClassSessionList.get(i).getRawTime());

                    if (nowTime.before(sessionStartTime)) {

                        if (!(mSortedTodayClassSessionList.isEmpty())) {
                            for (int j = 0; mSortedTodayClassSessionList.size() > j; j++) {
                                if (!(mTodayClassSessionList.get(i).getClassname().
                                        equals(mSortedTodayClassSessionList.get(j).getClassname()))) {
                                    mSortedTodayClassSessionList.add(mTodayClassSessionList.get(i));
                                }
                            }
                        } else {
                            mSortedTodayClassSessionList.add(mTodayClassSessionList.get(i));
                        }

                    }
                    if (nowTime.equals(sessionStartTime)) {

                        if (!(mSortedTodayClassSessionList.isEmpty())) {
                            for (int s = 0; mSortedTodayClassSessionList.size() > s; s++) {
                                if (!(mTodayClassSessionList.get(i).getClassname().
                                        equals(mSortedTodayClassSessionList.get(s).getClassname()))) {
                                    mSortedTodayClassSessionList.add(mTodayClassSessionList.get(i));
                                }
                            }
                        } else {
                            mSortedTodayClassSessionList.add(mTodayClassSessionList.get(i));
                        }

                    }


                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }

        }
    }

    //make today button background null in order for order menu buttons to give a responsive touch
    private void enableActiveButton(TextView backgroundChange) {
        backgroundChange.setBackground(getResources()
                .getDrawable(R.drawable.menu_text_background_shape));
        mTodayTextView.setBackground(null);

    }

    private void resetBackground(){
        mSettingTextView.setBackground(null);
        mTodayTextView.setBackground(getResources()
                .getDrawable(R.drawable.menu_text_background_shape));
    }


    @Override
    public void onResume() {
        super.onResume();

        constraintLayout.setVisibility(View.VISIBLE);
        resetBackground();

        calendar = Calendar.getInstance();
        int dayOfTheWeek = calendar.get(Calendar.DAY_OF_WEEK);


        SessionModelRepository.setTodayClassSessionListener(new TodayClassSessionListener() {
            @Override
            public void todayClassSession(LiveData<List<AddClassSession>> listLiveData) {

                listLiveData.observe(getViewLifecycleOwner(),
                        new Observer<List<AddClassSession>>() {
                            @Override
                            public void onChanged(List<AddClassSession> addClassSessionList) {

                                mTodayClassSessionList = addClassSessionList;
                                setUpClasses(calendar);
                            }
                        });
            }

            @Override
            public void deletedSession(String classname) {
                if (mTodayClassSessionList != null) {
                    for (int i = mTodayClassSessionList.size() - 1; i >= 0; i--) {
                        if (classname.equals(mTodayClassSessionList.get(i).getClassname())) {
                            mTodayClassSessionList.remove(i);
                            setUpClasses(calendar);
                        }
                    }
                }
            }
        });

        //get the right session according to the day of the week
        getTodaySessionFromDatabase(dayOfTheWeek);
    }

    private void getTodaySessionFromDatabase(int dayOfTheWeek) {
        switch (dayOfTheWeek) {
            case MyUtil.SUNDAY:
                if (sessionViewModel.getSundayScheduledClass() != null) {
                    sessionViewModel.getSundayScheduledClass().observe(
                            getViewLifecycleOwner(),
                            addClassSessionList -> {
                                setUpClasses(calendar);
                            });
                }
                return;

            case MyUtil.MONDAY:
                if (sessionViewModel.getMondayScheduledClass() != null) {
                    sessionViewModel.getMondayScheduledClass().
                            observe(getViewLifecycleOwner(),
                                    new Observer<List<AddClassSession>>() {
                                        @Override
                                        public void onChanged(List<AddClassSession> addClassSessionList) {
                                            mTodayClassSessionList = addClassSessionList;
                                            HomeFragment.this.setUpClasses(calendar);

                                        }
                                    });
                }

                return;

            case MyUtil.TUESDAY:
                if (sessionViewModel.getTuesdayScheduledClass() != null) {
                    sessionViewModel.getTuesdayScheduledClass().observe(
                            getViewLifecycleOwner(),
                            addClassSessionList -> {
                                setUpClasses(calendar);

                            });
                }

                return;

            case MyUtil.WEDNESDAY:
                if (sessionViewModel.getWednesdayScheduledClass() != null) {
                    sessionViewModel.getWednesdayScheduledClass().observe(
                            getViewLifecycleOwner(),
                            addClassSessionList -> {
                                mTodayClassSessionList = addClassSessionList;
                                setUpClasses(calendar);

                            });
                }

                return;

            case MyUtil.THURSDAY:
                if (sessionViewModel.getThursdayScheduledClass() != null) {
                    sessionViewModel.getThursdayScheduledClass().observe(
                            getViewLifecycleOwner(),
                            addClassSessionList -> {
                                setUpClasses(calendar);
                            });
                }
                return;

            case MyUtil.FRIDAY:
                if (sessionViewModel.getFridayScheduledClass() != null) {
                    sessionViewModel.getFridayScheduledClass().observe(
                            getViewLifecycleOwner(),
                            addClassSessionList -> {
                                mTodayClassSessionList = addClassSessionList;
                                setUpClasses(calendar);

                            });
                }

                return;

            case MyUtil.SATURDAY:
                if (sessionViewModel.getSaturdayScheduledClass() != null) {
                    sessionViewModel.getSaturdayScheduledClass().
                            observe(getViewLifecycleOwner(),
                                    addClassSessionList -> {
                                        mTodayClassSessionList = addClassSessionList;
                                        setUpClasses(calendar);

                                    });
                }
        }
    }


    private void setNotifyMeSwitchButton() {
        notifyMeSwitchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                schedulerNotification = (JobScheduler) Objects.requireNonNull(getActivity()).
                        getSystemService(Context.JOB_SCHEDULER_SERVICE);
                if (isChecked) {
                    if (addClassSessionNotify != null) {
                        String sessionName = addClassSessionNotify.getClassname();
                        long sessionStartTime = getDeadLineTime(addClassSessionNotify);

                        ComponentName componentName = new ComponentName
                                (Objects.requireNonNull(getActivity()),
                                        AlertNotificationJobService.class);

                        PersistableBundle sessionNameExtra = new PersistableBundle();

                        sessionNameExtra.putString("sessionName", sessionName);


                        JobInfo jobInfo = new JobInfo.Builder(234, componentName)
                                .setRequiresCharging(false)
                                .setRequiresDeviceIdle(false)
                                .setMinimumLatency(sessionStartTime - 20000)
                                .setOverrideDeadline(sessionStartTime)
                                .setExtras(sessionNameExtra)
                                .build();

                        int resultCode = schedulerNotification.schedule(jobInfo);

                        if (resultCode == JobScheduler.RESULT_SUCCESS) {

                        }
                    }

                    Toast.makeText(getContext(), "switch is on", Toast.LENGTH_SHORT).show();
                } else {

                    schedulerNotification.cancel(234);

                    Toast.makeText(getContext(), "switch is off", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    //gets the start time to notify user
    private Long getDeadLineTime(AddClassSession addClassSession) {

        long alarmTriggerTime = 0;

        if (addClassSession != null) {

            Calendar nowTimeCalender = Calendar.getInstance();

            int hourOfTheDay = nowTimeCalender.get(Calendar.HOUR_OF_DAY);

            int minutesOfTheDay = nowTimeCalender.get(Calendar.MINUTE);

            String nowTimeString = hourOfTheDay + ":" +
                    ((minutesOfTheDay / 10 < 1) ? "0" + minutesOfTheDay :
                            String.valueOf(minutesOfTheDay)) + ":" + "00";

            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat simpleDateClassStartTIme = new SimpleDateFormat("HH:mm:ss");

            try {

                Date startDateTime = simpleDateClassStartTIme.parse(Objects.
                        requireNonNull(addClassSession).getRawTime());

                Date nowTimeDate = simpleDateClassStartTIme.parse(nowTimeString);

                alarmTriggerTime = startDateTime.getTime() - nowTimeDate.getTime();

                return alarmTriggerTime;


            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return alarmTriggerTime;

    }

}
