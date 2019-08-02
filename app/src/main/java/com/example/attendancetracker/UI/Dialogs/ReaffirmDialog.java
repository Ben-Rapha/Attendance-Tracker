package com.example.attendancetracker.UI.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.SavedStateViewModelFactory;
import androidx.lifecycle.ViewModelProviders;

import com.example.attendancetracker.AddClassSession;
import com.example.attendancetracker.Model.Students;
import com.example.attendancetracker.R;
import com.example.attendancetracker.Repository.SessionClassRepository.SessionViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;
import java.util.Objects;

public class ReaffirmDialog extends DialogFragment {

    private MaterialAlertDialogBuilder materialAlertDialogBuilder;


    AlertDialog reAffirmDialog;

    Students student;

    int studentPosition;

    AddClassSession session;

    SessionViewModel sessionViewModel;

    List<Students> studentsList;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        materialAlertDialogBuilder = new MaterialAlertDialogBuilder(
                Objects.requireNonNull(getActivity()),
                R.style.ThemeOverlay_MaterialComponents_Dialog);

        materialAlertDialogBuilder.setTitle("Delete Student");
        materialAlertDialogBuilder.setMessage("Are you sure?");
        materialAlertDialogBuilder.setNegativeButton("Cancel", (dialog, which)
                -> reAffirmDialog.dismiss());


        materialAlertDialogBuilder.setPositiveButton("Yes", (dialog, which) -> {
            studentsList = session.getStudents();
            studentsList.remove(studentPosition);
            session.setStudents(studentsList);
            sessionViewModel.setAddClassSessionData(session);
            reAffirmDialog.dismiss();
        });

        reAffirmDialog = materialAlertDialogBuilder.create();

        return reAffirmDialog;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        sessionViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity()),
                new SavedStateViewModelFactory(getActivity())).
                get(SessionViewModel.class);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    public void setStudent(AddClassSession session, Students student, int studentIndexInArray){
        this.student = student;
        this.session = session;
        this.studentPosition = studentIndexInArray;
    }
}
