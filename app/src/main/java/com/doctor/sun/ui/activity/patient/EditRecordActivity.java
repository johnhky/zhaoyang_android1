package com.doctor.sun.ui.activity.patient;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.PActivityEditRecordBinding;
import com.doctor.sun.entity.MedicalRecord;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.module.ProfileModule;
import com.doctor.sun.ui.activity.BaseFragmentActivity2;
import com.doctor.sun.vo.ItemPickDate;

/**
 * Created by lucas on 1/12/16.
 */
public class EditRecordActivity extends BaseFragmentActivity2 implements View.OnClickListener {
    private ProfileModule api = Api.of(ProfileModule.class);

    private PActivityEditRecordBinding binding;
    private ItemPickDate time;
    private MedicalRecord data;

    public static Intent makeIntent(Context context, MedicalRecord data) {
        Intent i = new Intent(context, EditRecordActivity.class);
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
        initData();
    }

    private void initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.p_activity_edit_record);
        binding.tvHistory.setOnClickListener(this);
    }

    private void initData() {
        data = getData();
        data.setEnabled(false);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save: {
                api.editMedicalRecord(data.toHashMap()).enqueue(new SimpleCallback<MedicalRecord>() {
                    @Override
                    protected void handleResponse(MedicalRecord response) {
                        if (response == null) {
                            Toast.makeText(EditRecordActivity.this, "成功申请修改病历,请耐心等待审核", Toast.LENGTH_SHORT).show();
                            data.setEnabled(false);
                            invalidateOptionsMenu();
                        }
                    }
                });
                return true;
            }
            case R.id.action_edit: {
                data.setEnabled(true);
                invalidateOptionsMenu();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (!data.isEnabled()) {
            getMenuInflater().inflate(R.menu.menu_edit, menu);
        } else {
            getMenuInflater().inflate(R.menu.menu_save, menu);
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_history: {
                Intent i = HistoryActivity.makeIntent(this);
                startActivity(i);
            }
        }
    }
}
