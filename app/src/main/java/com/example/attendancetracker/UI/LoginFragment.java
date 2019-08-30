package com.example.attendancetracker.UI;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.attendancetracker.LifeCycleObservers.LoginFragmentLifeCycleObserver;
import com.example.attendancetracker.R;
import com.example.attendancetracker.UI.Dialogs.QuitAppDialog;
import com.example.attendancetracker.Util.MyUtil;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Locale;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginFragment extends Fragment {

    private loginListeners mLoginListener;

    @BindView(R.id.dont_have_account_textView)
    TextView mDontHaveAnAccountTextView;

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

    @BindView(R.id.rememberMeSwitch)
    SwitchMaterial rememberMeSwitch;

    private Boolean isPasswordValid = false,
            isUsernameValid = false,
            mUsernameIsSet = false,
            mPasswordIsSet = false,
            rememberMe = false,
            alreadySignedUp = false;


    private String usernameChosen,
            passwordChosen, prefUsername,
            prefPassword, onPauseUsername, onPausePassword;


    private SharedPreferences mPreferences, mPreferencesOnPause;

    private LoginFragmentLifeCycleObserver lifeCycleObserver;

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
        setUpSharedPreference();
    }



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_fragment, container, false);

        ButterKnife.bind(this, view);

        setUpUsernameEditTextListener();

        setUpPasswordEditTextListener();

        setUpSignInButtonListener();

        setUpForgotPasswordListener();

        setUpSignUpListener();

        setUpUsernameEndIconListener();

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                QuitAppDialog appDialog = new QuitAppDialog();
                appDialog.setQuitAppListener(() ->
                        Objects.requireNonNull(getActivity()).finish());

                appDialog.show(Objects.
                                requireNonNull(getFragmentManager()),
                        getString(R.string.quit_app));

            }
        };
        requireActivity().getOnBackPressedDispatcher().
                addCallback(getViewLifecycleOwner(), callback);


        rememberMeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            rememberMe = isChecked;
            SharedPreferences.Editor prefEditor = mPreferences.edit();
            prefEditor.putBoolean(MyUtil.LOGIN_REMEMBER_ME, rememberMe);
            prefEditor.apply();

        });

        return view;
    }

    private void setUpUsernameEndIconListener() {
        if (mTextInputLayoutUserName != null) {
            mTextInputLayoutUserName.setEndIconOnClickListener(v -> {
                mTextInputEditTextUserName.setText(null);
                isUsernameValid = false;
            });
        }
    }

    private void setUpForgotPasswordListener() {
        if (mForgotPassword != null) {
            mForgotPassword.setOnClickListener(v ->
                    mLoginListener.goToResetFragment());
        }
    }

    private void setUpSignUpListener() {
        if (mSignUp != null) {
            mSignUp.setOnClickListener(v ->
                    mLoginListener.goToSignUpFragment());
        }
    }

    private void setUpSignInButtonListener() {

        if (mSignInButton != null) {
            mSignInButton.setOnClickListener(v -> {
                String usernameField = "",passwordField ="",usernameTrimed = "",passwordTrimed = "";
                boolean usernameFieldValid = false,passwordFieldValid = false;

                usernameTrimed = Objects.requireNonNull(mTextInputEditTextUserName.
                        getText()).toString().trim();

                mTextInputEditTextUserName.setText(usernameTrimed);

                passwordTrimed = Objects.requireNonNull(mTextInputEditTextPassword.
                        getText()).toString().trim();

                mTextInputEditTextPassword.setText(passwordTrimed);


                if (Objects.requireNonNull(usernameTrimed).length() < 1){
                    mTextInputLayoutUserName.setError(getString(R.string.username_not_set));
                } else{
                    usernameField = usernameTrimed;
                    usernameFieldValid = MyUtil.checkInputValidity(usernameField);
                    if (!usernameFieldValid){
                        mTextInputLayoutUserName.setError("username must be 2 or more");
                    } else{
                        mTextInputLayoutUserName.setError(null);
                        usernameField = usernameField.trim();
                    }
                }

                if (Objects.requireNonNull(passwordTrimed).length() < 1){
                    mTextInputLayoutPassword.setError(getString(R.string.password_not_set));
                    return;
                } else{
                    passwordField = passwordTrimed;
                    passwordFieldValid = MyUtil.checkPasswordValidity(passwordField);
                    if (!passwordFieldValid){
                        mTextInputLayoutPassword.setError("password must be more than 5");
                        return;
                    } else{
                        mTextInputLayoutPassword.setError(null);
                    }
                }
                if (usernameFieldValid && passwordFieldValid) {

                    String checkUsernameValidity = mPreferences.
                            getString(MyUtil.LOGIN_USERNAME_KEY, MyUtil.LOGIN_USERNAME_VALUE_DEFAULT);
                    String checkPasswordValidity = mPreferences.
                            getString(MyUtil.LOGIN_PASSWORD_KEY, MyUtil.LOGIN_PASSWORD_VALUE_DEFAULT);

                    if (Objects.equals(checkUsernameValidity, usernameField) &&
                            Objects.equals(checkPasswordValidity, passwordField)) {
                        mLoginListener.goToMainActivity();
                        Objects.requireNonNull(getActivity()).finish();

                    } else {
                        if (alreadySignedUp){
                            Snackbar.make(mSignInButton,
                                    getString(R.string.invalid_username_and_password),
                                    Snackbar.LENGTH_SHORT).show();
                            mForgotPassword.setTextColor(getResources().
                                    getColor(R.color.light_cyan));
                            mForgotPassword.setEnabled(true);
                        } else{
                            Snackbar.make(mSignInButton,
                                    getString((R.string.no_account)),
                                    Snackbar.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }
    }

    private void setUpPasswordEditTextListener() {
        if (mTextInputEditTextPassword != null) {
            mTextInputEditTextPassword.addTextChangedListener(new MyTextWatcher() {
                @Override
                public void afterTextChanged(Editable password) {
                    mPasswordIsSet = true;
                    mPasswordIsSet = password.toString().length() > 0;
                    isPasswordValid = MyUtil.checkPasswordValidity(password.toString());
                    passwordChosen = password.toString();
                    if (isPasswordValid) {
                        mTextInputLayoutPassword.setError(null);
                        passwordChosen = password.toString().trim();

                    } else {
                        isPasswordValid = false;
                    }

                }
            });

        }
    }

    private void setUpUsernameEditTextListener() {
        if (mTextInputEditTextUserName != null) {
            mTextInputEditTextUserName.addTextChangedListener(new MyTextWatcher() {
                @Override
                public void afterTextChanged(Editable username) {
                    mUsernameIsSet = username.toString().length() > 0;
                    isUsernameValid = MyUtil.checkInputValidity(username.toString());
                    usernameChosen = username.toString();
                    if (isUsernameValid) {
                        mTextInputLayoutUserName.setError(null);
                        usernameChosen = username.toString().trim();

                    } else {
                        isUsernameValid = false;
                    }
                }
            });
        }
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


    @Override
    public void onDestroy() {
        super.onDestroy();
        mLoginListener = null;

    }

    @Override
    public void onResume() {
        super.onResume();
        setUpSharedPreference();
        animateLogo();
        setUpSwitchButton();
        if (alreadySignedUp) {
            mDontHaveAnAccountTextView.setEnabled(false);
            mSignUp.setEnabled(false);
            mSignUp.setTextColor(getResources().getColor(R.color.light_color_text));
            mDontHaveAnAccountTextView.setVisibility(View.GONE);
            mSignUp.setVisibility(View.GONE);
            if (rememberMe && isSharedPreferencePasswordsEqual()
                    && isSharedPreferencesUsernameEqual()) {
                mLoginListener.goToMainActivity();
                Objects.requireNonNull(getActivity()).finish();

            } else {
                setUpLoginFields();
            }
        } else {
            mForgotPassword.setEnabled(false);
            setUpLoginFields();
        }
    }

    private void animateLogo() {
        ObjectAnimator animatorBlink = ObjectAnimator.
                ofFloat(logoImageView, "translationY", 100f, 0.0f);
        animatorBlink.setDuration(500);
        animatorBlink.setInterpolator(new AccelerateInterpolator());
        animatorBlink.start();
    }

    private void setUpLoginFields() {
        if (alreadySignedUp){
            if (!(Objects.equals(onPauseUsername,
                    MyUtil.LOGIN_USERNAME_VALUE_DEFAULT_ON_PAUSE))) {
                mTextInputEditTextUserName.setText(onPauseUsername);
                mTextInputEditTextPassword.setText(onPausePassword);

            } else {
                mTextInputEditTextUserName.setText(prefUsername);
                mTextInputEditTextPassword.setText(prefPassword);
            }
        }else if (!(Objects.equals(onPauseUsername,
                MyUtil.LOGIN_USERNAME_VALUE_DEFAULT_ON_PAUSE)) && rememberMe){
            mTextInputEditTextUserName.setText(onPauseUsername);
            mTextInputEditTextPassword.setText(onPausePassword);
        }

    }

    private void setUpSwitchButton() {
        if (rememberMe) {
            rememberMeSwitch.setChecked(true);
        } else {
            rememberMeSwitch.setChecked(false);
        }
    }

    private boolean isSharedPreferencePasswordsEqual() {
        String onPausePassword = mPreferencesOnPause.getString(MyUtil.LOGIN_PASSWORD_ON_PAUSE_KEY,
                MyUtil.LOGIN_PASSWORD_VALUE_DEFAULT_ON_PAUSE);
        String password = mPreferences.getString(MyUtil.LOGIN_PASSWORD_KEY,
                MyUtil.LOGIN_PASSWORD_VALUE_DEFAULT);
        return Objects.equals(password, onPausePassword);
    }

    private boolean isSharedPreferencesUsernameEqual() {
        String username = mPreferences.getString(MyUtil.LOGIN_USERNAME_KEY,
                MyUtil.LOGIN_USERNAME_VALUE_DEFAULT);

        String onPauseUsername = mPreferencesOnPause.getString(MyUtil.LOGIN_USERNAME_ON_PAUSE_KEY,
                MyUtil.LOGIN_USERNAME_VALUE_DEFAULT_ON_PAUSE);

        return Objects.requireNonNull(username).equals(onPauseUsername);
    }

    private void setUpSharedPreference() {
        mPreferences = Objects.requireNonNull(getActivity())
                .getSharedPreferences(MyUtil.LOGIN_SHARED_PREF_FILE,
                        Context.MODE_PRIVATE);

        mPreferencesOnPause = getActivity().getSharedPreferences(
                MyUtil.LOGIN_SHARED_PREF_ON_PAUSE_FILE
                , Context.MODE_PRIVATE);

        prefUsername = mPreferences.getString(MyUtil.LOGIN_USERNAME_KEY,
                MyUtil.LOGIN_USERNAME_VALUE_DEFAULT);

        prefPassword = mPreferences.getString(MyUtil.LOGIN_PASSWORD_KEY,
                MyUtil.LOGIN_PASSWORD_VALUE_DEFAULT);

        rememberMe = mPreferences.getBoolean(MyUtil.LOGIN_REMEMBER_ME, false);

        onPauseUsername = mPreferencesOnPause.getString(MyUtil.LOGIN_USERNAME_ON_PAUSE_KEY,
                MyUtil.LOGIN_USERNAME_VALUE_DEFAULT_ON_PAUSE);

        onPausePassword = mPreferencesOnPause
                .getString(MyUtil.LOGIN_PASSWORD_ON_PAUSE_KEY,
                        MyUtil.LOGIN_PASSWORD_VALUE_DEFAULT_ON_PAUSE);

        alreadySignedUp = mPreferences.getBoolean(MyUtil.ALREADY_SIGNED_UP_KEY, false);
    }

    @Override
    public void onPause() {
        super.onPause();
        mPreferencesOnPause = Objects.requireNonNull(getActivity()).getSharedPreferences(MyUtil.
                LOGIN_SHARED_PREF_ON_PAUSE_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mPreferencesOnPause.edit();
        editor.putString(MyUtil.LOGIN_USERNAME_ON_PAUSE_KEY, usernameChosen);
        editor.putString(MyUtil.LOGIN_PASSWORD_ON_PAUSE_KEY, passwordChosen);
        editor.putBoolean(MyUtil.LOGIN_REMEMBER_ME_ON_PAUSE, rememberMe);
        editor.putBoolean(MyUtil.LOGIN_ON_PAUSE_CALLED_KEY, true);
        editor.apply();
    }


    private void showKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) Objects.requireNonNull(getActivity()).
                getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }

    private void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) Objects.requireNonNull(getActivity()).
                getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(view, InputMethodManager.HIDE_NOT_ALWAYS);
    }


    void gainOrLoose(View view, boolean setEnable) {
        view.setFocusable(setEnable);
        view.setFocusableInTouchMode(setEnable);
    }


    public interface loginListeners {
        void goToMainActivity();

        void goToResetFragment();

        void goToSignUpFragment();
    }


}
