package com.doctor.sun.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.databinding.Observable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.doctor.sun.entity.Prescription;
import com.doctor.sun.entity.constans.AppointmentType;
import com.doctor.sun.entity.handler.DoctorHandler;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.module.DiagnosisModule;
import com.doctor.sun.ui.activity.doctor.ContactActivity;
import com.doctor.sun.ui.activity.doctor.EditPrescriptionActivity;
import com.doctor.sun.ui.model.DiagnosisViewModel;
import com.doctor.sun.ui.widget.TwoChoiceDialog;
import com.doctor.sun.util.JacksonUtils;
import com.doctor.sun.vo.ItemButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
    private DiagnosisViewModel viewModel;

    private RadioGroup.OnCheckedChangeListener returnTypeChangeListener;
    private Doctor doctor;
    private int returnType = 1;
    private boolean shouldScrollDown = false;
    private ArrayList<Prescription> prescriptions;


    public static DiagnosisFragment newInstance(int appointmentId, int recordId) {

        Bundle args = new Bundle();
        args.putInt(Constants.PARAM_APPOINTMENT, appointmentId);
        args.putInt(Constants.PARAM_RECORD_ID, recordId);

        DiagnosisFragment fragment = new DiagnosisFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDiagnosisBinding.inflate(inflater, container, false);
        if (viewModel == null) {
            viewModel = new DiagnosisViewModel((Activity) getContext());
        }
        binding.needReturn.setData("需要专属咨询/转诊/留言咨询");
        binding.needReturn.setIsChecked(false);
        binding.swRoot.setVerticalScrollBarEnabled(false);
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
                        showReturn(DiagnosisViewModel.DETAIL);
                        viewModel.getDate().setType(AppointmentType.PREMIUM);
                        viewModel.getTime().setType(3);
                        break;
                    }
                    case RETURN_TYPE_FACE: {
                        showReturn(DiagnosisViewModel.QUICK);
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
        shouldScrollDown = false;

        api.diagnosisInfo(getAppointmentId()).enqueue(new SimpleCallback<DiagnosisInfo>() {
            @Override
            protected void handleResponse(DiagnosisInfo response) {
                if (response == null) {
                    return;
                }
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

    private void loadPrescription(int id) {
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
                final Prescription prescription = data.getParcelableExtra(Constants.DATA);
                addPrescription(prescription);
            }
        }
    }

    private void addPrescription(final Prescription prescription) {
        if (prescriptions == null) {
            prescriptions = new ArrayList<>();
        }
        prescriptions.add(prescription);
        final LinearLayout llyRoot = binding.editPrescription.llyRoot;
        final ItemPrescriptionBinding prescriptionBinding = ItemPrescriptionBinding.inflate(LayoutInflater.from(getContext()), llyRoot, false);
        prescriptionBinding.tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llyRoot.removeView(prescriptionBinding.getRoot());
                prescriptions.remove(prescription);
            }
        });
        prescriptionBinding.etOthers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!canWritePrescription()) {
                    Toast.makeText(getContext(), "咨询／治疗师认证无处方权", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = EditPrescriptionActivity.makeIntent(getContext(), prescription);
                Messenger messenger = new Messenger(new Handler(new Handler.Callback() {
                    @Override
                    public boolean handleMessage(Message msg) {
                        switch (msg.what) {
                            case EDIT_PRESCRITPION: {
                                Prescription parcelable = msg.getData().getParcelable(Constants.DATA);
                                llyRoot.removeView(prescriptionBinding.getRoot());
                                prescriptions.remove(prescription);
                                addPrescription(parcelable);
                            }
                        }
                        return false;
                    }
                }));
                intent.putExtra(Constants.HANDLER, messenger);
                startActivity(intent);
            }
        });
        prescriptionBinding.setData(prescription);
        llyRoot.addView(prescriptionBinding.getRoot(), llyRoot.getChildCount() - 1);
    }

    public void save() {
        TwoChoiceDialog.show(getContext(), getString(R.string.save_record_dialog),
                "存为草稿", "保存并结束", new TwoChoiceDialog.Options() {
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
            return JacksonUtils.toJson(prescriptions);
        } catch (Exception e) {
            return "";
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        if (appointment.canEdit == IntBoolean.NOT_GIVEN) {
        inflater.inflate(R.menu.menu_save, menu);
        super.onCreateOptionsMenu(menu, inflater);
//        } else {
//            inflater.inflate(R.menu.menu_modify, menu);
//            super.onCreateOptionsMenu(menu, inflater);
//        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save: {
                save();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public int getAppointmentId() {
        return getArguments().getInt(Constants.PARAM_APPOINTMENT);
    }

    public int getRecordId() {
        return getArguments().getInt(Constants.PARAM_RECORD_ID);
    }
}
