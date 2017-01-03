package com.doctor.sun.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.doctor.sun.BR;
import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.ActivityDoctorDetail2Binding;
import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.dto.PageDTO;
import com.doctor.sun.entity.AppointmentBuilder;
import com.doctor.sun.entity.Article;
import com.doctor.sun.entity.Comment;
import com.doctor.sun.entity.Coupon;
import com.doctor.sun.entity.Doctor;
import com.doctor.sun.entity.constans.AppointmentType;
import com.doctor.sun.event.AdapterItemsEvent;
import com.doctor.sun.event.SelectAppointmentTypeEvent;
import com.doctor.sun.event.ShowDialogEvent;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.model.DoctorDetailModel;
import com.doctor.sun.module.ProfileModule;
import com.doctor.sun.module.ToolModule;
import com.doctor.sun.ui.adapter.SimpleAdapter;
import com.doctor.sun.ui.adapter.ViewHolder.SortedItem;
import com.doctor.sun.ui.adapter.core.SortedListAdapter;
import com.doctor.sun.ui.handler.MedicalRecordEventHandler;
import com.doctor.sun.vm.BaseItem;
import com.doctor.sun.vm.ItemCouponMessage;
import com.doctor.sun.vm.ItemPickAppointmentDuration;
import com.doctor.sun.vm.ItemPremiumAppointment;
import com.doctor.sun.vm.ItemSpace;
import com.doctor.sun.vm.ItemStandardAppointment;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import io.ganguo.library.core.event.EventHub;
import retrofit2.Call;

/**
 * Created by kb on 13/12/2016.
 */

public class DoctorDetailActivity2 extends AppCompatActivity {

    private ActivityDoctorDetail2Binding binding;
    private SortedListAdapter adapter;
    private DoctorDetailModel model;
    private MaterialDialog dialog;

    private ProfileModule api = Api.of(ProfileModule.class);

    private boolean isPickingDuration = false;
    private AppointmentBuilder builder = new AppointmentBuilder();
    private MedicalRecordEventHandler medicalRecordHandler;

    private boolean isToolbarCollapsed = false;

    private Doctor doctor;

    public static Intent makeIntent(Context context, Doctor data) {
        Intent i = new Intent(context, DoctorDetailActivity2.class);
        i.putExtra(Constants.DATA, data);

        return i;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_doctor_detail2);
        adapter = new SortedListAdapter();
        model = new DoctorDetailModel();

        postponeTransition();

        showDoctorInfo();
    }

    private void showDoctorInfo() {
        ToolModule api = Api.of(ToolModule.class);
        api.doctorInfo(getData().getId()).enqueue(new SimpleCallback<Doctor>() {
            @Override
            protected void handleResponse(Doctor response) {
                builder.setDoctor(getData());
                binding.setData(builder);
                initToolbar();
                initView();

                doctor = response;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (medicalRecordHandler == null) {
            medicalRecordHandler = new MedicalRecordEventHandler(builder);
        }
        if (!medicalRecordHandler.isRegister()) {
            medicalRecordHandler.registerTo(this);
        }
        EventHub.register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventHub.unregister(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (medicalRecordHandler.isRegister()) {
            medicalRecordHandler.unregister();
            medicalRecordHandler = null;
        }
    }

    private Doctor getData() {
        return getIntent().getParcelableExtra(Constants.DATA);
    }

    private void initToolbar() {
        setSupportActionBar(binding.toolbar);
        // 上滑时，toolbar改变颜色
        binding.appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    isToolbarCollapsed = true;
                    binding.toolbar.setNavigationIcon(R.drawable.ic_back_white);
                } else {
                    isToolbarCollapsed = false;
                    binding.toolbar.setNavigationIcon(R.drawable.ic_back_blue);
                }
                invalidateOptionsMenu();
            }
        });
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        getSupportActionBar().setTitle("");
    }

    private void initView() {
        binding.rv.setLayoutManager(new LinearLayoutManager(this));
        binding.rv.setAdapter(adapter);
        fillAdapter();
    }

    private void fillAdapter() {
        if (isPickingDuration) {
            adapter.clear();
        }
        final Doctor data = getData();

        new Thread(new Runnable() {
            @Override
            public void run() {
                final CountDownLatch latch = new CountDownLatch(2);
                final List<Article> articleList = new ArrayList<>();
                final List<Comment> commentList = new ArrayList<>();
                Call<ApiDTO<PageDTO<Article>>> articles = api.articles(getData().getId(), "1");
                Call<ApiDTO<PageDTO<Comment>>> comments = api.comments(data.getId(), "1");
                articles.enqueue(new SimpleCallback<PageDTO<Article>>() {
                    @Override
                    protected void handleResponse(PageDTO<Article> response) {
                        if (response.getData().size() > 0) {
                            articleList.addAll(response.getData());
                        }
                        latch.countDown();
                    }
                });
                comments.enqueue(new SimpleCallback<PageDTO<Comment>>() {
                    @Override
                    protected void handleResponse(PageDTO<Comment> response) {
                        if (response.getData() != null && response.getData().size() >= 1) {
                            commentList.addAll(response.getData());
                        }
                        latch.countDown();
                    }
                });

                try {
                    latch.await(5000, TimeUnit.MILLISECONDS);
                    List<SortedItem> items = model.parseData(data, articleList, commentList);
                    EventHub.post(new AdapterItemsEvent(items));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Subscribe
    public void onEventMainThread(AdapterItemsEvent e) {
        adapter.insertAll(e.getItemList());
        binding.flSelectDuration.setVisibility(View.VISIBLE);
        binding.llSelectRecord.setVisibility(View.GONE);

        startPostponedTransition();
    }

    public void showDialog() {
        SimpleAdapter adapter = new SimpleAdapter();
        adapter.onFinishLoadMore(true);
        MaterialDialog.Builder builder = new MaterialDialog.Builder(this);
        builder.title("预约类型");
        builder.titleGravity(GravityEnum.CENTER);
        adapter.add(new ItemPremiumAppointment(getData().getMoney()));
        adapter.add(new ItemSpace());
        adapter.add(new ItemStandardAppointment(getData().getSecondMoney()));
        dialog = builder.adapter(adapter, new LinearLayoutManager(this)).build();
        dialog.show();
    }

    @Subscribe
    public void onShowDialogEvent(ShowDialogEvent event) {
        showDialog();
    }

    @Subscribe
    public void onSelectAppointmentTypeEvent(SelectAppointmentTypeEvent event) {

        ProfileModule api = Api.of(ProfileModule.class);

        builder.setType(event.getType());
        dialog.dismiss();
        if (event.getType() == AppointmentType.STANDARD) {
            builder.pickDate(this);
            return;
        }

        isPickingDuration = true;

        adapter.clear();
        int type = event.getType();
        double price;
        if (type == AppointmentType.PREMIUM) {
            price = getData().getMoney();
        } else {
            price = getData().getSecondMoney();
        }
        final ItemPickAppointmentDuration item = new ItemPickAppointmentDuration(price);
        item.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                if (i == BR.selectedItem) {
                    builder.setDurationNotifyAll(item.getSelectedItem() * 15);
                }
            }
        });
        api.coupons("").enqueue(new SimpleCallback<List<Coupon>>() {
            @Override
            protected void handleResponse(List<Coupon> response) {
                if (response.size() > 0) {
                    insertCouponMessage(response.get(0), response.size());
                }
                item.setPosition(adapter.size());
                adapter.insert(item);
                insertTail();

            }
        });
        binding.flSelectDuration.setVisibility(View.GONE);
        binding.llSelectRecord.setVisibility(View.VISIBLE);
    }

    private void insertCouponMessage(Coupon coupon, int size) {
        String couponMessage = "您的账户有" + size + "张" + coupon.couponMoney + "元优惠券" +
                "，满" + coupon.threshold + "元可以使用哦";
        ItemCouponMessage itemCouponMessage = new ItemCouponMessage(couponMessage);
        itemCouponMessage.setItemId("itemCouponMessage");
        itemCouponMessage.setPosition(adapter.size());
        adapter.insert(itemCouponMessage);
    }

    public void insertTail() {
        BaseItem tail = new BaseItem(R.layout.space_370dp_gray);
        tail.setItemId("space_" + adapter.size());
        tail.setPosition(adapter.size());
        adapter.insert(tail);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (isToolbarCollapsed) {
            if (doctor.getIsFav().equals("1")) {
                menu.getItem(0).setIcon(R.drawable.ic_favorite_white_fill);
            } else {
                menu.getItem(0).setIcon(R.drawable.ic_favorite_white_border);
            }
        } else {
            if (doctor.getIsFav().equals("1")) {
                menu.getItem(0).setIcon(R.drawable.ic_favorite_blue_fill);
            } else {
                menu.getItem(0).setIcon(R.drawable.ic_favorite_blue_border);
            }
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_fav_doctor, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_fav:
                doctor.getHandler().toggleFav(this, doctor);
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        if (isPickingDuration) {
            fillAdapter();
            isPickingDuration = false;
        } else {
            super.onBackPressed();
        }
    }

    private void postponeTransition() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            postponeEnterTransition();
        } else {
            supportPostponeEnterTransition();
        }
    }

    private void startPostponedTransition() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            startPostponedEnterTransition();
        } else {
            supportStartPostponedEnterTransition();
        }
    }
}
