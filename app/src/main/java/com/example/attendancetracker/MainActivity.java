package com.example.attendancetracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.LinearLayout;

import static androidx.navigation.Navigation.findNavController;


public class MainActivity extends AppCompatActivity implements MainMenuListeners {

    ConstraintLayout constraintLayout;
    LinearLayout menuListContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_host);


    }

    @Override
    public void goToHome() {
       findNavController(this,R.id.mainActivityNavigationHost)
               .navigate(R.id.home2);
    }

    @Override
    public void goToSetting() {

        findNavController(this,R.id.mainActivityNavigationHost)
                .navigate(R.id.userSettings);
    }

    @Override
    public void goToProfile() {
        findNavController(this,R.id.mainActivityNavigationHost)
                .navigate(R.id.userProfile);
    }

    @Override
    public void goToHistory() {
        findNavController(this,R.id.mainActivityNavigationHost)
                .navigate(R.id.userHistory);
    }

    @Override
    public void goToClasses() {
        findNavController(this,R.id.mainActivityNavigationHost)
                .navigate(R.id.registeredClasses);
    }
}


