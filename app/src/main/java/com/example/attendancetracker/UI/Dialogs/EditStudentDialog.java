package com.example.attendancetracker.UI.Dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.SavedStateVMFactory;
import androidx.lifecycle.ViewModelProviders;

import com.example.attendancetracker.AddClassSession;
import com.example.attendancetracker.Model.Students;
import com.example.attendancetracker.R;
import com.example.attendancetracker.Repository.SessionClassRepository.SessionModelRepository;
import com.example.attendancetracker.Repository.SessionClassRepository.SessionViewModel;
import com.example.attendancetracker.UI.MytextWatcher;
import com.example.attendancetracker.Util.MyUtil;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditStudentDialog extends DialogFragment {

    private MaterialAlertDialogBuilder materialAlertDialogBuilder;

    AlertDialog editStudentDialog;


    View view;

    Students student;

    int studentPosition;

    AddClassSession session;

    SessionViewModel sessionViewModel;


    @BindView(R.id.studentNameTextInputEditText)
    TextInputEditText studentNameTextInputEditText;

    @BindView(R.id.studentEmailTextInputEditText)
    TextInputEditText studentEmailTextInputEditText;

    @BindView(R.id.studentNameTextInputLayout)
    TextInputLayout studentNameTextInputLayout;

    @BindView(R.id.studentEmailTextInputLayout)
    TextInputLayout studentEmailTextInputLayout;


    @BindView(R.id.updateStudentButton)
    MaterialButton updateMaterialButton;

    @BindView(R.id.cancelStudentButton)
    MaterialButton cancelMaterialButton;

    @BindView(R.id.deleteStudentButton)
    MaterialButton deleteMaterialButton;


    String studentNameChange, studentEmailChange;

    boolean isStudentNameValid,isStudentEmailValid;

    List<Students>studentsList;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        materialAlertDialogBuilder = new MaterialAlertDialogBuilder(
                Objects.requireNonNull(getActivity()),
                R.style.ThemeOverlay_MaterialComponents_Dialog);

        materialAlertDialogBuilder.setView(view);

        editStudentDialog = materialAlertDialogBuilder.create();

        if (student != null){
            studentNameTextInputEditText.setText(student.getStudentName());
            studentEmailTextInputEditText.setText(student.getStudentEmail());

        }

        updateMaterialButton.setOnClickListener(v -> {
            student.setStudentName(studentNameChange);
            student.setStudentEmail(studentEmailChange);
            studentsList = session.getStudents();
            studentsList.remove(studentPosition);
            studentsList.set(studentPosition,student);
            session.setStudents(studentsList);
            sessionViewModel.setAddClassSessionData(session);
            editStudentDialog.dismiss();

        });

        cancelMaterialButton.setOnClickListener(v -> {
            editStudentDialog.dismiss();
        });


        deleteMaterialButton.setOnClickListener(v -> {
            ReaffirmDialog reaffirmDialog = new ReaffirmDialog();
            reaffirmDialog.setStudent(session,student,studentPosition);
            editStudentDialog.dismiss();
            reaffirmDialog.show(Objects.requireNonNull(getFragmentManager()),
                    "reaffirmDialog");


        });


        studentNameTextInputEditText.addTextChangedListener(new MytextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                isStudentNameValid = MyUtil.checkInputValidity(s.toString());

                if (isStudentNameValid){
                    studentNameTextInputLayout.setError(null);
                    studentNameChange = s.toString();
                }

            }
        });

        studentEmailTextInputEditText.addTextChangedListener(new MytextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
               isStudentEmailValid = MyUtil.checkInputValidity(s.toString());

               if (isStudentEmailValid){
                   studentEmailTextInputLayout.setError(null);
                   studentEmailChange = s.toString();
               }
            }
        });


        return editStudentDialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = View.inflate(getContext(),
                R.layout.edit_student_dialog_row, null);
        ButterKnife.bind(this, view);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        sessionViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity()),
                new SavedStateVMFactory(getActivity())).
                get(SessionViewModel.class);
    }


    public void setStudent(AddClassSession session, Students student, int studentIndexInArray){
        this.student = student;
        this.session = session;
        this.studentPosition = studentIndexInArray;
        studentNameChange = student.getStudentName();
        studentEmailChange = student.getStudentEmail();
    }

}
