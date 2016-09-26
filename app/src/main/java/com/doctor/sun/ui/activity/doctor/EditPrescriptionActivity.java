package com.doctor.sun.ui.activity.doctor;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.ActivityEditPrescriptionBinding;
import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.entity.DrugAutoComplete;
import com.doctor.sun.entity.Prescription;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.module.AutoComplete;
import com.doctor.sun.ui.activity.BaseFragmentActivity2;
import com.doctor.sun.ui.activity.SingleFragmentActivity;
import com.doctor.sun.ui.fragment.DiagnosisFragment;
import com.doctor.sun.ui.fragment.EditPrescriptionsFragment;
import com.doctor.sun.ui.widget.SingleChoiceDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.ganguo.library.util.log.Logger;
import io.ganguo.library.util.log.LoggerFactory;
import retrofit2.Call;

/**
 * Created by rick on 29/12/2015.
 */
public class EditPrescriptionActivity extends BaseFragmentActivity2 {
    public static final String MORNING_KEY = "早";
    public static final String AFTERNOON_KEY = "午";
    public static final String EVENING_KEY = "晚";
    public static final String NIGHT_KEY = "睡前";
    private Logger logger = LoggerFactory.getLogger(EditPrescriptionActivity.class);

    private ActivityEditPrescriptionBinding binding;
    private ArrayList<String> units;
    private ArrayList<String> intervals;

    public static Intent makeIntent(Context context, Prescription data) {
//        return SingleFragmentActivity.intentFor(context, "添加/编辑用药", EditPrescriptionsFragment.getArgs(data));
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
//        binding.setStopTakingMedicine(new ItemButton(-1, "停止用药") {
//            @Override
//            public void onClick(View view) {
//                finish();
//            }
//        });
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
        binding.afternoon.etAfternoon.setInputType(EditorInfo.TYPE_CLASS_NUMBER | EditorInfo.TYPE_NUMBER_FLAG_DECIMAL);

        binding.evening.setTitle("晚");
        binding.evening.etEvening.setInputType(EditorInfo.TYPE_CLASS_NUMBER | EditorInfo.TYPE_NUMBER_FLAG_DECIMAL);

        binding.night.setTitle("睡前");
        binding.night.etNight.setInputType(EditorInfo.TYPE_CLASS_NUMBER | EditorInfo.TYPE_NUMBER_FLAG_DECIMAL);


        initData();
        initAutoComplete();
    }

    public void initAutoComplete() {
        AutoComplete autoComplete = Api.of(AutoComplete.class);
        autoComplete.drugNames().enqueue(new SimpleCallback<List<DrugAutoComplete>>() {
            @Override
            protected void handleResponse(List<DrugAutoComplete> response) {
                ArrayAdapter<DrugAutoComplete> arrayAdapter = new ArrayAdapter<>(EditPrescriptionActivity.this, android.R.layout.simple_list_item_1, response);
                EditPrescriptionActivity.this.initAutoComplete(arrayAdapter);
            }

            @Override
            public void onFailure(Call<ApiDTO<List<DrugAutoComplete>>> call, Throwable t) {
                super.onFailure(call, t);
                ArrayAdapter<DrugAutoComplete> arrayAdapter = new ArrayAdapter<>(EditPrescriptionActivity.this, android.R.layout.simple_list_item_1, new ArrayList<DrugAutoComplete>());
                EditPrescriptionActivity.this.initAutoComplete(arrayAdapter);
            }
        });
    }

    private void initAutoComplete(ArrayAdapter<DrugAutoComplete> arrayAdapter) {
        final AutoCompleteTextView etInput = binding.medicineName.etInput;
        etInput.setAdapter(arrayAdapter);
        etInput.setThreshold(-1);
        etInput.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DrugAutoComplete item = (DrugAutoComplete) parent.getAdapter().getItem(position);
                etInput.setText(item.drugName);
                etInput.setSelection(item.drugName.length());
                binding.goodsName.etInput.setText(item.productName);
            }
        });
    }

    //    @Override
    public void onMenuClicked() {
//        super.onMenuClicked();
        Intent intent = new Intent();
        Prescription mPrescription = new Prescription();

        String mediaclName = binding.medicineName.etInput.getText().toString();
        mPrescription.setDrugName(mediaclName);
        mPrescription.setScientificName(binding.goodsName.etInput.getText().toString());
        mPrescription.setInterval(binding.interval.getValues().get(binding.interval.getSelectedItem()));

        List<HashMap<String, String>> numberList = new ArrayList<>();
        HashMap<String, String> morning = new HashMap<>(1);
        morning.put(MORNING_KEY, getValue(binding.morning.etInput));
        HashMap<String, String> afternoon = new HashMap<>(1);
        afternoon.put(AFTERNOON_KEY, getValue(binding.afternoon.etAfternoon));
        HashMap<String, String> evening = new HashMap<>(1);
        evening.put(EVENING_KEY, getValue(binding.evening.etEvening));
        HashMap<String, String> night = new HashMap<>(1);
        night.put(NIGHT_KEY, getValue(binding.night.etNight));
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

    private String getValue(EditText editText) {
        String string = editText.getText().toString();
        if (string.equals("")) {
            return "";
        } else if (string.equals(".")) {
            return string;
        } else if (string.startsWith(".")) {
            Double obj = Double.valueOf(string);
            if (obj <= 0) {
                return "";
            }
            return String.valueOf(obj);
        } else {
            if (string.contains(".")) {
                Double obj = Double.valueOf(string);
                if (obj <= 0) {
                    return "";
                }
                return String.valueOf(obj);
            } else {
                Integer obj = Integer.valueOf(string);
                if (obj <= 0) {
                    return "";
                }
                return String.valueOf(obj);
            }
        }
    }

    public boolean isValid(Prescription prescription) {

        if (prescription.getDrugName().equals("")) {
            Toast.makeText(EditPrescriptionActivity.this, "药名不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }

        boolean isErrorHandled = false;
        boolean isValid = false;
        List<HashMap<String, String>> numbers = prescription.getNumbers();
        String morning = numbers.get(0).get(MORNING_KEY);
        if (!morning.equals("")) {
            isValid = true;
        }
        String afternoon = numbers.get(1).get(AFTERNOON_KEY);
        if (!afternoon.equals("")) {
            isValid = true;
        }
        String evening = numbers.get(2).get(EVENING_KEY);
        if (!evening.equals("")) {
            isValid = true;
        }
        String night = numbers.get(3).get(NIGHT_KEY);
        if (!night.equals("")) {
            isValid = true;
        }
        if (!isValid) {
            isErrorHandled = true;
            Toast.makeText(EditPrescriptionActivity.this, "请填写药物服用份量", Toast.LENGTH_SHORT).show();
        }

        if (morning.equals(".")) {
            isValid = false;
        }
        if (afternoon.equals(".")) {
            isValid = false;
        }
        if (evening.equals(".")) {
            isValid = false;
        }
        if (night.equals(".")) {
            isValid = false;
        }

        if (!isValid) {
            if (!isErrorHandled) {
                Toast.makeText(EditPrescriptionActivity.this, "药物服用份量填写格式错误", Toast.LENGTH_SHORT).show();
            }
        }

        return isValid;
    }

    public void initData() {
        Prescription data = getData();
        if (data == null) return;
        binding.medicineName.etInput.setText(data.getDrugName());
        binding.goodsName.etInput.setText(data.getScientificName());
        binding.morning.etInput.setText(data.getNumbers().get(0).get("早"));
        binding.afternoon.etAfternoon.setText(data.getNumbers().get(1).get("午"));
        binding.evening.etEvening.setText(data.getNumbers().get(2).get("晚"));
        binding.night.etNight.setText(data.getNumbers().get(3).get("睡前"));
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
        return R.string.title_edit_prescription;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_confirm, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_confirm: {
                onMenuClicked();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
