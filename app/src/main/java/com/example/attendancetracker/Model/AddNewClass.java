package com.example.attendancetracker.Model;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.attendancetracker.R;
import com.example.attendancetracker.UI.MainMenuListeners;
import com.example.attendancetracker.UI.MytextWatcher;
import com.example.attendancetracker.UI.Dialogs.TimePicker;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;


public class AddNewClass extends Fragment {

    private MaterialButton mMaterialCancelButton;

    private MainMenuListeners mainMenuListeners;

    @BindView(R.id.createClassnameTextInputLayout)
    TextInputLayout  mTextInputLayoutClassName;

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

    static String timeFormat;

    DialogFragment dialogFragmentTimePicker;


    public AddNewClass() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_class, container, false);
        ButterKnife.bind(this,view);

        mMaterialCancelButton = view.findViewById(R.id.cancel_new_class);

        mMaterialCancelButton.setOnClickListener((View v) ->{
            mainMenuListeners.goToHome();

        });

        mTextInputEditTextClassName.addTextChangedListener(new MytextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (s != null){
                    String classnameString = s.toString();
                    if (!(classnameString.length() > 1)){
                        mTextInputLayoutClassName.
                                setError("classname must be more than 1 characters");
                    }
                }

            }
        });

        mAppCompatTextViewStartTime.setOnClickListener((View v) ->{

            dialogFragmentTimePicker = new TimePicker();
            dialogFragmentTimePicker.show(Objects.
                    requireNonNull(getFragmentManager()),"EndTimePicker");

        });

        mAppCompatTextViewEndTime.setOnClickListener((View v) ->{
            dialogFragmentTimePicker = new TimePicker();
            dialogFragmentTimePicker.show(Objects.
                    requireNonNull(getFragmentManager()),"EndTimePicker");

        });

        mAppCompatTextViewStartDate.setOnClickListener((View v) ->{

        });

        mAppCompatTextViewEndDate.setOnClickListener((View v) ->{

        });



        return view;
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mainMenuListeners = null;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof MainMenuListeners){
            mainMenuListeners = (MainMenuListeners) context;

        } else{
            throw new RuntimeException("must implement " +
                    "MainMenuListener & ShowDialogListener");
        }
    }




//    public static class TimePicker extends DialogFragment implements
//            TimePickerDialog.OnTimeSetListener {
//        private int chosenHour,chosenMinute;
//
//        @NonNull
//        @Override
//        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
//
//            final Calendar mCalendar = Calendar.getInstance();
//
//            int hour = mCalendar.get(Calendar.HOUR_OF_DAY);
//
//            int minute = mCalendar.get(Calendar.MINUTE);
//
//            TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
//                    this,hour,minute, DateFormat.is24HourFormat(getActivity()));
//            return timePickerDialog;
//        }
//        @Override
//        public void onTimeSet(android.widget.TimePicker view,
//                              int hourOfDay, int minute) {
//            String timeMode = "am";
//            chosenHour = hourOfDay;
//
//            if (hourOfDay > 12){
//                timeMode = "pm";
//                hourOfDay = hourOfDay -12;
//            } else{
//                timeMode = "am";
//            }
//            chosenMinute = minute;
//
//             timeFormat = chosenHour+":"+ chosenMinute+timeMode;
//
////            appCompatTextViewActualEndTime.setText(timeFormat);
//        }
//
//
//
//    }
}
