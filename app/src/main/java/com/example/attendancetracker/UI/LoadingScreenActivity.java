package com.example.attendancetracker.UI;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.transition.Explode;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityOptionsCompat;
import androidx.vectordrawable.graphics.drawable.AnimationUtilsCompat;

import com.example.attendancetracker.HandleMenuDropDownListener;
import com.example.attendancetracker.LifeCycleObservers.LoadingScreenLifeCycleObserver;
import com.example.attendancetracker.R;
import com.example.attendancetracker.Util.MyUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoadingScreenActivity extends AppCompatActivity {

    @BindView(R.id.imageViewLogo)
    ImageView mImageViewLogo;

    public static final String PROPERTY_ALPHA = "alpha";

    ObjectAnimator mAnimatorBlink;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_screen_activity);
        ButterKnife.bind(this);

        getWindow().getDecorView().setBackgroundColor(getResources().
                getColor(R.color.colorPrimaryDark));

        removeStatusBar();

        setUpLogoAnimation();

        getLifecycle().addObserver(
                new LoadingScreenLifeCycleObserver(mAnimatorBlink,this));

        setUpAnimationListener();

    }

    private void setUpAnimationListener() {
        mAnimatorBlink.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

                startActivity(new Intent(LoadingScreenActivity.this,
                        LoginActivity.class), ActivityOptions.makeSceneTransitionAnimation(
                        LoadingScreenActivity.this).toBundle());
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    private void setUpLogoAnimation() {
        mAnimatorBlink = ObjectAnimator.
                ofFloat(mImageViewLogo, PROPERTY_ALPHA, 0.0f, 1.0f);
        mAnimatorBlink.setRepeatMode(ValueAnimator.REVERSE);
        mAnimatorBlink.setDuration(1300);
        mAnimatorBlink.setRepeatCount(3);
        mAnimatorBlink.setInterpolator(new AccelerateInterpolator());
        mAnimatorBlink.start();
    }

    private void removeStatusBar() {
        getWindow().getDecorView()
                .setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}
