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
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.ScrollView;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.FragmentDiagnosisBinding;
import com.doctor.sun.databinding.ItemPrescriptionBinding;
import com.doctor.sun.entity.Appointment;
import com.doctor.sun.entity.DiagnosisInfo;
import com.doctor.sun.entity.Doctor;
import com.doctor.sun.vo.ItemButton;
import com.doctor.sun.entity.Prescription;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.ApiCallback;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.module.DiagnosisModule;
import com.doctor.sun.ui.activity.doctor.ContactActivity;
import com.doctor.sun.ui.activity.doctor.EditPrescriptionActivity;
import com.doctor.sun.ui.model.DiagnosisViewModel;
import com.doctor.sun.ui.widget.TwoSelectorDialog;
import com.doctor.sun.util.JacksonUtils;

import java.util.ArrayList;
import java.util.HashMap;

import io.ganguo.library.common.ToastHelper;
import io.ganguo.library.util.Tasks;
import retrofit2.Call;

/**
 * Created by rick on 12/21/15.
 */
public class DiagnosisFragment extends Fragment {
    public static final String TAG = DiagnosisFragment.class.getSimpleName();
    public static final String IS_DIAGNOSISED = "已面诊";
    public static final String NOT_DIAGNOSISED = "未面诊";
    //转诊
    public static final int RETURN_TYPE_TRANSFER = 3;
    //详细就诊
    public static final int RETURN_TYPE_FACE = 2;
    //详细就诊
    public static final int RETURN_TYPE_NET = 1;
    public static final String TYPE_NET = "";
    public static final String TYPE_FACE = "1";
    public static final int EDIT_PRESCRITPION = 1;
    private static DiagnosisFragment instance;

    private DiagnosisModule api = Api.of(DiagnosisModule.class);
    private FragmentDiagnosisBinding binding;
    private DiagnosisViewModel viewModel;

    private RadioGroup.OnCheckedChangeListener returnTypeChangeListener;
    private Doctor doctor;
    private int returnType = 1;
    private boolean shouldScrollDown = false;
    private Appointment appointment;
    private boolean shouldAsk = true;
    private ArrayList<Prescription> prescriptions;

    public static DiagnosisFragment getInstance(Appointment id) {
        if (instance == null) {
            instance = new DiagnosisFragment();
            Bundle args = new Bundle();
            args.putParcelable(Constants.DATA, id);
            instance.setArguments(args);
        }
        return instance;
    }

    public static DiagnosisFragment newInstance(Appointment id) {

        Bundle args = new Bundle();
        args.putParcelable(Constants.DATA, id);
        DiagnosisFragment fragment = new DiagnosisFragment();
        instance = fragment;
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
//        binding.isDiagnosis.setData(NOT_DIAGNOSISED);
//        binding.isDiagnosis.setIsChecked(false);
        binding.needReturn.setData("需要详细就诊/转诊/详细就诊");
        binding.needReturn.setIsChecked(false);
        binding.swRoot.setVerticalScrollBarEnabled(false);
        viewModel.getReturnType().setVisible(false);
        binding.setData(viewModel);
        prescriptions = new ArrayList<>();
        viewModel.getDate().addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                viewModel.getTime().setDate(viewModel.getDate().getDate());
            }
        });
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initListener();
    }

    private void askIfIsDiagnosised() {
        TwoSelectorDialog.showTwoSelectorDialog(getActivity(), getString(R.string.is_diagnosised),
                NOT_DIAGNOSISED, IS_DIAGNOSISED, new TwoSelectorDialog.GetActionButton() {
                    @Override
                    public void onClickPositiveButton(TwoSelectorDialog dialog) {
//                        binding.isDiagnosis.setData(IS_DIAGNOSISED);
//                        binding.isDiagnosis.setIsChecked(true);
                        dialog.dismiss();
                    }

                    @Override
                    public void onClickNegativeButton(TwoSelectorDialog dialog) {
//                        binding.isDiagnosis.setData(NOT_DIAGNOSISED);
//                        binding.isDiagnosis.setIsChecked(false);
                        dialog.dismiss();
                    }
                });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        viewModel = null;
        shouldScrollDown = false;
        instance = null;
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
        chooseDoctor.setVisible(false);
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
                    viewModel.getReturnType().setVisible(true);
                    viewModel.getReturnType().setSelectedItem(returnType);
                    returnType = viewModel.getReturnType().getSelectedItem();
                } else {
                    viewModel.getReturnType().setVisible(false);
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
                        showReturn(DiagnosisViewModel.FURTHER_CONSULTATION);
                        viewModel.getDate().setType(TYPE_NET);
                        viewModel.getTime().setType(3);
                        break;
                    }
                    case RETURN_TYPE_FACE: {
                        showReturn(DiagnosisViewModel.FACE_TO_FACE);
                        viewModel.getDate().setType(TYPE_FACE);
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
            viewModel.getChooseDoctor().setVisible(true);
            binding.itemDoctor.getRoot().setVisibility(View.GONE);
            binding.itemTransferTo.getRoot().setVisibility(View.GONE);
            binding.msgToDoctor.getRoot().setVisibility(View.GONE);
        } else {
            binding.llyTransfer.setVisibility(View.VISIBLE);
            viewModel.getChooseDoctor().setVisible(false);
            binding.itemDoctor.getRoot().setVisibility(View.VISIBLE);
            binding.itemTransferTo.getRoot().setVisibility(View.VISIBLE);
            binding.msgToDoctor.getRoot().setVisibility(View.VISIBLE);
            binding.itemDoctor.setData(doctor);
        }
        binding.llyTransfer.setVisibility(View.VISIBLE);
    }

    //详细就诊或者详细就诊
    private void showReturn(String furtherConsultation) {
        binding.llyReturn.setVisibility(View.VISIBLE);
        binding.llyTransfer.setVisibility(View.GONE);
        viewModel.setReturnType(furtherConsultation);
//        if (furtherConsultation.equals(DiagnosisViewModel.FURTHER_CONSULTATION)) {
//            binding.mission.llyRoot.setVisibility(View.GONE);
//        } else {
//            binding.mission.llyRoot.setVisibility(View.VISIBLE);
//        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appointment = getArguments().getParcelable(Constants.DATA);
        if (appointment != null) {
            shouldAsk = 1 != appointment.getIsFinish();
        } else {
            shouldAsk = false;
        }
        shouldScrollDown = true;
        if (appointment == null) {
            return;
        }
        api.diagnosisInfo(appointment.getId()).enqueue(new ApiCallback<DiagnosisInfo>() {
            @Override
            protected void handleResponse(DiagnosisInfo response) {
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
        });
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

    public void setDiagnosise() {
        TwoSelectorDialog.showTwoSelectorDialog(getActivity(), "是否结束本次就诊",
                "暂存", "保存并结束", new TwoSelectorDialog.GetActionButton() {
                    @Override
                    public void onClickPositiveButton(final TwoSelectorDialog dialog) {
                        final HashMap<String, String> query = viewModel.toHashMap(appointment, binding, getPrescriptions());
                        saveDiagnosis(dialog, query);
                    }

                    @Override
                    public void onClickNegativeButton(final TwoSelectorDialog dialog) {
                        final HashMap<String, String> query = viewModel.toHashMap(appointment, binding, getPrescriptions());
                        query.put("hold", "1");
                        saveDiagnosis(dialog, query);
                    }
                });
    }

    private void saveDiagnosis(final TwoSelectorDialog dialog, HashMap<String, String> query) {
        api.setDiagnosis(query).enqueue(new SimpleCallback<String>() {
            @Override
            protected void handleResponse(String response) {

//                                Log.d(TAG, response.toString());

                ToastHelper.showMessage(getActivity(), "保存成功");
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
        try {
            return JacksonUtils.toJson(prescriptions);
        } catch (Exception e) {
            return "";
        }
    }


    @Override
    public void setUserVisibleHint(final boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser && shouldAsk) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    askIfIsDiagnosised();
                    shouldAsk = false;
                }
            }, 100);
        }
    }
}
