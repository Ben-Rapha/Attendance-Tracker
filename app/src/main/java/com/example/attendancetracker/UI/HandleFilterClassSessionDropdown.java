package com.example.attendancetracker.UI;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Interpolator;

import androidx.recyclerview.widget.RecyclerView;

import com.example.attendancetracker.R;

public class HandleFilterClassSessionDropdown
        implements View.OnClickListener {

    Context context;

    private View animatedView, clickView;

    RecyclerView parentRecyclerView;

    private Interpolator interpolator;

    boolean droppedDown = true;

    private AnimatorSet animatorSet = new AnimatorSet();

    private int deviceHeight;



   public HandleFilterClassSessionDropdown(Context context, View animateView,
                                           Interpolator interpolator,RecyclerView parentRecyclerView ){

       this.context = context;
       this.animatedView = animateView;
       this.interpolator = interpolator;
       this.parentRecyclerView = parentRecyclerView;
       deviceHeight = getHeight();

   }

    private int getHeight() {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return displayMetrics.heightPixels;
    }


    @Override
    public void onClick(View v) {
       clickView = v;
       performAnimation();
    }

    public void performAnimation(){
       droppedDown = !droppedDown;
       animatorSet.removeAllListeners();
       animatorSet.end();
       animatorSet.cancel();


        int translateY = getHeight();

        ObjectAnimator mObjectAnimator = ObjectAnimator.ofFloat(animatedView,
                "translationY", droppedDown ? 0 : translateY,
                droppedDown ? translateY : 0);


        mObjectAnimator.setDuration(500);
        mObjectAnimator.setInterpolator(interpolator);

        animatorSet.playSequentially(mObjectAnimator);
        animatorSet.start();

        mObjectAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                parentRecyclerView.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });



    }
}
