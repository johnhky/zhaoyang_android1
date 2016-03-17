package com.doctor.sun.ui.activity.patient;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.PActivityInfoBinding;
import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.dto.PatientDTO;
import com.doctor.sun.entity.Patient;
import com.doctor.sun.entity.Photo;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.ApiCallback;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.module.ProfileModule;
import com.doctor.sun.module.ToolModule;
import com.doctor.sun.ui.activity.BaseActivity2;
import com.doctor.sun.ui.activity.patient.handler.EditPatientHandler;
import com.doctor.sun.ui.binding.CustomBinding;
import com.doctor.sun.ui.model.HeaderViewModel;
import com.doctor.sun.ui.widget.PickImageDialog;
import com.doctor.sun.ui.widget.TwoSelectorDialog;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;

import java.io.File;

import io.ganguo.library.common.ToastHelper;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by lucas on 1/4/16.
 */
public class EditPatientInfoActivity extends BaseActivity2 implements EditPatientHandler.IEditPatient {
    private static final int CODE_IMAGE_REQUEST = 8;
    private ProfileModule api = Api.of(ProfileModule.class);
    private ToolModule uploadUriApi = Api.of(ToolModule.class);

    private PActivityInfoBinding binding;
    private String avatar = "";
    private HeaderViewModel header = new HeaderViewModel(this);


    private boolean isEditMode = false;
    private Patient patient;

    public boolean isEditMode() {
        return isEditMode;
    }

    public void setIsEditMode(boolean isEditMode) {
        this.isEditMode = isEditMode;
    }

    public static Intent makeIntent(Context context, Patient data) {
        Intent i = new Intent(context, EditPatientInfoActivity.class);
        i.putExtra(Constants.DATA, data);
        return i;
    }


    private Patient getPatient() {
        return getIntent().getParcelableExtra(Constants.DATA);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        initData();
    }

    private void initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.p_activity_info);
        header.setMidTitle("个人信息").setRightTitle("编辑");
        patient = getPatient();
        binding.setData(patient);
        binding.setHeader(header);
    }

    private void initData() {
        api.patientProfile().enqueue(new ApiCallback<PatientDTO>() {
            @Override
            protected void handleResponse(PatientDTO response) {
                binding.setData(response.getInfo());
                if (response.getInfo().getAvatar().equals("")) {
                    binding.bivAvatar.setVisibility(View.GONE);
                    binding.tvAvatar.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        handleImageRequest(requestCode, resultCode, data);
    }

    public void handleImageRequest(final int requestCode, int resultCode, final Intent data) {
        File file = null;
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CODE_IMAGE_REQUEST:
                    file = PickImageDialog.handleAlbumRequest(this, data);
                    break;
                case CODE_IMAGE_REQUEST / 2:
                    file = PickImageDialog.handleCameraRequest();
                    break;
            }
            File compressImage = PickImageDialog.compressImage(file);
            RequestBody body = RequestBody.create(MediaType.parse("multipart/form-patient"), compressImage);
            uploadUriApi.uploadPhoto(body).enqueue(new Callback<ApiDTO<Photo>>() {
                @Override
                public void onResponse(Response<ApiDTO<Photo>> response, Retrofit retrofit) {
                    if (response.isSuccess()) {
                        String imgUrl = response.body().getData().getUrl();
                        patient.setAvatar(imgUrl);
                        avatar = imgUrl;
                        binding.bivAvatar.setVisibility(View.VISIBLE);
                        binding.tvAvatar.setVisibility(View.GONE);
                        binding.setData(patient);
                    }
                }

                @Override
                public void onFailure(Throwable t) {

                }
            });
        }
    }

    @Override
    public void onMenuClicked() {
        super.onMenuClicked();
        if (!isEditMode()) {
            TwoSelectorDialog.showTwoSelectorDialog(this, " 您好，昭阳医生不可以随便更改用户资料，所有用户资料的申请需要经过后台审核", "确定", "取消", new TwoSelectorDialog.GetActionButton() {
                @Override
                public void onClickPositiveButton(TwoSelectorDialog dialog) {
                    dialog.dismiss();
                }

                @Override
                public void onClickNegativeButton(TwoSelectorDialog dialog) {
                    dialog.dismiss();
                    header.setRightTitle("保存");
                    binding.setHeader(header);
                    binding.bivAvatar.setBackgroundResource(R.drawable.ripple_default);
                    binding.etName.setFocusableInTouchMode(true);
                    binding.etEmail.setFocusableInTouchMode(true);
                    binding.tvBirthday.setBackgroundResource(R.drawable.ripple_default);
                    binding.tvGender.setVisibility(View.GONE);
                    binding.rbMale.setVisibility(View.VISIBLE);
                    binding.rbFemale.setVisibility(View.VISIBLE);
                    CustomBinding.drawableRight(binding.tvBirthday, R.drawable.ic_enter);
                    setIsEditMode(!isEditMode());
                }
            });
        } else {
            api.editPatientInfo(binding.etName.getText().toString(), binding.etEmail.getText().toString(),
                    binding.tvBirthday.getText().toString(), getGender(), avatar).enqueue(new SimpleCallback<Patient>() {
                @Override
                protected void handleResponse(Patient response) {
                    ToastHelper.showMessage(EditPatientInfoActivity.this, "保存成功,请耐心等待资料审核");
                    finish();
                }
            });
        }
    }

    public int getGender() {
        int gender = 1;
        if (binding.rbMale.isChecked()) {
            gender = 1;
        }
        if (binding.rbFemale.isChecked()) {
            gender = 2;
        }
        return gender;
    }


    @Override
    public boolean getIsEditMode() {
        return isEditMode();
    }

}
