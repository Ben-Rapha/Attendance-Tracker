package com.example.attendancetracker.UI;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;

import com.example.attendancetracker.DatePicker;
import com.example.attendancetracker.MainMenuListeners;
import com.example.attendancetracker.MytextWatcher;
import com.example.attendancetracker.R;
import com.example.attendancetracker.TimePicker;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import static androidx.navigation.Navigation.findNavController;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddClassActivity extends AppCompatActivity implements
        TimePicker.TimePickerEndTimeListener, DatePicker.DatePickerListener {

    @BindView(R.id.cancel_new_class)
    MaterialButton mMaterialCancelButton;

    @BindView(R.id.addClassButton)
    MaterialButton materialAddClassButton;

    private MainMenuListeners mainMenuListeners;

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

    TimePicker dialogFragmentTimePicker, dialogFragmentTimePicker2;

    DatePicker mStartDateDialog, mEndDateDialog;

    boolean classnameBool,
            locationBool, isEndDateSet = false, isStartDateSet
            = false, isEndTimeSet = false, isStartTimeSet = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_class_layout_activity);
        ButterKnife.bind(this);

        mAppCompatTextViewEndTime.setOnClickListener((View v) -> {
            dialogFragmentTimePicker = new TimePicker();
            dialogFragmentTimePicker.setEndTimeSet(true);
            dialogFragmentTimePicker.show(getSupportFragmentManager(), "EndTimePicker");

        });

        mAppCompatTextViewStartTime.setOnClickListener((View v) -> {
            dialogFragmentTimePicker2 = new TimePicker();
            dialogFragmentTimePicker2.setStartTimeSet(true);
            dialogFragmentTimePicker2.show(getSupportFragmentManager(), "EndTimePicker");
        });

        mAppCompatTextViewStartDate.setOnClickListener((View v) -> {
            mStartDateDialog = new DatePicker();
            mStartDateDialog.show(getSupportFragmentManager(), "startDate");
            mStartDateDialog.setStartDateSet(true);
            if (mEndDateDialog != null) {
                mEndDateDialog.setEndDateSet(false);
            }
        });

        mAppCompatTextViewEndDate.setOnClickListener((View v) -> {
            mEndDateDialog = new DatePicker();
            mEndDateDialog.show(getSupportFragmentManager(), "endDate");
            mEndDateDialog.setEndDateSet(true);
            if (mStartDateDialog != null) {
                mStartDateDialog.setStartDateSet(false);
            }


        });


        mTextInputEditTextClassName.addTextChangedListener(new MytextWatcher() {
            @Override
            public void afterTextChanged(Editable classnameEditable) {
                classnameBool = checkInputValidity(classnameEditable.toString());
                if (classnameBool) {
                    mTextInputLayoutClassName.setError("");
                }
            }
        });

        mTextInputEditTextLocation.addTextChangedListener(new MytextWatcher() {
            @Override
            public void afterTextChanged(Editable locationEditable) {
                locationBool = checkInputValidity(locationEditable.toString());
                if (locationBool) {
                    mTextInputLayoutLocation.setError("");
                }
            }
        });

        mMaterialCancelButton.setOnClickListener((View v) -> {
            finish();
        });

        materialAddClassButton.setOnClickListener((View v) -> {
            if (!classnameBool) {
                mTextInputLayoutClassName.setError("Please set class name," +
                        " must be more than 1 letter");
            }
            if (!locationBool) {
                mTextInputLayoutLocation.setError("Please location name," +
                        " must be more than 1 letter");
            }
            if (!isEndDateSet) {
                mAppCompatTextViewEndDate.setError("please set end date");
            }
            if (!isStartDateSet) {
                mAppCompatTextViewStartDate.setError("Please set start date");
            }
            if (!isStartTimeSet) {
                mAppCompatTextViewStartTime.setError("Please set start time");
            }
            if (!isEndTimeSet) {
                mAppCompatTextViewEndTime.setError("please set end time");
            }
            if (classnameBool & locationBool & isEndTimeSet
                    & isStartTimeSet & isStartDateSet & isEndDateSet) {
                finish();
            } else {
                Snackbar.make(materialAddClassButton,
                        "Please make sure all fields are filled", Snackbar.LENGTH_LONG).
                        setBackgroundTint(getResources().getColor(R.color.light_cyan)).
                        setTextColor(getResources().getColor(R.color.colorPrimaryDark)).show();
            }
        });


    }

    boolean checkInputValidity(String text) {
        return text.length() > 1;
    }


    @Override
    public void updateTime(String endTime) {
        if (dialogFragmentTimePicker != null) {
            if (dialogFragmentTimePicker.getEndTimeSet()) {
                appCompatTextViewActualEndTime.setText(endTime);
                appCompatTextViewActualEndTime.setTextColor(getResources().getColor(R.color.white));
                isEndTimeSet = true;

            }
        }
        if (dialogFragmentTimePicker2 != null) {
            if (dialogFragmentTimePicker2.getStartTimeSet()) {
                actualStartTime.setText(endTime);
                actualStartTime.setTextColor(getResources().getColor(R.color.white));
                isStartTimeSet = true;
            }
        }

    }


    @Override
    public void setDate(String date) {
        if (mStartDateDialog != null) {
            if (mStartDateDialog.getStartDateSet()) {
                startActualDate.setText(date);
                startActualDate.setTextColor(getResources().getColor(R.color.white));
                mEndDateDialog = null;
                isStartDateSet = true;

            }
            if (mEndDateDialog != null) {
                if (mEndDateDialog.getEndDateSet()) {
                    endActualDate.setText(date);
                    endActualDate.setTextColor(getResources().getColor(R.color.white));
                    mStartDateDialog = null;
                    isEndDateSet = true;
                }
            }

        }
    }

}
