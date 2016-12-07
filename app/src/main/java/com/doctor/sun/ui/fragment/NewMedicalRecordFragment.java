package com.doctor.sun.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.doctor.auto.Factory;
import com.doctor.sun.R;
import com.doctor.sun.Settings;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.entity.MedicalRecord;
import com.doctor.sun.entity.Patient;
import com.doctor.sun.event.SelectMedicalRecordEvent;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.model.NewMedicalRecordModel;
import com.doctor.sun.ui.activity.SingleFragmentActivity;
import com.doctor.sun.ui.adapter.ViewHolder.SortedItem;
import com.doctor.sun.ui.widget.TwoChoiceDialog;
import com.doctor.sun.vo.ItemPickDate;
import com.doctor.sun.vo.ItemRadioGroup;
import com.doctor.sun.vo.ItemTextInput2;

import java.util.List;

import io.ganguo.library.core.event.EventHub;

/**
 * Created by kb on 16-9-18.
 */

@Factory(type = BaseFragment.class, id = "NewMedicalRecordFragment")
public class NewMedicalRecordFragment extends SortedListFragment {

    public static final String TAG = NewMedicalRecordFragment.class.getSimpleName();

    public static final int TYPE_SELF = 0;
    public static final int TYPE_OTHER = 1;

    private NewMedicalRecordModel model;

    public static Bundle getArgs(int recordType) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.FRAGMENT_NAME, TAG);
        bundle.putInt(Constants.RECORD_TYPE, recordType);

        return bundle;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        model = new NewMedicalRecordModel();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        disableRefresh();
        setHasOptionsMenu(true);
        List<SortedItem> sortedItems = model.parseItem(getRecordType());
        getAdapter().insertAll(sortedItems);

        Patient patientProfile = Settings.getPatientProfile();
        if (getRecordType() == TYPE_SELF && patientProfile != null && !patientProfile.getName().equals("")) {
            showDialog(patientProfile);
        }
    }

    private void showDialog(final Patient patient) {
        TwoChoiceDialog.showDialogUncancelable(getActivity(), getString(R.string.create_record_with_profile),
                "暂不生成", "一键生成", new TwoChoiceDialog.Options() {
                    @Override
                    public void onApplyClick(MaterialDialog dialog) {
                        ((ItemTextInput2) getAdapter().get("selfName")).setResult(patient.getName());
                        ((ItemPickDate) getAdapter().get("birthday")).setDateAndNotify(patient.getBirthday());
                        ((ItemRadioGroup) getAdapter().get("gender")).setSelectedItem(patient.getGender());
                        saveMedicalRecord();
                    }

                    @Override
                    public void onCancelClick(MaterialDialog dialog) {
                        getActivity().finish();
                    }
                });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_confirm, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_confirm: {
                saveMedicalRecord();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private int getRecordType() {
        return getArguments().getInt(Constants.RECORD_TYPE);
    }

    private void saveMedicalRecord() {
        model.saveMedicalRecord(getAdapter(), getRecordType(), new SimpleCallback<MedicalRecord>() {
            @Override
            protected void handleResponse(MedicalRecord response) {
                Toast.makeText(getContext(), "病历创建成功", Toast.LENGTH_SHORT).show();
                EventHub.post(new SelectMedicalRecordEvent(getArguments().getString(Constants.FROM), response));
                viewRecordDetail(response);
                getActivity().finish();
            }
        });
    }

    private void viewRecordDetail(MedicalRecord medicalRecord) {
        Bundle bundle = EditRecordFragment.getArgs(medicalRecord);
        Intent intent = SingleFragmentActivity.intentFor(getActivity(), "病历详情", bundle);
        getActivity().startActivity(intent);

    }

}
