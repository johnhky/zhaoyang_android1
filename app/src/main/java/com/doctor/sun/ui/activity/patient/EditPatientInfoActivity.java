package com.doctor.sun.ui.activity.patient;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.PActivityPatientInfoBinding;
import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.dto.PatientDTO;
import com.doctor.sun.entity.Patient;
import com.doctor.sun.entity.Photo;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.ApiCallback;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.module.ProfileModule;
import com.doctor.sun.module.ToolModule;
import com.doctor.sun.ui.activity.BaseFragmentActivity2;
import com.doctor.sun.ui.activity.patient.handler.PatientHandler;
import com.doctor.sun.ui.widget.PickImageDialog;
import com.doctor.sun.ui.widget.TwoChoiceDialog;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.JELLY_BEAN;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;
import static com.doctor.sun.R.drawable.ic_enter;
import static com.doctor.sun.R.drawable.ripple_default;
import static com.doctor.sun.ui.binding.CustomBinding.drawableRight;

/**
 * Created by lucas on 1/4/16.
 */
public class EditPatientInfoActivity extends BaseFragmentActivity2 implements PatientHandler.IEditPatient {
    private static final int CODE_IMAGE_REQUEST = 8;
    private ProfileModule api = Api.of(ProfileModule.class);
    private ToolModule uploadUriApi = Api.of(ToolModule.class);

    private PActivityPatientInfoBinding binding;
    private String avatar = "";


    private boolean isEditMode = false;
    private Patient patient;

    public boolean isEditMode() {
        return isEditMode;
    }

    public void setIsEditMode(boolean isEditMode) {
        this.isEditMode = isEditMode;
        invalidateOptionsMenu();
    }

    public static Intent intentFor(Context context, Patient data) {
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
        binding = DataBindingUtil.setContentView(this, R.layout.p_activity_patient_info);
        patient = getPatient();
        if (patient == null) {
            patient = new Patient();
        }
        binding.setData(patient);

    }

    private void initData() {
        api.patientProfile().enqueue(new ApiCallback<PatientDTO>() {
            @Override
            protected void handleResponse(PatientDTO response) {
                Patient info = response.getInfo();
                if (info == null) {
                    return;
                }
                binding.setData(info);
                if (info.getAvatar().equals("")) {
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
        if (resultCode == RESULT_OK) {
            File compressImage = PickImageDialog.handleRequest(this, data, requestCode);
            RequestBody body = RequestBody.create(MediaType.parse("multipart/form-patient"), compressImage);
            uploadUriApi.uploadPhoto(body).enqueue(new Callback<ApiDTO<Photo>>() {
                @Override
                public void onResponse(Call<ApiDTO<Photo>> call, Response<ApiDTO<Photo>> response) {
                    if (response.isSuccessful()) {
                        String imgUrl = response.body().getData().getUrl();
                        patient.setAvatar(imgUrl);
                        avatar = imgUrl;
                        binding.bivAvatar.setVisibility(View.VISIBLE);
                        binding.tvAvatar.setVisibility(View.GONE);
                        binding.setData(patient);
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {

                }
            });
        }
    }

    public void onMenuClicked() {
        if (!isEditMode()) {
            TwoChoiceDialog.show(this, " 您好，昭阳医生不可以随便更改用户资料，所有用户资料的申请需要经过后台审核", "取消", "确定", new TwoChoiceDialog.Options() {
                @Override
                public void onApplyClick(MaterialDialog dialog) {
                    dialog.dismiss();
                    binding.bivAvatar.setBackgroundResource(ripple_default);
                    binding.etName.setFocusableInTouchMode(true);
                    binding.etEmail.setFocusableInTouchMode(true);
                    binding.tvBirthday.setBackgroundResource(ripple_default);
                    binding.tvGender.setVisibility(GONE);
                    binding.rbMale.setVisibility(VISIBLE);
                    binding.rbFemale.setVisibility(VISIBLE);
                    if (patient.getGender() == 1) {
                        binding.rbMale.setChecked(true);
                    } else {
                        binding.rbFemale.setChecked(true);
                    }

                    drawableRight(binding.tvBirthday, ic_enter);
                    setIsEditMode(!isEditMode());

                    makeText(EditPatientInfoActivity.this, "编辑完成后请点击右上角保存", LENGTH_SHORT).show();
                }

                @Override
                public void onCancelClick(MaterialDialog dialog) {
                    dialog.dismiss();
                }
            });
        } else {
            api.editPatientInfo(binding.etName.getText().toString(), binding.etEmail.getText().toString(),
                    binding.tvBirthday.getText().toString(), getGender(), avatar).enqueue(new SimpleCallback<Patient>() {
                @Override
                protected void handleResponse(Patient response) {
                    setIsEditMode(!isEditMode());
                    Toast.makeText(EditPatientInfoActivity.this, "保存成功,请耐心等待资料审核", LENGTH_SHORT).show();
                    Intent intent = PMainActivity2.intentFor(EditPatientInfoActivity.this);
                    startActivity(intent);
                    if (SDK_INT >= JELLY_BEAN) {
                        finishAffinity();
                    } else {
                        finish();
                    }
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


    @Override
    public int getMidTitle() {
        return R.string.title_profile_info;
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
                onMenuClicked();
                break;
            }
            case R.id.action_edit: {
                onMenuClicked();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.clear();
        if (isEditMode()) {
            getMenuInflater().inflate(R.menu.menu_save, menu);
        } else {
            getMenuInflater().inflate(R.menu.menu_edit, menu);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        if (isEditMode()) {
            TwoChoiceDialog.show(this, "修改将不会被保存，确定退出编辑吗？", "取消", "确定", new TwoChoiceDialog.Options() {
                @Override
                public void onApplyClick(MaterialDialog dialog) {
                    EditPatientInfoActivity.super.onBackPressed();
                }

                @Override
                public void onCancelClick(MaterialDialog dialog) {
                    dialog.dismiss();
                }
            });
        } else {
            super.onBackPressed();
        }
    }
}
