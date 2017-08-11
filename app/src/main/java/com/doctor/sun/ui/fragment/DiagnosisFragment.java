package com.doctor.sun.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.databinding.Observable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.text.TextUtils;
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
import com.doctor.sun.AppContext;
import com.doctor.sun.R;
import com.doctor.sun.Settings;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.FragmentDiagnosisBinding;
import com.doctor.sun.databinding.ItemPrescriptionBinding;
import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.dto.QuestionDTO;
import com.doctor.sun.entity.AllTime;
import com.doctor.sun.entity.DiagnosisInfo;
import com.doctor.sun.entity.Doctor;
import com.doctor.sun.entity.Precriptions;
import com.doctor.sun.entity.Time;
import com.doctor.sun.entity.constans.AppointmentType;
import com.doctor.sun.entity.constans.ImportType;
import com.doctor.sun.entity.constans.IntBoolean;
import com.doctor.sun.entity.constans.QuestionsPath;
import com.doctor.sun.entity.handler.DoctorHandler;
import com.doctor.sun.event.ActivityResultEvent;
import com.doctor.sun.event.HideFABEvent;
import com.doctor.sun.event.ImportDiagnosisEvent;
import com.doctor.sun.event.ShowFABEvent;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.immutables.Prescription;
import com.doctor.sun.model.DiagnosisModel;
import com.doctor.sun.module.DiagnosisModule;
import com.doctor.sun.module.DrugModule;
import com.doctor.sun.module.QuestionModule;
import com.doctor.sun.module.TimeModule;
import com.doctor.sun.ui.activity.doctor.ContactActivity;
import com.doctor.sun.ui.widget.TwoChoiceDialog;
import com.doctor.sun.util.JacksonUtils;
import com.doctor.sun.vm.DiagnosisRecordList;
import com.doctor.sun.vm.ItemButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.squareup.otto.Subscribe;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import io.ganguo.library.common.LoadingHelper;
import io.ganguo.library.core.event.EventHub;
import io.ganguo.library.util.Tasks;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by rick on 12/21/15.
 */
public class DiagnosisFragment extends BaseFragment {
    public static final String TAG = DiagnosisFragment.class.getSimpleName();
    //诊所面诊
    public static final int RETURN_TYPE_SURFACE = 4;
    //转诊
    public static final int RETURN_TYPE_TRANSFER = 3;
    //简易复诊
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
    private int CREATE_DRUG = 1;
    private int NOT_CREATE_DRUG = 0;
    private int hasRecord = 1;
    private DrugModule drugModule = Api.of(DrugModule.class);
    public static TimeModule module = Api.of(TimeModule.class);

    public static DiagnosisFragment newInstance(String appointmentId, String recordId) {
        DiagnosisFragment fragment = new DiagnosisFragment();
        Bundle args = new Bundle();
        args.putString(Constants.PARAM_APPOINTMENT, appointmentId);
        args.putString(Constants.PARAM_RECORD_ID, recordId);

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

        if (Settings.getDoctorProfile().getSpecialistCateg() == 1 || Settings.getDoctorProfile().getIsOpen().isSimple() == false) {
            binding.needReturn.setData("需要VIP网诊/转诊/诊所面诊");
        } else {
            binding.needReturn.setData("需要VIP网诊/转诊/简易复诊/诊所面诊");
        }

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

    private final GregorianCalendar calendar = new GregorianCalendar();

    @NonNull
    private RadioGroup.OnCheckedChangeListener getReturnTypeChangeListener() {
        return new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case RETURN_TYPE_NET: {
                        module.getDate(Settings.getDoctorProfile().getId(), 30, 1).enqueue(new SimpleCallback<List<Time>>() {
                            @Override
                            protected void handleResponse(List<Time> response) {
                                if (response.size() == 0) {
                                    return;
                                }
                                List<Time> times = new ArrayList<Time>();
                                for (int i = 0; i < response.size(); i++) {
                                    if (response.get(i).getOptional() == 1) {
                                        times.add(response.get(i));
                                    }
                                }
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                try {
                                    Date month = dateFormat.parse(times.get(0).getDate());
                                    viewModel.getDate().setMonthOfYear(month.getMonth());
                                    viewModel.getDate().setDayOfMonth(month.getDate());
                                    showReturn(DiagnosisModel.DETAIL);
                                    viewModel.getDate().setType(AppointmentType.PREMIUM);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                        break;
                    }
                    case RETURN_TYPE_FACE: {
                        viewModel.getDate().setMonthOfYear(calendar.get(Calendar.MONTH));
                        viewModel.getDate().setDayOfMonth(calendar.get(Calendar.DAY_OF_MONTH) + 1);
                        showReturn(DiagnosisModel.QUICK);
                        viewModel.getDate().setType(AppointmentType.STANDARD);
                        break;
                    }
                    case RETURN_TYPE_TRANSFER: {
                        showTransfer();
                        break;
                    }
                    case RETURN_TYPE_SURFACE: {
                        module.getDate(Settings.getDoctorProfile().getId(), 30, 4).enqueue(new SimpleCallback<List<Time>>() {
                            @Override
                            protected void handleResponse(List<Time> response) {
                                if (response.size() == 0) {
                                    return;
                                }
                                List<Time> times = new ArrayList<Time>();
                                for (int i = 0; i < response.size(); i++) {
                                    if (response.get(i).getOptional() == 1) {
                                        times.add(response.get(i));
                                    }
                                }
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                try {
                                    Date month = dateFormat.parse(times.get(0).getDate());
                                    viewModel.getDate().setMonthOfYear(month.getMonth());
                                    viewModel.getDate().setDayOfMonth(month.getDate());
                                    showReturn(DiagnosisModel.SURFACE);
                                    viewModel.getDate().setType(AppointmentType.FACE);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

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
                if (response.getIsDiagnosis()!=0) {
                    hasRecord = 0;
                } else {
                    hasRecord = 1;
                }
                isAppointmentFinish = (response.isFinish == IntBoolean.TRUE);
                if (Settings.isDoctor()) {
                    if (isAppointmentFinish && response.canEdit != 0) {
                        Snackbar snackbar = Snackbar.make(binding.mAddview, "提醒:点击右上角可保存修改", Snackbar.LENGTH_INDEFINITE);
                        snackbar.getView().setBackgroundColor(getResources().getColor(R.color.black_transparent_80));
                        snackbar.show();
                    }
                }
                getActivity().invalidateOptionsMenu();
                viewModel.cloneFromDiagnosisInfo(response);
                returnType = response.getReturnType();
                viewModel.setType(returnType);
                binding.setData(viewModel);
                binding.executePendingBindings();
                if (response.getPrescription() != null) {
                    for (Prescription prescription : response.getPrescription()) {
                        addPrescription(prescription);
                    }
                }
                int returnX = response.getReturnX();
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

    private void addPrescription(Prescription prescription) {
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
                prescriptions.remove(prescriptionBinding.getData());
            }
        });
        prescriptionBinding.setData(prescription);
        llyRoot.addView(prescriptionBinding.getRoot(), llyRoot.getChildCount() - 1);
    }

    /*点击保存按钮*/
    public void save() {
        final String string;
        String positiveText;
        String negativeText;
        string = getString(R.string.save_diagnosis);
        positiveText = "保存提交";
        if (hasRecord == 1) {
            negativeText = "存为草稿";
        } else {
            negativeText = null;
        }

        TwoChoiceDialog.show(getContext(), string,
                negativeText, positiveText, new TwoChoiceDialog.Options() {

                    /*保存并结束*/
                    @Override
                    public void onApplyClick(final MaterialDialog dialog) {
                        final HashMap<String, String> query = viewModel.toHashMap(getAppointmentId(), getRecordId(), binding, getPrescriptions());
                        /*咨询未结束*/
                       /* if (!isAppointmentFinish) { */
                               /*判断是否填写了处方建议*/
                        if (!TextUtils.isEmpty(getPrescriptions())) {
                                    /*处方建议不为空*/
                            api.getSentDrugRecord(getRecordId()).enqueue(new Callback<ApiDTO>() {
                                @Override
                                public void onResponse(Call<ApiDTO> call, Response<ApiDTO> response) {
                                    boolean isSent = Boolean.valueOf(response.body().getData().toString());
                                    if (isSent == false) {
                                                /*没有寄药记录*/
                                        saveDiagnosis(dialog, query, CREATE_DRUG);
                                    } else {
                                                /*有过寄药记录*/
                                        saveDiagnosis(dialog, query, NOT_CREATE_DRUG);
                                    }
                                }

                                @Override
                                public void onFailure(Call<ApiDTO> call, Throwable t) {

                                }
                            });
                        } else {
                            /*未填写处方建议*/
                            saveDiagnosis(dialog, query, NOT_CREATE_DRUG);
                        }
                     /*   } else {
                            saveDiagnosis(dialog, query,NOT_CREATE_DRUG);
                        }*/
                    }

                    /*存为草稿*/
                    @Override
                    public void onCancelClick(final MaterialDialog dialog) {
                        final HashMap<String, String> query = viewModel.toHashMap(getAppointmentId(), getRecordId(), binding, getPrescriptions());
                        query.put("hold", "1");
                        saveDiagnosis(dialog, query, NOT_CREATE_DRUG);
                    }
                });

    }

    public void showCreateDrug(final HashMap<String, String> query) {
        String reminder = "该患者在昭阳医生平台还未有过寄药记录，请问是否将处方建议生成处方?";
        String cancel = "暂不生成";
        String apply = "生成处方";
        TwoChoiceDialog.show(getContext(), reminder, cancel, apply, new TwoChoiceDialog.Options() {
            @Override
            public void onApplyClick(final MaterialDialog dialog) {
            /*生成处方提交后台*/
                api.createRecipe(getAppointmentId(), "1").enqueue(new Callback<ApiDTO>() {
                    @Override
                    public void onResponse(Call<ApiDTO> call, Response<ApiDTO> response) {
                        if (response.body().getStatus().equals("200")) {
                            Toast.makeText(getContext(), "处方生成成功！", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            getActivity().finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiDTO> call, Throwable t) {
                        dialog.dismiss();
                    }
                });
            }

            @Override
            public void onCancelClick(final MaterialDialog dialog) {
            /*不生成处方提交后台*/
                api.createRecipe(getAppointmentId(), "0").enqueue(new Callback<ApiDTO>() {
                    @Override
                    public void onResponse(Call<ApiDTO> call, Response<ApiDTO> response) {
                        if (response.body().getStatus().equals("200")) {
                            dialog.dismiss();
                            getActivity().finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiDTO> call, Throwable t) {
                        dialog.dismiss();
                    }
                });
            }
        });
    }

    private void saveDiagnosis(final MaterialDialog dialog, final HashMap<String, String> query, final int type) {
        LoadingHelper.showMaterLoading(getContext(), "正在保存...");
        api.setDiagnosis(query).enqueue(new SimpleCallback<String>() {
            @Override
            protected void handleResponse(String response) {
                AppContext.getInstance().setType(0);
                LoadingHelper.hideMaterLoading();
                Toast.makeText(getActivity(), "保存成功", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                if (type != 0) {
                    showCreateDrug(query);
                } else {
                    getActivity().finish();
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                dialog.dismiss();
                LoadingHelper.hideMaterLoading();
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
                result.add(prescription);
            }
            Gson gson = new GsonBuilder().create();
            return gson.toJson(result);
        } catch (Exception e) {
            return "";
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (menu.size() == 0) {
            inflater.inflate(R.menu.menu_save, menu);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save: {
                save();
                break;
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
        if (e.appointmentType == 3) {
            drugModule.getPrescription(e.toId).enqueue(new SimpleCallback<List<Prescription>>() {
                @Override
                protected void handleResponse(List<Prescription> response) {
                    if (response != null) {
                        for (Prescription prescription : response) {
                            prescription.setTake_medicine_days("28");
                            addPrescription(prescription);
                        }
                    }
                    Toast.makeText(getContext(), "处方医嘱导入成功", Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            api.diagnosisInfo(e.toId).enqueue(new SimpleCallback<DiagnosisInfo>() {
                @Override
                protected void handleResponse(DiagnosisInfo response) {
                    if (response == null) {
                        Toast.makeText(getContext(), "导入病历记录失败", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    switch (e.type) {
                        case ImportType.ALL: {
                            viewModel.recordList = new DiagnosisRecordList();
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
                            viewModel.setAdvice("");
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
                            viewModel.recordList = new DiagnosisRecordList();
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
