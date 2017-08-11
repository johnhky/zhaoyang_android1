package com.doctor.sun.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.doctor.sun.BuildConfig;
import com.doctor.sun.R;
import com.doctor.sun.Settings;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.ActivityWelcomeBinding;
import com.doctor.sun.entity.constans.IntBoolean;
import com.doctor.sun.http.callback.TokenCallback;

import java.util.ArrayList;
import java.util.List;

import io.ganguo.library.Config;

/**
 * Created by rick on 16/2/2016.
 */
public class WelcomeActivity extends BaseFragmentActivity2 {

    private ActivityWelcomeBinding binding;
    View guide_1, guide_2, guide_3;
    SharedPreferences sharedPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_welcome);
        sharedPreferences = getSharedPreferences("MyData", 0);
        Boolean isFirst = sharedPreferences.getBoolean(Constants.FIRSTSTART, true);
        if (isFirst) {
            initView();
        } else {
            binding.getRoot().postDelayed(new Runnable() {
                @Override
                public void run() {
                    overridePendingTransition(0, 0);
                    if (Settings.isLogin()) {
                        TokenCallback.checkToken(WelcomeActivity.this);
                    } else {
                        Intent intent = LoginActivity.makeIntent(WelcomeActivity.this);
                        startActivity(intent);
                        finish();
                    }
                }
            }, 1000);
        }
    }

    public void initView() {
        binding.ivVp.setVisibility(View.VISIBLE);
        binding.ivPf.setVisibility(View.GONE);
        LayoutInflater inflater = LayoutInflater.from(this);
        if (BuildConfig.IS_DOCTOR == IntBoolean.TRUE) {
            guide_1 = inflater.inflate(R.layout.doctor_guide_1, null);
            guide_2 = inflater.inflate(R.layout.doctor_guide_2, null);
            guide_3 = inflater.inflate(R.layout.doctor_guide_3, null);
        } else {
            guide_1 = inflater.inflate(R.layout.patient_guide_1, null);
            guide_2 = inflater.inflate(R.layout.patient_guide_2, null);
            guide_3 = inflater.inflate(R.layout.patient_guide_3, null);
        }
        List<View> views = new ArrayList<>();
        views.add(guide_1);
        views.add(guide_2);
        views.add(guide_3);
        Button btnLogin = (Button) guide_3.findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferences.edit().putBoolean(Constants.FIRSTSTART, false).commit();
                Intent intent = LoginActivity.makeIntent(WelcomeActivity.this);
                startActivity(intent);
                finish();
            }
        });
        binding.ivVp.setAdapter(new MyAdapter(views));
    }

    class MyAdapter extends PagerAdapter {

        List<View> views;

        public MyAdapter(List<View> views) {
            this.views = views;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(views.get(position));
            return views.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(views.get(position));
        }

        @Override
        public int getCount() {
            return views.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

    @Override
    protected boolean shouldCheck() {
        return false;
    }
}
