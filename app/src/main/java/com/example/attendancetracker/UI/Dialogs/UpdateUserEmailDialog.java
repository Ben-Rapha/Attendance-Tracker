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

public class UpdateUserEmailDialog extends DialogFragment {

   private MaterialAlertDialogBuilder materialAlertDialogBuilder;

    private AlertDialog reAffirmDialog;

    String message = "";


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
       super.onCreateDialog(savedInstanceState);

        materialAlertDialogBuilder = new MaterialAlertDialogBuilder(
                Objects.requireNonNull(getActivity()),
                R.style.ThemeOverlay_MaterialComponents_Dialog);

        materialAlertDialogBuilder.setTitle("Passcode");

        materialAlertDialogBuilder.setMessage(message);


        reAffirmDialog = materialAlertDialogBuilder.create();

        return  reAffirmDialog;
    }

    public void setMessage(String message){

     this.message = message;
    }
}
