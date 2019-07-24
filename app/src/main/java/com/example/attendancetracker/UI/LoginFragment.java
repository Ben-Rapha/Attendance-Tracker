package com.example.attendancetracker.UI;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.attendancetracker.R;
import com.example.attendancetracker.Util.MyUtil;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginFragment extends Fragment {

    private loginListeners mLoginListener;

    @BindView(R.id.addClassButton)
    MaterialButton mSignInButton;

    @BindView(R.id.imageViewLogo)
    ImageView logoImageView;

    @BindView(R.id.usernameEditText)
    TextInputEditText mTextInputEditTextUserName;


    @BindView(R.id.passwordEditTex)
    TextInputEditText mTextInputEditTextPassword;

    @BindView(R.id.username)
    TextInputLayout mTextInputLayoutUserName;

    @BindView(R.id.password)
    TextInputLayout mTextInputLayoutPassword;

    Boolean isPasswordValid = false,
            isUsernameValid = false,
            mUsernameIsSet = false,
            mPasswordIsSet = false;


    Editable mUsernameValidate = null;


    String usernameChosen,passwordChosen;


    @BindView(R.id.forgot_password)
    TextView mForgotPassword;

    @BindView(R.id.signUpFrom_Login)
    TextView mSignUp;


    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(LoginFragment.class.getSimpleName(), "on create is called ");




    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_fragment, container, false);

        ButterKnife.bind(this, view);

        if (mTextInputEditTextUserName != null) {
            mTextInputEditTextUserName.addTextChangedListener(new MyTextWatcher() {
                @Override
                public void afterTextChanged(Editable username) {
                    mUsernameIsSet = username.toString().length()>0;
                    isUsernameValid = MyUtil.checkInputValidity(username.toString());
                    if (isUsernameValid){
                        mTextInputLayoutUserName.setError(null);
                        usernameChosen = username.toString().trim();

                    } else {
                        isUsernameValid = false;
                    }


                }
            });



            mTextInputEditTextUserName.
                    setOnClickListener(v -> {
                        mTextInputEditTextUserName.setFocusableInTouchMode(true);
                        mTextInputEditTextUserName.setFocusable(true);
                        showKeyboard(v);
                    });

        }


        if (mTextInputEditTextPassword != null) {
            mTextInputEditTextPassword.addTextChangedListener(new MyTextWatcher() {
                @Override
                public void afterTextChanged(Editable password) {
                    mPasswordIsSet = true;
                    mPasswordIsSet = password.toString().length()>0;
                        isPasswordValid = MyUtil.checkPasswordValidity(password.toString());
                        if (isPasswordValid) {
                            mTextInputLayoutPassword.setError("");
                            passwordChosen = password.toString();

                        } else{
                            isPasswordValid = false;
                        }

                }
            });
            mTextInputEditTextPassword.
                    setOnClickListener(v -> {
                        mTextInputEditTextPassword.setFocusable(true);
                        mTextInputEditTextPassword.setFocusableInTouchMode(true);
                        showKeyboard(v);
                    });
        }


        if (mSignInButton != null) {
            mSignInButton.setOnClickListener(new View.OnClickListener() {

                boolean isUsernameSet = false, isPasswordSet = false;
                @Override
                public void onClick(View v) {
                    if (isUsernameValid && isPasswordValid ) {
                        mLoginListener.goToMainActivity();

                    } else {

                        if (!mUsernameIsSet) {
                            mTextInputLayoutUserName.setError(getString(R.string.username_not_set));
                        } else{
                            isUsernameSet = true;
                        }
                        if (!mPasswordIsSet) {
                            mTextInputLayoutPassword.setError(getString(R.string.password_not_set));
                        } else{
                            isPasswordSet = true;
                        }

                        if (isUsernameSet){
                            if (!isUsernameValid) {
                                mTextInputLayoutUserName.setError("username must be more than 2");
                            }
                        }

                        if (isPasswordSet){
                            if (!isPasswordValid){
                                mTextInputLayoutPassword.setError("password must be more than 5");
                            }
                        }

                    }
                }
            });
        }


        if (mForgotPassword != null) {
            mForgotPassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mLoginListener.goToResetFragment();
                }
            });

        }


        if (mSignUp != null) {
            mSignUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mLoginListener.goToSignUpFragment();
                }
            });
        }


        if (mTextInputLayoutUserName != null) {
            mTextInputLayoutUserName.setEndIconOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mTextInputEditTextUserName.setText("");
                    isUsernameValid = false;
                }
            });
        }




        return view;
    }





    private boolean checkUserNameValidity(String username) {

        return username.length() > 3;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof loginListeners) {
            mLoginListener = (LoginFragment.loginListeners) context;
        } else {
            throw new RuntimeException("must implement onButtonClickListener");
        }
    }

    public interface loginListeners {
        void goToMainActivity();

        void goToResetFragment();

        void goToSignUpFragment();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mLoginListener = null;

    }

    @Override
    public void onResume() {
        super.onResume();

        MyUtil.loseOrGainFocus(mTextInputEditTextUserName, false);
        MyUtil.loseOrGainFocus(mTextInputEditTextPassword, false);
        ObjectAnimator animatorBlink = ObjectAnimator.
                ofFloat(logoImageView,"translationY",100f,0.0f);
        animatorBlink.setDuration(1000);
        animatorBlink.setInterpolator(new AccelerateInterpolator());
        animatorBlink.start();
    }

    @Override
    public void onPause() {
        super.onPause();
    }


    void showKeyboard(View view){
        InputMethodManager inputMethodManager = (InputMethodManager) Objects.requireNonNull(getActivity()).
                getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }


}
