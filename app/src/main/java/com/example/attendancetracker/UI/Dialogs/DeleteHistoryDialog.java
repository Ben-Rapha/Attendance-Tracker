package com.example.attendancetracker.UI.Dialogs;


import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.attendancetracker.Model.History;
import com.example.attendancetracker.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class DeleteHistoryDialog extends DialogFragment {

    MaterialAlertDialogBuilder materialAlertDialogBuilder;

    public DeleteHistoryDialog() {
        // Required empty public constructor
    }

    private AlertDialog reAffirmDialog;

    private History history;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
         super.onCreateDialog(savedInstanceState);
//        View view =  inflater.inflate(R.layout.fragment_delete_history_dialog, container, false);

        materialAlertDialogBuilder = new MaterialAlertDialogBuilder(
                Objects.requireNonNull(getActivity()),
                R.style.ThemeOverlay_MaterialComponents_Dialog);

        materialAlertDialogBuilder.setTitle("Delete History");

        materialAlertDialogBuilder.setMessage("Are you sure you want to delete this class history?");
        materialAlertDialogBuilder.setNegativeButton("Cancel", (dialog, which)
                -> reAffirmDialog.dismiss());


        materialAlertDialogBuilder.setPositiveButton("Yes", (dialog, which) -> {

            reAffirmDialog.dismiss();
        });

        reAffirmDialog = materialAlertDialogBuilder.create();

        return reAffirmDialog;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    public void setHistory(History history){
        this.history = history;
    }
}
