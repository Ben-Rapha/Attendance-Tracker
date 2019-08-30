package com.example.attendancetracker.UI;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.SavedStateViewModelFactory;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.attendancetracker.GMailSender;
import com.example.attendancetracker.R;
import com.example.attendancetracker.Repository.SessionClassRepository.SessionModelRepository;
import com.example.attendancetracker.Repository.SessionClassRepository.SessionViewModel;
import com.example.attendancetracker.UI.Dialogs.QuitAppDialog;
import com.example.attendancetracker.UI.Dialogs.UpdateUserEmailDialog;
import com.example.attendancetracker.Util.MyUtil;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Reset_Fragment extends Fragment {

    private resetListener mResetListener;

    @BindView(R.id.reset_passcode_Button)
    MaterialButton mResetPassword;


    @BindView(R.id.reset_text)
    TextView mResetPasscodeText;

    @BindView(R.id.reset_passcode_textInputLayout)
    TextInputLayout mRestPasscodeTextInputLayout;

    @BindView(R.id.progressBarReset)
    ProgressBar progressBar;

    @BindView(R.id.parentConstraintLayout)
    ConstraintLayout mParentConstraintLayout;

    @BindView(R.id.sending_passcode)
    TextView mSendingPasscodeTextView;

    @BindView(R.id.passcode_textInputEditText)
    TextInputEditText passcodeTextInputEditText;

    @BindView(R.id.noInternet)
    TextView mNoInternetTextView;

    @BindView(R.id.retryButton)
    MaterialButton mRetryButton;

    SessionViewModel sessionViewModel;

    private ObjectAnimator objectAnimator;

    private UpdateUserEmailDialog updateUserEmailDialog;


    private static String mUserEmail;

    private static String sharedPrefPasscode, passcode;


    public Reset_Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences mPreferences = Objects.requireNonNull(getActivity())
                .getSharedPreferences(MyUtil.LOGIN_SHARED_PREF_FILE,
                        Context.MODE_PRIVATE);


        SharedPreferences.Editor prefEditor = mPreferences.edit();

        sharedPrefPasscode = String.valueOf(MyUtil.getPassCodeRandom());

        prefEditor.putString(MyUtil.RESET_PASSCODE_KEY, sharedPrefPasscode);

        prefEditor.apply();

        mUserEmail = mPreferences.getString(MyUtil.LOGIN_EMAIL_KEY, MyUtil.APP_EMAIL_DEFAULT);


    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.reset_fragment, container, false);
        ButterKnife.bind(this, view);

        passcodeTextInputEditText.addTextChangedListener(new MyTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {

                passcode = s.toString();
            }
        });

        mResetPassword.setOnClickListener(v -> {

            if (passcode != null && (passcode.equals(sharedPrefPasscode))) {
                mResetListener.goToAuthenticatePassword();

            } else {
                mRestPasscodeTextInputLayout.setError("wrong passcode");
            }
        });

        mParentConstraintLayout.setVisibility(View.VISIBLE);

        mResetPasscodeText.setVisibility(View.GONE);

        mRestPasscodeTextInputLayout.setVisibility(View.GONE);

        mResetPassword.setVisibility(View.GONE);


        progressBar.setInterpolator(new AccelerateDecelerateInterpolator());


        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (updateUserEmailDialog != null) {
                    updateUserEmailDialog.dismiss();
                }

                final NavController navController = Navigation.findNavController(view);
                navController.popBackStack();

            }
        };

        requireActivity().getOnBackPressedDispatcher().
                addCallback(getViewLifecycleOwner(), callback);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

         sessionViewModel =  ViewModelProviders.
                of(Objects.requireNonNull(getActivity()))
                .get(SessionViewModel.class);

        if (isInternetConnected()) {
            observeEmailSent(sessionViewModel);

        } else {
            noInternetConnected();
        }

        mRetryButton.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            mNoInternetTextView.setVisibility(View.GONE);
            mRetryButton.setVisibility(View.GONE);
            mSendingPasscodeTextView.setVisibility(View.VISIBLE);

            if (isInternetConnected()){
                observeEmailSent(sessionViewModel);

            } else{
                noInternetConnected();
            }

        });

    }

    private void observeEmailSent(SessionViewModel sessionViewModel) {
        sessionViewModel.sendUserPasscode(sharedPrefPasscode, mUserEmail);
        SessionModelRepository.setEmailSentListener(isEmailSentSuccessfully -> {
            if (isEmailSentSuccessfully.getValue() != null) {
                if (isEmailSentSuccessfully.getValue()) {
                    showDialogMessage();
                } else {
                    Snackbar.make(mRetryButton, getString(R.string.unable_to_send_passcode),
                            Snackbar.LENGTH_LONG).show();
                }
            }
        });

    }

    private void showDialogMessage() {
        updateUserEmailDialog = new UpdateUserEmailDialog();
        boolean dialogVisible = updateUserEmailDialog.isVisible();
        if(!dialogVisible){
            updateUserEmailDialog.setMessage(getString(R.string.sent_passcode));
            if (getFragmentManager() != null) {
                updateUserEmailDialog.show(getFragmentManager(),
                        "emailUpdate");
            }
            mSendingPasscodeTextView.setVisibility(View.GONE);
            mParentConstraintLayout.setVisibility(View.VISIBLE);
            objectAnimator = ObjectAnimator.
                    ofFloat(mParentConstraintLayout, "Alpha", 0.0f, 1.0f);
            objectAnimator.setDuration(1000);
            objectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
            objectAnimator.start();
            progressBar.setVisibility(View.GONE);
            internetConnected();
        }
    }

    private void noInternetConnected() {
        progressBar.setVisibility(View.GONE);
        mSendingPasscodeTextView.setVisibility(View.GONE);
        mNoInternetTextView.setVisibility(View.VISIBLE);
        mRetryButton.setVisibility(View.VISIBLE);
        mResetPasscodeText.setVisibility(View.GONE);
        mRestPasscodeTextInputLayout.setVisibility(View.GONE);
        mResetPassword.setVisibility(View.GONE);
    }

    private void internetConnected() {
        mSendingPasscodeTextView.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        mResetPasscodeText.setVisibility(View.VISIBLE);
        mRestPasscodeTextInputLayout.setVisibility(View.VISIBLE);
        mResetPassword.setVisibility(View.VISIBLE);
    }

    public interface resetListener {
        void goToAuthenticatePassword();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof resetListener) {
            mResetListener = (resetListener) context;
        } else {
            throw new RuntimeException("must implement resetListener");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mResetListener = null;
    }



    @Override
    public void onPause() {
        super.onPause();
        if (objectAnimator != null) {
            objectAnimator.removeAllListeners();
            objectAnimator.cancel();
            objectAnimator.end();
        }
        if (updateUserEmailDialog != null) {
            updateUserEmailDialog.dismiss();
        }

    }

    private boolean isInternetConnected() {

        ConnectivityManager cm = (ConnectivityManager) Objects.
                requireNonNull(getContext())
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo = cm.getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
