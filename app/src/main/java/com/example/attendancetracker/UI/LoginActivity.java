package com.example.attendancetracker.UI;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.attendancetracker.LoginFragment;
import com.example.attendancetracker.R;
import com.example.attendancetracker.Reset_Fragment;
import com.example.attendancetracker.SignUpFragment;
import com.google.android.material.button.MaterialButton;

import static androidx.navigation.Navigation.findNavController;




public class LoginActivity extends AppCompatActivity implements
        LoginFragment.loginListeners, Reset_Fragment.resetListener, SignUpFragment.SignUpListener {

    MaterialButton mSignInButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_host);
        mSignInButton = findViewById(R.id.addClassButton);
    }

    private void removeStatusBar() {
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }


    @Override
    public void goToMainActivity() {
        findNavController(this,R.id.login_nav_host)
                .navigate(R.id.mainActivity);
    }

    @Override
    public void goToResetFragment() {
        findNavController(this,R.id.login_nav_host).
                navigate(R.id.action_main_destination_to_reset_fragment);
    }

    @Override
    public void goToSignUpFragment() {
        findNavController(this,R.id.login_nav_host).
                navigate(R.id.action_main_destination_to_signUpFragment);
    }

    @Override
    public void goToAuthenticatePassword() {
        findNavController(this,R.id.login_nav_host)
                .navigate(R.id.action_reset_fragment_to_authenticatePasswordFragment);

    }
}
