package com.doctor.sun.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.inputmethod.EditorInfo;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.ActivityViewPrescriptionBinding;
import com.doctor.sun.entity.Prescription;
import com.doctor.sun.ui.model.HeaderViewModel;

import java.util.ArrayList;

/**
 * Created by rick on 6/5/2016.
 */
public class ViewPrescriptionActivity extends BaseFragmentActivity2 {

    private ActivityViewPrescriptionBinding binding;
    private ArrayList<String> units;
    private ArrayList<String> intervals;

    public static Intent makeIntent(Context context, Prescription data) {
        Intent i = new Intent(context, ViewPrescriptionActivity.class);
        i.putExtra(Constants.DATA, data);
        return i;
    }


    private Prescription getData() {
        return getIntent().getParcelableExtra(Constants.DATA);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_view_prescription);
        binding.medicineName.setTitle("药名/成分名");
        binding.goodsName.setTitle("商品名");
        binding.goodsName.setHint("(选填)");

        binding.unit.setTitle("单位");
        units = new ArrayList<>();
        units.add("克");
        units.add("毫克");
        units.add("毫升");
        units.add("粒");
        binding.unit.setValues(units);
        binding.unit.setSelectedItem(0);
//        binding.unit.getRoot().setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                SingleChoiceDialog.show(ViewPrescriptionActivity.this, binding.unit);
//            }
//        });

        binding.interval.setTitle("间隔");
        intervals = new ArrayList<>();
        intervals.add("每天");
        intervals.add("每周");
        intervals.add("每月");
        intervals.add("隔天");
        intervals.add("隔两天");
        intervals.add("隔三天");
        binding.interval.setValues(intervals);
        binding.interval.setSelectedItem(0);
//        binding.interval.getRoot().setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                SingleChoiceDialog.show(EditPrescriptionActivity.this, binding.interval);
//            }
//        });

        binding.morning.setTitle("早");
        binding.morning.etInput.setInputType(EditorInfo.TYPE_CLASS_NUMBER | EditorInfo.TYPE_NUMBER_FLAG_DECIMAL);

        binding.afternoon.setTitle("午");
        binding.afternoon.etInput.setInputType(EditorInfo.TYPE_CLASS_NUMBER | EditorInfo.TYPE_NUMBER_FLAG_DECIMAL);

        binding.evening.setTitle("晚");
        binding.evening.etInput.setInputType(EditorInfo.TYPE_CLASS_NUMBER | EditorInfo.TYPE_NUMBER_FLAG_DECIMAL);

        binding.night.setTitle("睡前");
        binding.night.etInput.setInputType(EditorInfo.TYPE_CLASS_NUMBER | EditorInfo.TYPE_NUMBER_FLAG_DECIMAL);


        initData();
    }

    public void initData() {
        Prescription data = getData();
        if (data == null) return;
        binding.medicineName.etInput.setText(data.getDrugName());
        binding.goodsName.etInput.setText(data.getScientificName());
        binding.morning.etInput.setText(data.getNumbers().get(0).get("早"));
        binding.afternoon.etInput.setText(data.getNumbers().get(1).get("午"));
        binding.evening.etInput.setText(data.getNumbers().get(2).get("晚"));
        binding.night.etInput.setText(data.getNumbers().get(3).get("睡前"));
        for (int i = 0; i < units.size(); i++) {
            if (units.get(i).equals(data.getUnit())) {
                binding.unit.setSelectedItem(i);
                binding.unit.etInput.setText(data.getUnit());
            }
        }
        for (int i = 0; i < intervals.size(); i++) {
            if (intervals.get(i).equals(data.getInterval())) {
                binding.interval.setSelectedItem(i);
                binding.interval.etInput.setText(data.getInterval());
            }
        }
        binding.remark.setInput(data.getRemark());
    }

    @Override
    public int getMidTitle() {
        return R.string.title_add_prescription;
    }
}
