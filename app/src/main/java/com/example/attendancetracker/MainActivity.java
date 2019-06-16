package com.example.attendancetracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.NavOptions;

import android.os.Bundle;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.LinearLayout;

import java.util.Objects;

import static androidx.navigation.Navigation.findNavController;


public class MainActivity extends AppCompatActivity
        implements MainMenuListeners{

    ConstraintLayout constraintLayout;
    LinearLayout menuListContainer;
    NavOptions navOptions,menuNavOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_host);
        navOptions = new NavOptions.Builder()
                .setEnterAnim(R.anim.fade_in)
                .setExitAnim(R.anim.fade_out)
                .setPopEnterAnim(R.anim.fade_in)
                .setPopExitAnim(R.anim.fade_out)
                .build();

        menuNavOptions = new NavOptions.Builder().
                setEnterAnim(R.anim.main_menu_slide_up)
                .setExitAnim(R.anim.main_menu_slide_down)
                .setPopEnterAnim(R.anim.main_menu_slide_up)
                .setPopExitAnim(R.anim.main_menu_slide_down)
                .build();
    }

    @Override
    public void goToHome() {
       findNavController(this,R.id.mainActivityNavigationHost)
               .navigate(R.id.home2,null,navOptions);
    }

    @Override
    public void goToSetting() {

        findNavController(this,R.id.mainActivityNavigationHost)
                .navigate(R.id.userSettings,null,navOptions);
    }

    @Override
    public void goToProfile() {
        findNavController(this,R.id.mainActivityNavigationHost)
                .navigate(R.id.userProfile,null,navOptions);
    }

    @Override
    public void goToHistory() {
        findNavController(this,R.id.mainActivityNavigationHost)
                .navigate(R.id.userHistory,null,navOptions);
    }

    @Override
    public void goToClasses() {
        findNavController(this,R.id.mainActivityNavigationHost)
                .navigate(R.id.registeredClasses,null,navOptions);
    }

    @Override
    public void goToAddNewClass() {
//        findNavController(this,R.id.mainActivityNavigationHost)
//                .navigate(R.id.addNewClass,null,navOptions);

        findNavController(this,R.id.mainActivityNavigationHost).
                navigate(R.id.addClassActivity);

    }

}


