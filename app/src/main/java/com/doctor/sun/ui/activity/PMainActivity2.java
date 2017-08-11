package com.doctor.sun.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.doctor.sun.AppContext;
import com.doctor.sun.R;
import com.doctor.sun.Settings;
import com.doctor.sun.databinding.PMainActivity2Binding;
import com.doctor.sun.dto.PageDTO;
import com.doctor.sun.dto.PatientDTO;
import com.doctor.sun.entity.Article;
import com.doctor.sun.entity.CallConfig;
import com.doctor.sun.entity.SystemMsg;
import com.doctor.sun.event.MainTabChangedEvent;
import com.doctor.sun.event.ProgressEvent;
import com.doctor.sun.event.ReadMessageEvent;
import com.doctor.sun.event.UpdateEvent;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.ApiCallback;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.module.ProfileModule;
import com.doctor.sun.module.PushModule;
import com.doctor.sun.module.ToolModule;
import com.doctor.sun.ui.activity.patient.PConsultingActivity;
import com.doctor.sun.ui.activity.patient.PMainActivity;
import com.doctor.sun.ui.activity.patient.PMeActivity;
import com.doctor.sun.ui.adapter.SimpleAdapter;
import com.doctor.sun.ui.handler.patient.PMainHandler;
import com.doctor.sun.ui.model.FooterViewModel;
import com.doctor.sun.ui.widget.SelectRecordDialog;
import com.doctor.sun.util.DialogUtils;
import com.doctor.sun.util.NotificationUtil;
import com.doctor.sun.util.PermissionUtil;
import com.doctor.sun.util.UpdateUtil;
import com.squareup.otto.Subscribe;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.ganguo.library.Config;
import io.ganguo.library.core.event.EventHub;
import io.ganguo.library.util.Systems;

public class PMainActivity2 extends AppCompatActivity {

    private PMainActivity2Binding binding;

    private PMainHandler handler;
    public List<View> mViewList = new ArrayList<>();

    public static Intent makeIntent(Context context) {
        return new Intent(context, PMainActivity2.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.p_main_activity2);

        Systems.setStatusTranslucent(this);

        handler = new PMainHandler();
        binding.setData(handler);

        FooterViewModel footer = getFooter();
        binding.setFooter(footer);

        setPatientProfile();
        initDoctorView();
        showBanner();
        showUnreadMessageCount();
        showArticle();
        /*SelectRecordDialog.showNewRecordDialog(this);*/
    }

    public void showArticle() {
        ToolModule api = Api.of(ToolModule.class);
        api.getArticles().enqueue(new SimpleCallback<List<Article>>() {
            @Override
            protected void handleResponse(List<Article> response) {
                if (response.size() > 0) {
                    binding.llArticle.setVisibility(View.VISIBLE);
                    initScrollViewPager(response.size(), response);
                }

            }
        });
    }

    public void initScrollViewPager(int size, final List<Article> datas) {
        WindowManager manager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        int screenWinth = manager.getDefaultDisplay().getWidth();
        int screenHeight = manager.getDefaultDisplay().getHeight();
        for (int i = 0; i < size; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setAdjustViewBounds(true);
            imageView.setMaxWidth(screenWinth);
            int result = (int) (screenHeight * 0.33);
            imageView.setMaxHeight(result);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            Glide.with(AppContext.me()).load(datas.get(i).getImage()).placeholder(R.drawable.bg_main).into(imageView);
            mViewList.add(imageView);
            final int position = i;
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent to = WebBrowserActivity.intentFor(PMainActivity2.this, datas.get(position).getUrl(), datas.get(position).getTitle());
                    startActivity(to);
                }
            });
        }
        new Thread(runnable).start();
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (mViewList.size() > 0) {
                mHandler.sendEmptyMessage(0);
            }
        }
    };

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            binding.ivVp.start(PMainActivity2.this, mViewList, 5000, null, 0, 0, 0, 0);
        }
    };

    private void showBanner() {
        ToolModule api = Api.of(ToolModule.class);
        api.getCallConfig().enqueue(new SimpleCallback<CallConfig>() {
            @Override
            protected void handleResponse(CallConfig response) {
                if (binding != null) {
                    binding.ivAction.setVisibility(View.VISIBLE);
                    binding.setCallConfig(response);
                    handler.showPromotion(PMainActivity2.this, false);
                } else {
                    binding.ivAction.setVisibility(View.GONE);
                }
            }
        });
    }

    private void showUnreadMessageCount() {
        PushModule api = Api.of(PushModule.class);
        api.systemMsg("").enqueue(new SimpleCallback<PageDTO<SystemMsg>>() {
            @Override
            protected void handleResponse(PageDTO<SystemMsg> response) {
                int unreadMessageCount = response.getUnreadNum();
                if (unreadMessageCount > 0) {
                    binding.tvMsg.setText(String.valueOf(unreadMessageCount));
                    binding.tvMsg.setVisibility(View.VISIBLE);
                } else {
                    binding.tvMsg.setVisibility(View.GONE);
                }
            }
        });
    }

    private void initDoctorView() {
        SimpleAdapter adapter = handler.getDoctorAdapter();
        adapter.onFinishLoadMore(true);
        binding.rvRecommendDoctor.setLayoutManager(new LinearLayoutManager(this));
        binding.rvRecommendDoctor.setAdapter(adapter);
    }

    private void setPatientProfile() {
        ProfileModule api = Api.of(ProfileModule.class);
        api.patientProfile().enqueue(new ApiCallback<PatientDTO>() {
            @Override
            protected void handleResponse(PatientDTO response) {
                Settings.setPatientProfile(response);
                binding.setPatient(response);
            }
        });
    }

    private FooterViewModel getFooter() {
        return FooterViewModel.getInstance(R.id.tab_one);
    }

    private void startActivity(Class<?> cls) {
        Intent i = new Intent(this, cls);
        startActivity(i);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            finishAffinity();
        }
        overridePendingTransition(0, 0);
    }

    @Subscribe
    public void onMainTabChangedEvent(MainTabChangedEvent e) {
        switch (e.getPosition()) {
            case 0: {
                startActivity(PMainActivity2.class);
                break;
            }
            case 1: {
                startActivity(PConsultingActivity.class);
                break;
            }
            case 2: {
                startActivity(PMeActivity.class);
                break;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == UpdateUtil.STORE_REQUEST) {
            boolean isGranted = PermissionUtil.verifyPermissions(grantResults);
            if (isGranted) {
                UpdateUtil.checkUpdate(this, 1);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventHub.register(this);
        handler.getMedicineStore().registerRealmChanged();

        // 如果系统消息界面把所有的消息标记为已读，这里要重新加载消息，去除未读的红点标记
        showUnreadMessageCount();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        UpdateUtil.checkUpdate(this, 1);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventHub.unregister(this);
    }


    @Subscribe
    public void onUpdateEvent(UpdateEvent e) {

        UpdateUtil.handleNewVersion(this, e.getData(), e.getVersionName());
    }

    @Subscribe
    public void onReadMessageEvent(ReadMessageEvent event) {
        showUnreadMessageCount();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
