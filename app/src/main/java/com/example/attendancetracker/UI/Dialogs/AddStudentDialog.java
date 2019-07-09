package com.example.attendancetracker.UI.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.attendancetracker.AddClassSession;
import com.example.attendancetracker.Model.Students;
import com.example.attendancetracker.R;
import com.example.attendancetracker.Repository.SessionClassRepository.SessionViewModel;
import com.example.attendancetracker.UI.MytextWatcher;
import com.example.attendancetracker.Util.MyUtil;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddStudentDialog extends
        DialogFragment {

    private boolean isStudentNameValid,isEmailValid,isAllFiledValid;


    private MaterialAlertDialogBuilder materialAlertDialogBuilder;



    @BindView(R.id.cancelStudentButton)
    MaterialButton materialCancelButton;

    @BindView(R.id.addCStudentButton)
    MaterialButton materialAddStudentButton;

    private AlertDialog addStudentDialog;

    AddStudentClickListener mAddStudentClickListener;

    private View view;
    @BindView(R.id.studentNameTextInputEditText)

    TextInputEditText studentNameEditText;

    @BindView(R.id.studentNameTextInputLayout)
    TextInputLayout studentNAmeTextInputLayout;

    @BindView(R.id.emailTextInputEditText)
    TextInputEditText emailEditText;

    @BindView(R.id.emailTextInputLayout)
    TextInputLayout emailTextInputLayout;

    private SessionViewModel sessionViewModel;

    private AddClassSession addClassSession;

    String studentName,studentEmail;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        materialAlertDialogBuilder = new MaterialAlertDialogBuilder(Objects.requireNonNull(getActivity()),
                R.style.ThemeOverlay_MaterialComponents_Dialog);
        materialAlertDialogBuilder.setView(view);
        addStudentDialog = materialAlertDialogBuilder.create();
        setUpStudentEditText();
        setUpStudentEmail();
        materialCancelButton.setOnClickListener(v -> {
            addStudentDialog.dismiss();
        });
        materialAddStudentButton.setOnClickListener(v -> {
            mAddStudentClickListener.addStudentButtonClicked();
            if (isAllFieldsFilled()){
                List<Students> students = addClassSession.getStudents();
                students.add(new Students(studentName,studentEmail,false));
                addClassSession.setStudents(students);
                sessionViewModel.insertClassSessionIntoDatabase(addClassSession);
                sessionViewModel.setAddClassSessionData(addClassSession);
                addStudentDialog.dismiss();
            }
        });
        return addStudentDialog;
    }

    private boolean isAllFieldsFilled(){
        isAllFiledValid = true;
        if (!isStudentNameValid){
            studentNAmeTextInputLayout.setError("Students name must be more than 1");
            isAllFiledValid = false;

        } if (!isEmailValid){
            emailTextInputLayout.setError("Email is not valid");
            isAllFiledValid = false;
        }
        return isAllFiledValid;

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = View.inflate(getContext(),
                R.layout.add_student_dialog_layout, null);
        ButterKnife.bind(this, view);

        sessionViewModel = ViewModelProviders.of(Objects.
                requireNonNull(getActivity())).get(SessionViewModel.class);
    }

    public interface AddStudentClickListener {
        void addStudentButtonClicked();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof AddStudentClickListener) {
            mAddStudentClickListener = (AddStudentClickListener) context;
        } else {
            throw new RuntimeException("must implement AddStudentClickListener");
        }
    }


   private void setUpStudentEditText() {
        studentNameEditText.addTextChangedListener(new MytextWatcher() {
            @Override
            public void afterTextChanged(Editable student) {
                isStudentNameValid = MyUtil.checkInputValidity(student.toString());
                if (isStudentNameValid){
                    studentNAmeTextInputLayout.setError(null);
                    studentName = student.toString();
                }

            }
        });
    }
    private void setUpStudentEmail(){
        emailEditText.addTextChangedListener(new MytextWatcher() {
            @Override
            public void afterTextChanged(Editable email) {
                isEmailValid = MyUtil.isEmailValid(email.toString());
                if (isEmailValid){
                    emailTextInputLayout.setError(null);
                    studentEmail = email.toString();
                }
            }
        });
    }

    public void setAddClassSession(AddClassSession addClassSession) {
        this.addClassSession = addClassSession;
    }
}
