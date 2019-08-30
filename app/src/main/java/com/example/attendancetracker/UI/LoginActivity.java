package com.example.attendancetracker.UI;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavOptions;

import com.example.attendancetracker.LifeCycleObservers.LoginActivityLifeCycleObserver;
import com.example.attendancetracker.R;
import com.google.android.material.button.MaterialButton;

import static androidx.navigation.Navigation.findNavController;




public class LoginActivity extends AppCompatActivity implements
        LoginFragment.loginListeners, Reset_Fragment.resetListener{

    NavOptions options;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_host);

        getWindow().setEnterTransition(new Fade(Fade.IN));

        getLifecycle().addObserver(new LoginActivityLifeCycleObserver());


         options = new NavOptions.Builder().
                setEnterAnim(R.anim.fadein).
                setPopEnterAnim(R.anim.fadein).build();
    }

    private void removeStatusBar() {
        View decorView = getWindow().getDecorView();
        getWindow().getDecorView()
                .setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
    }


    @Override
    public void goToMainActivity() {
        findNavController(this,R.id.login_nav_host)
                .navigate(R.id.mainActivity,null,options);
    }

    @Override
    public void goToResetFragment() {
        findNavController(this,R.id.login_nav_host).
                navigate(R.id.action_main_destination_to_reset_fragment,null,options);
    }

    @Override
    public void goToSignUpFragment() {
        findNavController(this,R.id.login_nav_host).
                navigate(R.id.action_main_destination_to_signUpFragment,null,options);

    }

    @Override
    public void goToAuthenticatePassword() {
        findNavController(this,R.id.login_nav_host)
                .navigate(R.id.action_reset_fragment_to_authenticatePasswordFragment,
                        null,options);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}
