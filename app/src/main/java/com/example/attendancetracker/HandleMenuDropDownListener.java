package com.example.attendancetracker;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.ImageView;



public class HandleMenuDropDownListener implements View.OnClickListener {
    private int rotate = 180;
    private boolean droppedDown = false,isBackgroundLogoShowing = false;
    private Context context;
    private AnimatorSet animatorSet = new AnimatorSet();
    private View view,menuListContainer;
    private Interpolator interpolator,menuInterpolator;
    private int deviceHeight;
    private Drawable openedMenu, closedMenu;
    private ImageView menuBackGroundImage;




    public HandleMenuDropDownListener(
            Context context, View animateView ,
            Drawable openedMenu , Drawable closedMenu
            ,Interpolator interpolator, Interpolator menuInterpolator,
           View menuListContainer ){

        this.context = context;
        this.view = animateView;
        this.openedMenu = openedMenu;
        this.closedMenu = closedMenu;
        this.interpolator = interpolator;
        this.menuInterpolator = menuInterpolator;
        this.menuListContainer = menuListContainer;
        deviceHeight =  getHeight() - 150;

    }

    private int getHeight() {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return displayMetrics.heightPixels;
    }


    @Override
    public void onClick(View v) {
        droppedDown = !droppedDown;
        isBackgroundLogoShowing = true;
        animatorSet.removeAllListeners();
        animatorSet.end();
        animatorSet.cancel();
        int translateY = deviceHeight - context.getResources().
                getDimensionPixelSize(R.dimen.drop_down_foreground_marginTop);
        ObjectAnimator mObjectAnimator = ObjectAnimator.ofFloat(view,
                "translationY",droppedDown ? 0 : translateY,
                droppedDown ? translateY :0);

        ObjectAnimator mMenuAnimator = ObjectAnimator.
                ofFloat(v,"rotation",
                        droppedDown ? 0 : rotate,
                        droppedDown ? rotate :0);

        ObjectAnimator menuListAnimator = ObjectAnimator.
                ofFloat(menuListContainer,"alpha",droppedDown ? 0 : 1,
                droppedDown ? 1 :0 );

        menuListAnimator.setDuration(0);
        menuListAnimator.setInterpolator(interpolator);

        mMenuAnimator.setDuration(300);
        mMenuAnimator.setInterpolator(menuInterpolator);

        mObjectAnimator.setDuration(500);
        mObjectAnimator.setInterpolator(interpolator);

        animatorSet.playSequentially(mObjectAnimator,mMenuAnimator);
        animatorSet.start();

        mObjectAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {


            }

            @Override
            public void onAnimationEnd(Animator animation) {
                updateMenuIcon(v);

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }


    private void updateMenuIcon(View view){
        if (droppedDown){
            ((ImageView) view).setImageDrawable(openedMenu);
        } else{
            ((ImageView) view).setImageDrawable(closedMenu);
        }
    }
}
