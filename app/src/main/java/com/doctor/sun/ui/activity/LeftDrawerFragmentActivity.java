package com.doctor.sun.ui.activity;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.ActivityLeftDrawerWraperBinding;
import com.doctor.sun.event.HideFABEvent;
import com.doctor.sun.event.ShowFABEvent;
import com.doctor.sun.util.FragmentFactory;

/**
 * Created by rick on 17/8/2016.
 */

public class LeftDrawerFragmentActivity extends BaseFragmentActivity2 {
    private ActivityLeftDrawerWraperBinding binding;

    public static Intent intentFor(Context context, String title, Bundle contentArgs, Bundle drawerArgs) {
        return intentFor(context, title, null, contentArgs, drawerArgs);
    }

    public static Intent intentFor(Context context, String title, String fabText, Bundle contentArgs, Bundle drawerArgs) {
        Intent intent = new Intent(context, LeftDrawerFragmentActivity.class);
        intent.putExtra(Constants.FRAGMENT_TITLE, title);
        intent.putExtra(Constants.FRAGMENT_CONTENT_BUNDLE, contentArgs);
        intent.putExtra(Constants.FRAGMENT_FAB_TEXT, fabText);
        intent.putExtra(Constants.FRAGMENT_LEFT_DRAWER_BUNDLE, drawerArgs);
        return intent;
    }

    private Fragment mainContent;
    private Fragment leftDrawer;
    private View resultButton;
    private boolean animating;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_left_drawer_wraper);


        Bundle contentArgs = getIntent().getBundleExtra(Constants.FRAGMENT_CONTENT_BUNDLE);
        mainContent = FragmentFactory.getInstance().get(contentArgs);

        Bundle leftDrawerArgs = getIntent().getBundleExtra(Constants.FRAGMENT_LEFT_DRAWER_BUNDLE);
        leftDrawer = FragmentFactory.getInstance().get(leftDrawerArgs);

        binding.fab.setOnInflateListener(new ViewStub.OnInflateListener() {
            @Override
            public void onInflate(ViewStub stub, View inflated) {
                TextView viewById = (TextView) inflated.findViewById(R.id.tv_show_drawer);
                viewById.setText(getStringExtra(Constants.FRAGMENT_FAB_TEXT));

                inflated.findViewById(R.id.fab_show_drawer).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        binding.drawerLayout.openDrawer(GravityCompat.END);
                    }
                });
            }
        });
        resultButton = binding.fab.getViewStub().inflate();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fly_content, mainContent)
                .replace(R.id.fly_left_drawer, leftDrawer)
                .commit();
    }

    @com.squareup.otto.Subscribe
    public void onEventMainThread(HideFABEvent event) {
        if (resultButton == null || animating) {
            return;
        }
     /*   resultButton.animate()
                .translationY(resultButton.getHeight())
                .alpha(0)
                .setDuration(250)
                .setListener(getAnimationListener())
                .setInterpolator(new DecelerateInterpolator());*/

    }

    @com.squareup.otto.Subscribe
    public void onEventMainThread(ShowFABEvent event) {
        if (resultButton == null || animating) {
            return;
        }
       resultButton.animate()
                .translationY(0)
                .alpha(1)
                .setDuration(250)
                .setListener(getAnimationListener())
                .setInterpolator(new DecelerateInterpolator());
    }

    @NonNull
    public Animator.AnimatorListener getAnimationListener() {
        return new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                animating = true;
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                animating = false;
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                animating = false;
            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mainContent.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public String getMidTitleString() {
        return getStringExtra(Constants.FRAGMENT_TITLE);
    }

    @Override
    public void onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.END)) {
            binding.drawerLayout.closeDrawer(GravityCompat.END);
        } else {
            super.onBackPressed();
        }
    }
}
