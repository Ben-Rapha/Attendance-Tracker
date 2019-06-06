package com.example.attendancetracker.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.attendancetracker.HandleMenuDropDownListener;
import com.example.attendancetracker.R;

public class LoadingScreenActivity extends AppCompatActivity {
    ConstraintLayout constraintLayout;
    LinearLayout menuListContainer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        Toolbar toolbar = findViewById(R.id.mainActivityToolbar);
        constraintLayout = findViewById( R.id.backDrop_foreground);
        menuListContainer = findViewById(R.id.menuListLinearLayout);

        if (toolbar != null){
            toolbar.setTitle("");
            setSupportActionBar(toolbar);

            toolbar.setNavigationOnClickListener(new HandleMenuDropDownListener(
                    this, constraintLayout,getDrawable(R.drawable.ic_openedmenusvg),
                    getDrawable(R.drawable.ic_closed_menu),
                    new AccelerateDecelerateInterpolator(),
                    new OvershootInterpolator(), menuListContainer));
        }
    }

    private void removeStatusBar() {
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }

    private void loadToLoginActivity(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3*1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                startActivity(new Intent(getBaseContext(),LoginActivity.class));
            }
        });
        thread.start();
    }

    //TODO set listener on menu list to navigate to right fragment
}
