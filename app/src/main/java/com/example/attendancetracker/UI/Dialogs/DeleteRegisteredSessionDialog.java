package com.example.attendancetracker.UI.Dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.attendancetracker.AddClassSession;
import com.example.attendancetracker.Model.History;
import com.example.attendancetracker.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.Objects;

public class DeleteRegisteredSessionDialog extends DialogFragment {

    MaterialAlertDialogBuilder materialAlertDialogBuilder;
    private AlertDialog reAffirmDialog;

    private AddClassSession addClassSession;

    OnDeleteSessionListener onDeleteSessionListener;

    private String message,title;

    public DeleteRegisteredSessionDialog() {
        // Required empty public constructor
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        materialAlertDialogBuilder = new MaterialAlertDialogBuilder(
                Objects.requireNonNull(getActivity()),
                R.style.ThemeOverlay_MaterialComponents_Dialog);

        materialAlertDialogBuilder.setTitle(title);


        materialAlertDialogBuilder.setMessage(message);
        materialAlertDialogBuilder.setNegativeButton(getString(R.string.cancel),
                (DialogInterface dialog, int which)
                -> {
            onDeleteSessionListener.restoreSession();
            reAffirmDialog.cancel();
        });


        materialAlertDialogBuilder.setPositiveButton(getString(R.string.yes),
                (dialog, which) -> {

            if (onDeleteSessionListener != null){
                onDeleteSessionListener.deleteSession();
            }

            reAffirmDialog.dismiss();
        });

        reAffirmDialog = materialAlertDialogBuilder.create();

        return reAffirmDialog;
    }


    public void setClassSession(AddClassSession addClassSession){

        this.addClassSession = addClassSession;
    }


    public interface OnDeleteSessionListener{

        void deleteSession();

        void restoreSession();

        void onDialogDismiss();
    }

    public void setOnDeleteSessionListener(OnDeleteSessionListener onDeleteSessionListener) {
        this.onDeleteSessionListener = onDeleteSessionListener;
    }

    public void setTitleAndMessage(String title,String message){
        this.title = title;
        this.message = message;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);

        onDeleteSessionListener.onDialogDismiss();
    }
}
