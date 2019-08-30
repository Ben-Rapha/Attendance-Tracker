package com.example.attendancetracker.UI;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.attendancetracker.R;
import com.example.attendancetracker.Util.MyUtil;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SignUpFragment extends Fragment {

    @BindView(R.id.addClassButton)
    MaterialButton mSignInButton;

    @BindView(R.id.usernameEditText)
    TextInputEditText mUsernameEditText;

    @BindView(R.id.email_editText)
    TextInputEditText mEmailEditText;

    @BindView(R.id.password_inputEdit)
    TextInputEditText mPasswordEditText;

    @BindView(R.id.password_confirm_inputEdit)
    TextInputEditText mConfirmPasswordEditText;

    @BindView(R.id.username_InputLayout)
    TextInputLayout mUsernameTextLayout;

    @BindView(R.id.signUp_email)
    TextInputLayout mEmailTextLayout;

    @BindView(R.id.signUp_password)
    TextInputLayout mPasswordLayout;

    @BindView(R.id.signUp_confirmPassword)
    TextInputLayout mConfirmPasswordTextLayout;

    private SharedPreferences mPreferences;

    private Boolean usernameTest = false,
            passwordTest = false,
            confirmPasswordTest = false,
            emailTest = false,
            confirmPasswordPass = false;

    private Editable checkPasswordEditable = null;


    private String usernameChosen,
            passwordChosen,
            chosenEmail,
            confirmPassword;



    public SignUpFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPreferences = Objects.requireNonNull(getActivity())
                .getSharedPreferences(MyUtil.LOGIN_SHARED_PREF_FILE,
                        Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sign_up_fragment,
                container, false);
        ButterKnife.bind(this, view);

        setUpUsernameAddTextListener();

        setUpPasswordListener();

        setUpConfirmPasswordListener();

        setUpEmailListener();

        setUpSignInButtonListener(view);

        mUsernameTextLayout.setEndIconOnClickListener(v -> {
            mUsernameEditText.setText(null);
            mUsernameTextLayout.setError(null);
            usernameTest = false;
        });

        mEmailTextLayout.setEndIconOnClickListener(v -> {
            mEmailEditText.setText(null);
            mEmailTextLayout.setError(null);
            emailTest = false;
        });


        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                final NavController navController = Navigation.findNavController(view);
                navController.popBackStack();
            }
        };
        requireActivity().getOnBackPressedDispatcher().
                addCallback(getViewLifecycleOwner(), callback);

        return view;


    }

    private void setUpSignInButtonListener(View view) {
        mSignInButton.setOnClickListener(v -> {

            String username,password,email,confirmPassword2;

            boolean usernameFieldValid = false,passwordFieldValid = false,
                    emailFieldValid = false;

            username = Objects.
                    requireNonNull(mUsernameEditText.getText()).
                    toString().trim();

            password = Objects.
                    requireNonNull(mPasswordEditText.getText()).
                    toString().trim();

            email = Objects.
                    requireNonNull(mEmailEditText.getText()).
                    toString().trim();


            confirmPassword2 = Objects.
                    requireNonNull(mConfirmPasswordEditText.getText()).
                    toString().trim();


            if (username.length() < 1){
                mUsernameTextLayout.setError(getString(R.string.username_not_set));
            } else{
                usernameFieldValid = MyUtil.checkInputValidity(username);
                if (!usernameFieldValid){
                    mUsernameTextLayout.setError(
                            getString(R.string.error_username));
                } else{
                    mUsernameTextLayout.setError(null);
                    mUsernameEditText.setText(username);
                }
            }

            if (password.length() < 1){
                mPasswordLayout.setError(getString(R.string.password_not_set));
            } else{
                passwordFieldValid = MyUtil.checkPasswordValidity(password);
                if (!(passwordFieldValid)){
                    mPasswordLayout.
                            setError(getString(R.string.password_error));
                } else{
                    mPasswordLayout.setError(null);
                    mPasswordEditText.setText(passwordChosen);
                }
            }

            if (email.length() < 1){
                mEmailTextLayout.setError(getString(R.string.email_not_set));
            } else{
                emailFieldValid = isEmailValid(email);
                if (!emailFieldValid){
                    mEmailTextLayout.
                            setError(getString(R.string.enter_valid_email));
                } else{
                    mEmailTextLayout.setError(null);
                    mEmailEditText.setText(email);
                }
            }

            if (confirmPassword2.length() < 1){
                mConfirmPasswordTextLayout.setError(getString(R.string.password_not_set));
                return;
            } else{
                if (confirmPasswordTest){
                    if (confirmPassword2.equals(password)){
                        mConfirmPasswordEditText.setText(confirmPassword2);
                        mConfirmPasswordTextLayout.setError(null);
                    } else{
                        mConfirmPasswordTextLayout.
                                setError(getString(R.string.password_not_the_same));
                        return;
                    }
                }
            }

            if (passwordFieldValid && usernameFieldValid && confirmPasswordTest && emailFieldValid) {
                SharedPreferences.Editor prefEditor = mPreferences.edit();
                prefEditor.putString(MyUtil.LOGIN_USERNAME_KEY,
                        usernameChosen);
                prefEditor.putString(MyUtil.LOGIN_PASSWORD_KEY,
                        passwordChosen);
                prefEditor.putString(MyUtil.RESET_PASSCODE_KEY,
                        String.valueOf(MyUtil.getPassCodeRandom()));
                prefEditor.putString(MyUtil.LOGIN_EMAIL_KEY,chosenEmail);
                prefEditor.putBoolean(MyUtil.ALREADY_SIGNED_UP_KEY, true);
                prefEditor.apply();
                final NavController navController = Navigation.findNavController(view);
                navController.popBackStack();

            }
        });
    }

    private void setUpEmailListener() {
        mEmailEditText.addTextChangedListener(new MyTextWatcher() {
            @Override
            public void afterTextChanged(Editable email) {
                if (email != null) {
                    emailTest = isEmailValid(email.toString().trim());
                    if (emailTest) {
                        mEmailTextLayout.setError(null);
                        chosenEmail = email.toString().trim();

                    } else {
                        mEmailTextLayout.setError(getString(R.string.enter_valid_email));
                    }
                }
            }
        });
    }

    private void setUpConfirmPasswordListener() {
        mConfirmPasswordEditText.addTextChangedListener(new MyTextWatcher() {
            @Override
            public void afterTextChanged(Editable password) {
                if (password != null) {
                    confirmPasswordTest = MyUtil.checkPasswordValidity(password.toString());
                    if (confirmPasswordTest) {
                        mConfirmPasswordTextLayout.setError(null);
                        confirmPassword = password.toString().trim();
                        if (!(password.toString().equals(passwordChosen))) {
                            mConfirmPasswordTextLayout.setError(getString(R.string.password_not_the_same));
                            confirmPasswordPass = false;
                        } else{
                            confirmPasswordPass = true;
                        }
                    } else {
                        mConfirmPasswordTextLayout.
                                setError(getString(R.string.password_error));
                    }
                }
            }
        });
    }

    private void setUpPasswordListener() {
        mPasswordEditText.addTextChangedListener(new MyTextWatcher() {
            @Override
            public void afterTextChanged(Editable password) {
                if (password != null) {
                    passwordTest = MyUtil.checkPasswordValidity(password.toString());
                    if (passwordTest) {
                        mPasswordLayout.setError(null);
                        passwordChosen = password.toString().trim();

                        if ((password.toString().equals(confirmPassword))) {
                            mConfirmPasswordTextLayout.setError(null);
                            passwordChosen = password.toString().trim();
                            confirmPasswordPass = true;

                        } else {
                            mConfirmPasswordTextLayout.
                                    setError(getString(R.string.password_not_same));
                        }

                    } else {
                        mPasswordLayout.
                                setError(getString(R.string.password_error));
                    }
                }
            }
        });
    }

    private void setUpUsernameAddTextListener() {
        mUsernameEditText.addTextChangedListener(new MyTextWatcher() {
            @Override
            public void afterTextChanged(Editable username) {
                if (username != null) {
                    if (username.length() > 0) {
                        usernameTest = MyUtil.checkInputValidity(username.toString());
                        if (!usernameTest) {
                            mUsernameTextLayout.setError(
                                    getString(R.string.error_username));

                        } else {
                            mUsernameTextLayout.setError(null);
                            usernameChosen = username.toString().trim();

                        }
                    }
                }
            }
        });
    }

    private boolean checkPasswordValidity(String password) {
        return password.length() > 5;
    }

    private boolean isEmailValid(String email) {

        return android.util.Patterns.EMAIL_ADDRESS.matcher(email)
                .matches();
    }

}
