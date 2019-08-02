package com.example.attendancetracker.UI.Dialogs;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.attendancetracker.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.Objects;

public class QuitAppDialog extends DialogFragment {

    private MaterialAlertDialogBuilder materialAlertDialogBuilder;

    private QuitAppListener quitAppListener;




    private AlertDialog reAffirmDialog;

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

        materialAlertDialogBuilder.setTitle("Quit Attendance Tracker");

        materialAlertDialogBuilder.setMessage("Are you sure you want to quit Attendance Tracker?");
        materialAlertDialogBuilder.setNegativeButton("Cancel", (dialog, which)
                -> reAffirmDialog.dismiss());

        materialAlertDialogBuilder.setPositiveButton("Yes", (dialog, which) -> {

            if (quitAppListener!= null){
                quitAppListener.quitAttendanceTracker();
            }

        });

        reAffirmDialog = materialAlertDialogBuilder.create();



        return  reAffirmDialog;
    }

    public void setQuitAppListener(QuitAppListener quitAppListener) {
        this.quitAppListener = quitAppListener;
    }

    public interface QuitAppListener{

        void quitAttendanceTracker();
    }
}
