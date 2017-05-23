package com.doctor.sun.ui.activity.patient;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.PActivityOrderMessageBinding;
import com.doctor.sun.R;
import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.entity.Doctor;
import com.doctor.sun.entity.LogTime;
import com.doctor.sun.entity.constans.AppointmentType;
import com.doctor.sun.http.Api;
import com.doctor.sun.module.TimeModule;
import com.doctor.sun.ui.activity.BaseFragmentActivity2;
import com.doctor.sun.util.DialogUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.ganguo.library.common.LoadingHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by heky on 17/5/16.
 */

public class POrderMessageActivity extends BaseFragmentActivity2 {

    private PActivityOrderMessageBinding binding;
    private int id = 0;
    private String log_time, face_time;
    private int time, recordId;
    private long appointmentDay;
    private double totalMoney;
    private String patient;
    private String appointmentTime;
    private Gson gson = new GsonBuilder().create();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(POrderMessageActivity.this, R.layout.p_activity_order_message);
        binding.setData(getData());
        IntentFilter filter = new IntentFilter();
        filter.addAction("getTime");
        filter.addAction("updateType");
        filter.addAction(Constants.DATE);
        filter.addAction(Constants.PICKCUSTOM);
        filter.addAction(Constants.CREATE_SUCCESS);
        registerReceiver(receiver, filter);
        initView();
        initWidget();
    }

    public void initView() {
        if (getId() != 0) {
            switch (getId()) {
                case 1:
                    binding.tvType.setText("专属网诊");
                    binding.tvType2.setText("咨询+取药");
                    break;
                case 2:
                    binding.tvType.setText("简易复诊");
                    binding.tvType2.setText("快捷取药");
                    break;
                case 3:
                    binding.tvType.setText("诊所面诊");
                    binding.tvType2.setText("现场看病+取药");
                    break;
            }
        }
    }

    public void initWidget() {
        getTime();
        getFaceTime();
        binding.llType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int mId;
                if (id == 0) {
                    mId = getId();
                } else {
                    mId = id;
                }
                DialogUtils.showAppointmentType(POrderMessageActivity.this, v, new DialogUtils.OnPopItemClickListener() {
                    @Override
                    public void onItemClickListener(int which) {
                        switch (which) {
                            case 1:
                                id = 1;
                                binding.tvType.setText("专属网诊");
                                binding.tvType2.setText("咨询+取药");
                                break;
                            case 2:
                                id = 3;
                                binding.tvType.setText("诊所面诊");
                                binding.tvType2.setText("现场看病+取药");
                                break;
                            case 3:
                                id = 2;
                                binding.tvType.setText("简易复诊");
                                binding.tvType2.setText("快捷取药");
                                break;
                            case 4:
                                Intent intent = new Intent();
                                intent.setAction("updateType");
                                if (getId() != id) {
                                    sendBroadcast(intent);
                                }
                                break;
                        }
                    }
                }, getData(), mId, getAddress());
            }
        });
        binding.llTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (id == 0) {
                    if (getId() == 3) {
                        DialogUtils.showTime(POrderMessageActivity.this, v, AppointmentType.FACE, getData(), face_time);
                    } else {
                        DialogUtils.showTime(POrderMessageActivity.this, v, getId(), getData(), log_time);
                    }
                } else {
                    if (id == 3) {
                        DialogUtils.showTime(POrderMessageActivity.this, v, AppointmentType.FACE, getData(), face_time);
                    } else {
                        DialogUtils.showTime(POrderMessageActivity.this, v, id, getData(), log_time);
                    }

                }

            }
        });
        binding.llDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(binding.tvTime.getText().toString().trim())) {
                    Toast.makeText(POrderMessageActivity.this, "请先选择预约时长!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (id == 0) {
                    if (getId() == 3) {
                        DialogUtils.showPickDate(POrderMessageActivity.this, v, AppointmentType.FACE, getData().getId(), time);
                    } else {
                        DialogUtils.showPickDate(POrderMessageActivity.this, v, getId(), getData().getId(), time);
                    }
                } else {
                    if (id == 3) {
                        DialogUtils.showPickDate(POrderMessageActivity.this, v, AppointmentType.FACE, getData().getId(), time);
                    }
                    {
                        DialogUtils.showPickDate(POrderMessageActivity.this, v, id, getData().getId(), time);
                    }
                }

            }
        });
        binding.llCustom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(binding.tvTime.getText().toString().trim())) {
                    Toast.makeText(POrderMessageActivity.this, "请先选择预约时长!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(binding.tvDate.getText().toString().trim())) {
                    Toast.makeText(POrderMessageActivity.this, "请先选择预约日期!", Toast.LENGTH_SHORT).show();
                    return;
                }
                DialogUtils.showPickCustom(POrderMessageActivity.this, v);
            }
        });
        binding.flNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(binding.tvTime.getText().toString().trim())) {
                    Toast.makeText(POrderMessageActivity.this, "请先选择预约时长!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(binding.tvDate.getText().toString().trim())) {
                    Toast.makeText(POrderMessageActivity.this, "请先选择预约日期!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(binding.tvCustom.getText().toString().trim())) {
                    Toast.makeText(POrderMessageActivity.this, "请先选择需要帮助的患者!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (id == 0) {
                    if (getId() == 3) {
                        DialogUtils.showAppointmentPay(POrderMessageActivity.this, v, AppointmentType.FACE, getData(), totalMoney, patient, time, appointmentDay, appointmentTime, recordId);
                    } else {
                        DialogUtils.showAppointmentPay(POrderMessageActivity.this, v, getId(), getData(), totalMoney, patient, time, appointmentDay, appointmentTime, recordId);
                    }
                } else {
                    if (id == 3) {
                        DialogUtils.showAppointmentPay(POrderMessageActivity.this, v, AppointmentType.FACE, getData(), totalMoney, patient, time, appointmentDay, appointmentTime, recordId);
                    } else {
                        DialogUtils.showAppointmentPay(POrderMessageActivity.this, v, id, getData(), totalMoney, patient, time, appointmentDay, appointmentTime, recordId);
                    }
                }

            }
        });
    }

    private void getTime() {
        LoadingHelper.showMaterLoading(POrderMessageActivity.this, "正在加载...");
        TimeModule api = Api.of(TimeModule.class);
        api.getLogTime(getData().getId(), AppointmentType.PREMIUM).enqueue(new Callback<ApiDTO>() {
            @Override
            public void onResponse(Call<ApiDTO> call, Response<ApiDTO> response) {
                LogTime logTime = gson.fromJson(response.body().getData().toString(), LogTime.class);
                log_time = logTime.getLog_time();
                LoadingHelper.hideMaterLoading();
            }

            @Override
            public void onFailure(Call<ApiDTO> call, Throwable t) {
                LoadingHelper.hideMaterLoading();
            }
        });
    }

    private void getFaceTime() {
        LoadingHelper.showMaterLoading(POrderMessageActivity.this, "正在加载...");
        TimeModule api = Api.of(TimeModule.class);
        api.getLogTime(getData().getId(), AppointmentType.FACE).enqueue(new Callback<ApiDTO>() {
            @Override
            public void onResponse(Call<ApiDTO> call, Response<ApiDTO> response) {
                LogTime logTime = gson.fromJson(response.body().getData().toString(), LogTime.class);
                face_time = logTime.getLog_time();
                LoadingHelper.hideMaterLoading();
            }

            @Override
            public void onFailure(Call<ApiDTO> call, Throwable t) {
                LoadingHelper.hideMaterLoading();
            }
        });
    }

    public Doctor getData() {
        return getIntent().getParcelableExtra(Constants.DATA);
    }

    private int getId() {
        return getIntent().getIntExtra(Constants.REMARK, 0);
    }

    private String getAddress() {
        return getIntent().getStringExtra(Constants.ADDRESS);
    }

    @Override
    public int getMidTitle() {
        return R.string.reservation_information;
    }

    public BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("updateType")) {
                binding.tvTimeProgress.setBackgroundResource(R.drawable.have_diong);
                binding.vTimeProgress.setBackgroundResource(R.color.gray_bb);
                binding.vDateProgress.setBackgroundResource(R.color.gray_bb);
                binding.tvDateProgress.setBackgroundResource(R.drawable.ic_record_selector_disable);
                binding.tvCustomProgress.setBackgroundResource(R.drawable.ic_record_selector_disable);
                binding.tvTimeVisible.setVisibility(View.GONE);
                binding.tvCustomVisible.setVisibility(View.GONE);
                binding.tvDateVisible.setVisibility(View.GONE);
                binding.tvTime.setText("");
                binding.tvDate.setText("");
                binding.tvCustom.setText("");
            } else if (intent.getAction().equals("getTime")) {
                binding.tvTime.setText(intent.getStringExtra(Constants.TIME) + "分钟");
                time = Integer.parseInt(intent.getStringExtra(Constants.TIME));
                totalMoney = Double.parseDouble(intent.getStringExtra(Constants.APPOINTMENT_MONEY));
                binding.tvTimeProgress.setBackgroundResource(R.drawable.ic_record_selector_selected);
                binding.vTimeProgress.setBackgroundResource(R.color.green);
                binding.tvDateProgress.setBackgroundResource(R.drawable.have_diong);
                binding.tvTimeVisible.setVisibility(View.VISIBLE);
                binding.tvDate.setText("");
                binding.tvCustom.setText("");
                binding.tvCustomVisible.setVisibility(View.GONE);
                binding.tvDateVisible.setVisibility(View.GONE);
                binding.vDateProgress.setBackgroundResource(R.color.gray_bb);
                binding.tvCustomProgress.setBackgroundResource(R.drawable.ic_record_selector_disable);
            } else if (intent.getAction().equals(Constants.DATE)) {
                binding.tvDateVisible.setVisibility(View.VISIBLE);
                binding.tvDateProgress.setBackgroundResource(R.drawable.ic_record_selector_selected);
                binding.vDateProgress.setBackgroundResource(R.color.green);
                binding.tvCustomProgress.setBackgroundResource(R.drawable.have_diong);
                binding.tvDate.setText(intent.getStringExtra(Constants.TIME));
                appointmentTime = intent.getStringExtra(Constants.TIME);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    Date date = simpleDateFormat.parse(intent.getStringExtra(Constants.TIME));
                    appointmentDay = date.getTime() / 1000;
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                binding.tvCustom.setText("");
                binding.tvCustomVisible.setVisibility(View.GONE);
            } else if (intent.getAction().equals(Constants.PICKCUSTOM)) {
                recordId = intent.getIntExtra(Constants.DATA, 0);
                patient = intent.getStringExtra(Constants.ADDRESS);
                binding.tvCustomVisible.setVisibility(View.VISIBLE);
                binding.tvCustom.setText(intent.getStringExtra(Constants.REMARK));
                binding.tvCustomProgress.setBackgroundResource(R.drawable.ic_record_selector_selected);
            } else if (intent.getAction().equals(Constants.CREATE_SUCCESS)) {
                DialogUtils.showPickCustom(context, binding.llCustom);
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}
