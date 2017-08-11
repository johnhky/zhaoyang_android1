package com.doctor.sun.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import java.text.SimpleDateFormat;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.doctor.auto.Factory;
import com.doctor.sun.AppContext;
import com.doctor.sun.R;
import com.doctor.sun.Settings;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.entity.Description;
import com.doctor.sun.entity.DiagnosisInfo;
import com.doctor.sun.entity.constans.IntBoolean;
import com.doctor.sun.entity.handler.AppointmentHandler2;
import com.doctor.sun.event.HideFABEvent;
import com.doctor.sun.event.ModifyStatusEvent;
import com.doctor.sun.event.ShowFABEvent;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.module.DiagnosisModule;
import com.doctor.sun.ui.activity.patient.AppointmentDetailActivity;
import com.doctor.sun.ui.adapter.SimpleAdapter;
import com.doctor.sun.ui.model.DiagnosisReadOnlyViewModel;
import com.doctor.sun.ui.widget.TwoChoiceDialog;
import com.doctor.sun.vm.ItemTextInput;

import io.ganguo.library.core.event.EventHub;
import io.ganguo.library.util.date.Date;
import retrofit2.Call;

/**
 * Created by rick on 29/6/2016.
 * 就诊医生建议只读
 * 03-24 09:45:29.903 4448-4448/com.doctor.sun.dev E/eee:
 * DiagnosisInfo{id=1163, doctorId=69, id=2630, isDiagnosis=1,
 * perception={0=0, 1=1, otherContent= }, thinking={0=1, otherContent= },
 * pipedream={0=0, 1=1, otherContent= }, emotion={0=1, otherContent= },
 * memory={0=1, otherContent= }, insight={0=1, otherContent= },
 * description='', diagnosisRecord=, currentStatus=0, recovered=0,
 * treatment=0, effect=0, prescription=[Prescription{drug_name=阿戈美拉汀片(阿羹宁),
 * scientific_name=, frequency=隔天, morning=3, noon= , night= , before_sleep= ,
 * drug_unit=粒, remark= , take_medicine_days=28, units=毫克, specification=5,
 * drug_id=, dose_units=, total_num=0.0, total_fee=0.0},
 * Prescription{drug_name=阿立哌唑口腔崩解片(博思清), scientific_name=,
 * frequency=每月, morning=2, noon= , night= , before_sleep= , drug_unit=粒,
 * remark= , take_medicine_days=28, units=毫克, specification=5,
 * drug_id=, dose_units=, total_num=0.0, total_fee=0.0}], doctorAdvince='坚持治疗,定期复诊',
 * returnX=0, returnType=0, returnPaid=0, isAccept=0, returnTime=0, money=0, returnAppointmentId=0
 * , doctorRequire=0, comment='', status=4, hasPay=0, date='null', time='null', doctorInfo=null}
 */
@Factory(type = BaseFragment.class, id = "ReadDiagnosisFragment")
public class ReadDiagnosisFragment extends RefreshListFragment {
    private DiagnosisModule api = Api.of(DiagnosisModule.class);
    private DiagnosisReadOnlyViewModel viewModel;
    public static boolean show = true;
    private int canEdit = 0;
    public static final String TAG = ReadDiagnosisFragment.class.getSimpleName();

    public static ReadDiagnosisFragment newInstance(String appointmentId) {
        Bundle args = new Bundle();
        args.putString(Constants.DATA, appointmentId);
        ReadDiagnosisFragment fragment = new ReadDiagnosisFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static Bundle getArgs(String appointmentId) {
        Bundle args = new Bundle();
        args.putString(Constants.FRAGMENT_NAME, TAG);
        args.putString(Constants.DATA, appointmentId);

        return args;
    }

    public String getAppointmentId() {
        return getArguments().getString(Constants.DATA);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void loadMore() {
        super.loadMore();
        api.diagnosisInfo(getAppointmentId()).enqueue(new SimpleCallback<DiagnosisInfo>() {
            @Override
            protected void handleResponse(DiagnosisInfo response) {
                viewModel = new DiagnosisReadOnlyViewModel();
                viewModel.cloneFromDiagnosisInfo(response);
                getAdapter().onFinishLoadMore(true);
                getAdapter().clear();
                getAdapter().addAll(viewModel.toList());
                if (Settings.isDoctor()) {
                    if (hasOptionsMenu() == true && canEdit != 0) {
                        Snackbar snackbar = Snackbar.make(binding.recyclerView, "提醒：点击右上角可修改建议", Snackbar.LENGTH_INDEFINITE);
                        snackbar.getView().setBackgroundColor(getResources().getColor(R.color.black_transparent_80));
                        snackbar.show();
                    }
                }
                getAdapter().notifyDataSetChanged();
                binding.swipeRefresh.setRefreshing(false);
                if (getAdapter().isEmpty()) {
                    if (response != null && response.isFinish == IntBoolean.TRUE) {
                        Description divider = new Description(R.layout.item_description, "嘱咐");
                        ItemTextInput textInput = new ItemTextInput(R.layout.item_text_option_display, "");
                        textInput.setInput("坚持治疗,定期复诊");
                        getAdapter().clear();
                        getAdapter().add(divider);
                        getAdapter().add(textInput);
                        getAdapter().notifyDataSetChanged();
                        binding.swipeRefresh.setRefreshing(false);
                        return;
                    } else {
                        binding.emptyIndicator.setText("请耐心等待");
                        binding.emptyIndicator.setVisibility(View.VISIBLE);
                    }
                } else {
                    binding.emptyIndicator.setVisibility(View.GONE);
                }

            }
        });
    }

    @NonNull
    @Override
    public SimpleAdapter createAdapter() {
        SimpleAdapter adapter = super.createAdapter();
        adapter.mapLayout(R.layout.item_symptom, R.layout.item_symptom_readonly);
        adapter.mapLayout(R.layout.item_symptom_single_choice, R.layout.item_symptom_readonly);
        adapter.mapLayout(R.layout.item_diagnosis, R.layout.item_consultation_readonly);
        adapter.mapLayout(R.layout.item_prescription, R.layout.item_r_prescription);
        adapter.mapLayout(R.layout.item_doctor, R.layout.item_transfer_doctor);
        return adapter;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (AppContext.getInstance().getPosition() == 1) {
            if (menu.size() == 0) {
                if (Settings.isDoctor()){
                    getActivity().getMenuInflater().inflate(R.menu.menu_change, menu);
                }
                canEdit = 1;
            }
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.advice_change: {
                showEndAppointmentDialog();
                return true;
            }

        }
        return super.onOptionsItemSelected(item);
    }

    public void showEndAppointmentDialog() {
        String applyText = "";
        String cancelText = "";
        applyText = "确认";
        cancelText = "取消";
        TwoChoiceDialog.show(getActivity(), getString(R.string.edit_diagnosis),
                cancelText, applyText, new TwoChoiceDialog.Options() {
                    @Override
                    public void onApplyClick(final MaterialDialog dialog) {
                        dialog.dismiss();
                        update();
                    }

                    @Override
                    public void onCancelClick(final MaterialDialog dialog) {
                        dialog.dismiss();
                    }
                });
    }


    private void update() {
        /*AppointmentDetailActivity.onlyRead = false;*/
     /*   AppointmentDetailActivity activity = (AppointmentDetailActivity)getActivity();
        activity.initPagerAdapter();*/
       Intent intent =  AppointmentDetailActivity.makeIntent(getActivity(),AppContext.getInstance().getEditAppointment(),1,false);
        getActivity().startActivity(intent);
        getActivity().finish();
    }

    @Override
    public ShowFABEvent getShowFABEvent() {
        return new ShowFABEvent(getAppointmentId());
    }

    @Override
    public HideFABEvent getHideFABEvent() {
        return new HideFABEvent(getAppointmentId());
    }
}
