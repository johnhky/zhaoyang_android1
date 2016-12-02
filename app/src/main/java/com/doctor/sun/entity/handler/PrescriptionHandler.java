package com.doctor.sun.entity.handler;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.support.annotation.NonNull;
import android.view.View;

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
import java.util.Map;

/**
 * Created by rick on 27/10/2016.
 */

public class PrescriptionHandler {
    private static final String[] keys = new String[]{"早", "午", "晚", "睡前"};

    public static void modify(Context context, final BaseListAdapter adapter, final Prescription data) {
        Bundle args = EditPrescriptionsFragment.getArgs(data, false);
        Intent intent = SingleFragmentActivity.intentFor(context, "添加/编辑处方", args);
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
//                Intent intent = ViewPrescriptionActivity.makeIntent(v.getContext(), data);
//                v.getContext().startActivity(intent);
                Bundle bundle = EditPrescriptionsFragment.getArgs(data, true);
                Intent intent = SingleFragmentActivity.intentFor(v.getContext(), "处方详情", bundle);
                v.getContext().startActivity(intent);
            }
        };
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
        return "<font color='#898989'>用药天数:   </font>" + data.getTake_medicine_days() + "天";
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
            String amount = numbers.get(i);
            if (null != amount && !amount.equals("") && !amount.equals("0")) {
                builder.append(keys[i]).append(amount).append(data.getDrug_unit()).append(",");
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
        builder.morning(Strings.nullToEmpty(map.get("morning")));
        builder.noon(Strings.nullToEmpty(map.get("noon")));
        builder.night(Strings.nullToEmpty(Strings.nullToEmpty(map.get("night"))));
        builder.before_sleep(Strings.nullToEmpty(map.get("before_sleep")));
        builder.take_medicine_days(Strings.nullToEmpty(map.get("take_medicine_days")));
        return builder.build();
    }

    public static Prescription fromLegacy(Prescription data) {
//        if (data == null) {
//            return null;
//        }
//
//        ImmutablePrescription.Builder builder = emptyBuilder();
//        builder.drug_name(data.getDrugName());
//        builder.scientific_name(data.getScientificName());
//        builder.frequency(data.getInterval());
//        builder.drug_unit(data.getUnit());
//        builder.remark(data.getRemark());
//        String morning = data.getNumbers().get(0).get("早");
//        builder.morning(Strings.nullToEmpty(morning));
//        String noon = data.getNumbers().get(1).get("午");
//        builder.noon(Strings.nullToEmpty(noon));
//        String evening = data.getNumbers().get(2).get("晚");
//        builder.night(Strings.nullToEmpty(evening));
//        String night = data.getNumbers().get(3).get("睡前");
//        builder.before_sleep(Strings.nullToEmpty(night));
//        return builder.build();
        return data;
    }
}
