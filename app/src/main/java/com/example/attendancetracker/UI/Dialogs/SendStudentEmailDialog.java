package com.example.attendancetracker.UI.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.attendancetracker.Model.Students;
import com.example.attendancetracker.R;
import com.example.attendancetracker.UI.MyTextWatcher;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SendStudentEmailDialog extends DialogFragment {

    @BindView(R.id.sendEmailButton)
    MaterialButton sendEmailButton;

    @BindView(R.id.cancelEmailButton)
    MaterialButton cancelEmailButton;

    @BindView(R.id.subjectTextInputLayout)
    TextInputLayout subjectTextInputLayout;

    @BindView(R.id.subjectTextInputEditText)
    TextInputEditText subjectTextInputEditText;

    @BindView(R.id.messageTextInputEditText)
    TextInputEditText emailMessageEditText;

    @BindView(R.id.messageTextInputLayout)
    TextInputLayout emailMessageInputLayout;

    @BindView(R.id.studentName)
    TextView studentNameTextView;

    @BindView(R.id.studentEmail)
    TextView studentEmailTextView;

    private MaterialAlertDialogBuilder materialAlertDialogBuilder;

    private AlertDialog sendStudentEmailDialog;

    private View view;

    String message, subject;

    boolean isMessageValid, isSubjectValid;

    Students student;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        materialAlertDialogBuilder = new MaterialAlertDialogBuilder(
                Objects.requireNonNull(getActivity()),
                R.style.ThemeOverlay_MaterialComponents_Dialog);

        materialAlertDialogBuilder.setView(view);

        if (student != null) {
            studentNameTextView.setText(student.getStudentName());
            studentEmailTextView.setText(student.getStudentEmail());
        }


        subjectTextInputEditText.addTextChangedListener(new MyTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                subject = s.toString();
                isSubjectValid = isMessageValid(subject);
            }
        });

        sendStudentEmailDialog = materialAlertDialogBuilder.create();

        emailMessageEditText.addTextChangedListener(new MyTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                message = s.toString();
                isMessageValid = isMessageValid(message);
            }
        });

        sendEmailButton.setOnClickListener(v -> {
            if (isMessageValid & isSubjectValid) {
                emailMessageInputLayout.setError(null);
                subjectTextInputLayout.setError(null);

                Intent sendEmailIntent = new Intent(Intent.ACTION_SENDTO);
                sendEmailIntent.setData(Uri.parse("mailto:"));
                sendEmailIntent.putExtra(Intent.EXTRA_EMAIL,
                        new String[]{student.getStudentEmail()});
                sendEmailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
                sendEmailIntent.putExtra(Intent.EXTRA_TEXT, message);

                if (sendEmailIntent.resolveActivity(getActivity().
                        getPackageManager()) != null) {
                    startActivity(sendEmailIntent);
                }
                sendStudentEmailDialog.dismiss();

            } else {
                emailMessageInputLayout.setError("Please enter a message");
            }

            if (!isSubjectValid) {
                subjectTextInputLayout.setError("please enter a subject");
            } else {
                subjectTextInputLayout.setError(null);
            }

        });

        cancelEmailButton.setOnClickListener(v -> {
            sendStudentEmailDialog.dismiss();

        });

        return sendStudentEmailDialog;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        view = View.inflate(getContext(),
                R.layout.send_student_email_layout, null);
        ButterKnife.bind(this, view);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    private boolean isMessageValid(String message) {
        return message.length() > 1;
    }

    public void setStudent(Students student) {
        this.student = student;
    }



}
