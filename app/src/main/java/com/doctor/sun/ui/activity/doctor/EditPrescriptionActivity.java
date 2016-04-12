package com.doctor.sun.ui.activity.doctor;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.ActivityEditPrescriptionBinding;
import com.doctor.sun.entity.ItemButton;
import com.doctor.sun.entity.Prescription;
import com.doctor.sun.ui.activity.BaseActivity2;
import com.doctor.sun.ui.fragment.DiagnosisFragment;
import com.doctor.sun.ui.model.HeaderViewModel;
import com.doctor.sun.ui.widget.SingleChoiceDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.ganguo.library.util.log.Logger;
import io.ganguo.library.util.log.LoggerFactory;

/**
 * Created by rick on 29/12/2015.
 */
public class EditPrescriptionActivity extends BaseActivity2 {
    public static final String MORNING_KEY = "早";
    public static final String AFTERNOON_KEY = "午";
    public static final String EVENING_KEY = "晚";
    public static final String NIGHT_KEY = "睡前";
    private Logger logger = LoggerFactory.getLogger(EditPrescriptionActivity.class);

    private ActivityEditPrescriptionBinding binding;
    private ArrayList<String> units;
    private ArrayList<String> intervals;

    public static Intent makeIntent(Context context, Prescription data) {
        Intent i = new Intent(context, EditPrescriptionActivity.class);
        i.putExtra(Constants.DATA, data);
        return i;
    }

    private Prescription getData() {
        return getIntent().getParcelableExtra(Constants.DATA);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_prescription);
        HeaderViewModel header = new HeaderViewModel(this);
        header.setLeftIcon(R.drawable.ic_back).setMidTitle("添加/编辑用药").setRightTitle("确定");
        binding.setHeader(header);
        binding.setStopTakingMedicine(new ItemButton(-1, "停止用药") {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
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
        binding.unit.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SingleChoiceDialog.show(EditPrescriptionActivity.this, binding.unit);
            }
        });

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
        binding.interval.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SingleChoiceDialog.show(EditPrescriptionActivity.this, binding.interval);
            }
        });

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


    @Override
    public void onMenuClicked() {
        super.onMenuClicked();
        Intent intent = new Intent();
        Prescription mPrescription = new Prescription();

        String mediaclName = binding.medicineName.etInput.getText().toString();
        mPrescription.setMediaclName(mediaclName);
        mPrescription.setProductName(binding.goodsName.etInput.getText().toString());
        mPrescription.setInterval(binding.interval.getValues().get(binding.interval.getSelectedItem()));

        List<HashMap<String, String>> numberList = new ArrayList<>();
        HashMap<String, String> morning = new HashMap<>(1);
        morning.put(MORNING_KEY, binding.morning.etInput.getText().toString());
        HashMap<String, String> afternoon = new HashMap<>(1);
        afternoon.put(AFTERNOON_KEY, binding.afternoon.etInput.getText().toString());
        HashMap<String, String> evening = new HashMap<>(1);
        evening.put(EVENING_KEY, binding.evening.etInput.getText().toString());
        HashMap<String, String> night = new HashMap<>(1);
        night.put(NIGHT_KEY, binding.night.etInput.getText().toString());
        numberList.add(morning);
        numberList.add(afternoon);
        numberList.add(evening);
        numberList.add(night);
        mPrescription.setNumbers(numberList);

        mPrescription.setUnit(binding.unit.getValues().get(binding.unit.getSelectedItem()));
        mPrescription.setRemark(binding.remark.etOthers.getText().toString());

        if (!isValid(mPrescription)) {
            return;
        }

        Messenger messenger = getIntent().getParcelableExtra(Constants.HANDLER);
        if (messenger != null) {
            try {
                Message message = new Message();
                Bundle bundle = new Bundle();
                bundle.putParcelable(Constants.DATA, mPrescription);
                message.setData(bundle);
                message.what = DiagnosisFragment.EDIT_PRESCRITPION;
                messenger.send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        intent.putExtra(Constants.DATA, mPrescription);
        setResult(RESULT_OK, intent);
        finish();
    }

    public boolean isValid(Prescription prescription) {
        if (prescription.getMediaclName().equals("")) {
            Toast.makeText(EditPrescriptionActivity.this, "药名不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }

        List<HashMap<String, String>> numbers = prescription.getNumbers();
        if (numbers.get(0).get(MORNING_KEY).equals("")) {
            Toast.makeText(EditPrescriptionActivity.this, "请填写早上应服份量", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (numbers.get(1).get(AFTERNOON_KEY) .equals("")) {
            Toast.makeText(EditPrescriptionActivity.this, "请填写中午应服份量", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (numbers.get(2).get(EVENING_KEY) .equals("")) {
            Toast.makeText(EditPrescriptionActivity.this, "请填写晚上应服份量", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (numbers.get(3).get(NIGHT_KEY) .equals("")) {
            Toast.makeText(EditPrescriptionActivity.this, "请填写睡前应服份量", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (prescription.getRemark().equals("")) {
            Toast.makeText(EditPrescriptionActivity.this, "请填写备注信息", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    public void initData() {
        Prescription data = getData();
        if (data == null) return;
        binding.medicineName.etInput.setText(data.getMediaclName());
        binding.goodsName.etInput.setText(data.getProductName());
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
}
