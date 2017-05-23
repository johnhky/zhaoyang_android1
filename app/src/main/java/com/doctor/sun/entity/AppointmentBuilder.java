package com.doctor.sun.entity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.Observable;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.doctor.sun.BR;
import com.doctor.sun.BuildConfig;
import com.doctor.sun.R;
import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.entity.constans.AppointmentType;
import com.doctor.sun.entity.constans.CouponType;
import com.doctor.sun.entity.constans.PayMethod;
import com.doctor.sun.entity.handler.AppointmentHandler2;
import com.doctor.sun.event.SelectMedicalRecordEvent;
import com.doctor.sun.event.ShowDialogEvent;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.ApiCallback;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.immutables.Appointment;
import com.doctor.sun.module.AppointmentModule;
import com.doctor.sun.module.ProfileModule;
import com.doctor.sun.module.TimeModule;
import com.doctor.sun.module.ToolModule;
import com.doctor.sun.ui.activity.SingleFragmentActivity;
import com.doctor.sun.ui.activity.patient.ApplyAppointmentActivity;
import com.doctor.sun.ui.activity.patient.SearchDoctorActivity;
import com.doctor.sun.ui.fragment.DoctorArticleFragment;
import com.doctor.sun.ui.fragment.DoctorCommentFragment;
import com.doctor.sun.ui.widget.SelectRecordDialog;
import com.doctor.sun.vm.AppointmentWrapper;
import com.doctor.sun.vm.BaseItem;
import com.doctor.sun.vm.ClickMenu;
import com.doctor.sun.vm.ItemButton;
import com.doctor.sun.vm.ItemRadioGroup;
import com.doctor.sun.vm.ItemTextInput2;
import com.squareup.otto.Subscribe;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import io.ganguo.library.core.event.EventHub;
import retrofit2.Call;

/**
 * Created by rick on 6/7/2016.
 * 预约操作类
 *
 */

public class AppointmentBuilder extends BaseObservable implements Parcelable {
    private static final String YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm:ss";

    private ProfileModule api;
    private AppointmentModule appointmentModule;
    private ToolModule toolModule;

    private int type = AppointmentType.PREMIUM;
    private int duration = -1;
    private Time time;
    private boolean isToday;

    private Doctor doctor;
    private MedicalRecord record;
    private int selectedCoupon = -1;
    private List<Coupon> coupons;
    private Integer[] selectTags;

    public AppointmentBuilder() {
        init();
    }

    public void init() {
        api = Api.of(ProfileModule.class);
        appointmentModule = Api.of(AppointmentModule.class);
        toolModule = Api.of(ToolModule.class);
    }

    public AppointmentBuilder(ProfileModule api, AppointmentModule appointmentModule, ToolModule toolModule) {
        this.api = api;
        this.appointmentModule = appointmentModule;
        this.toolModule = toolModule;
    }


    public void setIsPremium(boolean isPremium) {
        if (isPremium) {
            setType(AppointmentType.PREMIUM);
        }
    }

    public void setIsNormal(boolean isNormal) {
        if (isNormal) {
            setType(AppointmentType.STANDARD);
        }
    }

    @Bindable
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
        notifyChange();
    }

    @Bindable
    public int getDuration() {
        return duration;
    }

    public String getDurationLabel() {
        if (getType() == AppointmentType.STANDARD) {
            return "<font color=\"#363636\">预约时长:</font>  预约当天";
        }
        if (duration <= 0) {
            return "<font color=\"#363636\">预约时长:</font>  <font color=\"#c1cacf\">暂未选择</font>";
        } else {
            return "<font color=\"#363636\">预约时长:</font>  " + duration + "分钟";
        }
    }

    public String getMoneyLabel() {
        if (getType() == AppointmentType.STANDARD) {
            return "<font color=\"#363636\">诊金:</font>  " + money() + "元";
        }
        //前面的空格
        if (duration <= 0) {
            return "<font color=\"#363636\">诊金:</font>  <font color=\"#c1cacf\">暂无</font>";
        } else {
            return "<font color=\"#363636\">诊金:</font>  " + money() + "元";
        }
    }

    public void setDuration(int duration) {
        this.duration = duration;
        notifyPropertyChanged(BR.duration);
    }

    public void setDurationNotifyAll(int duration) {
        this.duration = duration;
        notifyChange();
    }

    @Bindable
    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
        notifyPropertyChanged(BR.time);
    }

    @Bindable
    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
        notifyPropertyChanged(BR.doctor);
    }


    @Bindable
    public MedicalRecord getRecord() {
        return record;
    }

    public boolean canIncrement() {
        return duration < 120;
    }

    public boolean canDecrement() {
        return duration > 15;
    }

    public void incrementDuration() {
        if (duration < 60) {
            duration += 15;
        } else {
            duration += 30;
        }
        notifyChange();
    }

    public void decrementDuration() {
        if (duration <= 60) {
            duration -= 15;
        } else {
            duration -= 30;
        }
        notifyChange();
    }

    public void setRecord(MedicalRecord record) {
        this.record = record;
        notifyPropertyChanged(BR.record);
    }

    public void searchDoctor(Context context, int type) {
        Intent intent = SearchDoctorActivity.makeIntent(context, type);
        context.startActivity(intent);
    }

    public void pickDate(final Context context) {
        if (duration <= 0 && getType() == AppointmentType.PREMIUM) {
            Toast.makeText(context, "请选择就诊时长", Toast.LENGTH_SHORT).show();
            return;
        }
        SelectRecordDialog.showRecordDialog(context, null);
    }

    public void latestAvailableTime(final Context context, int doctorId, int duration) {
        TimeModule timeModule = Api.of(TimeModule.class);
        timeModule.latestAvailableTime(doctorId, duration, getTime().getDate()).enqueue(new SimpleCallback<Time>() {
            @Override
            protected void handleResponse(Time response) {
                if (response == null) {
                    return;
                }
                setTime(response);
                reviewAppointment(context);
            }
        });
    }

    public void reviewAppointment(Context context) {
        Intent intent = ApplyAppointmentActivity.makeIntent(context, AppointmentBuilder.this);
        context.startActivity(intent);
    }

    public double money() {
        switch (getType()) {
            case AppointmentType.STANDARD:
                return doctor.getSecondMoney();
            case AppointmentType.PREMIUM:
                int scalar = getDuration() / 15;
                return doctor.getMoney() * scalar;
            default:
                return 0;
        }
    }

    public double price() {
        switch (getType()) {
            case AppointmentType.STANDARD:
                return doctor.getSecondMoney();
            case AppointmentType.PREMIUM:
                return doctor.getMoney();
            default:
                return 0;
        }
    }

    public boolean isToday() {
        return isToday;
    }

    public void setToday(boolean today) {
        isToday = today;
    }


    public void selectCoupon(Context context) {
        if (coupons == null || coupons.isEmpty()) {
            Toast.makeText(context, "暂时没有可以使用的优惠券", Toast.LENGTH_SHORT).show();
            return;
        }
        new MaterialDialog.Builder(context)
                .title("选择优惠券(单选)")
                .negativeText("不使用优惠券")
                .items(coupons)
                .itemsCallbackSingleChoice(selectedCoupon, new MaterialDialog.ListCallbackSingleChoice() {

                    @Override
                    public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                        selectedCoupon = which;
                        notifyChange();
                        return true;
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        selectedCoupon = -1;
                        notifyChange();
                    }
                })
                .build().show();
    }

    public void loadCoupons() {
        api.coupons(CouponType.CAN_USE_NOW, scope(), money()).enqueue(new SimpleCallback<List<Coupon>>() {
            @Override
            protected void handleResponse(List<Coupon> response) {
                if (response != null && !response.isEmpty()) {
                    coupons = response;
                    notifyChange();
                } else {
                    coupons = null;
                    notifyChange();
                }
            }
        });
    }

    private String scope() {
        if (getType() == AppointmentType.PREMIUM) {
            return Coupon.Scope.PREMIUM_APPOINTMENT;
        } else if (getType() == AppointmentType.STANDARD) {
            return Coupon.Scope.STANDARD_APPOINTMENT;
        }
        return "";
    }

    public void loadTags() {
        toolModule.doctorInfo(doctor.getId()).enqueue(new SimpleCallback<Doctor>() {
            @Override
            protected void handleResponse(Doctor response) {
                doctor = response;
                notifyChange();
            }
        });
    }


    public String getCouponRemarks() {
        if (coupons == null || coupons.isEmpty()) {
            return "您暂时没有可用优惠券";
        }
        if (isCouponSelected()) {
            return "已使用" + coupons.get(selectedCoupon).detail();
        }
        return String.format(Locale.CHINA, "您有%d张可用优惠券", coupons.size());
    }

    public String getShouldPay() {
        if (isCouponSelected()) {
            double couponMoney = Double.parseDouble(coupons.get(selectedCoupon).couponMoney);
            double d = Math.max(0D, money() - couponMoney);
            return String.valueOf(d);
        } else {
            return String.valueOf(money());
        }
    }

    public boolean isCouponSelected() {
        if (coupons == null || coupons.isEmpty()) {
            return false;
        }
        return selectedCoupon >= 0 && selectedCoupon < coupons.size();
    }

    public void changeMedicalRecord(Context context) {
        SelectRecordDialog.showRecordDialog(context, this);
    }

    public void showSelectTagsDialog(Context context) {
        if (hasNoTags()) {
            Toast.makeText(context, "该医生没有任何标签可选", Toast.LENGTH_SHORT).show();
            return;
        }
        new MaterialDialog.Builder(context)
                .title("选择标签(多选)")
                .positiveText("确定")
                .items(doctor.tags)
                .itemsCallbackMultiChoice(selectTags, new MaterialDialog.ListCallbackMultiChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, Integer[] which, CharSequence[] text) {
                        selectTags = which;
                        return false;
                    }
                })
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                        notifyChange();
                    }
                })
                .build().show();
    }

    public boolean hasNoTags() {
        return doctor.tags == null || doctor.tags.isEmpty();
    }

    public String tagsRemarks() {
        if (hasNoTags()) {
            return "该医生没有任何标签可选";
        }
        if (selectTags == null || selectTags.length == 0) {
            return "您暂时没有选择任何标签";
        }
        return String.format(Locale.CHINA, "您已选择%d个咨询标签", selectTags.length);
    }

    public void applyAppointment(final Context context, final boolean isUseWechat) {
        final String finalCouponId = getFinalCouponId();

        final ApiCallback<Appointment> callback = new ApiCallback<Appointment>() {
            @Override
            protected void handleResponse(Appointment response) {
//                final String medicalRecordId = String.valueOf(getRecord().getMedicalRecordId());
              if (response.getId()!=null){
                  if (isUseWechat) {
                      AppointmentHandler2.payWithWeChat((Activity) context, finalCouponId, response);
                  } else {
                      AppointmentHandler2.payWithAlipay((Activity) context, finalCouponId, response);
                  }
              }


            }
            @Override
            public void onFailure(Call<ApiDTO<Appointment>> call, Throwable t) {
                super.onFailure(call, t);
            }
        };
        applyAppointment(callback);

    }

    public String getFinalCouponId() {
        String couponId = "";
        if (isCouponSelected()) {
            couponId = getCouponId();
        }
        return couponId;
    }

    public void applyAppointment(ApiCallback<Appointment> callback) {
        int doctorId = getDoctor().getId();
        int type = getType();

        HashMap<String, String> params = new HashMap<String, String>();
        if (type == AppointmentType.PREMIUM) {
            params.put("takeTime", String.valueOf(getDuration()));
        }

        final String medicalRecordId = String.valueOf(getRecord().getMedicalRecordId());

        //noinspection WrongConstant
        appointmentModule.orderAppointment(doctorId, getTimestamp(), type, medicalRecordId, getFinalCouponId(), getSelectedTagIds(), params).enqueue(callback);

    }


    public ArrayList<String> getSelectedTagIds() {
        ArrayList<String> result = new ArrayList<>();
        if (selectTags != null && selectTags.length > 0) {
            for (Integer selectTag : selectTags) {
                Tags tags = doctor.tags.get(selectTag);
                result.add(tags.tagId);
            }
        }
        return result;
    }

    public String getTimestamp() {
        String dateFormat = YYYY_MM_DD_HH_MM;
        if (getType() == AppointmentType.STANDARD) {
            if (!isToday()) {
                getTime().setFrom("00:00:00");
            } else {
                Date date = new Date(System.currentTimeMillis());
                SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss", Locale.CHINA);
                String hours = format.format(date);
                getTime().setFrom(hours);
            }
        }
        SimpleDateFormat format = new SimpleDateFormat(dateFormat, Locale.CHINA);
        Date parse = null;
        try {
            parse = format.parse(time.getDate() + " " + time.getFrom());
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        final String timestamp;
        if (parse != null) {
            timestamp = String.valueOf(parse.getTime()).substring(0, 10);
        } else {
            timestamp = "";
        }
        return timestamp;
    }

    public String getFormatedTimeLabel() {
        return String.format("预约时间:%s", getTimeLabel());
    }

    public String getTimeLabel() {
        Time time = getTime();
        StringBuilder builder = new StringBuilder();
        builder.append(time.getDate());
        if (getType() == AppointmentType.PREMIUM) {
            builder.append(" ").append(time.getFrom()).append("-").append(time.getTo());
        }
        return builder.toString();
    }

    public String getTypeLabel() {
        String type = "";
        switch (getType()) {
            case AppointmentType.PREMIUM:
                type = "专属网诊";
                break;
            case AppointmentType.STANDARD:
                type = "闲时咨询";
                break;
        }
        return type;
    }

    public String formatTypeLabel() {
        return String.format("预约类型:%s", getTypeLabel());
    }


    @Subscribe
    public void onEventMainThread(SelectMedicalRecordEvent event) {
        setRecord(event.getRecord());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.type);
        dest.writeInt(this.duration);
        dest.writeParcelable(this.time, flags);
        dest.writeByte(isToday ? (byte) 1 : (byte) 0);
        dest.writeParcelable(this.doctor, flags);
        dest.writeParcelable(this.record, flags);
    }

    protected AppointmentBuilder(Parcel in) {
        this.type = in.readInt();
        this.duration = in.readInt();
        this.time = in.readParcelable(Time.class.getClassLoader());
        this.isToday = in.readByte() != 0;
        this.doctor = in.readParcelable(Doctor.class.getClassLoader());
        this.record = in.readParcelable(MedicalRecord.class.getClassLoader());
    }

    public static final Creator<AppointmentBuilder> CREATOR = new Creator<AppointmentBuilder>() {
        @Override
        public AppointmentBuilder createFromParcel(Parcel source) {
            return new AppointmentBuilder(source);
        }

        @Override
        public AppointmentBuilder[] newArray(int size) {
            return new AppointmentBuilder[size];
        }
    };

    public String getCouponId() {
        return coupons.get(selectedCoupon).id;
    }

    public List<BaseItem> toSortedItems(final Appointment response) {
        ArrayList<BaseItem> result = new ArrayList<>();

//        result.add(record);
//        result.add(new Description(R.layout.item_description, "预约详情"));
//        result.add(doctor);

//        result.add(new BaseItem(R.layout.divider_1px));

        result.add(new AppointmentWrapper(R.layout.item_appointment_detail, response));

        Description couponDescription = new Description(R.layout.item_description, "优惠券");
        couponDescription.setBackgroundColor(R.color.color_coupon_background_yellow);
        couponDescription.setTitleColor(R.color.white);
        result.add(couponDescription);

        int discountMoney = AppointmentHandler2.getDiscountMoney(response);
        String status;
        if (discountMoney > 0) {
            status = "已使用优惠券优惠" + discountMoney;
        } else {
            status = "没有使用优惠券";
        }
        final ClickMenu selectCoupon = new ClickMenu(R.layout.item_select_coupon, 0, status, null);
        selectCoupon.setEnabled(false);
        result.add(selectCoupon);

        String shouldPayMoneyString = "<font color=\"#f65600\">实际付款：￥" + response.getNeed_pay() + "</font>";
        final ItemTextInput2 shouldPayMoney = new ItemTextInput2(R.layout.item_total_money, shouldPayMoneyString, "");
        shouldPayMoney.setTitleGravity(Gravity.START);
        shouldPayMoney.setItemId("shouldPayMoney");
        shouldPayMoney.setPosition(result.size());

        selectCoupon.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                double shouldPay = response.getNeed_pay();
                if (shouldPay < 0D) {
                    shouldPay = 0D;
                }
                BigDecimal bigDecimal = new BigDecimal(shouldPay).setScale(2, BigDecimal.ROUND_UP);
                String shouldPayMoneyString = "<font color=\"#f65600\">实际付款：￥" + bigDecimal.doubleValue() + "</font>";
                shouldPayMoney.setTitle(shouldPayMoneyString);
                shouldPayMoney.notifyChange();
            }
        });
        result.add(shouldPayMoney);

        BaseItem baseItem = new BaseItem(R.layout.space_8dp);
        baseItem.setItemId("SPACE" + result.size());
        baseItem.setPosition(result.size());
        result.add(baseItem);

        result.add(new Description(R.layout.item_description, "支付方式"));
        final ItemRadioGroup payMethod = new ItemRadioGroup(R.layout.item_pick_pay_method);
        result.add(payMethod);

        ItemButton confirmButton = new ItemButton(R.layout.item_big_button, "确定") {
            @Override
            public void onClick(View view) {
                switch (payMethod.getSelectedItem()) {
                    case PayMethod.ALIPAY:
                        AppointmentHandler2.payWithAlipay((Activity) view.getContext(), "", response);
                        break;
                    case PayMethod.SIMULATED:
                        if (BuildConfig.DEBUG) {
                            AppointmentHandler2.simulatedPayImpl(response);
                        } else {
                            Toast.makeText(view.getContext(), "正式服不支持模拟支付", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case PayMethod.WECHAT:
                        AppointmentHandler2.payWithWeChat((Activity) view.getContext(), "", response);
                        break;
                    default:

                        break;
                }
            }
        };
        result.add(confirmButton);

        return result;
    }

    public void showSelectAppointmentTypeDialog() {
        EventHub.post(new ShowDialogEvent());
    }
}
