package com.example.attendancetracker;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoginFragment extends Fragment {

   private loginListeners mLoginListener;

  private MaterialButton mSignInButton;

  TextInputEditText mTextInputEditTextUserName,mTextInputEditTextPassword;

  TextInputLayout mTextInputLayoutUserName,mTextInputLayoutPassword;

    Boolean passwordValid = false,
            usernameTest = false,
            mUsernameHasFocus = false,
            mPasswordHasFocus = false;


    Editable mUsernameValidate = null;




  private TextView mForgotPassword;

  private TextView mSignUp;



    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(LoginFragment.class.getSimpleName(),"on create is called ");


    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view =  inflater.inflate(R.layout.login_fragment,container,false);
        Log.v(LoginFragment.class.getSimpleName(),"on createView is called ");

       mSignInButton = view.findViewById(R.id.addClassButton);
       mForgotPassword = view.findViewById(R.id.forgot_password);
       mSignUp = view.findViewById(R.id.signUpFrom_Login);
       mTextInputLayoutUserName = view.findViewById(R.id.username);
       mTextInputLayoutPassword = view.findViewById(R.id.password);
       mTextInputEditTextUserName = view.findViewById(R.id.usernameEditText);
       mTextInputEditTextPassword = view.findViewById(R.id.passwordEditTex);

       mTextInputEditTextUserName.addTextChangedListener(new MytextWatcher() {
           @Override
           public void afterTextChanged(Editable username) {
               mUsernameHasFocus = true;
               if (username.length() > 0) {
                   mUsernameValidate = username;
                   usernameTest = checkUserNameValidity(username.toString());
                   if (!usernameTest) {
                       mTextInputLayoutUserName.setError(
                               getString(R.string.error_username));
                   } else {
                       mTextInputLayoutUserName.setError("");
                   }
               }
           }
       });



       mTextInputEditTextPassword.addTextChangedListener(new MytextWatcher() {
           @Override
           public void afterTextChanged(Editable password) {
               mPasswordHasFocus = true;
               if (password != null){
                   passwordValid = checkPasswordValidity(password.toString());
                   if (passwordValid){
                       mTextInputLayoutPassword.setError("");
                   }
                   else {
                       mTextInputLayoutPassword.
                               setError(getString(R.string.password_error));
                   }
               }
           }
       });


       mSignInButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if (usernameTest && passwordValid && checkUserNameValidity(mUsernameValidate.toString())){
                   mLoginListener.goToMainActivity();
               } else {

                   if (!mUsernameHasFocus){
                       mTextInputLayoutUserName.setError(getString(R.string.username_not_set));
                   }
                   if (! mPasswordHasFocus){
                       mTextInputLayoutPassword.setError(getString(R.string.password_not_set));
                   }if (!usernameTest){
                       mTextInputLayoutUserName.setError(getString(R.string.username_not_set));
                   }

               }
           }
       });

       mForgotPassword.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               mLoginListener.goToResetFragment();
           }
       });

       mSignUp.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               mLoginListener.goToSignUpFragment();
           }
       });

       mTextInputLayoutUserName.setEndIconOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               mTextInputEditTextUserName.setText("");
               usernameTest = false;
           }
       });

       return view;
    }


    private boolean checkPasswordValidity(String password) {
        return password.length() > 6;
    }


    private boolean checkUserNameValidity(String username){

        return username.length()>3;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof loginListeners){
            mLoginListener = (LoginFragment.loginListeners) context;
        } else{
            throw new RuntimeException("must implement onButtonClickListener");
        }
    }

    public interface loginListeners{
        void goToMainActivity();
        void goToResetFragment();
        void goToSignUpFragment();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mLoginListener = null;
        Log.v(LoginFragment.class.getSimpleName(),"on destroy is called ");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.v(LoginFragment.class.getSimpleName(),"on resume is called ");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.v(LoginFragment.class.getSimpleName(),"on pause is called ");
    }
}
