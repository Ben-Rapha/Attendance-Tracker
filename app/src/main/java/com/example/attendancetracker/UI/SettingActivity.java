package com.example.attendancetracker.UI;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.transition.Fade;
import android.view.View;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.SavedStateViewModelFactory;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.attendancetracker.R;
import com.example.attendancetracker.Repository.SessionClassRepository.SessionViewModel;

import java.util.Objects;

import static androidx.navigation.Navigation.findNavController;

public class SettingActivity extends AppCompatActivity
        implements MainMenuListeners{

    private Toolbar mSettingToolbar;

    private MainMenuListeners mainMenuListeners;

    SessionViewModel sessionViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity_host);

        setTheme(R.style.PreferenceScreen);

        getWindow().setEnterTransition(new Fade(Fade.IN));
        getWindow().setExitTransition(new Fade(Fade.OUT));

        sessionViewModel = ViewModelProviders.
                of(this)
                .get(SessionViewModel.class);

        mSettingToolbar = findViewById(R.id.settingToolbarActivity);


        if (mSettingToolbar != null) {
            setSupportActionBar(mSettingToolbar);

            mSettingToolbar.setNavigationIcon(R.drawable.ic_openedmenusvg);
            mSettingToolbar.setTitle(" ");

            mSettingToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                    SettingActivity.this.overridePendingTransition(0,R.anim.fadeout);


                }
            });
        }

        getSupportFragmentManager().
                beginTransaction().
                replace(R.id.setting_activity_root_layout_id,
                        new UserSettings()).addToBackStack(null)
                .commit();


        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
                SettingActivity.this.overridePendingTransition(0,R.anim.fadeout);


            }
        };

        getOnBackPressedDispatcher().
                addCallback(this, callback);

    }

    @Override
    public void goToHome() {

    }

    @Override
    public void goToSetting() {

    }

    @Override
    public void goToProfile() {

    }

    @Override
    public void goToHistory() {

    }

    @Override
    public void goToClasses() {

    }

    @Override
    public void goToAddNewClass() {

    }

    @Override
    public void goToLoginActivity() {

    }
}
