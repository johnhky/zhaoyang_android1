package com.doctor.sun.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.widget.Toast;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.ActivityLeftDrawerWraperBinding;
import com.doctor.sun.util.FragmentFactory;

/**
 * Created by rick on 17/8/2016.
 */

public class LeftDrawerFragmentActivity extends BaseFragmentActivity2 {
    private ActivityLeftDrawerWraperBinding binding;

    public static Intent intentFor(Context context, String title, Bundle contentArgs, Bundle drawerArgs) {
        Intent intent = new Intent(context, LeftDrawerFragmentActivity.class);
        intent.putExtra(Constants.FRAGMENT_TITLE, title);
        intent.putExtra(Constants.FRAGMENT_CONTENT_BUNDLE, contentArgs);
        intent.putExtra(Constants.FRAGMENT_LEFT_DRAWER_BUNDLE, drawerArgs);
        return intent;
    }

    private Fragment mainContent;
    private Fragment leftDrawer;

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
                Log.e(TAG, "OnInflate: ");
            }
        });
        View view = binding.fab.getViewStub().inflate();
        view.findViewById(R.id.fab_view_result).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "FAB", Toast.LENGTH_SHORT).show();
            }
        });

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fly_content, mainContent)
                .replace(R.id.fly_left_drawer, leftDrawer)
                .commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mainContent.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public String getMidTitleString() {
        return getStringExtra(Constants.FRAGMENT_TITLE);
    }
}
