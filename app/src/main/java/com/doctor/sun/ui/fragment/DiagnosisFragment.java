package com.doctor.sun.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.databinding.Observable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.doctor.sun.R;
import com.doctor.sun.Settings;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.FragmentDiagnosisBinding;
import com.doctor.sun.databinding.ItemPrescriptionBinding;
import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.entity.DiagnosisInfo;
import com.doctor.sun.entity.Doctor;
import com.doctor.sun.entity.constans.AppointmentType;
import com.doctor.sun.entity.constans.ImportType;
import com.doctor.sun.entity.constans.IntBoolean;
import com.doctor.sun.entity.handler.DoctorHandler;
import com.doctor.sun.event.ActivityResultEvent;
import com.doctor.sun.event.HideFABEvent;
import com.doctor.sun.event.ImportDiagnosisEvent;
import com.doctor.sun.event.ShowFABEvent;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.immutables.ImmutablePrescription;
import com.doctor.sun.immutables.ModifiablePrescription;
import com.doctor.sun.immutables.Prescription;
import com.doctor.sun.model.DiagnosisModel;
import com.doctor.sun.module.DiagnosisModule;
import com.doctor.sun.ui.activity.doctor.ContactActivity;
import com.doctor.sun.ui.activity.patient.AppointmentDetailActivity;
import com.doctor.sun.ui.widget.TwoChoiceDialog;
import com.doctor.sun.util.JacksonUtils;
import com.doctor.sun.vm.ItemButton;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.ganguo.library.Config;
import io.ganguo.library.core.event.EventHub;
import io.ganguo.library.util.Tasks;
import retrofit2.Call;

/**
 * Created by rick on 12/21/15.
 */
public class DiagnosisFragment extends BaseFragment {
    public static final String TAG = DiagnosisFragment.class.getSimpleName();
    //转诊
    public static final int RETURN_TYPE_TRANSFER = 3;
    //专属咨询
    public static final int RETURN_TYPE_FACE = 2;
    //专属咨询
    public static final int RETURN_TYPE_NET = 1;
    public static final int EDIT_PRESCRITPION = 1;

    private DiagnosisModule api = Api.of(DiagnosisModule.class);
    private FragmentDiagnosisBinding binding;
    private DiagnosisModel viewModel;

    private RadioGroup.OnCheckedChangeListener returnTypeChangeListener;
    private Doctor doctor;
    private int returnType = 1;
    private boolean shouldScrollDown = false;
    private ArrayList<Prescription> prescriptions;
    private int mActionBarAutoHideSignal = 0;
    private boolean neverScrollAfterOnCreateView = true;
    private boolean isAppointmentFinish = false;


    public static DiagnosisFragment newInstance(String appointmentId, String recordId) {

        Bundle args = new Bundle();
        args.putString(Constants.PARAM_APPOINTMENT, appointmentId);
        args.putString(Constants.PARAM_RECORD_ID, recordId);

        DiagnosisFragment fragment = new DiagnosisFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDiagnosisBinding.inflate(inflater, container, false);
        if (viewModel == null) {
            viewModel = new DiagnosisModel((Activity) getContext());
        }
        binding.needReturn.setData("需要专属咨询/转诊/闲时咨询");
        binding.needReturn.setIsChecked(false);
        binding.swRoot.setVerticalScrollBarEnabled(false);
        binding.swRoot.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (neverScrollAfterOnCreateView) {
                    neverScrollAfterOnCreateView = false;
                    return;
                }

                if (scrollY - oldScrollY <= 0) {
                    EventHub.post(new ShowFABEvent(getAppointmentId()));
                } else {
                    EventHub.post(new HideFABEvent(getAppointmentId()));
                }
            }
        });

        viewModel.getReturnType().setEnabled(false);
        binding.setData(viewModel);
        prescriptions = new ArrayList<>();
        viewModel.getDate().addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                viewModel.getTime().setDate(viewModel.getDate().getDate());
            }
        });
        setHasOptionsMenu(true);
        return binding.getRoot();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventHub.unregister(this);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initListener();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        viewModel = null;
        shouldScrollDown = false;
    }

    private void initListener() {
        ItemButton chooseDoctor = new ItemButton(R.layout.item_big_button, "选择医生") {
            @Override
            public void onClick(View view) {
                Intent intent = ContactActivity.makeIntent(getContext(), Constants.DOCTOR_REQUEST_CODE);
                Activity context = (Activity) getContext();
                context.startActivityForResult(intent, Constants.DOCTOR_REQUEST_CODE);
            }
        };
        chooseDoctor.setEnabled(false);
        viewModel.setChooseDoctor(chooseDoctor);
        returnTypeChangeListener = getReturnTypeChangeListener();
        binding.needReturn.switchButton.setOnCheckedChangeListener(getNeedReturnChangeListener());
        viewModel.getReturnType().setListener(returnTypeChangeListener);

    }

    @NonNull
    private CompoundButton.OnCheckedChangeListener getNeedReturnChangeListener() {
        return new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    viewModel.getReturnType().setEnabled(true);
                    viewModel.getReturnType().setSelectedItem(returnType);
                    returnType = viewModel.getReturnType().getSelectedItem();
                } else {
                    viewModel.getReturnType().setEnabled(false);
                    returnType = viewModel.getReturnType().getSelectedItem();
                    viewModel.getReturnType().setSelectedItem(-1);
                }
            }
        };
    }

    @NonNull
    private RadioGroup.OnCheckedChangeListener getReturnTypeChangeListener() {
        return new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case RETURN_TYPE_NET: {
                        showReturn(DiagnosisModel.DETAIL);
                        viewModel.getDate().setType(AppointmentType.PREMIUM);
                        viewModel.getTime().setType(3);
                        break;
                    }
                    case RETURN_TYPE_FACE: {
                        showReturn(DiagnosisModel.QUICK);
                        viewModel.getDate().setType(AppointmentType.STANDARD);
                        viewModel.getTime().setType(2);
                        break;
                    }
                    case RETURN_TYPE_TRANSFER: {
                        showTransfer();
                        break;
                    }
                    default: {
                        binding.llyReturn.setVisibility(View.GONE);
                        binding.llyTransfer.setVisibility(View.GONE);
                    }
                }
                if (shouldScrollDown) {
                    Tasks.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            binding.swRoot.fullScroll(ScrollView.FOCUS_DOWN);
                        }
                    }, 320);
                }
            }
        };
    }

    //转诊
    private void showTransfer() {
        binding.llyReturn.setVisibility(View.GONE);
        if (doctor == null) {
            viewModel.getChooseDoctor().setEnabled(true);
            binding.itemDoctor.getRoot().setVisibility(View.GONE);
            binding.itemTransferTo.getRoot().setVisibility(View.GONE);
        } else {
            binding.llyTransfer.setVisibility(View.VISIBLE);
            viewModel.getChooseDoctor().setEnabled(false);
            binding.itemDoctor.getRoot().setVisibility(View.VISIBLE);
            binding.itemTransferTo.getRoot().setVisibility(View.VISIBLE);
            binding.itemDoctor.setData(doctor);
        }
        binding.llyTransfer.setVisibility(View.VISIBLE);
    }

    //专属咨询或者专属咨询
    private void showReturn(String furtherConsultation) {
        binding.llyReturn.setVisibility(View.VISIBLE);
        binding.llyTransfer.setVisibility(View.GONE);
        viewModel.setReturnType(furtherConsultation);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventHub.register(this);
        shouldScrollDown = false;

        api.diagnosisInfo(getAppointmentId()).enqueue(new SimpleCallback<DiagnosisInfo>() {
            @Override
            protected void handleResponse(DiagnosisInfo response) {
                if (response == null) {
                    return;
                }
                isAppointmentFinish = (response.isFinish == IntBoolean.TRUE);
                getActivity().invalidateOptionsMenu();
                viewModel.cloneFromDiagnosisInfo(response);
                returnType = response.getReturnType();
                binding.setData(viewModel);
                binding.executePendingBindings();
                if (response.getPrescription() != null) {
                    for (Prescription prescription : response.getPrescription()) {
                        addPrescription(prescription);
                    }
                }
                int returnX = response.getReturnX();
                Log.e(TAG, "handleResponse: " + returnX);
                binding.needReturn.setIsChecked(returnX == 1);
                if (response.getDoctorInfo() != null) {
                    doctor = response.getDoctorInfo();
                    binding.itemDoctor.setData(doctor);
                    viewModel.setDoctor(doctor);
                }
                doctor = response.getDoctorInfo();
            }

            @Override
            protected void handleApi(ApiDTO<DiagnosisInfo> body) {
                super.handleApi(body);
                if (body.getData() == null) {
                    if (canWritePrescription()) {
                        loadPrescription(getAppointmentId());
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiDTO<DiagnosisInfo>> call, Throwable t) {
                super.onFailure(call, t);
                Log.d(TAG, "onFailure() called with: " + "call = [" + call + "], t = [" + t + "]");
            }
        });
    }

    public boolean canWritePrescription() {
        Doctor doctorProfile = Settings.getDoctorProfile();
        if (doctorProfile == null) {
            return false;
        }
        DoctorHandler handler = doctorProfile.getHandler();
        return handler.canWritePrescription();
    }

    private void loadPrescription(String id) {
        Api.of(DiagnosisModule.class).patientDrug(id).enqueue(new SimpleCallback<List<Prescription>>() {
            @Override
            protected void handleResponse(List<Prescription> response) {
                if (response == null) {
                    return;
                }
                for (Prescription prescription : response) {
                    addPrescription(prescription);
                }
            }
        });
    }

    @Subscribe
    public void onActivityResultEvent(ActivityResultEvent event) {
        onActivityResult(event.getRequestCode(), event.getResultCode(), event.getData());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        handlerResult(requestCode, resultCode, data);
    }

    public void handlerResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.DOCTOR_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                doctor = data.getParcelableExtra(Constants.DATA);
                binding.itemDoctor.setData(doctor);
                viewModel.setDoctor(doctor);
                viewModel.getReturnType().setSelectedItem(RETURN_TYPE_TRANSFER);
            }
        }
        if (requestCode == Constants.PRESCRITION_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                String jsonData = data.getStringExtra(Constants.DATA);
                Prescription prescription = JacksonUtils.fromJson(jsonData, Prescription.class);
                addPrescription(prescription);
            }
        }
    }

    private void addPrescription(final Prescription prescription) {
        if (prescriptions == null) {
            prescriptions = new ArrayList<>();
        }
        ModifiablePrescription from = ModifiablePrescription.create().from(prescription);
        prescriptions.add(from);
        final LinearLayout llyRoot = binding.editPrescription.llyRoot;
        final ItemPrescriptionBinding prescriptionBinding = ItemPrescriptionBinding.inflate(LayoutInflater.from(getContext()), llyRoot, false);
        prescriptionBinding.tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llyRoot.removeView(prescriptionBinding.getRoot());
                prescriptions.remove(prescriptionBinding.getData());
            }
        });
        prescriptionBinding.setData(from);
        llyRoot.addView(prescriptionBinding.getRoot(), llyRoot.getChildCount() - 1);
    }

    public void save() {
        final String string;
        String positiveText;
        String negativeText;
        if (isAppointmentFinish) {
            string = getString(R.string.edit_diagnosis);
            positiveText = "保存并通知患者";
            negativeText = null;
        } else {
            string = getString(R.string.save_diagnosis);
            positiveText = "保存并结束";
            negativeText = "存为草稿";
        }
        TwoChoiceDialog.show(getContext(), string,
                negativeText, positiveText, new TwoChoiceDialog.Options() {
                    @Override
                    public void onApplyClick(final MaterialDialog dialog) {
                        final HashMap<String, String> query = viewModel.toHashMap(getAppointmentId(), getRecordId(), binding, getPrescriptions());
                        saveDiagnosis(dialog, query);

                    }

                    @Override
                    public void onCancelClick(final MaterialDialog dialog) {
                        final HashMap<String, String> query = viewModel.toHashMap(getAppointmentId(), getRecordId(), binding, getPrescriptions());
                        query.put("hold", "1");
                        saveDiagnosis(dialog, query);
                    }
                });

    }

    private void saveDiagnosis(final MaterialDialog dialog, HashMap<String, String> query) {
        api.setDiagnosis(query).enqueue(new SimpleCallback<String>() {
            @Override
            protected void handleResponse(String response) {
                Toast.makeText(getActivity(), "保存成功", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                getActivity().finish();
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
                dialog.dismiss();
            }
        });
    }

    private String getPrescriptions() {
        if (prescriptions.isEmpty()) {
            return "";
        }
        try {
            ArrayList<Prescription> result = new ArrayList<>();
            for (Prescription prescription : prescriptions) {
                Prescription copy = ImmutablePrescription.copyOf(prescription);
                result.add(copy);
            }
            return JacksonUtils.toJson(result);
        } catch (Exception e) {
            return "";
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (isAppointmentFinish) {
            inflater.inflate(R.menu.menu_edit, menu);
        } else {
            inflater.inflate(R.menu.menu_save, menu);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save: {
                save();
                return true;
            }
            case R.id.action_edit: {
                save();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public String getAppointmentId() {
        return getArguments().getString(Constants.PARAM_APPOINTMENT);
    }

    public String getRecordId() {
        return getArguments().getString(Constants.PARAM_RECORD_ID);
    }

    @Subscribe
    public void onEventMainThread(final ImportDiagnosisEvent e) {
        if (getAppointmentId().equals(e.formId)) {
            api.diagnosisInfo(e.toId).enqueue(new SimpleCallback<DiagnosisInfo>() {
                @Override
                protected void handleResponse(DiagnosisInfo response) {
                    if (response == null) {
                        Toast.makeText(getContext(), "导入病历记录失败", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    switch (e.type) {
                        case ImportType.ALL: {
                            viewModel.cloneAll(response);
                            if (response.getPrescription() != null) {
                                for (Prescription prescription : response.getPrescription()) {
                                    addPrescription(prescription);
                                }
                            }
                            Toast.makeText(getContext(), "全部导入成功", Toast.LENGTH_SHORT).show();
                            break;
                        }
                        case ImportType.ADVICE_AND_PRESCRIPTION: {
                            viewModel.cloneAdvice(response);
                            if (response.getPrescription() != null) {
                                for (Prescription prescription : response.getPrescription()) {
                                    addPrescription(prescription);
                                }
                            }
                            Toast.makeText(getContext(), "处方医嘱导入成功", Toast.LENGTH_SHORT).show();
                            break;
                        }
                        case ImportType.DIAGNOSIS: {
                            viewModel.cloneDiagnosisRecord(response);
                            Toast.makeText(getContext(), "病程记录导入成功", Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }
                    binding.setData(viewModel);
                }
            });
        }
    }
}
