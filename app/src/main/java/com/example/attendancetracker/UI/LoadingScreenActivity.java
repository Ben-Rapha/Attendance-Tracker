package com.example.attendancetracker.UI;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.transition.Explode;
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
import com.example.attendancetracker.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoadingScreenActivity extends AppCompatActivity {

    @BindView(R.id.imageViewLogo)
    ImageView imageViewLogo;

    ObjectAnimator animatorBlink;

    Boolean finishLoading = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_screen_activity);
        getWindow().getDecorView().setBackgroundColor(getResources().
                getColor(R.color.colorPrimaryDark));
        ButterKnife.bind(this);
        removeStatusBar();

        getWindow().setExitTransition(new Explode());

//        ObjectAnimator animatorX = ObjectAnimator.
//                ofFloat(imageViewLogo, "scaleX", 1.0f, 1.0f);
//
//        ObjectAnimator animatorY = ObjectAnimator.
//                ofFloat(imageViewLogo, "scaleY", 0.0f, 1.0f);


        animatorBlink = ObjectAnimator.
                ofFloat(imageViewLogo, "Alpha", 0.0f, 1.0f);
        animatorBlink.setRepeatMode(ValueAnimator.REVERSE);
        animatorBlink.setDuration(1300);
        animatorBlink.setRepeatCount(3);
        animatorBlink.setInterpolator(new AccelerateInterpolator());
        animatorBlink.start();

        animatorBlink.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

                if (finishLoading){
                    Intent mIntent = new Intent(LoadingScreenActivity.this,
                            LoginActivity.class);

                    startActivity(mIntent, ActivityOptions.makeSceneTransitionAnimation(
                            LoadingScreenActivity.this).toBundle());
                }



            }

            @Override
            public void onAnimationCancel(Animator animation) {


            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });


//        AnimatorSet animatorSet = new AnimatorSet();
//
//        animatorSet.setDuration(3000)
//                .setInterpolator(new BounceInterpolator());
//
//        animatorSet.playTogether(animatorX, animatorY);
//        animatorSet.start();
//
//        animatorX.addListener(new Animator.AnimatorListener() {
//            @Override
//            public void onAnimationStart(Animator animation) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animator animation) {
//
//
////                finish();
//
//            }
//
//            @Override
//            public void onAnimationCancel(Animator animation) {
//
//            }
//
//            @Override
//            public void onAnimationRepeat(Animator animation) {
//
//            }
//        });

//        loadToLoginActivity();


    }

    private void removeStatusBar() {
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }

//    private void loadToLoginActivity() {
//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//                try {
//                    Thread.sleep(4 * 1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//
//                LoadingScreenActivity.this.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Intent mIntent = new Intent(LoadingScreenActivity.this,
//                                LoginActivity.class);
//                        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.
//                                makeSceneTransitionAnimation(LoadingScreenActivity.this,
//                                        imageViewLogo,"appLogo");
//                        startActivity(mIntent,optionsCompat.toBundle());
//                        finish();
//
//                    }
//                });
//
//            }
//        });
//        thread.start();
//    }

    //TODO set listener on menu list to navigate to right fragment
    //TODO added the various menu items fragment, next compose the navigation graph tree


    @Override
    protected void onPause() {
        super.onPause();
        finishLoading = false;
        animatorBlink.removeAllListeners();
        animatorBlink.cancel();
        animatorBlink.end();



    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    @Override
    public void onBackPressed() {
        if (finishLoading){
            super.onBackPressed();
            finish();
        }
    }
}
