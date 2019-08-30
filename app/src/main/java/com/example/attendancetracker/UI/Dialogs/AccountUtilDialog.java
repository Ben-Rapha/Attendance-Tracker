package com.example.attendancetracker.UI.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.attendancetracker.R;
import com.example.attendancetracker.Util.MyUtil;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AccountUtilDialog extends DialogFragment {

    @BindView(R.id.title_data)
    TextView mTitleTextView;

    @BindView(R.id.previous_data)
    TextView mPreviousDataTextView;

    @BindView(R.id.data_textInputLayout)
    TextInputLayout mDataTextInputLayout;

    @BindView(R.id.affirm_textInputLayout)
    TextInputLayout mAffirmDataTextInputLayout;


    @BindView(R.id.data_EditText)
    TextInputEditText mDataEditText;

    @BindView(R.id.affirm_editText)
    TextInputEditText mAffirmEditText;

    @BindView(R.id.cancel_button)
    MaterialButton mCancelButton;

    @BindView(R.id.update_button)
    MaterialButton mUpdateButton;

    private View view;

    private AlertDialog mAccountUtilDialog;

    MaterialAlertDialogBuilder materialAlertDialogBuilder;

    private boolean isUsername, isEmail,isPassword;

    private String mTitle, mPreviousData,mEmail;

    private SharedPreferences mPreferences;;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = View.inflate(getContext(), R.layout.account_dialog_layout,null);
        ButterKnife.bind(this, view);

        mPreferences = Objects.requireNonNull(getActivity())
                .getSharedPreferences(MyUtil.LOGIN_SHARED_PREF_FILE,
                        Context.MODE_PRIVATE);



    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        materialAlertDialogBuilder = new MaterialAlertDialogBuilder(Objects.requireNonNull(getActivity()),
                R.style.ThemeOverlay_MaterialComponents_Dialog);
        materialAlertDialogBuilder.setView(view);

        mAccountUtilDialog = materialAlertDialogBuilder.create();

        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAccountUtilDialog.dismiss();
            }
        });

        mUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor prefEditor = mPreferences.edit();
                if (isUsername) {
                    resolveUsername(prefEditor);
                }
                if (isEmail) {
                    resolveEmail(prefEditor);
                }

                if (isPassword){
                    resolvePassword(prefEditor);

                }
            }
        });

        if (!mTitle.isEmpty()){
            mTitleTextView.setText(mTitle);
        }

        if (!mPreviousData.isEmpty()){
            mPreviousDataTextView.setText(mPreviousData);
        }

        if (isUsername){
            mDataTextInputLayout.setHint(getString(R.string.new_username));
            mDataEditText.setHint(getString(R.string.new_username));
            mAffirmDataTextInputLayout.setVisibility(View.GONE);
        }

        if (isEmail){
            mDataTextInputLayout.setHint(getString(R.string.new_email));
            mDataEditText.setHint(getString(R.string.new_email));
            mAffirmDataTextInputLayout.setVisibility(View.GONE);
        }

        if (isPassword){
            mDataTextInputLayout.setHint(getString(R.string.old_password));
            mAffirmDataTextInputLayout.setVisibility(View.VISIBLE);
            mAffirmDataTextInputLayout.setHint(getString(R.string.new_password));
        }

        return mAccountUtilDialog;
    }

    private void resolvePassword(SharedPreferences.Editor prefEditor) {
        String prefPassword = mPreferences.
                getString(MyUtil.LOGIN_PASSWORD_KEY,MyUtil.LOGIN_PASSWORD_VALUE_DEFAULT);

        Editable oldEditable = mDataEditText.getEditableText();

        Editable newEditable = mAffirmEditText.getEditableText();


        if (oldEditable.toString().length() < 1){
            mDataTextInputLayout.setError(getString(R.string.password_not_set));
            return;
        } else {
            mDataTextInputLayout.setError(null);
        }
        if (newEditable.toString().length() < 1){
            mAffirmDataTextInputLayout.setError(getString(R.string.password_not_set));
            return;
        } else{
            mAffirmDataTextInputLayout.setError(null);
        }

        if( !MyUtil.checkPasswordValidity(oldEditable.toString())){
            mDataTextInputLayout.setError(getString(R.string.password_error));
            return;
        }
        if ( !MyUtil.checkPasswordValidity(newEditable.toString())){
            mAffirmDataTextInputLayout.setError(getString(R.string.password_error));
            return;
        }

        if (prefPassword != null) {
            if (!prefPassword.equals(oldEditable.toString())){
                mDataTextInputLayout.setError(getString(R.string.wrong_password));
                return;
            }
        }



        if (prefPassword != null) {
            if (!prefPassword.equals(newEditable.toString())){
                prefEditor.putString(MyUtil.LOGIN_PASSWORD_KEY,newEditable.toString());
                prefEditor.apply();
                mAccountUtilDialog.dismiss();
            } else{
                mAffirmDataTextInputLayout.setError(getString(R.string.password_old_new_same));
            }
        }
    }

    private void resolveEmail(SharedPreferences.Editor prefEditor) {
        boolean isEmailValid;
        Editable editable =  mDataEditText.getText();
        if (editable != null) {
            if (editable.toString().length() > 0){
                String email =  editable.toString();
                 isEmailValid = isEmailValid(email.trim());
                if (isEmailValid){
                    prefEditor.putString(MyUtil.LOGIN_EMAIL_KEY,email);
                    prefEditor.apply();
                    mAccountUtilDialog.dismiss();
                } else{
                    mDataTextInputLayout.setError(getString(R.string.enter_valid_email));
                }
            } else{
                mDataTextInputLayout.setError(getString(R.string.email_not_set));
            }
        }
    }

    private void resolveUsername(SharedPreferences.Editor prefEditor) {
        if (isUsername){
            boolean isUsernameValid;
           Editable editable = mDataEditText.getText();
            if (editable != null) {
                if (editable.toString().length() > 0) {
                   isUsernameValid = MyUtil.checkInputValidity(editable.toString());
                   if (isUsernameValid){
                       prefEditor.putString(MyUtil.LOGIN_USERNAME_KEY,editable.toString());
                       prefEditor.apply();
                       mAccountUtilDialog.dismiss();
                   } else{
                       mDataTextInputLayout.setError(getString(R.string.error_username));
                   }
                }else{
                    mDataTextInputLayout.setError(getString(R.string.username_not_set));
                }
            }
        }
    }


    public void setTitle(String title){
        if (isEmpty(title)){
            this.mTitle = title;
        }
    }

    public void setPreviousData(String previousData){
        if (isEmpty(previousData)){
            this.mPreviousData = previousData;
        }
    }

    public void isUsername(boolean isUsername){
        this.isUsername  = isUsername;
        isEmail = false;
        isPassword = false;
    }

    public void isEmail(boolean isEmail){
        this.isEmail = isEmail;
        isUsername = false;
        isPassword = false;
    }

    public void isPassword(boolean isPassword){
        this.isPassword = isPassword;
        isUsername = false;
        isEmail = false;
    }

    private boolean isEmpty(String data){
        return !data.isEmpty();
    }

    private boolean isEmailValid(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email)
                .matches();
    }
}
