package com.doctor.sun.entity.handler;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.support.annotation.NonNull;
import android.view.View;

import com.doctor.sun.Settings;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.immutables.ImmutablePrescription;
import com.doctor.sun.immutables.ModifiablePrescription;
import com.doctor.sun.immutables.Prescription;
import com.doctor.sun.ui.activity.SingleFragmentActivity;
import com.doctor.sun.ui.adapter.core.BaseListAdapter;
import com.doctor.sun.ui.fragment.DiagnosisFragment;
import com.doctor.sun.ui.fragment.EditPrescriptionsFragment;
import com.doctor.sun.util.JacksonUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.Strings;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

/**
 * Created by rick on 27/10/2016.
 *
 */

public class PrescriptionHandler {
    private static final String[] keys = new String[]{"早", "午", "晚", "睡前"};

    public static void modify(Context context, final BaseListAdapter adapter, final Prescription data) {
        View focusCurrent = ((Activity) context).getWindow().getCurrentFocus();
        if (focusCurrent != null) {
            focusCurrent.clearFocus();
        }

        if (!data.isEnabled()) {
            viewDetailImpl(context, data);
            return;
        }
        Bundle args = EditPrescriptionsFragment.getArgs(data, false);
        Intent intent = SingleFragmentActivity.intentFor(context, "添加/编辑用药", args);
        Messenger messenger = new Messenger(new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case DiagnosisFragment.EDIT_PRESCRITPION: {
                        String string = msg.getData().getString(Constants.DATA);
                        Prescription parcelable = JacksonUtils.fromJson(string, Prescription.class);
                        parcelable.setItemId(data.getItemId());
                        parcelable.setPosition(data.getPosition());
                        if (adapter != null) {
                            adapter.update(parcelable);
                        } else {
                            if (data instanceof ModifiablePrescription) {
                                ModifiablePrescription temp = (ModifiablePrescription) data;
                                temp.from(parcelable);
                                temp.notifyChange();
                            }
                        }
                    }
                }
                return false;
            }
        }));
        intent.putExtra(Constants.HANDLER, messenger);
        context.startActivity(intent);
    }

    public static View.OnClickListener viewDetail(final Prescription data) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewDetailImpl(v.getContext(), data);
            }
        };
    }
//在这判断在倒入病患处方信息的时候 患者的处方是否符合标准，不符合则弹出提示
    public static boolean isValid(Prescription data, BaseListAdapter adapter) {
        if (!Settings.isDoctor()){
            return true;
        }
        if (adapter != null && adapter.getString(8).equals("1")) {
            return true;
        }

        //1s单位
        boolean isOneS = data.getSpecification().equals("-1");
        if (isOneS) {
            return true;
        }

        //克 毫克
        if (data.getDrug_unit().equals("克") || data.getDrug_unit().equals("毫克")) {
            return true;
        }

        return !Strings.isNullOrEmpty(data.getSpecification());
    }

    private static void viewDetailImpl(Context v, Prescription data) {
        Bundle bundle = EditPrescriptionsFragment.getArgs(data, true);
        Intent intent = SingleFragmentActivity.intentFor(v, "处方详情", bundle);
        v.startActivity(intent);
    }


    @JsonIgnore
    public static String getLabel(Prescription data) {
        StringBuilder builder = new StringBuilder();
        builder.append(data.getDrug_name());
        if (data.getScientific_name() != null && !data.getScientific_name().isEmpty()) {
            builder.append("(").append(data.getScientific_name()).append("),");
        } else {
            builder.append(",");
        }

        if (data.getTake_medicine_days() != null && !data.getTake_medicine_days().equals("")) {
            String takeMedicineDays = "用药天数:" + data.getTake_medicine_days() + "天";
            builder.append(takeMedicineDays);
            builder.append(",");
        }

        if (!"每天".equals(data.getFrequency())) {
            builder.append(data.getFrequency()).append(":");
        }

        ArrayList<String> numbers = numbers(data);
        if (numbers != null) {
            for (int i = 0; i < numbers.size(); i++) {
                String amount = numbers.get(i);
                if (null != amount && !amount.equals("")) {
                    try {
                        double amountDouble = Double.parseDouble(amount);
                        if (amountDouble > 0) {
                            builder.append(keys[i]).append(amount).append(data.getDrug_unit());
                            builder.append(",");
                        }
                    } catch (Exception e) {
                    }
                }
            }
        }

        if (data.getRemark() != null && !data.getRemark().isEmpty()) {
            builder.append(data.getRemark());
        } else {
            builder.deleteCharAt(builder.length() - 1);
        }
        return builder.toString();
    }

    public static String getInstruction(Prescription data) {
        StringBuilder builder = new StringBuilder();

        if (data.getTake_medicine_days() != null && !data.getTake_medicine_days().equals("")) {
            String takeMedicineDays = "用药天数:" + data.getTake_medicine_days() + "天";
            builder.append(takeMedicineDays);
            builder.append(",");
        }

        if (!"每天".equals(data.getFrequency())) {
            builder.append(data.getFrequency()).append(":");
        }

        ArrayList<String> numbers = numbers(data);
        if (numbers != null) {
            for (int i = 0; i < numbers.size(); i++) {
                String amount = numbers.get(i);
                if (null != amount && !amount.equals("")) {
                    try {
                        double amountDouble = Double.parseDouble(amount);
                        if (amountDouble > 0) {
                            builder.append(keys[i]).append(amount).append(data.getDrug_unit());
                            builder.append(",");
                        }
                    } catch (Exception e) {
                    }
                }
            }
        }
        builder.deleteCharAt(builder.length() - 1);
        return builder.toString();
    }

    @JsonIgnore
    public static String getName(Prescription data) {
        String s = "<font color='#898989'>药名: </font>" + data.getDrug_name();
        if (data.getScientific_name() != null && !data.getScientific_name().equals("")) {
            s += "(" + data.getScientific_name() + ")";
        } else {
            s += "";
        }
        return s;
    }

    @JsonIgnore
    public static String getTakeMedicineDays(Prescription data) {
        String take_medicine_days = data.getTake_medicine_days();
        if (Strings.isNullOrEmpty(take_medicine_days)) {
            take_medicine_days = "28";
        }
        return "<font color='#898989'>用药天数:   </font>" + take_medicine_days + "天";
    }

    @JsonIgnore
    public static String getIntervalWithLabel(Prescription data) {
        String builder = "<font color='#898989'>间隔:   </font>" + data.getFrequency();
        return builder;
    }

    @JsonIgnore
    public static String getAmountKV(Prescription data) {
        StringBuilder builder = new StringBuilder();
        builder.append(getAmountK());
        StringBuilder amountV = getAmountV(data);
        builder.append(amountV);
        return builder.toString();
    }

    @NonNull
    public static String getAmountK() {
        return "<font color='#898989'>数量:   </font>";
    }

    public static StringBuilder getAmountV(Prescription data) {
        StringBuilder builder = new StringBuilder();
        ArrayList<String> numbers = numbers(data);
       for (int i = 0; i < numbers.size(); i++) {
            String amonut = numbers.get(i);
            if(null!=amonut.trim()&&!amonut.trim().equals("0")&&!amonut.trim().equals("")){
                builder.append(keys[i]).append(amonut).append(data.getDrug_unit()).append(",");
            }
        }
        builder.deleteCharAt(builder.length() - 1);
        return builder;
    }


    @JsonIgnore
    public static String getRemarkLabel(Prescription data) {
        StringBuilder builder = new StringBuilder();
        if (data.getRemark() != null && !data.getRemark().equals("")) {
            builder.append("<font color='#898989'>备注:   </font>");
            builder.append(data.getRemark());
        }
        return builder.toString();
    }

    @JsonIgnore
    public static String getNumberLabel(Prescription data) {
        StringBuilder builder = new StringBuilder();
        ArrayList<String> numbers = numbers(data);
        for (int i = 0; i < numbers.size(); i++) {
            String amount = numbers.get(i);
            if (null != amount && !amount.equals("")) {
                builder.append(keys[i]).append(amount).append(data.getDrug_unit()).append(",");
            }
        }
        return builder.toString();
    }


    public static ArrayList<String> numbers(Prescription data) {
        ArrayList<String> strings = new ArrayList<>();
            strings.add(data.getMorning());
            strings.add(data.getNoon());
            strings.add(data.getNight());
            strings.add(data.getBefore_sleep());
        return strings;
    }

    public static String concatNumbers(Prescription data) {
        return data.getMorning() + data.getNoon() + data.getNight() + data.getBefore_sleep();
    }

    public static double totalNumberPerFrequency(Prescription data) {
        String morningS = nullOrEmptyToZero(trimZero(data.getMorning()));
        String noonS = nullOrEmptyToZero(trimZero(data.getNoon()));
        String nightS = nullOrEmptyToZero(trimZero(data.getNight()));
        String beforeSleepS = nullOrEmptyToZero(trimZero(data.getBefore_sleep()));
        double morningV;
        try {
            morningV = Strings.isNullOrEmpty(morningS) ? 0 : Double.valueOf(morningS);
        } catch (NumberFormatException e) {
            morningV = 0D;
        }
        double noonV;
        try {
            noonV = Strings.isNullOrEmpty(noonS) ? 0 : Double.valueOf(noonS);
        } catch (NumberFormatException e) {
            noonV = 0D;
        }
        double nightV;
        try {
            nightV = Strings.isNullOrEmpty(beforeSleepS) ? 0 : Double.valueOf(beforeSleepS);
        } catch (NumberFormatException e) {
            nightV = 0D;
        }
        double beforeSleepV;
        try {
            beforeSleepV = Strings.isNullOrEmpty(nightS) ? 0 : Double.valueOf(nightS);
        } catch (NumberFormatException e) {
            beforeSleepV = 0D;
        }

        return morningV + noonV + nightV + beforeSleepV;
    }

    public static Prescription newInstance() {
        ImmutablePrescription.Builder builder = emptyBuilder();
        return builder.build();
    }

    @NonNull
    public static ImmutablePrescription.Builder emptyBuilder() {
        ImmutablePrescription.Builder builder = ImmutablePrescription.builder();
        builder.drug_name("");
        builder.scientific_name("");
        builder.frequency("");
        builder.drug_unit("");
        builder.remark("");
        builder.morning("");
        builder.noon("");
        builder.night("");
        builder.before_sleep("");
        builder.take_medicine_days("");
        builder.specification("");
        builder.units("");
        return builder;
    }

    public static Prescription fromHashMap(Map<String, String> map) {
        if (map == null) {
            return null;
        }
        ImmutablePrescription.Builder builder = ImmutablePrescription.builder();
        builder.drug_name(Strings.nullToEmpty(map.get("drug_name")));
        builder.scientific_name(Strings.nullToEmpty(map.get("scientific_name")));
        builder.frequency(Strings.nullToEmpty(map.get("frequency")));
        builder.drug_unit(Strings.nullToEmpty(map.get("drug_unit")));
        builder.remark(Strings.nullToEmpty(map.get("remark")));

        builder.morning(getNumber(map.get("morning")));
        builder.noon(getNumber(map.get("noon")));
        builder.night(getNumber(map.get("night")));
        builder.before_sleep(getNumber(map.get("before_sleep")));

        builder.take_medicine_days(Strings.nullToEmpty(map.get("take_medicine_days")));
        builder.units(Strings.nullToEmpty(map.get("units")));
        builder.specification(Strings.nullToEmpty(map.get("specification")));
        return builder.build();
    }

    @NonNull
    private static String getNumber(String num) {
        return trimZero(Strings.nullToEmpty(num));
    }

    private static String nullOrEmptyToZero(String string) {
        return (string == null || "".equals(string)) ? "0" : string;
    }

    private static String trimZero(String string) {
        if (Strings.isNullOrEmpty(string)) {
            return "";
        }
        if (string.length() == 0) {
            return "";
        }
        if (string.charAt(0) == '0') {
            return trimZero(string.substring(1));
        } else {
            if (string.startsWith(".")) {
                return "0" + string;
            } else {
                return string;
            }
        }
    }

    public static String totalNum(Prescription data) {
        return String.format(Locale.CHINA, "%.2f", data.getTotal_num());
    }
}
