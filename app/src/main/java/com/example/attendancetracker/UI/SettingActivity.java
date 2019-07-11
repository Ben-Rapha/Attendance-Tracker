package com.example.attendancetracker.UI;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.attendancetracker.R;

import java.util.Objects;

import static androidx.navigation.Navigation.findNavController;

public class SettingActivity extends AppCompatActivity
        implements MainMenuListeners{

    private Toolbar mSettingToolbar;

    private MainMenuListeners mainMenuListeners;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity_host);
        mSettingToolbar = findViewById(R.id.settingToolbarActivity);

        if (mSettingToolbar != null) {
            setSupportActionBar(mSettingToolbar);

            mSettingToolbar.setNavigationIcon(R.drawable.openedmenusvgcoppergold);
            mSettingToolbar.setTitle(" ");

        }
        getSupportFragmentManager().
                beginTransaction().
                replace(R.id.setting_activity_root_layout_id,
                        new UserSettings()).commit();
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
}
