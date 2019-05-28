package com.example.attendancetracker.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.attendancetracker.Login_Fragment;
import com.example.attendancetracker.R;
import com.google.android.material.button.MaterialButton;




public class LoginActivity extends AppCompatActivity implements
        Login_Fragment.onButtonClickListener {

    MaterialButton mSignInButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_host);
        mSignInButton = findViewById(R.id.signInButton);
    }

    private void removeStatusBar() {
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }

    @Override
    public void goToDestination() {
      androidx.navigation.Navigation.findNavController(this,R.id.login_nav_host)
              .navigate(R.id.action_main_destination_to_signUpFragment);
    }
}
