package com.doctor.sun.ui.activity.patient;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.PActivityOrderMessageBinding;
import com.doctor.sun.R;
import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.entity.Coupon;
import com.doctor.sun.entity.Doctor;
import com.doctor.sun.entity.LogTime;
import com.doctor.sun.entity.constans.AppointmentType;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.module.ProfileModule;
import com.doctor.sun.module.TimeModule;
import com.doctor.sun.module.ToolModule;
import com.doctor.sun.ui.activity.BaseFragmentActivity2;
import com.doctor.sun.util.DialogUtils;
import com.doctor.sun.util.PayEventHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    Doctor doctor;
    ToolModule api = Api.of(ToolModule.class);
    ProfileModule couponApi = Api.of(ProfileModule.class);
    private PayEventHandler payEventHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(POrderMessageActivity.this, R.layout.p_activity_order_message);
        binding.setData(getData());
        payEventHandler = PayEventHandler.register(this);
        LoadingHelper.showMaterLoading(POrderMessageActivity.this, "正在加载...");
        api.doctorInfo(getData().getId()).enqueue(new SimpleCallback<Doctor>() {
            @Override
            protected void handleResponse(Doctor response) {
                doctor = response;
                LoadingHelper.hideMaterLoading();
            }

            @Override
            public void onFailure(Call<ApiDTO<Doctor>> call, Throwable t) {
                super.onFailure(call, t);
                LoadingHelper.hideMaterLoading();
            }
        });
        IntentFilter filter = new IntentFilter();
        filter.addAction("getTime");
        filter.addAction("updateType");
        filter.addAction(Constants.DATE);
        filter.addAction(Constants.PICKCUSTOM);
        filter.addAction(Constants.CREATE_SUCCESS);
        filter.addAction(Constants.RESET_CALENDAR);
        registerReceiver(receiver, filter);
        initView();
        initWidget();
    }

    public void initView() {
        if (getId() != 0) {
            switch (getId()) {
                case 1:
                    if (getData().getSpecialistCateg() == 1) {
                        binding.tvType2.setText("咨询+取药");
                    } else {
                        binding.tvType2.setText("看病+取药");
                    }
                    binding.tvType.setText("VIP网诊");
                    break;
                case 2:
                    binding.tvType.setText("简易复诊");
                    binding.tvType2.setText("快捷取药");
                    break;
                case 4:
                    binding.tvType.setText("诊所面诊");
                    if (getData().getSpecialistCateg() == 1) {
                        binding.tvType2.setText("现场咨询+取药");
                    } else {
                        binding.tvType2.setText("现场看病+取药");
                    }

                    break;
            }
        }
    }

    public void initWidget() {
        getTime();
        getFaceTime();
        if (getId() == 2) {
            binding.rlTime.setVisibility(View.GONE);
            binding.tvTimeProgress.setVisibility(View.GONE);
            binding.vTimeProgress.setVisibility(View.GONE);
            binding.tvDateProgress.setVisibility(View.GONE);
            binding.vDateProgress.setVisibility(View.GONE);
            binding.tvCustomSelect.setVisibility(View.VISIBLE);
            binding.tvCustomProgress.setBackgroundResource(R.drawable.ic_blue_point);
            binding.rlDate.setVisibility(View.GONE);
            couponApi.getAppointmentCoupons("valid", "simple_consult", true, getData().getSecondMoney() + "", "all", "1").enqueue(new SimpleCallback<List<Coupon>>() {
                @Override
                protected void handleResponse(List<Coupon> response) {
                    List<Coupon> lists = new ArrayList<Coupon>();
                    for (int i = 0; i < response.size(); i++) {
                        if (response.get(i).isThreshold_available() == true) {
                            lists.add(response.get(i));
                        }
                    }
                    if (lists.size() > 0) {
                        binding.llCoupon.setVisibility(View.VISIBLE);
                        binding.tvCouponNum.setText("您有" + lists.size() + "张优惠券,在支付时记得使用哦~");
                    }
                }

                @Override
                public void onFailure(Call<ApiDTO<List<Coupon>>> call, Throwable t) {
                    super.onFailure(call, t);
                }
            });

        }
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
                                binding.tvType.setText("VIP网诊");
                                if (getData().getSpecialistCateg() == 1) {
                                    binding.tvType2.setText("咨询+取药");
                                } else {
                                    binding.tvType2.setText("看病+取药");
                                }

                                break;
                            case 2:
                                id = 4;
                                binding.tvType.setText("诊所面诊");
                                if (getData().getSpecialistCateg() == 1) {
                                    binding.tvType2.setText("现场咨询+取药");
                                } else {
                                    binding.tvType2.setText("现场看病+取药");
                                }
                                break;
                            case 3:
                                id = 2;
                                binding.tvType.setText("简易复诊");
                                binding.tvType2.setText("快捷取药");
                                break;
                            case 4:
                                if (id != 0) {
                                    Intent intent = new Intent();
                                    intent.setAction("updateType");
                                    sendBroadcast(intent);
                                }
                                break;
                            case 5:
                                id = 0;
                                break;
                        }
                    }
                }, doctor, mId, getAddress(), getNetwork(), getSurface(), getSimple());
            }
        });
        binding.llTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (id == 0) {
                    if (getId() == 4) {
                        DialogUtils.showTime(POrderMessageActivity.this, v, AppointmentType.FACE, doctor, face_time);
                    } else {
                        DialogUtils.showTime(POrderMessageActivity.this, v, getId(), doctor, log_time);
                    }
                } else {
                    if (id == 4) {
                        DialogUtils.showTime(POrderMessageActivity.this, v, AppointmentType.FACE, doctor, face_time);
                    } else {
                        DialogUtils.showTime(POrderMessageActivity.this, v, id, doctor, log_time);
                    }

                }

            }
        });
        binding.llDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getId() != 2) {
                    if (TextUtils.isEmpty(binding.tvTime.getText().toString().trim())) {
                        Toast.makeText(POrderMessageActivity.this, "请先选择预约时长!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                if (id == 0) {
                    if (getId() == 4) {
                        DialogUtils.showPickDate(POrderMessageActivity.this, v, AppointmentType.FACE, getData().getId(), time);
                    } else {
                        DialogUtils.showPickDate(POrderMessageActivity.this, v, getId(), getData().getId(), time);
                    }
                } else {
                    if (id == 4) {
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
                if (id == 0) {
                    if (getId() != 2) {
                        if (TextUtils.isEmpty(binding.tvTime.getText().toString().trim())) {
                            Toast.makeText(POrderMessageActivity.this, "请先选择预约时长!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                } else {
                    if (id != 2) {
                        if (TextUtils.isEmpty(binding.tvTime.getText().toString().trim())) {
                            Toast.makeText(POrderMessageActivity.this, "请先选择预约时长!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                }
                if (id == 0) {
                    if (getId() != 2) {
                        if (TextUtils.isEmpty(binding.tvDate.getText().toString().trim())) {
                            Toast.makeText(POrderMessageActivity.this, "请先选择预约日期!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                } else {
                    if (id != 2) {
                        if (TextUtils.isEmpty(binding.tvDate.getText().toString().trim())) {
                            Toast.makeText(POrderMessageActivity.this, "请先选择预约日期!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                }
                DialogUtils.showPickCustom(POrderMessageActivity.this, v, "");
            }
        });
        binding.flNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (id == 0) {
                    if (getId() != 2) {
                        if (TextUtils.isEmpty(binding.tvTime.getText().toString().trim())) {
                            Toast.makeText(POrderMessageActivity.this, "请先选择预约时长!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }

                } else {
                    if (id != 2) {
                        if (TextUtils.isEmpty(binding.tvTime.getText().toString().trim())) {
                            Toast.makeText(POrderMessageActivity.this, "请先选择预约时长!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                }
                if (id == 0) {
                    if (getId() != 2) {
                        if (TextUtils.isEmpty(binding.tvDate.getText().toString().trim())) {
                            Toast.makeText(POrderMessageActivity.this, "请先选择预约日期!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                } else {
                    if (id != 2) {
                        if (TextUtils.isEmpty(binding.tvDate.getText().toString().trim())) {
                            Toast.makeText(POrderMessageActivity.this, "请先选择预约日期!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                }
                if (TextUtils.isEmpty(binding.tvCustom.getText().toString().trim())) {
                    Toast.makeText(POrderMessageActivity.this, "请先选择需要帮助的患者!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (id == 0) {
                    if (getId() == 4) {
                        DialogUtils.showAppointmentPay(POrderMessageActivity.this, v, AppointmentType.FACE, doctor, totalMoney, patient, time, appointmentDay, appointmentTime, recordId);
                    } else {
                        if (getId() == 2) {
                            DialogUtils.showAppointmentPay(POrderMessageActivity.this, v, getId(), doctor, getData().getSecondMoney(), patient, time, appointmentDay, appointmentTime, recordId);
                        } else {
                            DialogUtils.showAppointmentPay(POrderMessageActivity.this, v, getId(), doctor, totalMoney, patient, time, appointmentDay, appointmentTime, recordId);
                        }
                    }
                } else {
                    if (id == 4) {
                        DialogUtils.showAppointmentPay(POrderMessageActivity.this, v, AppointmentType.FACE, doctor, totalMoney, patient, time, appointmentDay, appointmentTime, recordId);
                    } else {
                        if (id == 2) {
                            DialogUtils.showAppointmentPay(POrderMessageActivity.this, v, id, doctor, getData().getSecondMoney(), patient, time, appointmentDay, appointmentTime, recordId);
                        } else {
                            DialogUtils.showAppointmentPay(POrderMessageActivity.this, v, id, doctor, totalMoney, patient, time, appointmentDay, appointmentTime, recordId);
                        }
                    }
                }

            }
        });
    }

    private void getTime() {
        LoadingHelper.showMaterLoading(POrderMessageActivity.this, "正在加载...");
        TimeModule api = Api.of(TimeModule.class);
        api.getLogTime(getData().getId(), AppointmentType.PREMIUM).enqueue(new SimpleCallback<LogTime>() {
            @Override
            protected void handleResponse(LogTime response) {
                log_time = response.getLog_time();
            }
        });
    }

    private void getFaceTime() {
        LoadingHelper.showMaterLoading(POrderMessageActivity.this, "正在加载...");
        TimeModule api = Api.of(TimeModule.class);
        api.getLogTime(getData().getId(), AppointmentType.FACE).enqueue(new SimpleCallback<LogTime>() {
            @Override
            protected void handleResponse(LogTime response) {
                face_time = response.getLog_time();
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

    private boolean getSimple() {
        return getIntent().getBooleanExtra(Constants.SIMPLE, false);
    }

    private boolean getSurface() {
        return getIntent().getBooleanExtra(Constants.SURFACE, false);
    }

    private boolean getNetwork() {
        return getIntent().getBooleanExtra(Constants.NETWORK, false);
    }

    @Override
    public int getMidTitle() {
        return R.string.reservation_information;
    }

    public BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("updateType")) {
                if (id == 2) {
                    binding.tvCustomSelect.setVisibility(View.VISIBLE);
                    binding.rlTime.setVisibility(View.GONE);
                    binding.tvTimeProgress.setVisibility(View.GONE);
                    binding.vTimeProgress.setVisibility(View.GONE);
                    binding.tvDateProgress.setVisibility(View.GONE);
                    binding.vDateProgress.setVisibility(View.GONE);
                    binding.tvCustomProgress.setBackgroundResource(R.drawable.ic_blue_point);
                    binding.rlDate.setVisibility(View.GONE);
                    couponApi.getAppointmentCoupons("valid", "simple_consult", true, getData().getSecondMoney() + "", "all", "1").enqueue(new SimpleCallback<List<Coupon>>() {
                        @Override
                        protected void handleResponse(List<Coupon> response) {
                            List<Coupon> lists = new ArrayList<Coupon>();
                            for (int i = 0; i < response.size(); i++) {
                                if (response.get(i).isThreshold_available() == true) {
                                    lists.add(response.get(i));
                                }
                            }
                            if (lists.size() > 0) {
                                binding.llCoupon.setVisibility(View.VISIBLE);
                                binding.tvCouponNum.setText("您有" + lists.size() + "张优惠券,在支付时记得使用哦~");
                            }
                        }
                    });
                } else {
                    binding.rlTime.setVisibility(View.VISIBLE);
                    binding.tvTimeProgress.setBackgroundResource(R.drawable.ic_blue_point);
                    binding.vTimeProgress.setVisibility(View.VISIBLE);
                    binding.tvTimeProgress.setVisibility(View.VISIBLE);
                    binding.rlDate.setVisibility(View.VISIBLE);
                    binding.tvDateProgress.setVisibility(View.VISIBLE);
                    binding.vDateProgress.setVisibility(View.VISIBLE);
                    binding.vTimeProgress.setBackgroundResource(R.color.gray_bb);
                    binding.vDateProgress.setBackgroundResource(R.color.gray_bb);
                    binding.tvDateProgress.setBackgroundResource(R.drawable.ic_record_selector_disable);
                    binding.tvCustomProgress.setBackgroundResource(R.drawable.ic_record_selector_disable);
                    binding.tvTimeVisible.setVisibility(View.GONE);
                    binding.tvCustomVisible.setVisibility(View.GONE);
                    binding.tvDateVisible.setVisibility(View.GONE);
                    binding.tvTimeSelect.setVisibility(View.VISIBLE);
                    binding.tvDateSelect.setVisibility(View.GONE);
                    binding.tvCustomSelect.setVisibility(View.GONE);
                    binding.tvTime.setText("");
                    binding.tvDate.setText("");
                    binding.tvCustom.setText("");
                }
            } else if (intent.getAction().equals("getTime")) {
                binding.tvTime.setText(intent.getStringExtra(Constants.TIME) + "分钟");
                time = Integer.parseInt(intent.getStringExtra(Constants.TIME));
                totalMoney = Double.parseDouble(intent.getStringExtra(Constants.APPOINTMENT_MONEY));
                binding.tvTimeProgress.setBackgroundResource(R.drawable.ic_record_selector_selected);
                binding.vTimeProgress.setBackgroundResource(R.color.green);
                binding.tvDateProgress.setBackgroundResource(R.drawable.ic_blue_point);
                binding.tvTimeVisible.setVisibility(View.VISIBLE);
                binding.tvDateSelect.setVisibility(View.VISIBLE);
                binding.tvTimeSelect.setVisibility(View.GONE);
                binding.tvCustomSelect.setVisibility(View.GONE);
                binding.tvDate.setText("");
                binding.tvCustom.setText("");
                binding.vDateProgress.setBackgroundResource(R.color.gray_bb);
                binding.tvCustomProgress.setBackgroundResource(R.drawable.ic_record_selector_disable);
                String typeText = "";
                if (id == 0) {
                    if (getId() == 1) {
                        typeText = "detail_consult";
                    } else if (getId() == 2) {
                        typeText = "simple_consult";
                    } else if (getId() == 4) {
                        typeText = "surface_consult";
                    }
                } else {
                    if (id == 1) {
                        typeText = "detail_consult";
                    } else if (id == 2) {
                        typeText = "simple_consult";
                    } else if (id == 4) {
                        typeText = "surface_consult";
                    }
                }
                couponApi.getAppointmentCoupons("valid", typeText, true, totalMoney + "", "all", "1").enqueue(new SimpleCallback<List<Coupon>>() {
                    @Override
                    protected void handleResponse(List<Coupon> response) {
                        List<Coupon> lists = new ArrayList<Coupon>();
                        for (int i = 0; i < response.size(); i++) {
                            if (response.get(i).isThreshold_available() == true) {
                                lists.add(response.get(i));
                            }
                        }
                        if (lists.size() > 0) {
                            binding.llCoupon.setVisibility(View.VISIBLE);
                            binding.tvCouponNum.setText("您有" + lists.size() + "张优惠券,在支付时记得使用哦~");
                        }
                    }
                });

            } else if (intent.getAction().equals(Constants.DATE)) {
                binding.tvDateVisible.setVisibility(View.VISIBLE);
                binding.tvDateProgress.setBackgroundResource(R.drawable.ic_record_selector_selected);
                binding.vDateProgress.setBackgroundResource(R.color.green);
                binding.tvCustomProgress.setBackgroundResource(R.drawable.ic_blue_point);
                binding.tvDate.setText(intent.getStringExtra(Constants.TIME));
                appointmentTime = intent.getStringExtra(Constants.TIME);
                binding.tvCustomSelect.setVisibility(View.VISIBLE);
                binding.tvDateSelect.setVisibility(View.GONE);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    Date date = simpleDateFormat.parse(intent.getStringExtra(Constants.BOOK_TIME));
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
                DialogUtils.showPickCustom(POrderMessageActivity.this, binding.llCustom, intent.getStringExtra(Constants.DATA));
            } else if (intent.getAction().equals(Constants.RESET_CALENDAR)) {
                int type = intent.getIntExtra("dType", 0);
                DialogUtils.showPickDate(POrderMessageActivity.this, binding.llDate, type, getData().getId(), time);
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
        PayEventHandler.unregister(payEventHandler);
    }


}
