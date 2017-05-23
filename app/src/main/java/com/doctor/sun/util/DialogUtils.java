package com.doctor.sun.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.alipay.sdk.auth.APAuthInfo;
import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.entity.AppointmentBuilder;
import com.doctor.sun.entity.Coupon;
import com.doctor.sun.entity.Doctor;
import com.doctor.sun.entity.MedicalRecord;
import com.doctor.sun.entity.Time;
import com.doctor.sun.entity.constans.AppointmentType;
import com.doctor.sun.entity.handler.AppointmentHandler2;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.immutables.Appointment;
import com.doctor.sun.immutables.UploadDrugData;
import com.doctor.sun.module.AppointmentModule;
import com.doctor.sun.module.ProfileModule;
import com.doctor.sun.module.TimeModule;
import com.doctor.sun.ui.activity.BundlesTabActivity;
import com.doctor.sun.ui.activity.SingleFragmentActivity;
import com.doctor.sun.ui.activity.doctor.ChattingActivity;
import com.doctor.sun.ui.activity.patient.handler.MedicalRecordHandler;
import com.doctor.sun.ui.adapter.AddressAdapter;
import com.doctor.sun.ui.fragment.NewMedicalRecordFragment;
import com.doctor.sun.ui.fragment.PickDateFragment;
import com.doctor.sun.ui.pager.PickDatePagerAdapter;
import com.squareup.timessquare.CalendarPickerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Handler;

import io.ganguo.library.common.LoadingHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by heky on 17/5/4.
 */

public class DialogUtils {

    private static PopupWindow popupWindow;
    public static final int ONE_DAY = 86400000;
    private static GridAdapter mAdapter;
    private static MyCustomAdapter myCustomAdapter;
    private static MyPayAdapter mPayAdapter;

    /*弹出选择支付方式弹窗*/
    public static void showPayDialog(Context context, View mview, final OnPopItemClickListener onDialogItemClickListener) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_to_pay, null);
        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.showAtLocation(mview, Gravity.BOTTOM, 0, 0);
        Button btn_cancel = (Button) view.findViewById(R.id.btn_quit);
        Button btn_ali = (Button) view.findViewById(R.id.ll_payali);
        Button btn_wechat = (Button) view.findViewById(R.id.ll_paywechat);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        btn_ali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                onDialogItemClickListener.onItemClickListener(0);
            }
        });
        btn_wechat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                onDialogItemClickListener.onItemClickListener(1);
            }
        });
    }

    /*弹出选择预约类型弹窗*/
    public static void showAppointmentType(final Context context, View mView, final OnPopItemClickListener onPopItemClickListener, Doctor doctor, int id, String address) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.include_appointment_type, null);
        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.showAtLocation(mView, Gravity.BOTTOM, 0, 0);
        final TextView tvMoney = (TextView) view.findViewById(R.id.tv_money);
        final TextView tvSurfaceMoney = (TextView) view.findViewById(R.id.tv_surfaceMoney);
        final TextView tvSimpleMoney = (TextView) view.findViewById(R.id.tv_simpleMoney);
        final TextView tvAddress = (TextView) view.findViewById(R.id.tv_address);
        final TextView tvSimple = (TextView) view.findViewById(R.id.tv_simple);
        final TextView tvSimple2 = (TextView) view.findViewById(R.id.tv_simple2);
        final TextView tvSurface = (TextView) view.findViewById(R.id.tv_surface);
        final TextView tvSurface2 = (TextView) view.findViewById(R.id.tv_surface2);
        final TextView tvNet = (TextView) view.findViewById(R.id.tv_net);
        final TextView tvNet2 = (TextView) view.findViewById(R.id.tv_net2);
        final LinearLayout ll_net = (LinearLayout) view.findViewById(R.id.rad_net);
        final LinearLayout ll_simple = (LinearLayout) view.findViewById(R.id.rad_simple);
        final LinearLayout ll_surface = (LinearLayout) view.findViewById(R.id.rad_surface);
        final LinearLayout llNet = (LinearLayout) view.findViewById(R.id.tv_netRecommond);
        final LinearLayout llSurface = (LinearLayout) view.findViewById(R.id.tv_surfaceRecommond);
        final LinearLayout llSimple = (LinearLayout) view.findViewById(R.id.tv_simpleRecommond);
        Button btn_truth = (Button) view.findViewById(R.id.tv_truth);
        final Button btn_delete = (Button) view.findViewById(R.id.btn_delete);
        tvAddress.setText(address + "");
        tvMoney.setText("￥" + doctor.getMoney() + "/" + doctor.getSurfaceMoney() + "分钟");
        tvSimpleMoney.setText("￥" + doctor.getSecondMoney() + "/次");
        tvSurfaceMoney.setText("￥" + doctor.getSurfaceMinute() + "/" + doctor.getNetworkMinute() + "分钟");
        if (doctor.getSpecialistCateg() == 1) {
            ll_simple.setEnabled(false);
        }
        if (id == 1) {
            ll_net.setBackgroundResource(R.drawable.ic_type_checked);
            tvNet.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
            tvNet2.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
            llNet.setVisibility(View.VISIBLE);
            tvMoney.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
        } else if (id == 2) {
            ll_simple.setBackgroundResource(R.drawable.ic_type_checked);
            tvSimple.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
            tvSimple2.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
            llSimple.setVisibility(View.VISIBLE);
            tvSimpleMoney.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
        } else if (id == 3) {
            ll_surface.setBackgroundResource(R.drawable.ic_type_checked);
            tvSurface.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
            tvSurface2.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
            llSurface.setVisibility(View.VISIBLE);
            tvSurfaceMoney.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
        }
        ll_net.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPopItemClickListener.onItemClickListener(1);
                ll_net.setBackgroundResource(R.drawable.ic_type_checked);
                ll_simple.setBackgroundResource(R.drawable.ic_type_unchecked);
                ll_surface.setBackgroundResource(R.drawable.ic_type_unchecked);
                tvSimple.setTextColor(context.getResources().getColor(R.color.text_color_black));
                tvSimple2.setTextColor(context.getResources().getColor(R.color.text_color_black));
                tvNet.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
                tvNet2.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
                tvSurface.setTextColor(context.getResources().getColor(R.color.text_color_black));
                tvSurface2.setTextColor(context.getResources().getColor(R.color.text_color_black));
                tvMoney.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
                tvSimpleMoney.setTextColor(context.getResources().getColor(R.color.red_f7));
                tvSurfaceMoney.setTextColor(context.getResources().getColor(R.color.red_f7));
                llNet.setVisibility(View.VISIBLE);
                llSimple.setVisibility(View.GONE);
                llSurface.setVisibility(View.GONE);
            }
        });
        ll_surface.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPopItemClickListener.onItemClickListener(2);
                ll_surface.setBackgroundResource(R.drawable.ic_type_checked);
                ll_simple.setBackgroundResource(R.drawable.ic_type_unchecked);
                ll_net.setBackgroundResource(R.drawable.ic_type_unchecked);
                tvSimple.setTextColor(context.getResources().getColor(R.color.text_color_black));
                tvSimple2.setTextColor(context.getResources().getColor(R.color.text_color_black));
                tvSurface.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
                tvSurface2.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
                tvNet.setTextColor(context.getResources().getColor(R.color.text_color_black));
                tvNet2.setTextColor(context.getResources().getColor(R.color.text_color_black));
                tvSurfaceMoney.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
                tvSimpleMoney.setTextColor(context.getResources().getColor(R.color.red_f7));
                tvMoney.setTextColor(context.getResources().getColor(R.color.red_f7));
                llSurface.setVisibility(View.VISIBLE);
                llSimple.setVisibility(View.GONE);
                llNet.setVisibility(View.GONE);
            }
        });
        ll_simple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPopItemClickListener.onItemClickListener(3);
                ll_simple.setBackgroundResource(R.drawable.ic_type_checked);
                ll_surface.setBackgroundResource(R.drawable.ic_type_unchecked);
                ll_net.setBackgroundResource(R.drawable.ic_type_unchecked);
                tvSurface.setTextColor(context.getResources().getColor(R.color.text_color_black));
                tvSurface2.setTextColor(context.getResources().getColor(R.color.text_color_black));
                tvSimple.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
                tvSimple2.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
                tvNet.setTextColor(context.getResources().getColor(R.color.text_color_black));
                tvNet2.setTextColor(context.getResources().getColor(R.color.text_color_black));
                tvSimpleMoney.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
                tvSurfaceMoney.setTextColor(context.getResources().getColor(R.color.red_f7));
                tvMoney.setTextColor(context.getResources().getColor(R.color.red_f7));
                llSimple.setVisibility(View.VISIBLE);
                llSurface.setVisibility(View.GONE);
                llNet.setVisibility(View.GONE);
            }
        });
        btn_truth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                onPopItemClickListener.onItemClickListener(4);
            }
        });
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }

    /*弹出选择预约时长弹窗*/
    public static void showTime(final Context context, View mView, int type, final Doctor doctor, String log_time) {
        Log.e("eeee",doctor.toString()+"<>"+log_time);
        final LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_appointment_time, null);
        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.showAtLocation(mView, Gravity.BOTTOM, 0, 0);
        TextView tvClose = (TextView) view.findViewById(R.id.tv_close);
        TextView tvTimeNmoney = (TextView) view.findViewById(R.id.tv_timeNmoney);
        final TextView tvTotalMoney = (TextView) view.findViewById(R.id.tv_totalMoney);
        Button btnTruth = (Button) view.findViewById(R.id.btn_truth);
        GridView recyclerView = (GridView) view.findViewById(R.id.rv_recycle);
        int duration = Integer.valueOf(log_time);
        int faceTime = Integer.valueOf(doctor.getNetworkMinute());
        int netTime = Integer.valueOf(doctor.getSurfaceMoney());
        int num;
        /*判断医生身份是否心理医生*/
        if (doctor.getSpecialistCateg() == 1) {
            switch (type){
                case AppointmentType.PREMIUM:
                    tvTimeNmoney.setText("￥" + doctor.getMoney() + "/" + doctor.getSurfaceMoney()+ "分钟");
                    List<String> list = new ArrayList<>();
                    for (int i = 1; i <= 2; i++) {
                        list.add(netTime * i + "");
                    }
                    final MyAdapter myAdapter = new MyAdapter(context, list, tvTotalMoney, doctor.getMoney() + "");
                    recyclerView.setAdapter(myAdapter);
                    recyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            myAdapter.setSelectedIndex(position);
                            myAdapter.notifyDataSetChanged();
                        }
                    });
                    break;
                case AppointmentType.FACE:
                    tvTimeNmoney.setText("￥" + doctor.getSurfaceMinute()+ "/" + doctor.getNetworkMinute() + "分钟");
                    List<String> lists = new ArrayList<>();
                    for (int i = 1; i <= 2; i++) {
                        lists.add(faceTime * i + "");
                    }
                    final MyAdapter myAdapter2 = new MyAdapter(context, lists, tvTotalMoney, doctor.getSurfaceMinute());
                    recyclerView.setAdapter(myAdapter2);
                    recyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            myAdapter2.setSelectedIndex(position);
                            myAdapter2.notifyDataSetChanged();
                        }
                    });
                    break;
            }
        } else {
            switch (type){
                case AppointmentType.PREMIUM:
                    tvTimeNmoney.setText("￥" + doctor.getMoney() + "/" + doctor.getSurfaceMoney()+ "分钟");
                    List<String> list = new ArrayList<>();
                    num = duration / netTime;
                    for (int i = 1; i <= num; i++) {
                        list.add(netTime * i + "");
                    }
                    final MyAdapter myAdapter = new MyAdapter(context, list, tvTotalMoney, doctor.getMoney() + "");
                    recyclerView.setAdapter(myAdapter);
                    recyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            myAdapter.setSelectedIndex(position);
                            myAdapter.notifyDataSetChanged();
                        }
                    });
                    break;
                case AppointmentType.FACE:
                    tvTimeNmoney.setText("￥" + doctor.getSurfaceMinute() + "/" + doctor.getNetworkMinute()+ "分钟");
                    num = duration / faceTime;
                    List<String> lists = new ArrayList<>();
                    for (int i = 1; i <= num; i++) {
                        lists.add(faceTime * i + "");
                    }
                    final MyAdapter myAdapter2 = new MyAdapter(context, lists, tvTotalMoney, doctor.getSurfaceMinute());
                    recyclerView.setAdapter(myAdapter2);
                    recyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            myAdapter2.setSelectedIndex(position);
                            myAdapter2.notifyDataSetChanged();
                        }
                    });
                    break;

            }
        }
        tvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btnTruth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                Intent intent = new Intent();
                intent.setAction("getTime");
                intent.putExtra(Constants.APPOINTMENT_MONEY, tvTotalMoney.getText().toString());
                intent.putExtra(Constants.TIME, tvTotalMoney.getHint().toString());
                if (!TextUtils.isEmpty(tvTotalMoney.getText().toString()) && !TextUtils.isEmpty(tvTotalMoney.getHint().toString())) {
                    context.sendBroadcast(intent);
                }
            }
        });
    }

    /*弹出选择预约日期和时长弹窗*/
    public static void showPickDate(final Context context, final View mview, final int type, final int doctorId, final int time) {
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        final Calendar now = Calendar.getInstance();
        final Calendar nextMonth = Calendar.getInstance();
        nextMonth.add(Calendar.MONTH, 5);
        TimeModule api = Api.of(TimeModule.class);
        final LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_appointment_pick_date, null);
        final PopupWindow mPopupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        mPopupWindow.showAtLocation(mview, Gravity.BOTTOM, 0, 0);
        TextView tv_pickdate = (TextView) view.findViewById(R.id.tv_pickdate);
        TextView tv_delete = (TextView) view.findViewById(R.id.tv_delete);
        final CalendarPickerView pickerView = (CalendarPickerView) view.findViewById(R.id.calendar_view);
        pickerView.init(now.getTime(), nextMonth.getTime())
                .inMode(CalendarPickerView.SelectionMode.MULTIPLE);
        String typeText = "";
        if (type == 1) {
            typeText = "请选择预约日期(专属网诊)";
        } else if (type == 2) {
            typeText = "请选择预约日期(简易复诊)";
        } else if (type == 4) {
            typeText = "请选择预约日期(诊所面诊)";
        }
        tv_pickdate.setText(typeText);
        api.getDate(doctorId, time, type).enqueue(new SimpleCallback<List<Time>>() {
            @Override
            protected void handleResponse(List<Time> response) {
                if (response == null) {
                    return;
                }
                try {
                    Date minDate = simpleDateFormat.parse(response.get(0).getDate());
                    Date maxDate = simpleDateFormat.parse(response.get(response.size() - 1).getDate());
                    maxDate.setTime(maxDate.getTime() + ONE_DAY);
                    pickerView.init(minDate, maxDate).inMode(CalendarPickerView.SelectionMode.MULTIPLE);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                for (int i = response.size() - 1; i >= 0; i--) {
                    Time time = response.get(i);
                    String data = time.getDate();
                    try {
                        pickerView.selectDate(simpleDateFormat.parse(data));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        pickerView.setCellClickInterceptor(new CalendarPickerView.CellClickInterceptor() {
            @Override
            public boolean onCellClicked(Date date) {
                if (pickerView.getSelectedDates().contains(date)) {
                    String pickDate = simpleDateFormat.format(date);

                    showPickTime(context, mview, type, time, pickDate, doctorId, mPopupWindow);
                }
                return false;
            }
        });
        tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
            }
        });
    }

    /*弹出选择时间段弹窗*/
    public static void showPickTime(final Context context, View mView, int type, int time, final String date, int doctorId, final PopupWindow mPopupWindow) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_appointment_pick_time, null);
        TimeModule api = Api.of(TimeModule.class);
        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.showAtLocation(mView, Gravity.BOTTOM, 0, 0);
        LinearLayout ll_back = (LinearLayout) view.findViewById(R.id.ll_back);
        TextView tv_date = (TextView) view.findViewById(R.id.tv_date);
        TextView tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
        Button btn_truth = (Button) view.findViewById(R.id.btn_truth);
        final GridView recyclerView = (GridView) view.findViewById(R.id.rv_recycle);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日", Locale.CHINA);
        SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss", Locale.CHINA);
        try {
            Date d = simple.parse(date + "-00:00:00");
            Date date1 = new Date(d.getTime());
            tv_date.setText(simpleDateFormat.format(date1));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        api.getDaySchedule(doctorId, date, type, time + "").enqueue(new SimpleCallback<List<Time>>() {
            @Override
            protected void handleResponse(List<Time> response) {
                mAdapter = new GridAdapter(context, response);
                recyclerView.setAdapter(mAdapter);
            }
        });
        recyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mAdapter != null) {
                    mAdapter.setSelectIndex(position);
                    mAdapter.notifyDataSetChanged();

                }
            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                mPopupWindow.dismiss();
            }
        });
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        btn_truth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAdapter != null && mAdapter.getSelectIndex() != -1) {
                    Intent intent = new Intent();
                    intent.setAction(Constants.DATE);
                    intent.putExtra(Constants.TIME, date + " " + mAdapter.getList().get(mAdapter.getSelectIndex()).getFrom());
                    context.sendBroadcast(intent);
                }
                dismiss();
                mPopupWindow.dismiss();
            }
        });
    }

    /*弹出选择患者弹窗*/
    public static void showPickCustom(final Context context, View mView) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_appointment_pick_custom, null);
        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.showAtLocation(mView, Gravity.BOTTOM, 0, 0);
        TextView tv_close = (TextView) view.findViewById(R.id.tv_close);
        final GridView gridView = (GridView) view.findViewById(R.id.grid_custom);
        Button btn_truth = (Button) view.findViewById(R.id.btn_truth);
        ProfileModule api = Api.of(ProfileModule.class);
        api.medicalRecordList().enqueue(new SimpleCallback<List<MedicalRecord>>() {
            @Override
            protected void handleResponse(List<MedicalRecord> response) {
                myCustomAdapter = new MyCustomAdapter(context, response);
                gridView.setAdapter(myCustomAdapter);
            }
        });
        tv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        btn_truth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myCustomAdapter != null) {
                    if (myCustomAdapter.getSelectIndex() == -1) {
                        Toast.makeText(context, "请选择需要咨询的患者!", Toast.LENGTH_LONG).show();
                        return;
                    }
                    Intent intent = new Intent();
                    intent.setAction(Constants.PICKCUSTOM);
                    MedicalRecord record = myCustomAdapter.getList().get(myCustomAdapter.getSelectIndex());
                    intent.putExtra(Constants.REMARK, record.getRecordName());
                    intent.putExtra(Constants.DATA, record.getMedicalRecordId());
                    String sex;
                    if (record.getGender() == 1) {
                        sex = "男";
                    } else {
                        sex = "女";
                    }
                    String msg = record.getRecordName() + "(" + sex + "/" + record.getAge() + ")";
                    intent.putExtra(Constants.ADDRESS, msg);
                    context.sendBroadcast(intent);
                    dismiss();
                }
            }
        });
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (myCustomAdapter != null) {
                    myCustomAdapter.setSelectIndex(position);
                    myCustomAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    public static void showAppointmentPay(final Context context, View mView, final int type, final Doctor doctor, final double money, String patient, final int time, final long bookTime, String date, final int recordId) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_appointment_pay, null);

        ProfileModule api = Api.of(ProfileModule.class);
        final AppointmentModule http = Api.of(AppointmentModule.class);
        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.showAtLocation(mView, Gravity.BOTTOM, 0, 0);

        TextView tv_painetName = (TextView) view.findViewById(R.id.tv_painetName);
        TextView tv_close = (TextView) view.findViewById(R.id.tv_close);
        TextView tv_doctorName = (TextView) view.findViewById(R.id.tv_doctorName);
        TextView tv_type = (TextView) view.findViewById(R.id.tv_d_type);
        TextView tv_time = (TextView) view.findViewById(R.id.tv_time);
        TextView tv_money = (TextView) view.findViewById(R.id.tv_money);
        final GridView grid_discount = (GridView) view.findViewById(R.id.grid_discount);
        RadioGroup radio_payment = (RadioGroup) view.findViewById(R.id.radio_payment);
        final RadioButton radio_ali = (RadioButton) view.findViewById(R.id.radio_ali);
        final RadioButton radio_wechat = (RadioButton) view.findViewById(R.id.radio_wechat);
        Button btn_truth = (Button) view.findViewById(R.id.btn_truth);
        String typeText = "";
        String currentScope = "";
        if (type == 1) {
            typeText = "专属网诊";
            currentScope = "detail_consult";
        } else if (type == 2) {
            typeText = "简易复诊";
            currentScope = "simple_consult";
        } else if (type == 4) {
            typeText = "诊所面诊";
            currentScope = "surface_consult";
        }
        tv_type.setText(typeText);
        tv_doctorName.setText(doctor.getName());
        tv_painetName.setText(patient);
        tv_money.setText("￥" + money);
        tv_time.setText(date);

        api.getAppointmentCoupons("valid", currentScope, true, money + "", "all", "1").enqueue(new SimpleCallback<List<Coupon>>() {
            @Override
            protected void handleResponse(List<Coupon> response) {
                mPayAdapter = new MyPayAdapter(context, response);
                grid_discount.setAdapter(mPayAdapter);
            }
        });
        grid_discount.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mPayAdapter != null) {
                    mPayAdapter.setSelectedIndex(position);
                    mPayAdapter.notifyDataSetChanged();
                }
            }
        });
        radio_payment.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_ali:
                        radio_ali.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
                        radio_wechat.setTextColor(context.getResources().getColor(R.color.text_color_black));
                        break;
                    case R.id.radio_wechat:
                        radio_wechat.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
                        radio_ali.setTextColor(context.getResources().getColor(R.color.text_color_black));
                        break;
                }
            }
        });
        tv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        btn_truth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPayAdapter != null) {
                    if (mPayAdapter.getSelectedIndex()!=-1){
                        final Coupon coupon = mPayAdapter.getList().get(mPayAdapter.getSelectedIndex());
                        if (coupon.isThreshold_available() == true) {
                            if (!TextUtils.isEmpty(coupon.getCouponMoney())) {
                                double couponMoney = Double.parseDouble(coupon.getCouponMoney());
                        /*    double finalMoney = 0;
                            if (money - couponMoney >= 0) {
                                finalMoney = money - couponMoney;
                            } else {
                                finalMoney = 0;
                            }*/
                                http.toAppointmentDoctor(bookTime, doctor.getId(), recordId, time, type).enqueue(new SimpleCallback<Appointment>() {
                                    @Override
                                    protected void handleResponse(Appointment response) {
                                        if(radio_ali.isChecked()){
                                            AppointmentHandler2.payWithAlipay((Activity) context, coupon.id, response);
                                        }else{
                                            AppointmentHandler2.payWithWeChat((Activity) context, coupon.id, response);
                                        }
                                    }
                                });


                            }

                        }
                    }else{
                        http.toAppointmentDoctor(bookTime, doctor.getId(), recordId, time, type).enqueue(new SimpleCallback<Appointment>() {
                            @Override
                            protected void handleResponse(Appointment response) {
                                if(radio_ali.isChecked()){
                                    AppointmentHandler2.payWithAlipay((Activity) context, "", response);
                                }else{
                                    AppointmentHandler2.payWithWeChat((Activity) context, "", response);
                                }
                            }
                        });
                    }

                }
            }
        });

    }

    public interface OnPopItemClickListener {
        void onItemClickListener(int which);
    }

    public static void dismiss() {
        if (popupWindow != null) {
            popupWindow.dismiss();
        }
    }

    /*支付界面适配器*/
    public static class MyPayAdapter extends BaseAdapter {
        Context context;
        List<Coupon> list;
        int selectedIndex = -1;

        public MyPayAdapter(Context context, List<Coupon> list) {
            this.context = context;
            this.list = list;
        }

        public List<Coupon> getList() {
            return list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        public int getSelectedIndex() {
            return selectedIndex;
        }

        public void setSelectedIndex(int selectedIndex) {
            this.selectedIndex = selectedIndex;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        ViewHolder holder;

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.item_appointment_coupons, null);
                holder.ll_couponBg = (LinearLayout) convertView.findViewById(R.id.ll_coupon_bg);
                holder.tv_couponMoney = (TextView) convertView.findViewById(R.id.tv_coupon_money);
                holder.tv_couponTime = (TextView) convertView.findViewById(R.id.tv_valid_time);
                holder.tv_couponType = (TextView) convertView.findViewById(R.id.ic_coupon_type);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            Coupon coupon = list.get(position);
            holder.tv_couponMoney.setText(coupon.couponDetail());
            if (coupon.isThreshold_available() == true) {
                holder.tv_couponTime.setText(coupon.getValidEndTime() + "前可用");
                holder.tv_couponType.setVisibility(View.GONE);
            } else {
                holder.tv_couponType.setVisibility(View.VISIBLE);
                holder.tv_couponType.setText(coupon.couponScope());
            }
            if (selectedIndex == position && coupon.isThreshold_available() == true) {
                holder.tv_couponMoney.setTextColor(context.getResources().getColor(R.color.orange));
                holder.ll_couponBg.setBackgroundResource(R.drawable.ic_coupon_checked);
            } else {
                if (coupon.isThreshold_available() == true) {
                    holder.tv_couponMoney.setTextColor(context.getResources().getColor(R.color.text_color_black));
                    holder.ll_couponBg.setBackgroundResource(R.drawable.ic_coupon_unchecked);
                } else {
                    holder.ll_couponBg.setBackgroundResource(R.drawable.item_appointment_gray_bg);
                    holder.tv_couponType.setVisibility(View.VISIBLE);
                    holder.tv_couponType.setText(coupon.couponScope());
                    holder.ll_couponBg.setEnabled(false);
                }

            }
            return convertView;
        }

        class ViewHolder {
            TextView tv_couponTime, tv_couponMoney, tv_couponType;
            LinearLayout ll_couponBg;
        }
    }

    /*选择预约患者适配器*/
    public static class MyCustomAdapter extends BaseAdapter {
        List<MedicalRecord> list;
        Context context;
        int selectIndex = -1;

        public MyCustomAdapter(Context context, List<MedicalRecord> list) {
            this.context = context;
            this.list = list;
        }

        public int getSelectIndex() {
            return selectIndex;
        }

        public List<MedicalRecord> getList() {
            return list;
        }

        public void setSelectIndex(int selectIndex) {
            this.selectIndex = selectIndex;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public int getCount() {
            return list.size() + 1;
        }

        ViewHolder holder;

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.item_appointment_time_checked, null);
                holder.textView = (TextView) convertView.findViewById(R.id.item_checked);
                holder.tv_minute = (TextView) convertView.findViewById(R.id.tv_minute);
                holder.ll_time = (LinearLayout) convertView.findViewById(R.id.item_ll_checked);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tv_minute.setVisibility(View.GONE);
            if (position < list.size()) {
                holder.textView.setText(list.get(position).getRecordName());
            } else {
                holder.textView.setText("+添加新患者");
            }

            if (selectIndex == position && selectIndex != list.size()) {
                holder.ll_time.setBackgroundResource(R.drawable.item_time_checked);
            } else {
                holder.ll_time.setBackgroundResource(R.drawable.item_time_unchecked);
            }
            holder.textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (selectIndex == list.size()) {
                        Bundle otherRecord = NewMedicalRecordFragment.newOtherRecord();
                        if (MedicalRecordHandler.hasSelfRecord(list)) {
                            Intent intent = SingleFragmentActivity.intentFor(context, "新建病历", otherRecord);
                            context.startActivity(intent);
                        } else {
                            Bundle selfRecord = NewMedicalRecordFragment.newSelfRecord();
                            Intent intent = BundlesTabActivity.intentFor(context, selfRecord, otherRecord);
                            context.startActivity(intent);
                        }
                        dismiss();
                    }
                }
            });
            return convertView;
        }

        class ViewHolder {
            TextView textView, tv_minute;
            LinearLayout ll_time;
        }
    }

    /*选择预约时长适配器*/
    private static class MyAdapter extends BaseAdapter {
        Context context;
        List<String> list;
        private TextView textView;
        private String money;
        private int selectedIndex;

        public MyAdapter(Context context, List<String> list, TextView textView, String money) {
            this.context = context;
            this.list = list;
            this.textView = textView;
            this.money = money;
        }

        public int getSelectedIndex() {
            return selectedIndex;
        }

        public void setSelectedIndex(int selectedIndex) {
            this.selectedIndex = selectedIndex;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        ViewHolder holder;

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.item_appointment_time_checked, null);
                holder.item_checked = (TextView) convertView.findViewById(R.id.item_checked);
                holder.item_ll_checked = (LinearLayout) convertView.findViewById(R.id.item_ll_checked);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.item_checked.setText(list.get(position));
            if (selectedIndex == position) {
                holder.item_ll_checked.setBackgroundResource(R.drawable.item_time_checked);
                textView.setText(Double.valueOf(money) * (position + 1) + "");
                textView.setHint(holder.item_checked.getText().toString());
            } else {
                holder.item_ll_checked.setBackgroundResource(R.drawable.item_time_unchecked);
            }

            return convertView;
        }

        class ViewHolder {
            TextView item_checked;
            LinearLayout item_ll_checked;
        }
    }

    /*选择预约时间段适配器*/
    public static class GridAdapter extends BaseAdapter {

        Context context;
        List<Time> list;
        int selectIndex = -1;

        public GridAdapter(Context context, List<Time> list) {
            this.context = context;
            this.list = list;
        }

        public List<Time> getList() {
            return list;
        }

        public int getSelectIndex() {
            return selectIndex;
        }

        public void setSelectIndex(int selectIndex) {
            this.selectIndex = selectIndex;
        }

        public String time(Time data) {
            if (data.getFrom() == null || data.getFrom().equals("")) {
                return "";
            }
            return data.getFrom().substring(0, 5) + " - " + data.getTo().substring(0, 5);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        ViewHolder holder;

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.item_time_data, parent, false);
                holder.tv_time = (TextView) convertView.findViewById(R.id.tv_item_time);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tv_time.setText(time(list.get(position)));
            if (selectIndex == position) {
                holder.tv_time.setBackgroundResource(R.color.colorPrimaryDark);
                holder.tv_time.setTextColor(context.getResources().getColor(R.color.white));
            } else {
                holder.tv_time.setBackgroundResource(R.color.white);
                holder.tv_time.setTextColor(context.getResources().getColor(R.color.text_color_black));
            }
            return convertView;
        }

        class ViewHolder {
            TextView tv_time;
        }
    }
}
