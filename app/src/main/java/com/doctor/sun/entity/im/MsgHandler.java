package com.doctor.sun.entity.im;

import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

/**
 * Created by rick on 15/4/2016.
 */
public class MsgHandler {
    public View.OnClickListener onAudioClick(final int childPosition){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FrameLayout frameLayout = (FrameLayout) v;
                ImageView imageView = (ImageView) frameLayout.getChildAt(childPosition);
                play(imageView);
            }
        };
    }

    private void play(View animationView) {
        if (animationView.getBackground() instanceof AnimationDrawable) {
            AnimationDrawable animation = (AnimationDrawable) animationView.getBackground();
            animation.start();
        }
    }

    private void stop(View animationView) {
        if (animationView.getBackground() instanceof AnimationDrawable) {
            AnimationDrawable animation = (AnimationDrawable) animationView.getBackground();
            animation.stop();
            animation.selectDrawable(2);
        }
    }
}

