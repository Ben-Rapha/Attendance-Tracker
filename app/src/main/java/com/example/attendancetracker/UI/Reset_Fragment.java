package com.example.attendancetracker.UI;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.attendancetracker.GMailSender;
import com.example.attendancetracker.R;
import com.example.attendancetracker.UI.Dialogs.QuitAppDialog;
import com.example.attendancetracker.UI.Dialogs.UpdateUserEmailDialog;
import com.example.attendancetracker.Util.MyUtil;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Reset_Fragment extends Fragment {

    private resetListener mResetListener;

    @BindView(R.id.reset_passcode_Button)
    MaterialButton mResetPassword;

    @BindView(R.id.progressBarReset)
    ProgressBar progressBar;

    @BindView(R.id.parentConstraintLayout)
    ConstraintLayout mParentConstraintLayout;

    @BindView(R.id.sending_passcode)
    TextView mSendingPasscodeTextView;


    @BindView(R.id.reset_passcode_textInputLayout)
    TextInputLayout passcodeTextInputLayout;

    @BindView(R.id.passcode_textInputEditText)
    TextInputEditText passcodeTextInputEditText;

    private static UpdateProgressListener updateProgressListener;

    private ObjectAnimator objectAnimator;

    private UpdateUserEmailDialog updateUserEmailDialog;



    private static String sharedPrefPasscode,passcode;

    private SharedPreferences mPreferences;


    public Reset_Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPreferences = Objects.requireNonNull(getActivity())
                .getSharedPreferences(MyUtil.LOGIN_SHARED_PREF_FILE,
                        Context.MODE_PRIVATE);


        SharedPreferences.Editor prefEditor = mPreferences.edit();

        sharedPrefPasscode = String.valueOf(MyUtil.getPassCodeRandom());

        prefEditor.putString(MyUtil.RESET_PASSCODE_KEY,sharedPrefPasscode);

        prefEditor.apply();




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

        mResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (passcode!= null &&(passcode.equals(sharedPrefPasscode))){
                    mResetListener.goToAuthenticatePassword();

                } else{
                    passcodeTextInputLayout.setError("wrong passcode");
                }
            }
        });

        mParentConstraintLayout.setVisibility(View.GONE);

        progressBar.setInterpolator(new AccelerateDecelerateInterpolator());

        new SendEmailAsyncTask().execute();

        setUpdateProgressListener(new UpdateProgressListener() {
            @Override
            public void statusProgress(String value) {



            }

            @Override
            public void dismissDialog() {
                 updateUserEmailDialog = new UpdateUserEmailDialog();
                updateUserEmailDialog.setMessage("A passcode has been sent to your email we have on file");
                if (getFragmentManager() != null) {
                    updateUserEmailDialog.show(getFragmentManager(),
                            "emailUpdate");
                }
                mSendingPasscodeTextView.setVisibility(View.GONE);
                mParentConstraintLayout.setVisibility(View.VISIBLE);
                 objectAnimator = ObjectAnimator.
                        ofFloat(mParentConstraintLayout, "Alpha", 0.0f,1.0f);
                objectAnimator.setDuration(1000);
                objectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
                objectAnimator.start();
                progressBar.setVisibility(View.GONE);

            }
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
               if (updateUserEmailDialog != null){
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

    public static class SendEmailAsyncTask extends AsyncTask<Void, String, Boolean> {

        /**
         * Override this method to perform a computation on a background thread. The
         * specified parameters are the parameters passed to {@link #execute}
         * by the caller of this task.
         * <p>
         * This method can call {@link #publishProgress} to publish updates
         * on the UI thread.
         *
         * @param voids The parameters of the task.
         * @return A result, defined by the subclass of this task.
         * @see #onPreExecute()
         * @see #onPostExecute
         * @see #publishProgress
         */
        @Override
        protected Boolean doInBackground(Void... voids) {
            try {

                GMailSender gMailSender = new GMailSender(MyUtil.APP_EMAIL, MyUtil.APP_PASSWORD);
                gMailSender.sendMail("Reset password Requested", "The reset code is: "+
                                sharedPrefPasscode,
                        MyUtil.APP_EMAIL, "Sekou.dosso82@gmail.com");

                return true;

            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            updateProgressListener.dismissDialog();


        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            updateProgressListener.statusProgress(values[0]);
        }
    }


    interface UpdateProgressListener {
        void statusProgress(String value);

        void dismissDialog();
    }


    private void setUpdateProgressListener(UpdateProgressListener listener) {
        updateProgressListener = listener;


    }

    @Override
    public void onPause() {
        super.onPause();
        if (objectAnimator!= null){
            objectAnimator.removeAllListeners();
            objectAnimator.cancel();
            objectAnimator.end();
        }

        if (updateUserEmailDialog != null){
            updateUserEmailDialog.dismiss();
        }

    }
}
