package com.doctor.sun.ui.activity.doctor;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.doctor.sun.R;
import com.doctor.sun.Settings;
import com.doctor.sun.databinding.ActivityFeeBinding;
import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.entity.Doctor;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.ApiCallback;
import com.doctor.sun.module.ProfileModule;
import com.doctor.sun.ui.activity.BaseFragmentActivity2;
import com.doctor.sun.ui.widget.TwoChoiceDialog;

import java.util.Locale;

/**
 * Created by lucas on 12/8/15.
 * 诊金设置
 */
public class FeeActivity extends BaseFragmentActivity2 {

    private ActivityFeeBinding binding;

    private ProfileModule api = Api.of(ProfileModule.class);

    private Doctor doctor;

    private String question = "";

    public static Intent makeIntent(Context context) {
        Intent i = new Intent(context, FeeActivity.class);
        return i;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_fee);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_confirm,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_confirm:
                String money = binding.etMoney.getText().toString();
                String netMinute = binding.etNetMinute.getText().toString();
                String surfaceMoney = binding.etFaceMoney.getText().toString();
                String surfaceMinute = binding.etFaceMinute.getText().toString();
                String secondMoney = binding.etSimple.getText().toString();
                if (money.isEmpty() || secondMoney.isEmpty() || netMinute.isEmpty() || surfaceMinute.isEmpty() || surfaceMoney.isEmpty()) {
                    Toast.makeText(FeeActivity.this, "有必填的输入项为空", Toast.LENGTH_SHORT).show();
                } else {
                    api.setFee(money, netMinute, secondMoney, surfaceMinute, surfaceMoney)
                            .enqueue(new ApiCallback<String>() {
                                @Override
                                protected void handleResponse(String response) {
                                }

                                @Override
                                protected void handleApi(ApiDTO<String> body) {
                                    Toast.makeText(FeeActivity.this, "诊金设置完成", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            });
                }
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initData() {
        doctor = Settings.getDoctorProfile();
        binding.etNetMinute.setText(doctor.getNetworkMinute());
        binding.etMoney.setText(doctor.getMoney()+"");
        binding.etSimple.setText(doctor.getSecondMoney()+"");
        binding.etFaceMinute.setText(doctor.getSurfaceMinute());
        binding.etFaceMoney.setText(doctor.getSurfaceMoney());
        if (doctor.getSpecialistCateg() == 1) {
            binding.llSimple.setVisibility(View.GONE);
        }
    }

    private void initListener() {
        if (doctor.getSpecialistCateg() == 1) {
            question = "例如:\n若设置诊金为每【50】分钟,收费【100】元,则患者可预约的就诊时长即为50、100可选";
        } else {
            question = "例如:\n若设置诊金为每50分钟,收费【100元】,则患者可预约的就诊时长即为50、100、150等可选";
        }
        binding.tvRemind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TwoChoiceDialog.show(FeeActivity.this, question, "", "返回", new TwoChoiceDialog.Options() {
                    @Override
                    public void onApplyClick(MaterialDialog dialog) {
                        dialog.dismiss();
                    }

                    @Override
                    public void onCancelClick(MaterialDialog dialog) {

                    }
                });

            }
        });
        binding.tvRemind2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TwoChoiceDialog.show(FeeActivity.this, question, "", "返回", new TwoChoiceDialog.Options() {
                    @Override
                    public void onApplyClick(MaterialDialog dialog) {
                        dialog.dismiss();
                    }

                    @Override
                    public void onCancelClick(MaterialDialog dialog) {

                    }
                });
            }
        });

    }


    @Override
    public int getMidTitle() {
        return R.string.title_edit_fee;
    }
}