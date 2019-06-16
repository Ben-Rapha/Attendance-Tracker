package com.example.attendancetracker;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SignUpFragment extends Fragment {

    @BindView(R.id.addClassButton)
    MaterialButton  mSignInButton;

    @BindView(R.id.usernameEditText)
    TextInputEditText mUsernameEditText;

    @BindView(R.id.email_editText)
    TextInputEditText mEmailEditText;

    @BindView(R.id.password_inputEdit)
    TextInputEditText mPasswordEditText;

    @BindView(R.id.password_confirm_inputEdit)
    TextInputEditText  mConfirmPasswordEditText;

    @BindView(R.id.username_InputLayout)
    TextInputLayout mUsernameTextLayout;

    @BindView(R.id.signUp_email)
    TextInputLayout mEmailTextLayout;

    @BindView(R.id.signUp_password)
    TextInputLayout mPasswordLayout;

    @BindView(R.id.signUp_confirmPassword)
    TextInputLayout mConfirmPasswordTextLayout;

    private SignUpListener mSignUpListener;


    private Boolean usernameTest = false,
            passwordTest = false,
            confirmPasswordTest = false,
            emailTest = false;

    private  Editable checkPasswordEditable = null;



    public SignUpFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.sign_up_fragment,
                container, false);
        // Inflate the layout for this fragment
        ButterKnife.bind(this,view);


        mUsernameEditText.addTextChangedListener(new MytextWatcher() {
            @Override
            public void afterTextChanged(Editable username) {

                if (username.length() > 0) {
                    usernameTest = checkUserNameValidity(username.toString());
                    if (!usernameTest) {
                        mUsernameTextLayout.setError(
                                getString(R.string.error_username));
                    } else {
                        mUsernameTextLayout.setError("");
                    }
                }

            }
        });
        mUsernameTextLayout.setEndIconOnClickListener(v -> {
            mUsernameEditText.setText("");
            usernameTest = false;
        });

        mPasswordEditText.addTextChangedListener(new MytextWatcher() {
            @Override
            public void afterTextChanged(Editable password) {
                if (password != null){
                    passwordTest = checkPasswordValidity(password.toString());
                    if (passwordTest){
                        mPasswordLayout.setError("");
                        checkPasswordEditable = password;
                    }
                    else {
                       mPasswordLayout.
                                setError(getString(R.string.password_error));
                    }
                }

            }
        });

        mEmailEditText.addTextChangedListener(new MytextWatcher() {
            @Override
            public void afterTextChanged(Editable email) {
                if (email != null){
                    emailTest = isEmailValid(email.toString());
                    if (emailTest){
                        mEmailTextLayout.setError("");
                    } else {
                        mEmailTextLayout.setError("Please enter a valid email");
                    }
                }
            }
        });

        mEmailTextLayout.setEndIconOnClickListener(v -> {
            mEmailEditText.setText("");
            mEmailTextLayout.setError("");
            emailTest = false;
        });

        mConfirmPasswordEditText.addTextChangedListener(new MytextWatcher() {
            @Override
            public void afterTextChanged(Editable password) {
                if (password != null){
                    confirmPasswordTest = checkPasswordValidity(password.toString());
                    if (confirmPasswordTest){
                        mConfirmPasswordTextLayout.setError("");
                        if (!(password.toString().equals(checkPasswordEditable.toString()))){
                            mConfirmPasswordTextLayout.setError("Password is not the same");
                        }
                    }
                    else {
                        mConfirmPasswordTextLayout.
                                setError(getString(R.string.password_error));
                    }
                }
            }
        });

        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (usernameTest && passwordTest && confirmPasswordTest && emailTest ){
                    mSignUpListener.goToMainActivity();
                } if (!(usernameTest )){
                    mUsernameTextLayout.setError(getString(R.string.username_not_set));

                }if (!(passwordTest)){
                    mPasswordLayout.setError(getString(R.string.password_not_set));
                } if (!(emailTest)){
                    mEmailTextLayout.setError(getString(R.string.email_not_set));
                } if (!(confirmPasswordTest)){
                    mConfirmPasswordTextLayout.setError(getString(R.string.password_not_set));
                }
            }
        });

        return view;


    }

    private Boolean checkUserNameValidity(String username) {
        return username.length()>3;

    }

    private boolean checkPasswordValidity(String password) {
        return password.length() > 6;
    }

     private boolean isEmailValid(String email){
            return android.util.Patterns.EMAIL_ADDRESS.matcher(email)
                    .matches();
    }

    public interface SignUpListener{
        void goToMainActivity();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof SignUpListener){
            mSignUpListener = (SignUpListener) context;

        } else{
            throw new RuntimeException("must implement SignUpListener");
        }
    }
}
