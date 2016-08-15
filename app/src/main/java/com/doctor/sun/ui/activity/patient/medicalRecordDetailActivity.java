package com.doctor.sun.ui.activity.patient;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.PActivityMedicalRecordBinding;
import com.doctor.sun.entity.MedicalRecord;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.module.ProfileModule;
import com.doctor.sun.ui.activity.BaseFragmentActivity2;
import com.doctor.sun.vo.ItemPickDate;

/**
 * Created by lucas on 1/12/16.
 */
public class MedicalRecordDetailActivity extends BaseFragmentActivity2 {
    private ProfileModule api = Api.of(ProfileModule.class);

    private boolean isEditMode;
    private PActivityMedicalRecordBinding binding;
    private ItemPickDate time;
    private MedicalRecord data;

    public boolean isEditMode() {
        return isEditMode;
    }

    public void setIsEditMode(boolean isEditMode) {
        this.isEditMode = isEditMode;
    }

    public static Intent makeIntent(Context context, MedicalRecord data) {
        Intent i = new Intent(context, MedicalRecordDetailActivity.class);
        i.putExtra(Constants.DATA, data);
        return i;
    }

    private MedicalRecord getData() {
        return getIntent().getParcelableExtra(Constants.DATA);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.p_activity_medical_record);
        data = getData();
        binding.setData(data);
        time = new ItemPickDate(0, "");
        time.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                data.setBirthday(time.getBirthMonth());
            }
        });
        time.setDate(data.getBirthday());
        binding.setTime(time);
    }

    @Override
    public int getMidTitle() {
        return R.string.title_record_detail;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save: {
                api.editMedicalRecord(data.toHashMap()).enqueue(new SimpleCallback<MedicalRecord>() {
                    @Override
                    protected void handleResponse(MedicalRecord response) {
                        Log.e(TAG, "handleResponse: " + data.toHashMap());
                        Log.e(TAG, "handleResponse: " + data);
                        Log.d(TAG, "handleResponse() called with: response = [" + response + "]");
                    }
                });
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
