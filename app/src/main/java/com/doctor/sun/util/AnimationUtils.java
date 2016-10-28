package com.doctor.sun.util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Build;
import android.view.View;
import android.view.ViewAnimationUtils;

/**
 * Created by rick on 12/8/2016.
 */

public class AnimationUtils {

    @android.databinding.BindingAdapter("android:visibilityAnimate")
    public static void visibilityWithAnimation(View view, boolean visible) {
        if (visible) {
            revealView(view);
        } else {
            hideView(view);
        }
    }

    public static void revealView(View myView) {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && myView.isAttachedToWindow()) {
            int cx = (myView.getLeft() + myView.getRight()) / 2;
            int cy = (myView.getTop() + myView.getBottom()) / 2;
            int finalRadius = Math.max(myView.getWidth(), myView.getHeight());

            Animator anim =
                    ViewAnimationUtils.createCircularReveal(myView, cx, cy, 0, finalRadius);

            myView.setVisibility(View.VISIBLE);
            anim.start();
        } else {
            myView.setVisibility(View.VISIBLE);
        }
    }

    public static void hideView(final View myView) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && myView.isAttachedToWindow()) {
            int cx = (myView.getLeft() + myView.getRight()) / 2;
            int cy = (myView.getTop() + myView.getBottom()) / 2;
            int initialRadius = myView.getWidth();

            Animator anim =
                    ViewAnimationUtils.createCircularReveal(myView, cx, cy, initialRadius, 0);

            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    myView.setVisibility(View.GONE);
                }
            });

            anim.start();
        } else {
            myView.setVisibility(View.GONE);
        }
    }
}
