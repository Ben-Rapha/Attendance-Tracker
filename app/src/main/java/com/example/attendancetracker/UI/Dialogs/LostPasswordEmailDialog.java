package com.example.attendancetracker.UI.Dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.attendancetracker.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.Objects;

import butterknife.ButterKnife;

public class LostPasswordEmailDialog extends DialogFragment {

    private AlertDialog addStudentDialog;

    private MaterialAlertDialogBuilder materialAlertDialogBuilder;

    private AlertDialog reAffirmDialog;


    View view;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = View.inflate(getContext(),
                R.layout.lost_password_email_layout, null);
        ButterKnife.bind(this, view);

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
       super.onCreateDialog(savedInstanceState);
        materialAlertDialogBuilder = new MaterialAlertDialogBuilder(Objects.requireNonNull(getActivity()),
                R.style.ThemeOverlay_MaterialComponents_Dialog);

        materialAlertDialogBuilder.setView(view);

        materialAlertDialogBuilder.setTitle("Quit Attendance Tracker");

        materialAlertDialogBuilder.setMessage("Are you sure you want to quit Attendance Tracker?");
        materialAlertDialogBuilder.setNegativeButton("Cancel", (dialog, which)
                -> reAffirmDialog.dismiss());

        materialAlertDialogBuilder.setPositiveButton("Yes", (dialog, which) -> {

        });

        reAffirmDialog = materialAlertDialogBuilder.create();



        return  reAffirmDialog;

    }
}
