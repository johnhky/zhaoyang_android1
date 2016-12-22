package com.doctor.sun.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.doctor.sun.BR;
import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.ActivityDoctorDetail2Binding;
import com.doctor.sun.dto.PageDTO;
import com.doctor.sun.entity.AppointmentBuilder;
import com.doctor.sun.entity.Article;
import com.doctor.sun.entity.Comment;
import com.doctor.sun.entity.Doctor;
import com.doctor.sun.entity.constans.AppointmentType;
import com.doctor.sun.event.SelectAppointmentTypeEvent;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.immutables.Appointment;
import com.doctor.sun.model.DoctorDetailModel;
import com.doctor.sun.module.ProfileModule;
import com.doctor.sun.ui.adapter.SimpleAdapter;
import com.doctor.sun.ui.adapter.ViewHolder.SortedItem;
import com.doctor.sun.ui.adapter.core.SortedListAdapter;
import com.doctor.sun.ui.handler.MedicalRecordEventHandler;
import com.doctor.sun.vm.ItemPickAppointmentDuration;
import com.doctor.sun.vm.ItemPremiumAppointment;
import com.doctor.sun.vm.ItemSpace;
import com.doctor.sun.vm.ItemStandardAppointment;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import io.ganguo.library.core.event.EventHub;

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

        builder.setDoctor(getData());
        binding.setData(builder);
        initToolbar();
        initView();
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
                    binding.toolbar.setNavigationIcon(R.drawable.ic_back);
                } else {
                    binding.toolbar.setNavigationIcon(R.drawable.ic_back_blue);
                }
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
        Doctor data = getData();

        final List<Article> articleList = new ArrayList<>();
        final List<Comment> commentList = new ArrayList<>();
        api.articles(getData().getId(), "1").enqueue(new SimpleCallback<PageDTO<Article>>() {
            @Override
            protected void handleResponse(PageDTO<Article> response) {
                if (response.getData().size() > 0) {

                }
            }
        });
        api.comments(data.getId(), "1").enqueue(new SimpleCallback<PageDTO<Comment>>() {
            @Override
            protected void handleResponse(PageDTO<Comment> response) {
                commentList.add(response.getData().get(0));
                commentList.add(response.getData().get(1));
            }
        });

        List<SortedItem> items = model.parseData(data, articleList, commentList);
        adapter.insertAll(items);
        binding.flSelectDuration.setVisibility(View.VISIBLE);
        binding.llSelectRecord.setVisibility(View.GONE);
    }

    public void appointment(View view) {
        SimpleAdapter adapter = new SimpleAdapter();
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
    public void onSelectAppointmentTypeEvent(SelectAppointmentTypeEvent event) {

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
        final ItemPickAppointmentDuration item = new ItemPickAppointmentDuration(type, price);
        item.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                if (i == BR.selectedItem) {
                    builder.setDurationNotifyAll(item.getSelectedItem() * 15);
                }
            }
        });
        adapter.insert(item);
        binding.flSelectDuration.setVisibility(View.GONE);
        binding.llSelectRecord.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventHub.register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventHub.unregister(this);
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
}
