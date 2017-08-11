package com.doctor.sun.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
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
import com.doctor.sun.http.callback.ApiCallback;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.model.NewMedicalRecordModel;
import com.doctor.sun.ui.activity.PMainActivity2;
import com.doctor.sun.ui.activity.SingleFragmentActivity;
import com.doctor.sun.ui.adapter.ViewHolder.SortedItem;
import com.doctor.sun.ui.widget.TwoChoiceDialog;
import com.doctor.sun.vm.ItemPickDate;
import com.doctor.sun.vm.ItemRadioGroup;
import com.doctor.sun.vm.ItemTextInput2;

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

    public static Bundle getArgs(int recordType,int type) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.FRAGMENT_NAME, TAG);
        bundle.putInt(Constants.RECORD_TYPE, recordType);
        bundle.putInt(Constants.MOCK,type);
        return bundle;
    }

    public static Bundle newOtherRecord() {
        Bundle args = getArgs(TYPE_OTHER,0);
        args.putString(Constants.FRAGMENT_TITLE, "新建亲友病历");
        return args;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        model = new NewMedicalRecordModel();
        IntentFilter filter = new IntentFilter();
        filter.addAction("CREATE_REGISTER_SUCCESS");
        getActivity().registerReceiver(receiver,filter);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        disableRefresh();
        setHasOptionsMenu(true);
        List<SortedItem> sortedItems = model.parseItem(getAdapter(),getType());
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
                        saveMedicalRecord(true);
                    }

                    @Override
                    public void onCancelClick(MaterialDialog dialog) {
                        dialog.dismiss();
                    }
                });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (getType()==0){
            inflater.inflate(R.menu.menu_confirm, menu);
        }else{
            inflater.inflate(R.menu.menu_next_to_main, menu);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_confirm: {
                saveMedicalRecord(false);
                break;
            }
            case R.id.action_next: {
                Intent i = PMainActivity2.makeIntent(getActivity());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    getActivity().finishAffinity();
                } else {
                    getActivity().finish();
                }
                startActivity(i);
                getActivity().finish();
                break;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private int getRecordType() {
        return getArguments().getInt(Constants.RECORD_TYPE);
    }

    private int getType(){
        return getArguments().getInt(Constants.MOCK);
    }

    private void saveMedicalRecord(final boolean isFromProfile) {
        model.saveMedicalRecord(getAdapter(), getRecordType(), new SimpleCallback<MedicalRecord>() {
            @Override
            protected void handleResponse(MedicalRecord response) {
                if (getType()!=0){
                    Intent intent = new Intent();
                    intent.setAction(Constants.CREATE_SUCCESS);
                    intent.putExtra(Constants.DATA,response.getRecordName());
                    getActivity().sendBroadcast(intent);
                }
                EventHub.post(new SelectMedicalRecordEvent(getArguments().getString(Constants.FROM), response));
                if (isFromProfile) {
                    viewRecordDetail(response);
                }
                Settings.loadPatientProfile(new Runnable() {
                    @Override
                    public void run() {
                        getActivity().finish();
                    }
                });
            }
        });
    }

    private void viewRecordDetail(MedicalRecord medicalRecord) {
        Bundle bundle = EditRecordFragment.getArgs(medicalRecord);
        Intent intent = SingleFragmentActivity.intentFor(getActivity(), "病历详情", bundle);
        getActivity().startActivity(intent);

    }

    public BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("CREATE_REGISTER_SUCCESS")){
                Intent i = PMainActivity2.makeIntent(getActivity());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    getActivity().finishAffinity();
                } else {
                    getActivity().finish();
                }
                startActivity(i);
                getActivity().finish();
            }
        }
    };
    public static boolean hasSelfRecord(List<MedicalRecord> records) {
        if (records == null) {
            return false;
        }
        for (int i = 0; i < records.size(); i++) {
            if ("本人".equals(records.get(i).getRelation())) {
                return true;
            }
        }
        return false;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(receiver);
    }
}
