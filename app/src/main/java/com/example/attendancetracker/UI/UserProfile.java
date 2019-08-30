package com.example.attendancetracker.UI;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.attendancetracker.LifeCycleObservers.UserProfileLifeCycleObserver;
import com.example.attendancetracker.R;
import com.example.attendancetracker.UI.Dialogs.AccountUtilDialog;
import com.example.attendancetracker.Util.MyUtil;
import com.google.android.material.button.MaterialButton;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserProfile extends Fragment {

    private MainMenuListeners mainMenuListeners;

    private Toolbar mProfileToolbar;

    @BindView(R.id.logout_button)
    MaterialButton mLogOutButton;


    @BindView(R.id.username_value)
    TextView mUserNameValueTextView;

    @BindView(R.id.email_value)
    TextView mEmailValueTextView;


    @BindView(R.id.password_value)
    TextView mPasswordValueTextView;

    @BindView(R.id.hard_reset_value)
    TextView mHardResetValueTextView;


    private SharedPreferences mPreferences;


    String mUsernameValue,mEmailValue,mPasscodeValue;

    private AccountUtilDialog accountUtilDialog;


    public UserProfile() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPreferences = Objects.requireNonNull(getActivity())
                .getSharedPreferences(MyUtil.LOGIN_SHARED_PREF_FILE,
                        Context.MODE_PRIVATE);
        getLifecycle().addObserver(new UserProfileLifeCycleObserver());



    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.
                inflate(R.layout.fragment_user_profile, container, false);

        ButterKnife.bind(this,view);

        accountUtilDialog = new AccountUtilDialog();
        accountUtilDialog.setCancelable(false);

        mProfileToolbar = view.findViewById(R.id.profileToolbar);

        if (mProfileToolbar != null){
            ((AppCompatActivity) Objects.requireNonNull(getActivity())).
                    setSupportActionBar(mProfileToolbar);
            mProfileToolbar.setTitle(null);

            mProfileToolbar.setNavigationOnClickListener((View v)->{

                final NavController navController = Navigation.findNavController(view);
                navController.popBackStack();
            });
        }

        mUserNameValueTextView.setOnLongClickListener(
                new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        accountUtilDialog.isUsername(true);
                        showAccountDialog();
                        accountUtilDialog.setTitle(getString(R.string.change_username));
                        accountUtilDialog.setPreviousData(mUserNameValueTextView.getText().toString());
                        return true;
                    }
                });

        mEmailValueTextView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                accountUtilDialog.isEmail(true);
                showAccountDialog();
                accountUtilDialog.setTitle(getString(R.string.change_email));
                accountUtilDialog.setPreviousData(mEmailValueTextView.getText().toString());

                return true;
            }
        });

        mPasswordValueTextView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                accountUtilDialog.isPassword(true);
                showAccountDialog();
                accountUtilDialog.setTitle(getString(R.string.change_password));
                accountUtilDialog.setPreviousData(mPasswordValueTextView.getText().toString());
                return true;
            }
        });


                mLogOutButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SharedPreferences.Editor prefEditor = mPreferences.edit();
                        prefEditor.putBoolean(MyUtil.LOGIN_REMEMBER_ME, false);
                        prefEditor.apply();

                        if (getActivity() != null) {
                            getActivity().finish();
                        }

                        mainMenuListeners.goToLoginActivity();

                    }
                });



        mUserNameValueTextView.setText(mPreferences.getString(MyUtil.LOGIN_USERNAME_KEY,
                MyUtil.LOGIN_USERNAME_VALUE_DEFAULT));

        mEmailValueTextView.setText(mPreferences.getString(MyUtil.LOGIN_EMAIL_KEY,
                "NO EMAIL"));

        mPasswordValueTextView.setText(setPasswordAsterisks());


        mHardResetValueTextView.setText(mPreferences.getString(MyUtil.RESET_PASSCODE_KEY,
                "NO PASSCODE"));

        return view;
    }

    private void showAccountDialog() {
        if (getFragmentManager() != null) {
            accountUtilDialog.show(getFragmentManager(), "account");
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof MainMenuListeners){
            mainMenuListeners = (MainMenuListeners) context;
        } else {
            throw new RuntimeException("must implement MainMenuListeners");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mainMenuListeners = null;
    }


    private String setPasswordAsterisks() {
        StringBuilder stringBuilder = new StringBuilder();

        String password = mPreferences.
                getString(MyUtil.LOGIN_PASSWORD_KEY, MyUtil.LOGIN_PASSWORD_VALUE_DEFAULT);
        if (password != null) {
            for (int i = 0; i < password.length(); i++) {

                stringBuilder.append("*");
            }
        }
        return stringBuilder.toString();
    }


}
