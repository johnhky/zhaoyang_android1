package com.doctor.sun.entity.handler;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.alibaba.fastjson.JSONArray;
import com.doctor.sun.Settings;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.immutables.Prescription;
import com.doctor.sun.immutables.Titration;
import com.doctor.sun.ui.activity.SingleFragmentActivity;
import com.doctor.sun.ui.adapter.core.BaseListAdapter;
import com.doctor.sun.ui.fragment.DiagnosisFragment;
import com.doctor.sun.ui.fragment.EditPrescriptionsFragment;
import com.doctor.sun.util.JacksonUtils;
import com.doctor.sun.vm.TitrationTextInput;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by rick on 27/10/2016.
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
                        Gson gson = new GsonBuilder().create();
                        Prescription parcelable = JacksonUtils.fromJson(string, Prescription.class);
                        parcelable.setItemId(data.getItemId() + "");
                        parcelable.setPosition(data.getPosition());
                        if (adapter != null) {
                            adapter.update(parcelable);
                        } else {
                            if (data instanceof Prescription) {
                                Prescription temp = data;
                                temp.from(parcelable);
                                temp.notifyChange();
                            }
                        }
                        break;
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
        if (!Settings.isDoctor()) {
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
        if (!TextUtils.isEmpty(data.getDrug_unit())) {
            if (data.getDrug_unit().equals("克") || data.getDrug_unit().equals("毫克")) {
                return true;
            }
        }

        return !Strings.isNullOrEmpty(data.getSpecification());
    }

    private static void viewDetailImpl(Context v, Prescription data) {
        Bundle bundle = EditPrescriptionsFragment.getArgs(data, true);
        Intent intent = SingleFragmentActivity.intentFor(v, "处方详情", bundle);
        v.startActivity(intent);
    }

    @JsonIgnore
    public static String getDays(Prescription data) {
        return "X " + data.getTake_medicine_days() + "天";
    }

    @JsonIgnore
    public static String getLabel(Prescription data) {
        StringBuilder builder = new StringBuilder();
        builder.append(data.getDrug_name());
        if (data.getScientific_name() != null && !data.getScientific_name().isEmpty()) {
            builder.append("(").append(data.getScientific_name()).append(")");
        }
        builder.append("\n");
                /*When I wrote this code,only God and I understood what I was doing,
        mabey a month or a few months later,only God understood*/
        if (null != data.getTitration() && 0 != data.getTitration().size()) {
            List<Titration> titrations = data.getTitration();
            if (titrations.size() > 0) {
               /* for (int i = 0; i < titrations.size(); i++) {*/
                int size = titrations.size();
                if (size == 1) {
                    builder.append(titrations.get(0).getTake_medicine_days() + "-" + Integer.parseInt(data.getTake_medicine_days())).append("天,");
                    ArrayList<String> strings = titrationNumbers(titrations.get(0));
                    for (int j = 0; j < strings.size(); j++) {
                        if (!TextUtils.isEmpty(strings.get(j))) {
                            builder.append(keys[j]).append(strings.get(j)).append(data.getDrug_unit());
                        }
                    }
                } else if (size == 2) {
                    int date = Integer.parseInt(titrations.get(1).getTake_medicine_days());
                    builder.append(titrations.get(0).getTake_medicine_days() + "-" + (date - 1)).append("天,");
                    ArrayList<String> strings = titrationNumbers(titrations.get(0));
                    for (int j = 0; j < strings.size(); j++) {
                        if (!TextUtils.isEmpty(strings.get(j))) {
                            builder.append(keys[j]).append(strings.get(j)).append(data.getDrug_unit());
                        }
                    }
                    builder.append("\n");
                    builder.append(titrations.get(1).getTake_medicine_days() + "-" + data.getTake_medicine_days()).append("天,");
                    ArrayList<String> strings2 = titrationNumbers(titrations.get(1));
                    for (int j = 0; j < strings2.size(); j++) {
                        if (!TextUtils.isEmpty(strings2.get(j))) {
                            builder.append(keys[j]).append(strings2.get(j)).append(data.getDrug_unit());
                        }
                    }
                } else if (size == 3) {
                    int date = Integer.parseInt(titrations.get(1).getTake_medicine_days());
                    builder.append(titrations.get(0).getTake_medicine_days() + "-" + (date - 1)).append("天,");
                    ArrayList<String> strings = titrationNumbers(titrations.get(0));
                    for (int j = 0; j < strings.size(); j++) {
                        if (!TextUtils.isEmpty(strings.get(j))) {
                            builder.append(keys[j]).append(strings.get(j)).append(data.getDrug_unit());
                        }
                    }
                    builder.append("\n");

                    int date2 = Integer.parseInt(titrations.get(2).getTake_medicine_days());
                    builder.append(titrations.get(1).getTake_medicine_days() + "-" + (date2 - 1)).append("天,");
                    ArrayList<String> string2 = titrationNumbers(titrations.get(1));
                    for (int j = 0; j < string2.size(); j++) {
                        if (!TextUtils.isEmpty(string2.get(j))) {
                            builder.append(keys[j]).append(string2.get(j)).append(data.getDrug_unit());
                        }
                    }
                    builder.append("\n");

                    builder.append(titrations.get(2).getTake_medicine_days() + "-" + data.getTake_medicine_days()).append("天,");
                    ArrayList<String> strings3 = titrationNumbers(titrations.get(2));
                    for (int j = 0; j < strings3.size(); j++) {
                        if (!TextUtils.isEmpty(strings3.get(j))) {
                            builder.append(keys[j]).append(strings3.get(j)).append(data.getDrug_unit());
                        }
                    }
                } else if (size == 4) {
                    int date = Integer.parseInt(titrations.get(1).getTake_medicine_days());
                    builder.append(titrations.get(0).getTake_medicine_days() + "-" + (date - 1)).append("天,");
                    ArrayList<String> strings = titrationNumbers(titrations.get(0));
                    for (int j = 0; j < strings.size(); j++) {
                        if (!TextUtils.isEmpty(strings.get(j))) {
                            builder.append(keys[j]).append(strings.get(j)).append(data.getDrug_unit());
                        }
                    }
                    builder.append("\n");

                    int date2 = Integer.parseInt(titrations.get(2).getTake_medicine_days());
                    builder.append(titrations.get(1).getTake_medicine_days() + "-" + (date2 - 1)).append("天,");
                    ArrayList<String> string2 = titrationNumbers(titrations.get(1));
                    for (int j = 0; j < string2.size(); j++) {
                        if (!TextUtils.isEmpty(string2.get(j))) {
                            builder.append(keys[j]).append(string2.get(j)).append(data.getDrug_unit());
                        }
                    }
                    builder.append("\n");

                    int date3 = Integer.parseInt(titrations.get(3).getTake_medicine_days());
                    builder.append(titrations.get(2).getTake_medicine_days() + "-" + (date3 - 1)).append("天,");
                    ArrayList<String> string3 = titrationNumbers(titrations.get(2));
                    for (int j = 0; j < string3.size(); j++) {
                        if (!TextUtils.isEmpty(string3.get(j))) {
                            builder.append(keys[j]).append(string3.get(j)).append(data.getDrug_unit());
                        }
                    }
                    builder.append("\n");

                    builder.append(titrations.get(3).getTake_medicine_days() + "-" + data.getTake_medicine_days()).append("天,");
                    ArrayList<String> strings4 = titrationNumbers(titrations.get(3));
                    for (int j = 0; j < strings4.size(); j++) {
                        if (!TextUtils.isEmpty(strings4.get(j))) {
                            builder.append(keys[j]).append(strings4.get(j)).append(data.getDrug_unit());
                        }
                    }
                } else if (size == 5) {
                    int date = Integer.parseInt(titrations.get(1).getTake_medicine_days());
                    builder.append(titrations.get(0).getTake_medicine_days() + "-" + (date - 1)).append("天,");
                    ArrayList<String> strings = titrationNumbers(titrations.get(0));
                    for (int j = 0; j < strings.size(); j++) {
                        if (!TextUtils.isEmpty(strings.get(j))) {
                            builder.append(keys[j]).append(strings.get(j)).append(data.getDrug_unit());
                        }
                    }
                    builder.append("\n");

                    int date2 = Integer.parseInt(titrations.get(2).getTake_medicine_days());
                    builder.append(titrations.get(1).getTake_medicine_days() + "-" + (date2 - 1)).append("天,");
                    ArrayList<String> string2 = titrationNumbers(titrations.get(1));
                    for (int j = 0; j < string2.size(); j++) {
                        if (!TextUtils.isEmpty(string2.get(j))) {
                            builder.append(keys[j]).append(string2.get(j)).append(data.getDrug_unit());
                        }
                    }
                    builder.append("\n");

                    int date3 = Integer.parseInt(titrations.get(3).getTake_medicine_days());
                    builder.append(titrations.get(2).getTake_medicine_days() + "-" + (date3 - 1)).append("天,");
                    ArrayList<String> string3 = titrationNumbers(titrations.get(2));
                    for (int j = 0; j < string3.size(); j++) {
                        if (!TextUtils.isEmpty(string3.get(j))) {
                            builder.append(keys[j]).append(string3.get(j)).append(data.getDrug_unit());
                        }
                    }
                    builder.append("\n");

                    int date4 = Integer.parseInt(titrations.get(4).getTake_medicine_days());
                    builder.append(titrations.get(3).getTake_medicine_days() + "-" + (date4 - 1)).append("天,");
                    ArrayList<String> string4 = titrationNumbers(titrations.get(3));
                    for (int j = 0; j < string4.size(); j++) {
                        if (!TextUtils.isEmpty(string4.get(j))) {
                            builder.append(keys[j]).append(string4.get(j)).append(data.getDrug_unit());
                        }
                    }
                    builder.append("\n");

                    builder.append(titrations.get(4).getTake_medicine_days() + "-" + data.getTake_medicine_days()).append("天,");
                    ArrayList<String> strings5 = titrationNumbers(titrations.get(4));
                    for (int j = 0; j < strings5.size(); j++) {
                        if (!TextUtils.isEmpty(strings5.get(j))) {
                            builder.append(keys[j]).append(strings5.get(j)).append(data.getDrug_unit());
                        }
                    }
                } else if (data.getTitration().size() == 6) {
                    int date = Integer.parseInt(titrations.get(1).getTake_medicine_days());
                    builder.append(titrations.get(0).getTake_medicine_days() + "-" + (date - 1)).append("天,");
                    ArrayList<String> strings = titrationNumbers(titrations.get(0));
                    for (int j = 0; j < strings.size(); j++) {
                        if (!TextUtils.isEmpty(strings.get(j))) {
                            builder.append(keys[j]).append(strings.get(j)).append(data.getDrug_unit());
                        }
                    }
                    builder.append("\n");

                    int date2 = Integer.parseInt(titrations.get(2).getTake_medicine_days());
                    builder.append(titrations.get(1).getTake_medicine_days() + "-" + (date2 - 1)).append("天,");
                    ArrayList<String> string2 = titrationNumbers(titrations.get(1));
                    for (int j = 0; j < string2.size(); j++) {
                        if (!TextUtils.isEmpty(string2.get(j))) {
                            builder.append(keys[j]).append(string2.get(j)).append(data.getDrug_unit());
                        }
                    }
                    builder.append("\n");

                    int date3 = Integer.parseInt(titrations.get(3).getTake_medicine_days());
                    builder.append(titrations.get(2).getTake_medicine_days() + "-" + (date3 - 1)).append("天,");
                    ArrayList<String> string3 = titrationNumbers(titrations.get(2));
                    for (int j = 0; j < string3.size(); j++) {
                        if (!TextUtils.isEmpty(string3.get(j))) {
                            builder.append(keys[j]).append(string3.get(j)).append(data.getDrug_unit());
                        }
                    }
                    builder.append("\n");

                    int date4 = Integer.parseInt(titrations.get(4).getTake_medicine_days());
                    builder.append(titrations.get(3).getTake_medicine_days() + "-" + (date4 - 1)).append("天,");
                    ArrayList<String> string4 = titrationNumbers(titrations.get(3));
                    for (int j = 0; j < string4.size(); j++) {
                        if (!TextUtils.isEmpty(string4.get(j))) {
                            builder.append(keys[j]).append(string4.get(j)).append(data.getDrug_unit());
                        }
                    }
                    builder.append("\n");

                    int date5 = Integer.parseInt(titrations.get(5).getTake_medicine_days());
                    builder.append(titrations.get(4).getTake_medicine_days() + "-" + (date5 - 1)).append("天,");
                    ArrayList<String> string5 = titrationNumbers(titrations.get(4));
                    for (int j = 0; j < string5.size(); j++) {
                        if (!TextUtils.isEmpty(string5.get(j))) {
                            builder.append(keys[j]).append(string5.get(j)).append(data.getDrug_unit());
                        }
                    }
                    builder.append("\n");

                    builder.append(titrations.get(5).getTake_medicine_days() + "-" + data.getTake_medicine_days()).append("天,");
                    ArrayList<String> strings5 = titrationNumbers(titrations.get(5));
                    for (int j = 0; j < strings5.size(); j++) {
                        if (!TextUtils.isEmpty(strings5.get(j))) {
                            builder.append(keys[j]).append(strings5.get(j)).append(data.getDrug_unit());
                        }
                    }
                }

              /*  }*/
            }

        } else {
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
                builder.deleteCharAt(builder.length() - 1);
            }
        }
        if (!TextUtils.isEmpty(data.getRemark())) {
            builder.append("\n" + data.getRemark());
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
        String s = "药名: " + data.getDrug_name();
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
        return "用药天数:   " + take_medicine_days + "天";
    }

    @JsonIgnore
    public static boolean visibleFrequency(Prescription data) {
        if (data.getFrequency().equals("每天")) {
            return false;
        } else {
            return true;
        }
    }

    @JsonIgnore
    public static String getIntervalWithLabel(Prescription data) {
        String builder;
        if (data.getFrequency().equals("每天")) {
            builder = null;
        } else {
            builder = "间隔:   " + data.getFrequency();
        }
        return builder;
    }

    @JsonIgnore
    public static String getAmountKV(Prescription data) {
        String amountV = getAmountK() + getAmountV(data);
        return amountV;
    }

    @NonNull
    public static String getAmountK() {
        return "<font color='#898989'>数量:   </font>";
    }

    @JsonIgnore
    public static String getAmountV(Prescription data) {
        StringBuilder builder = new StringBuilder();
        if (null != data.getTitration() && 0 != data.getTitration().size()) {
            builder.append("\n");
            List<Titration> titrations = data.getTitration();
            int size = titrations.size();
            if (size == 1) {
                builder.append(titrations.get(0).getTake_medicine_days() + "-" + Integer.parseInt(data.getTake_medicine_days())).append("天,");
                ArrayList<String> strings = titrationNumbers(titrations.get(0));
                for (int j = 0; j < strings.size(); j++) {
                    if (!TextUtils.isEmpty(strings.get(j))) {
                        builder.append(keys[j]).append(strings.get(j)).append(data.getDrug_unit());
                    }
                }
            } else if (size == 2) {
                int date = Integer.parseInt(titrations.get(1).getTake_medicine_days());
                builder.append(titrations.get(0).getTake_medicine_days() + "-" + (date - 1)).append("天,");
                ArrayList<String> strings = titrationNumbers(titrations.get(0));
                for (int j = 0; j < strings.size(); j++) {
                    if (!TextUtils.isEmpty(strings.get(j))) {
                        builder.append(keys[j]).append(strings.get(j)).append(data.getDrug_unit());
                    }
                }
                builder.append("\n");
                builder.append(titrations.get(1).getTake_medicine_days() + "-" + data.getTake_medicine_days()).append("天,");
                ArrayList<String> strings2 = titrationNumbers(titrations.get(1));
                for (int j = 0; j < strings2.size(); j++) {
                    if (!TextUtils.isEmpty(strings2.get(j))) {
                        builder.append(keys[j]).append(strings2.get(j)).append(data.getDrug_unit());
                    }
                }
            } else if (size == 3) {
                int date = Integer.parseInt(titrations.get(1).getTake_medicine_days());
                builder.append(titrations.get(0).getTake_medicine_days() + "-" + (date - 1)).append("天,");
                ArrayList<String> strings = titrationNumbers(titrations.get(0));
                for (int j = 0; j < strings.size(); j++) {
                    if (!TextUtils.isEmpty(strings.get(j))) {
                        builder.append(keys[j]).append(strings.get(j)).append(data.getDrug_unit());
                    }
                }
                builder.append("\n");
                int date2 = Integer.parseInt(titrations.get(2).getTake_medicine_days());
                builder.append(titrations.get(1).getTake_medicine_days() + "-" + (date2 - 1)).append("天,");
                ArrayList<String> string2 = titrationNumbers(titrations.get(1));
                for (int j = 0; j < string2.size(); j++) {
                    if (!TextUtils.isEmpty(string2.get(j))) {
                        builder.append(keys[j]).append(string2.get(j)).append(data.getDrug_unit());
                    }

                }
                builder.append("\n");
                builder.append(titrations.get(2).getTake_medicine_days() + "-" + data.getTake_medicine_days()).append("天,");
                ArrayList<String> strings3 = titrationNumbers(titrations.get(2));
                for (int j = 0; j < strings3.size(); j++) {
                    if (!TextUtils.isEmpty(strings3.get(j))) {
                        builder.append(keys[j]).append(strings3.get(j)).append(data.getDrug_unit());
                    }

                }
            } else if (size == 4) {
                int date = Integer.parseInt(titrations.get(1).getTake_medicine_days());
                builder.append(titrations.get(0).getTake_medicine_days() + "-" + (date - 1)).append("天,");
                ArrayList<String> strings = titrationNumbers(titrations.get(0));
                for (int j = 0; j < strings.size(); j++) {
                    if (!TextUtils.isEmpty(strings.get(j))) {
                        builder.append(keys[j]).append(strings.get(j)).append(data.getDrug_unit());
                    }

                }
                builder.append("\n");
                int date2 = Integer.parseInt(titrations.get(2).getTake_medicine_days());
                builder.append(titrations.get(1).getTake_medicine_days() + "-" + (date2 - 1)).append("天,");
                ArrayList<String> string2 = titrationNumbers(titrations.get(1));
                for (int j = 0; j < string2.size(); j++) {
                    if (!TextUtils.isEmpty(string2.get(j))) {
                        builder.append(keys[j]).append(string2.get(j)).append(data.getDrug_unit());
                    }

                }
                builder.append("\n");
                int date3 = Integer.parseInt(titrations.get(3).getTake_medicine_days());
                builder.append(titrations.get(2).getTake_medicine_days() + "-" + (date3 - 1)).append("天,");
                ArrayList<String> string3 = titrationNumbers(titrations.get(2));
                for (int j = 0; j < string3.size(); j++) {
                    if (!TextUtils.isEmpty(string3.get(j))) {
                        builder.append(keys[j]).append(string3.get(j)).append(data.getDrug_unit());
                    }

                }
                builder.append("\n");
                builder.append(titrations.get(3).getTake_medicine_days() + "-" + data.getTake_medicine_days()).append("天,");
                ArrayList<String> strings4 = titrationNumbers(titrations.get(3));
                for (int j = 0; j < strings4.size(); j++) {
                    if (!TextUtils.isEmpty(strings4.get(j))) {
                        builder.append(keys[j]).append(strings4.get(j)).append(data.getDrug_unit());
                    }

                }
            } else if (size == 5) {
                int date = Integer.parseInt(titrations.get(1).getTake_medicine_days());
                builder.append(titrations.get(0).getTake_medicine_days() + "-" + (date - 1)).append("天,");
                ArrayList<String> strings = titrationNumbers(titrations.get(0));
                for (int j = 0; j < strings.size(); j++) {
                    if (!TextUtils.isEmpty(strings.get(j))) {
                        builder.append(keys[j]).append(strings.get(j)).append(data.getDrug_unit());
                    }

                }
                builder.append("\n");
                int date2 = Integer.parseInt(titrations.get(2).getTake_medicine_days());
                builder.append(titrations.get(1).getTake_medicine_days() + "-" + (date2 - 1)).append("天,");
                ArrayList<String> string2 = titrationNumbers(titrations.get(1));
                for (int j = 0; j < string2.size(); j++) {
                    if (!TextUtils.isEmpty(string2.get(j))) {
                        builder.append(keys[j]).append(string2.get(j)).append(data.getDrug_unit());
                    }

                }
                builder.append("\n");
                int date3 = Integer.parseInt(titrations.get(3).getTake_medicine_days());
                builder.append(titrations.get(2).getTake_medicine_days() + "-" + (date3 - 1)).append("天,");
                ArrayList<String> string3 = titrationNumbers(titrations.get(2));
                for (int j = 0; j < string3.size(); j++) {
                    if (!TextUtils.isEmpty(string3.get(j))) {
                        builder.append(keys[j]).append(string3.get(j)).append(data.getDrug_unit());
                    }

                }
                builder.append("\n");
                int date4 = Integer.parseInt(titrations.get(4).getTake_medicine_days());
                builder.append(titrations.get(3).getTake_medicine_days() + "-" + (date4 - 1)).append("天,");
                ArrayList<String> string4 = titrationNumbers(titrations.get(3));
                for (int j = 0; j < string4.size(); j++) {
                    if (!TextUtils.isEmpty(string4.get(j))) {
                        builder.append(keys[j]).append(string4.get(j)).append(data.getDrug_unit());
                    }

                }
                builder.append("\n");
                builder.append(titrations.get(4).getTake_medicine_days() + "-" + data.getTake_medicine_days()).append("天,");
                ArrayList<String> strings5 = titrationNumbers(titrations.get(4));
                for (int j = 0; j < strings5.size(); j++) {
                    if (!TextUtils.isEmpty(strings5.get(j))) {
                        builder.append(keys[j]).append(strings5.get(j)).append(data.getDrug_unit());
                    }

                }
            } else if (data.getTitration().size() == 6) {
                int date = Integer.parseInt(titrations.get(1).getTake_medicine_days());
                builder.append(titrations.get(0).getTake_medicine_days() + "-" + (date - 1)).append("天,");
                ArrayList<String> strings = titrationNumbers(titrations.get(0));
                for (int j = 0; j < strings.size(); j++) {
                    if (!TextUtils.isEmpty(strings.get(j))) {
                        builder.append(keys[j]).append(strings.get(j)).append(data.getDrug_unit());
                    }

                }
                builder.append("\n");
                int date2 = Integer.parseInt(titrations.get(2).getTake_medicine_days());
                builder.append(titrations.get(1).getTake_medicine_days() + "-" + (date2 - 1)).append("天,");
                ArrayList<String> string2 = titrationNumbers(titrations.get(1));
                for (int j = 0; j < string2.size(); j++) {
                    if (!TextUtils.isEmpty(string2.get(j))) {
                        builder.append(keys[j]).append(string2.get(j)).append(data.getDrug_unit());
                    }

                }
                builder.append("\n");
                int date3 = Integer.parseInt(titrations.get(3).getTake_medicine_days());
                builder.append(titrations.get(2).getTake_medicine_days() + "-" + (date3 - 1)).append("天,");
                ArrayList<String> string3 = titrationNumbers(titrations.get(2));
                for (int j = 0; j < string3.size(); j++) {
                    if (!TextUtils.isEmpty(string3.get(j))) {
                        builder.append(keys[j]).append(string3.get(j)).append(data.getDrug_unit());
                    }

                }
                builder.append("\n");
                int date4 = Integer.parseInt(titrations.get(4).getTake_medicine_days());
                builder.append(titrations.get(3).getTake_medicine_days() + "-" + (date4 - 1)).append("天,");
                ArrayList<String> string4 = titrationNumbers(titrations.get(3));
                for (int j = 0; j < string4.size(); j++) {
                    if (!TextUtils.isEmpty(string4.get(j))) {
                        builder.append(keys[j]).append(string4.get(j)).append(data.getDrug_unit());
                    }

                }
                builder.append("\n");
                int date5 = Integer.parseInt(titrations.get(5).getTake_medicine_days());
                builder.append(titrations.get(4).getTake_medicine_days() + "-" + (date5 - 1)).append("天,");
                ArrayList<String> string5 = titrationNumbers(titrations.get(4));
                for (int j = 0; j < string5.size(); j++) {
                    if (!TextUtils.isEmpty(string5.get(j))) {
                        builder.append(keys[j]).append(string5.get(j)).append(data.getDrug_unit());
                    }

                }
                builder.append("\n");
                builder.append(titrations.get(5).getTake_medicine_days() + "-" + data.getTake_medicine_days()).append("天,");
                ArrayList<String> strings5 = titrationNumbers(titrations.get(5));
                for (int j = 0; j < strings5.size(); j++) {
                    if (!TextUtils.isEmpty(strings5.get(j))) {
                        builder.append(keys[j]).append(strings5.get(j)).append(data.getDrug_unit());
                    }

                }

            }
        } else {
            ArrayList<String> numbers = numbers(data);
            for (int i = 0; i < numbers.size(); i++) {
                String amonut = numbers.get(i);
                if (!TextUtils.isEmpty(amonut)) {
                    builder.append(keys[i]).append(amonut).append(data.getDrug_unit()).append(",");
                }
            }
            if (builder.length() > 0) {
                builder.deleteCharAt(builder.length() - 1);
            }
        }


        return builder.toString();
    }

    @JsonIgnore
    public static boolean visibleRemork(Prescription data) {
        if (TextUtils.isEmpty(data.getRemark())) {
            return false;
        } else {
            return true;
        }
    }


    @JsonIgnore
    public static String getRemarkLabel(Prescription data) {
        StringBuilder builder = new StringBuilder();
        if (data.getRemark() != null && !data.getRemark().equals("")) {
            builder.append("备注:   ");
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
            if (!TextUtils.isEmpty(amount)) {
                builder.append(keys[i]).append(amount).append(data.getDrug_unit()).append(",");
            }
        }
        return builder.toString();
    }

    public static ArrayList<String> titrationNumbers(Titration data) {
        ArrayList<String> strings = new ArrayList<>();
        strings.add(data.getMorning());
        strings.add(data.getNoon());
        strings.add(data.getNight());
        strings.add(data.getBefore_sleep());
        return strings;
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

    public static double totalTitrationNumberPerFrequency(TitrationTextInput data) {
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


    public static double totalTiNumberPerFrequency(Titration data) {
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
        Prescription builder = emptyBuilder();
        return builder;
    }

    @NonNull
    public static Prescription emptyBuilder() {
        Prescription builder = new Prescription();
        builder.setDrug_name("");
        builder.setScientific_name("");
        builder.setFrequency("");
        builder.setDrug_unit("");
        builder.setRemark("");
        builder.setMorning("");
        builder.setNoon("");
        builder.setNight("");
        builder.setBefore_sleep("");
        builder.setTake_medicine_days("");
        builder.setUnits("");
        builder.setSpecification("");
        builder.setTitration(new ArrayList<Titration>());
        return builder;
    }

    public static Prescription fromHashMap(Map<String, String> map) {
        if (map == null) {
            return null;
        }
        Prescription builder = new Prescription();
        builder.setDrug_name(Strings.nullToEmpty(map.get("drug_name")));
        builder.setScientific_name(Strings.nullToEmpty(map.get("scientific_name")));
        builder.setFrequency(Strings.nullToEmpty(map.get("frequency")));
        builder.setDrug_unit(Strings.nullToEmpty(map.get("drug_unit")));
        builder.setRemark(Strings.nullToEmpty(map.get("remark")));

        builder.setMorning(getNumber(map.get("morning")));
        builder.setNoon(getNumber(map.get("noon")));
        builder.setNight(getNumber(map.get("night")));
        builder.setBefore_sleep(getNumber(map.get("before_sleep")));

        builder.setTake_medicine_days(Strings.nullToEmpty(map.get("take_medicine_days")));
        builder.setUnits(Strings.nullToEmpty(map.get("units")));
        builder.setSpecification(Strings.nullToEmpty(map.get("specification")));
        builder.setTitration(getTitrations(map.get("titration")));
        return builder;
    }

    public static ArrayList<Titration> getTitrations(String str) {
        Gson gson = new GsonBuilder().create();
      /*  List<Titration> titrations = JSONArray.parseArray(str,Titration.class);*/
        ArrayList<Titration> titrations = gson.fromJson(str, new TypeToken<ArrayList<Titration>>() {
        }.getType());
        return titrations;
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

    public static boolean isVisibles(Prescription data) {
        if (Settings.isDoctor()) {
            if (data.getTitration().size() > 0) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    public static String totalNum(Prescription data) {
        return data.getTotal_num();
    }
}
