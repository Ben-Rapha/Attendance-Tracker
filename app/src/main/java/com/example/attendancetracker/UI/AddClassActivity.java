package com.example.attendancetracker.UI;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.SavedStateVMFactory;
import androidx.lifecycle.ViewModelProviders;

import com.example.attendancetracker.AddClassSession;
import com.example.attendancetracker.Model.Students;
import com.example.attendancetracker.Model.WeekDays;
import com.example.attendancetracker.R;
import com.example.attendancetracker.Repository.SessionClassRepository.SessionModelRepository;
import com.example.attendancetracker.Repository.SessionClassRepository.SessionViewModel;
import com.example.attendancetracker.UI.Dialogs.DatePicker;
import com.example.attendancetracker.UI.Dialogs.TimePicker;
import com.example.attendancetracker.Util.MyUtil;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;


import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddClassActivity extends AppCompatActivity implements
        TimePicker.TimePickerEndTimeListener, DatePicker.DatePickerListener {

    @BindView(R.id.chipGroup)
    ChipGroup chipGroup;

    @BindView(R.id.monday)
    Chip monChip;

    @BindView(R.id.tuesday)
    Chip tuesChip;

    @BindView(R.id.wednesday)
    Chip wedsChip;

    @BindView(R.id.thursday)
    Chip thurChip;

    @BindView(R.id.friday)
    Chip fridChip;

    @BindView(R.id.saturday)
    Chip satChip;
    @BindView(R.id.sunday)
    Chip sunChip;


    @BindView(R.id.cancelNewClassButton)
    MaterialButton mMaterialCancelButton;

    @BindView(R.id.addClassButton)
    MaterialButton materialAddClassButton;

    @BindView(R.id.createClassnameTextInputLayout)
    TextInputLayout mTextInputLayoutClassName;

    @BindView(R.id.createLocationTextInputLayout)
    TextInputLayout mTextInputLayoutLocation;

    @BindView(R.id.classnameTextInputEditText)
    TextInputEditText mTextInputEditTextClassName;

    @BindView(R.id.locationTextInputEditText)
    TextInputEditText mTextInputEditTextLocation;

    @BindView(R.id.endDate)
    AppCompatTextView mAppCompatTextViewEndDate;

    @BindView(R.id.startDate)
    AppCompatTextView mAppCompatTextViewStartDate;

    @BindView(R.id.startTime)
    AppCompatTextView mAppCompatTextViewStartTime;

    @BindView(R.id.endTime)
    AppCompatTextView mAppCompatTextViewEndTime;

    @BindView(R.id.appCompatTextViewActualEndTime)
    AppCompatTextView appCompatTextViewActualEndTime;

    @BindView(R.id.actualStartTime)
    AppCompatTextView actualStartTime;

    @BindView(R.id.startActualDate)
    AppCompatTextView startActualDate;

    @BindView(R.id.actualEndDate)
    AppCompatTextView endActualDate;

    @BindView(R.id.progressBarAddClass)
    ProgressBar progressBar;

    TimePicker dialogFragmentEndTimePicker,
            dialogFragmentStartTimePicker;

    DatePicker mStartDateDialog, mEndDateDialog;

    boolean classnameBool,
            locationBool, isEndDateSet = false, isStartDateSet
            = false, isEndTimeSet = false, isStartTimeSet = false,
            sunday = false, monday = false, tuesday = false, wednesday = false,
            thursday = false, friday = false,
            saturday = false, isSetTimeValid = false, isDateSetValid = false,alreadyCalled=false;


    SessionViewModel sessionViewModel;

    LiveData<AddClassSession> addClassSession;

    MaterialAlertDialogBuilder materialAlertDialogBuilder;

    List<String> selectedDaysList;
    List<AddClassSession> addClassSessionsList;
    LiveData<List<AddClassSession>> listLiveData;

    String classname, location, dateStart = "",
            dateEnd = "", timeStart = "", timeEnd = "";

    java.sql.Date sqlStartDate, sqlEndDate;

    Time sqlStartTime, sqlEndTime;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_class_layout_activity);
        ButterKnife.bind(this);
        selectedDaysList = new ArrayList<>();
        loseOrGainFocus(mAppCompatTextViewStartDate, false);
        loseOrGainFocus(mAppCompatTextViewEndDate, false);
        loseOrGainFocus(mAppCompatTextViewStartTime, false);
        loseOrGainFocus(mAppCompatTextViewEndTime, false);

        sessionViewModel = ViewModelProviders.of(this,new SavedStateVMFactory(this))
                .get(SessionViewModel.class);

        listLiveData = sessionViewModel.getAllClassSessionFromSessionRepository();

        setChipDirection();

        setTimeDateListener();

        setTextChangeListener();

        setOnButtonAddListener();

        setListenerOnChips();

        cancelAddClass();



    }

    /**
     * Add button
     * adds new class to data base
     * checks if all fields are correct before processed
     */
    private void setOnButtonAddListener() {
        materialAddClassButton.setOnClickListener((View v) -> {
            checkFieldError();
            if (classnameBool & locationBool & isEndTimeSet
                    & isStartTimeSet & isStartDateSet & isEndDateSet) {
                if (!(sunday || monday || tuesday || wednesday || thursday || friday || saturday)) {
                    Snackbar.make(materialAddClassButton,
                            getString(R.string.set_select_day_error), Snackbar.LENGTH_LONG).
                            setBackgroundTint(getResources().getColor(R.color.light_cyan)).
                            setTextColor(getResources().getColor(R.color.colorPrimaryDark)).show();

                } else if ((isDateSetValid & isSetTimeValid)) {
                    progressBar.setVisibility(View.VISIBLE);

                    sessionViewModel.getSpecifiedClass(classname).observe(this, session -> {

                    });

                    SessionModelRepository.setCheckClassnameListener(addClassSessionLiveData ->
                            addClassSessionLiveData.
                            observe(AddClassActivity.this, addClassSession -> {
                                if (addClassSession == null) {
                                    sessionViewModel.insertClassSessionIntoDatabase(new AddClassSession(classname,
                                            location, sqlStartDate, sqlEndDate, sqlStartTime, sqlEndTime, sunday ? "sunday" : null,
                                            monday ? "monday" : null, tuesday ? "tuesday" : null, wednesday ? "wednesday" : null,
                                            thursday ? "thursday" : null, friday ? "friday" : null, saturday ? "saturday" : null,new ArrayList<Students>()));
                                    progressBar.setVisibility(View.GONE);
                                    AddClassActivity.this.finish();
                                } else {
                                    AddClassActivity.this.showConflictDialog(addClassSession).show();
                                }
                            }));
                }

            } else {
                Snackbar.make(materialAddClassButton,
                        getString(R.string.assert_all_fields_are_set_error), Snackbar.LENGTH_LONG).
                        setBackgroundTint(getResources().getColor(R.color.light_cyan)).
                        setTextColor(getResources().getColor(R.color.colorPrimaryDark)).show();
            }
        });
    }

    private void cancelAddClass() {
        mMaterialCancelButton.setOnClickListener((View v) -> {
            finish();
        });
    }


    private void checkFieldError() {
        if (!classnameBool) {
            mTextInputLayoutClassName.setError(getString(R.string.set_classname_error));

        }
        if (!locationBool) {
            mTextInputLayoutLocation.setError(getString(R.string.set_location_error));

        }
        if (!isEndDateSet) {
            mAppCompatTextViewEndDate.setError(getString(R.string.please_set_end_date));
        }

        if (!isStartDateSet) {
            mAppCompatTextViewStartDate.setError(getString(R.string.please_set_start_date));
        }
        if (!isStartTimeSet) {
            mAppCompatTextViewStartTime.setError(getString(R.string.set_start_time));
        }
        if (!isEndTimeSet) {
            mAppCompatTextViewEndTime.setError(getString(R.string.set_end_time));
        }

    }


    private void setListenerOnChips() {
        monChip.setOnClickListener((View v) -> {
            monday = !monday;
            if (monday) {
                verifyChosenDaysByUser(WeekDays.getMONDAY(), monday);
            } else {
                verifyChosenDaysByUser(WeekDays.getMONDAY(), monday);
            }
        });

        tuesChip.setOnClickListener((View v) -> {
            tuesday = !tuesday;
            if (tuesday) {
                verifyChosenDaysByUser(WeekDays.getTUESDAY(), tuesday);
            } else {
                verifyChosenDaysByUser(WeekDays.getTUESDAY(), tuesday);
            }
        });
        wedsChip.setOnClickListener((View v) -> {
            wednesday = !wednesday;
            if (wednesday) {
                verifyChosenDaysByUser(WeekDays.getWEDNESDAY(), wednesday);
            } else {
                verifyChosenDaysByUser(WeekDays.getWEDNESDAY(), wednesday);
            }
        });
        thurChip.setOnClickListener((View v) -> {
            thursday = !thursday;
            if (thursday) {
                verifyChosenDaysByUser(WeekDays.getTHURSDAY(), thursday);
            } else {
                verifyChosenDaysByUser(WeekDays.getTHURSDAY(), thursday);
            }
        });
        fridChip.setOnClickListener((View v) -> {
            friday = !friday;
            if (friday) {
                verifyChosenDaysByUser(WeekDays.getFRIDAY(), friday);
            } else {

                verifyChosenDaysByUser(WeekDays.getFRIDAY(), friday);
            }
        });
        satChip.setOnClickListener((View v) -> {

            saturday = !saturday;
            if (saturday) {
                verifyChosenDaysByUser(WeekDays.getSATURDAY(), saturday);
            } else {
                verifyChosenDaysByUser(WeekDays.getSATURDAY(), saturday);
            }
        });

        sunChip.setOnClickListener((View v) -> {
            sunday = !sunday;
            if (sunday) {
                verifyChosenDaysByUser(WeekDays.getSUNDAY(), sunday);
            } else {
                verifyChosenDaysByUser(WeekDays.getSUNDAY(), sunday);
            }
        });
    }


    /**
     * classname and location text change listener
     * checks that text field is not empty
     * input must be one or more
     */
    private void setTextChangeListener() {
        mTextInputEditTextClassName.addTextChangedListener(new MytextWatcher() {
            @Override
            public void afterTextChanged(Editable classnameEditable) {
                loseOrGainFocus(mAppCompatTextViewStartDate, false);
                loseOrGainFocus(mAppCompatTextViewEndDate, false);
                loseOrGainFocus(mAppCompatTextViewStartTime, false);
                loseOrGainFocus(mAppCompatTextViewEndTime, false);
                classnameBool = MyUtil.checkInputValidity(classnameEditable.toString());
                if (classnameBool) {
                    mTextInputLayoutClassName.setError(null);
                    classname = classnameEditable.toString();
                }
            }
        });


        mTextInputEditTextLocation.addTextChangedListener(new MytextWatcher() {

            @Override
            public void afterTextChanged(Editable locationEditable) {
                loseOrGainFocus(mAppCompatTextViewStartDate, false);
                loseOrGainFocus(mAppCompatTextViewEndDate, false);
                loseOrGainFocus(mAppCompatTextViewStartTime, false);
                loseOrGainFocus(mAppCompatTextViewEndTime, false);

                locationBool = MyUtil.checkInputValidity(locationEditable.toString());
                if (locationBool) {
                    mTextInputLayoutLocation.setError(null);
                    location = locationEditable.toString();
                }
            }
        });
    }

    private void setTimeDateListener() {
        mAppCompatTextViewEndTime.setOnClickListener((View v) -> {
            dialogFragmentEndTimePicker = new TimePicker();
            dialogFragmentEndTimePicker.setEndTimeSet(true);
            dialogFragmentEndTimePicker.show(getSupportFragmentManager(),
                    getString(R.string.endtimepicker));

        });

        mAppCompatTextViewStartTime.setOnClickListener((View v) -> {
            dialogFragmentStartTimePicker = new TimePicker();
            dialogFragmentStartTimePicker.setStartTimeSet(true);
            dialogFragmentStartTimePicker.show(getSupportFragmentManager(),
                    getString(R.string.starttimepicker));

        });

        mAppCompatTextViewStartDate.setOnClickListener((View v) -> {
            mStartDateDialog = new DatePicker();
            mStartDateDialog.show(getSupportFragmentManager(),
                    getString(R.string.startDate));
            mStartDateDialog.setStartDateSet(true);

        });

        mAppCompatTextViewEndDate.setOnClickListener((View v) -> {
            mEndDateDialog = new DatePicker();
            mEndDateDialog.show(getSupportFragmentManager(),
                    getString(R.string.enddate));
            mEndDateDialog.setEndDateSet(true);


        });
    }

    private void setChipDirection() {
        monChip.setLayoutDirection(View.LAYOUT_DIRECTION_LOCALE);
        tuesChip.setLayoutDirection(View.LAYOUT_DIRECTION_LOCALE);
        wedsChip.setLayoutDirection(View.LAYOUT_DIRECTION_LOCALE);
        thurChip.setLayoutDirection(View.LAYOUT_DIRECTION_LOCALE);
        fridChip.setLayoutDirection(View.LAYOUT_DIRECTION_LOCALE);
        satChip.setLayoutDirection(View.LAYOUT_DIRECTION_LOCALE);
        sunChip.setLayoutDirection(View.LAYOUT_DIRECTION_LOCALE);
    }

    private void verifyChosenDaysByUser(String dayChosen, boolean addORemove) {
        if (addORemove) {
            selectedDaysList.add(dayChosen);
        }
        if (!addORemove) {
            if (!selectedDaysList.isEmpty()) {
                for (int i = 0; selectedDaysList.size() > i; i++) {
                    if (dayChosen.equals(selectedDaysList.get(i))) {
                        selectedDaysList.remove(i);
                        break;
                    }
                }
            }
        }

    }




    //fires when the user chooses a time
    @Override
    public void updateTime(String endTime, String rawTime) {

        if (dialogFragmentStartTimePicker != null) {
            if (dialogFragmentStartTimePicker.getStartTimeSet()) {
                actualStartTime.setText(endTime);
                actualStartTime.setTextColor(getResources().getColor(R.color.white));
                isStartTimeSet = true;
                sqlStartTime = getFormattedTime(rawTime);
                timeStart = rawTime;
                mAppCompatTextViewStartTime.setError(null);
            }
        }

        if (dialogFragmentEndTimePicker != null) {
            if (dialogFragmentEndTimePicker.getEndTimeSet()) {
                appCompatTextViewActualEndTime.setText(endTime);
                appCompatTextViewActualEndTime.setTextColor(getResources().getColor(R.color.white));
                isEndTimeSet = true;
                sqlEndTime = getFormattedTime(rawTime);
                timeEnd = rawTime;
                mAppCompatTextViewEndTime.setError(null);
            }
        }

        if (isEndTimeSet & isStartTimeSet) {
            checkIsTimeValid(timeStart, timeEnd);
        }

    }


    //fires when the user chooses a date
    @Override
    public void setDate(String date, String sqlFormatDate) {
        if (mStartDateDialog != null) {
            if (mStartDateDialog.getStartDateSet()) {
                startActualDate.setText(date);
                startActualDate.setTextColor(getResources().getColor(R.color.white));
                isStartDateSet = true;
                sqlStartDate = getFormattedSqlDate(sqlFormatDate);
                dateStart = sqlFormatDate;
                mAppCompatTextViewStartDate.setError(null);
            }
        }
        if (mEndDateDialog != null) {
            if (mEndDateDialog.getEndDateSet()) {
                endActualDate.setText(date);
                endActualDate.setTextColor(getResources().getColor(R.color.white));
                isEndDateSet = true;
                sqlEndDate = getFormattedSqlDate(sqlFormatDate);
                dateEnd = sqlFormatDate;
                mAppCompatTextViewEndDate.setError(null);
            }
        }
        if ((!(dateStart.isEmpty()) & !(dateEnd.isEmpty()))) {
            checkIsDateValid(dateStart, dateEnd);
        }
    }

    private Time getFormattedTime(String time) {
        Time formattedTime;
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm:ss");

        try {
            Date date = simpleDateFormat.parse(time);
            Calendar timeCalender = Calendar.getInstance();
            timeCalender.setTime(date);
//            Log.v("timeFormatted", timeCalender.get(Calendar.HOUR)+" ");
//            Log.v("timeFormatted", timeCalender.get(Calendar.MINUTE)+" ");
//            Log.v("timeFormatted", timeCalender.get(Calendar.SECOND)+" ");

            formattedTime = Time.valueOf(time);
            Log.v("timeFormatted", formattedTime.toString());
            return formattedTime;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private java.sql.Date getFormattedSqlDate(String date) {

        java.sql.Date sqlFormattedDate;

        sqlFormattedDate = java.sql.Date.valueOf(date);

        Log.v("dateFormatted", sqlFormattedDate.toString());
        return sqlFormattedDate;
    }

    @SuppressLint("SimpleDateFormat")
    private void checkIsTimeValid(String startTime, String endTime) {
        SimpleDateFormat simpleDateFormatStartTime = new SimpleDateFormat("hh:mm:ss");
        SimpleDateFormat simpleDateFormatEndTime = new SimpleDateFormat("hh:mm:ss");

        try {
            Date startTimeDate = simpleDateFormatStartTime.parse(startTime);
            Date endTimeDate = simpleDateFormatEndTime.parse(endTime);

            if (startTimeDate.after(endTimeDate)) {

                isSetTimeValid = true;

            }
            if (startTimeDate.before(endTimeDate)) {
                isSetTimeValid = true;

                mAppCompatTextViewStartTime.setError(null);

                mAppCompatTextViewEndTime.setError(null);
            }
            if (startTimeDate.equals(endTimeDate)) {
                loseOrGainFocus(mAppCompatTextViewEndTime, true);
                mAppCompatTextViewEndTime.setError(getString(R.string.error_starta_and_end_equal));
                isSetTimeValid = false;
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @SuppressLint("SimpleDateFormat")
    private void checkIsDateValid(String startDate, String endDate) {

        SimpleDateFormat dateFormatStart = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat dateFormatEnd = new SimpleDateFormat("yyyy-MM-dd");

        try {
            Date startDateFormat = dateFormatStart.parse(startDate);
            Date endDateFormat = dateFormatEnd.parse(endDate);

            if (startDateFormat.after(endDateFormat)) {
                loseOrGainFocus(mAppCompatTextViewStartDate, true);
                mAppCompatTextViewStartDate.setError(
                        getString(R.string.error_start_date_cant_be_ahead));
                isDateSetValid = false;

            }
            if (startDateFormat.before(endDateFormat)) {
                isDateSetValid = true;
                mAppCompatTextViewStartDate.setError(null);
            }

            if (startDateFormat.equals(endDateFormat)) {
                isDateSetValid = true;
                loseOrGainFocus(mAppCompatTextViewEndDate, true);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void loseOrGainFocus(View view, boolean gain) {
        view.setFocusable(gain);
        view.setFocusableInTouchMode(gain);

    }
    private MaterialAlertDialogBuilder showConflictDialog(AddClassSession addClassSession) {

        materialAlertDialogBuilder =
                new MaterialAlertDialogBuilder(AddClassActivity.this,
                        R.style.ThemeOverlay_MaterialComponents_Dialog);

        materialAlertDialogBuilder.setTitle(getString(R.string.classname_conflict));
        materialAlertDialogBuilder.setMessage(getString(R.string.updateMessage));
        materialAlertDialogBuilder.setNegativeButton(getString(R.string.cancel),
                (dialog, which) -> dialog.dismiss());


        materialAlertDialogBuilder.setPositiveButton(getString(R.string.update), (dialog, which) -> {

            if (addClassSession.getStudents()!= null){
                List<Students> students = addClassSession.getStudents();
                sessionViewModel.insertClassSessionIntoDatabase(new AddClassSession(classname,
                        location, sqlStartDate, sqlEndDate, sqlStartTime, sqlEndTime, sunday ? "sunday" : null,
                        monday ? "monday" : null, tuesday ? "tuesday" : null, wednesday ? "wednesday" : null,
                        thursday ? "thursday" : null, friday ? "friday" : null, saturday ? "saturday" : null,students));
            } else{
                sessionViewModel.insertClassSessionIntoDatabase(new AddClassSession(classname,
                        location, sqlStartDate, sqlEndDate, sqlStartTime, sqlEndTime, sunday ? "sunday" : null,
                        monday ? "monday" : null, tuesday ? "tuesday" : null, wednesday ? "wednesday" : null,
                        thursday ? "thursday" : null, friday ? "friday" : null, saturday ? "saturday" :
                        null,new ArrayList<Students>()));
            }

            finish();
        });
        progressBar.setVisibility(View.GONE);
        return materialAlertDialogBuilder;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SessionModelRepository.setCheckClassnameListener(null);
    }
}
