package com.doctor.sun.ui.activity.patient;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.PActivityUrgentCallBinding;
import com.doctor.sun.entity.EmergencyCall;
import com.doctor.sun.entity.MedicalRecord;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.AlipayCallback;
import com.doctor.sun.http.callback.ApiCallback;
import com.doctor.sun.http.callback.WeChatPayCallback;
import com.doctor.sun.module.EmergencyModule;
import com.doctor.sun.ui.activity.BaseActivity2;
import com.doctor.sun.ui.model.HeaderViewModel;

import java.util.ArrayList;

import io.ganguo.library.common.ToastHelper;

/**
 * Created by lucas on 1/20/16.
 */
public class UrgentCallActivity extends BaseActivity2 implements View.OnClickListener {
    private PActivityUrgentCallBinding binding;
    private EmergencyModule api = Api.of(EmergencyModule.class);

    public static Intent makeIntent(Context context, MedicalRecord data) {
        Intent i = new Intent(context, UrgentCallActivity.class);
        i.putExtra(Constants.DATA, data);
        return i;
    }

    private MedicalRecord getData() {
        return getIntent().getParcelableExtra(Constants.DATA);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.p_activity_urgent_call);
        HeaderViewModel header = new HeaderViewModel(this);
        header.setMidTitle("确认预约");
        binding.setHeader(header);
        binding.tvSelect1.setSelected(true);
        binding.tvSelect2.setSelected(true);
        binding.tvSelect3.setSelected(true);
        binding.rbMinute.setChecked(true);
        binding.rbAlipay.setChecked(true);
        binding.setData(getData());
        setClickListener();
    }

    public ArrayList<String> getTitles() {
        ArrayList<String> titles = new ArrayList<>();
        for (int i = 1; i < binding.flyTitle.getChildCount(); i++) {
            if (binding.flyTitle.getChildAt(i).isSelected()) {
                titles.add(String.valueOf(i));
            }
        }
        /*for (int i = 1; i < binding.llyTitle1.getChildCount(); i++) {
            if (binding.llyTitle1.getChildAt(i).isSelected()) {
                titles.add(String.valueOf(i));
            }
        }
        for (int i = 0; i < binding.llyTitle2.getChildCount(); i++) {
            if (binding.llyTitle2.getChildAt(i).isSelected()) {
                titles.add(String.valueOf(i + 3));
            }
        }*/
        if (binding.tvSelect1.isSelected()) {
            titles.clear();
            titles.add("all");
        }
        return titles;
    }

    public String getCity() {
        String city = "";
        if (binding.tvLocation1.isSelected())
            city = binding.tvLocation1.getText().toString();

        if (binding.tvLocation2.isSelected())
            city = binding.tvLocation2.getText().toString();

        if (binding.tvLocation1.isSelected() && binding.tvLocation2.isSelected())
            city = binding.tvLocation2.getText().toString();

        if (binding.tvSelect2.isSelected())
            city = "all";
        return city;
    }

    public int getGender() {
        int gender = 4;
        if (binding.tvGender1.isSelected())
            gender = 1;
        if (binding.tvGender2.isSelected())
            gender = 2;
        if (binding.tvGender1.isSelected() && binding.tvGender2.isSelected())
            gender = 0;
        if (binding.tvSelect2.isSelected())
            gender = 0;
        return gender;
    }

    public String getTime() {
        int currentTime = Integer.valueOf(binding.etTime.getText().toString());
        String time = "";
        switch (binding.rgTime.getCheckedRadioButtonId()) {
            case R.id.rb_minute:
                time = String.valueOf(currentTime * 60);
                break;
            case R.id.rb_hour:
                time = String.valueOf(currentTime * 60 * 60);
                break;
        }
        return time;
    }

    public String getPayType() {
        String payType = "alipay";
        switch (binding.rgPay.getCheckedRadioButtonId()) {
            case R.id.rb_alipay:
                payType = "alipay";
                break;
            case R.id.rb_wechat:
                payType = "wechat";
                break;
        }
        return payType;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_apply:
                Log.e(TAG, "onClick: " + getTitles() + getCity() + getGender());
                if (getTitle() == null || getCity() == null || getGender() == 4) {
                    ToastHelper.showMessage(this, "有必要的选项未选");
                } else {
                    String string = binding.etFee.getText().toString();
                    if (string.equals("")) {
                        ToastHelper.showMessage(this, "请填写愿意支付的紧急咨询费用");
                        return;
                    }
                    api.publish(String.valueOf(getData().getMedicalRecordId()), getTitles(), getCity()
                            , getGender(), Integer.valueOf(string)
                            , getTime()).enqueue(new ApiCallback<EmergencyCall>() {
                        @Override
                        protected void handleResponse(EmergencyCall response) {
//                            Log.e(TAG, "handleResponse: " + response);
                            if (binding.rbWechat.isChecked()) {
                                api.buildWeChatOrder(response.getId(), "wechat").enqueue(new WeChatPayCallback(UrgentCallActivity.this,response));
                            } else {
                                api.buildOrder(response.getId(),"alipay").enqueue(new AlipayCallback(UrgentCallActivity.this,response));
                            }
                        }
                    });
                }
                break;

            case R.id.tv_select1:
                if (!binding.tvSelect1.isSelected()) {
                    binding.tvSelect1.setSelected(true);
                    binding.flyTitle.getChildAt(1).setSelected(false);
                    binding.flyTitle.getChildAt(2).setSelected(false);
                    binding.flyTitle.getChildAt(3).setSelected(false);
                    binding.flyTitle.getChildAt(4).setSelected(false);
                    binding.flyTitle.getChildAt(5).setSelected(false);
                } else {
                    binding.tvSelect1.setSelected(false);
                }
                break;

            case R.id.tv_select2:
                if (!binding.tvSelect2.isSelected()) {
                    binding.tvSelect2.setSelected(true);
                    binding.llyLocation.getChildAt(1).setSelected(false);
                    binding.llyLocation.getChildAt(2).setSelected(false);
                } else {
                    binding.tvSelect2.setSelected(false);
                }
                break;

            case R.id.tv_select3:
                if (!binding.tvSelect3.isSelected()) {
                    binding.tvSelect3.setSelected(true);
                    binding.llyGender.getChildAt(1).setSelected(false);
                    binding.llyGender.getChildAt(2).setSelected(false);
                } else {
                    binding.tvSelect3.setSelected(false);
                }
                break;

            case R.id.tv_title1:
                binding.tvTitle1.setSelected(!binding.tvTitle1.isSelected());
                binding.tvSelect1.setSelected(false);
                break;

            case R.id.tv_title2:
                binding.tvTitle2.setSelected(!binding.tvTitle2.isSelected());
                binding.tvSelect1.setSelected(false);
                break;

            case R.id.tv_title3:
                binding.tvTitle3.setSelected(!binding.tvTitle3.isSelected());
                binding.tvSelect1.setSelected(false);
                break;

            case R.id.tv_title4:
                binding.tvTitle4.setSelected(!binding.tvTitle4.isSelected());
                binding.tvSelect1.setSelected(false);
                break;

            case R.id.tv_title5:
                binding.tvTitle5.setSelected(!binding.tvTitle5.isSelected());
                binding.tvSelect1.setSelected(false);
                break;

            case R.id.tv_location1:
                binding.tvLocation1.setSelected(!binding.tvLocation1.isSelected());
                binding.tvSelect2.setSelected(false);
                break;

            case R.id.tv_location2:
                binding.tvLocation2.setSelected(!binding.tvLocation2.isSelected());
                binding.tvSelect2.setSelected(false);
                break;

            case R.id.tv_gender1:
                binding.tvGender1.setSelected(!binding.tvGender1.isSelected());
                binding.tvSelect3.setSelected(false);
                break;

            case R.id.tv_gender2:
                binding.tvGender2.setSelected(!binding.tvGender2.isSelected());
                binding.tvSelect3.setSelected(false);
                break;
        }
    }

    private void setClickListener() {
        binding.tvApply.setOnClickListener(this);
        binding.tvSelect1.setOnClickListener(this);
        binding.tvSelect2.setOnClickListener(this);
        binding.tvSelect3.setOnClickListener(this);
        binding.tvTitle1.setOnClickListener(this);
        binding.tvTitle2.setOnClickListener(this);
        binding.tvTitle3.setOnClickListener(this);
        binding.tvTitle4.setOnClickListener(this);
        binding.tvTitle5.setOnClickListener(this);
        binding.tvLocation1.setOnClickListener(this);
        binding.tvLocation2.setOnClickListener(this);
        binding.tvGender1.setOnClickListener(this);
        binding.tvGender2.setOnClickListener(this);
    }
}
