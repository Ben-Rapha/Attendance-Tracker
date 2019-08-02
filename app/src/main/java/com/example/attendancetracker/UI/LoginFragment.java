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
            alreadySignedUp = false,
            onPauseCalled = false;


    private String usernameChosen,
            passwordChosen, prefUsername,
            prefPassword;


    private SharedPreferences mPreferences, mPreferencesOnPause;

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
        mPreferences = Objects.requireNonNull(getActivity())
                .getSharedPreferences(MyUtil.LOGIN_SHARED_PREF_FILE,
                        Context.MODE_PRIVATE);

        mPreferencesOnPause = getActivity().getSharedPreferences(
                MyUtil.LOGIN_SHARED_PREF_ON_PAUSE_FILE
                , Context.MODE_PRIVATE);


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

        if (mTextInputEditTextPassword != null) {
            mTextInputEditTextPassword.addTextChangedListener(new MyTextWatcher() {
                @Override
                public void afterTextChanged(Editable password) {
                    mPasswordIsSet = true;
                    mPasswordIsSet = password.toString().length() > 0;
                    isPasswordValid = MyUtil.checkPasswordValidity(password.toString());
                    passwordChosen = password.toString();
                    if (isPasswordValid) {
                        mTextInputLayoutPassword.setError("");
                        passwordChosen = password.toString();

                    } else {
                        isPasswordValid = false;
                    }

                }
            });

        }


        if (mSignInButton != null) {

            mSignInButton.setOnClickListener(new View.OnClickListener() {


                String userGivenUsername,userGivenPassword;
                boolean isUsernameSet = false, isPasswordSet = false;

                @Override
                public void onClick(View v) {



                    Editable usernameEditable = mTextInputEditTextUserName.getText();

                    Editable passwordEditable = mTextInputEditTextPassword.getText();

                    if (usernameEditable != null) {
                        usernameChosen = usernameEditable.toString();
                        isUsernameValid = MyUtil.checkInputValidity(usernameChosen);

                        if (isUsernameValid){
                            mTextInputLayoutUserName.setError(null);
                            usernameChosen = usernameChosen.trim();
                            userGivenUsername = usernameChosen;
                        } else{
                            checkFields();
                        }
                    }
                    if (passwordEditable != null) {
                      passwordChosen = passwordEditable.toString();
                      isPasswordValid = MyUtil.checkPasswordValidity(passwordChosen);
                      if (isPasswordValid){
                          mTextInputLayoutPassword.setError("");

                          userGivenPassword = passwordChosen;

                      } else{
                          checkFields();
                      }
                    }

                    checkFields();


                    if (isUsernameValid && isPasswordValid && isPasswordSet && isUsernameSet){

                        String checkUsernameValidity = mPreferences.getString(MyUtil.LOGIN_USERNAME_KEY, MyUtil.LOGIN_USERNAME_VALUE_DEFAULT);
                        String checkPasswordValidity = mPreferences.getString(MyUtil.LOGIN_PASSWORD_KEY, MyUtil.LOGIN_PASSWORD_VALUE_DEFAULT);

                        if (Objects.equals(checkUsernameValidity, userGivenUsername) &&
                                Objects.equals(checkPasswordValidity, userGivenPassword)) {
                            mLoginListener.goToMainActivity();
                            Objects.requireNonNull(getActivity()).finish();

                        } else {
                            Snackbar.make(mSignInButton,
                                    "Invalid username & password", Snackbar.LENGTH_SHORT).show();

                            mForgotPassword.setTextColor(getResources().getColor(R.color.red));
                        }

                    } else{
                        checkFields();
                    }

                }

                private void checkFields() {
                    if (!mUsernameIsSet) {
                        mTextInputLayoutUserName.setError(getString(R.string.username_not_set));
                    } else {
                        isUsernameSet = true;
                    }
                    if (!mPasswordIsSet) {
                        mTextInputLayoutPassword.setError(getString(R.string.password_not_set));
                    } else {
                        isPasswordSet = true;
                    }

                    if (isUsernameSet) {
                        if (!isUsernameValid) {
                            mTextInputLayoutUserName.setError("username must be more than 2");
                        }
                    }

                    if (isPasswordSet) {
                        if (!isPasswordValid) {
                            mTextInputLayoutPassword.setError("password must be more than 5");
                        }
                    }
                }
            });
        }


        if (mForgotPassword != null) {
            mForgotPassword.setOnClickListener(v ->

                    mLoginListener.goToResetFragment());

        }


        if (mSignUp != null) {
            mSignUp.setOnClickListener(v ->
                    mLoginListener.goToSignUpFragment());
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

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                QuitAppDialog appDialog = new QuitAppDialog();
                appDialog.setQuitAppListener(new QuitAppDialog.QuitAppListener() {
                    @Override
                    public void quitAttendanceTracker() {
                        Objects.requireNonNull(getActivity()).finish();
                    }
                });

                appDialog.show(Objects.requireNonNull(getFragmentManager()), "quit_app");
            }
        };

        requireActivity().getOnBackPressedDispatcher().
                addCallback(getViewLifecycleOwner(), callback);



        rememberMeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                rememberMe = isChecked;
                SharedPreferences.Editor prefEditor = mPreferences.edit();
//                SharedPreferences.Editor preEditorOnPause = mPreferencesOnPause.edit();
//                preEditorOnPause.putBoolean()
                prefEditor.putBoolean(MyUtil.LOGIN_REMEMBER_ME, rememberMe);
                prefEditor.apply();

            }
        });

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
        rememberMe = mPreferences.getBoolean(MyUtil.LOGIN_REMEMBER_ME, false);

        alreadySignedUp = mPreferences.getBoolean(MyUtil.ALREADY_SIGNED_UP_KEY, false);

        if (alreadySignedUp) {
            mDontHaveAnAccountTextView.setEnabled(false);
            mSignUp.setEnabled(false);
            mSignUp.setTextColor(getResources().getColor(R.color.light_color_text));
            mDontHaveAnAccountTextView.setVisibility(View.GONE);
            mSignUp.setVisibility(View.GONE);
            mForgotPassword.setTextColor(getResources().getColor(R.color.light_cyan));
            if (rememberMe){
                mLoginListener.goToMainActivity();
                Objects.requireNonNull(getActivity()).finish();

            } else{

                rememberMeSwitch.setChecked(false);
                prefUsername = mPreferences.getString(MyUtil.LOGIN_USERNAME_KEY,
                        MyUtil.LOGIN_USERNAME_VALUE_DEFAULT);

                prefPassword = mPreferences.getString(MyUtil.LOGIN_PASSWORD_KEY,
                        MyUtil.LOGIN_PASSWORD_VALUE_DEFAULT);
            }

        } else {

            mForgotPassword.setEnabled(false);
            if (rememberMe){
                prefUsername = mPreferencesOnPause.getString(MyUtil.LOGIN_USERNAME_ON_PAUSE_KEY,
                        MyUtil.LOGIN_USERNAME_VALUE_DEFAULT);

                prefPassword = mPreferencesOnPause.getString(MyUtil.LOGIN_PASSWORD_ON_PAUSE_KEY,
                        MyUtil.LOGIN_PASSWORD_VALUE_DEFAULT);
                rememberMeSwitch.setChecked(true);
            } else{
                rememberMeSwitch.setChecked(false);
            }
        }

        if (prefUsername != null){
            if (!prefUsername.equals(MyUtil.LOGIN_USERNAME_VALUE_DEFAULT)) {
                mTextInputEditTextUserName.setText(prefUsername);
            }
        }

        if (prefPassword != null){
            if (!prefPassword.equals(MyUtil.LOGIN_PASSWORD_VALUE_DEFAULT)) {
                mTextInputEditTextPassword.setText(prefPassword);
            }
        }

        ObjectAnimator animatorBlink = ObjectAnimator.
                ofFloat(logoImageView, "translationY", 100f, 0.0f);
        animatorBlink.setDuration(500);
        animatorBlink.setInterpolator(new AccelerateInterpolator());
        animatorBlink.start();
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


    void gainOrLoose(View view ,boolean setEnable){
       view.setFocusable(setEnable);
       view.setFocusableInTouchMode(setEnable);
    }




}
