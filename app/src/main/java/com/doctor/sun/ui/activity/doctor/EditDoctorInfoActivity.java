package com.doctor.sun.ui.activity.doctor;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.ActivityEditDoctorInfoBinding;
import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.entity.Doctor;
import com.doctor.sun.entity.Photo;
import com.doctor.sun.event.CloseDialogEvent;
import com.doctor.sun.http.Api;
import com.doctor.sun.module.ToolModule;
import com.doctor.sun.ui.activity.BaseFragmentActivity2;
import com.doctor.sun.ui.binding.CustomBinding;
import com.doctor.sun.ui.handler.EditDoctorInfoHandler;
import com.doctor.sun.ui.model.HeaderViewModel;
import com.doctor.sun.ui.widget.PickImageDialog;
import com.doctor.sun.ui.widget.TwoSelectorDialog;
import com.doctor.sun.util.PermissionsUtil;

import java.io.File;

import io.ganguo.library.common.ToastHelper;
import io.ganguo.library.core.event.EventHub;
import io.ganguo.library.util.Strings;
import io.ganguo.library.util.log.Logger;
import io.ganguo.library.util.log.LoggerFactory;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * 设置医生个人信息
 * Created by rick on 11/18/15.
 */
public class EditDoctorInfoActivity extends BaseFragmentActivity2 implements EditDoctorInfoHandler.DoctorInfoInput {
    private Logger logger = LoggerFactory.getLogger(EditDoctorInfoActivity.class);
    private ToolModule api = Api.of(ToolModule.class);
    private ActivityEditDoctorInfoBinding binding;
    private HeaderViewModel header;
    private EditDoctorInfoHandler handler;
    private int gender = 0;
    private String avatarImg = "";
    private String titleImg = "";
    private String practitionerImg = "";
    private String certifiedImg = "";
    private Doctor data;

    public static Intent makeIntent(Context context, Doctor data) {
        Intent i = new Intent(context, EditDoctorInfoActivity.class);
        i.putExtra(Constants.DATA, data);
        return i;
    }

    private Doctor getData() {
        return getIntent().getParcelableExtra(Constants.DATA);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    private void initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_doctor_info);
        header = new HeaderViewModel(this);
        header.setMidTitle("个人信息");
        header.setRightTitle("完成");
        binding.setHeader(header);
    }

    private void initData() {
        this.data = getData();
        if (data == null) {
            data = new Doctor();
        }
        binding.setData(data);
        handler = new EditDoctorInfoHandler(this, this.data);
        binding.setHandler(handler);
        if (this.data != null) {
            avatarImg = this.data.getAvatar();
            titleImg = this.data.getTitleImg();
            practitionerImg = this.data.getPractitionerImg();
            certifiedImg = this.data.getCertifiedImg();
        }
        if (!Strings.isEmpty(data.getAvatar()))
            binding.tvAvatar.setVisibility(View.GONE);
        if (!Strings.isEmpty(data.getCertifiedImg())) {
            binding.certifiedImg.setVisibility(View.GONE);
            binding.tvCertified.setVisibility(View.GONE);
        }
        if (!Strings.isEmpty(data.getTitleImg())) {
            binding.titleImg.setVisibility(View.GONE);
            binding.tvTitle.setVisibility(View.GONE);
        }
        if (!Strings.isEmpty(data.getPractitionerImg())) {
            binding.tvPractitioner.setVisibility(View.GONE);
            binding.practitionerImg.setVisibility(View.GONE);
        }
        switch (data.getGender()) {
            case 1:
                binding.rbMale.setChecked(true);
                break;
            case 2:
                binding.rbFemale.setChecked(true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        logger.d("permission result called");
        switch (requestCode) {
            case PermissionsUtil.PERMISSION_REQUEST_CODE:
                isPermissionsGranted(grantResults);
                return;
        }
    }

    @Override
    public void onMenuClicked() {
        handler.done(binding.getRoot());
    }

    @Override
    public Doctor getDoctorInfo() {
        String name = getText(binding.etName);
        String email = getText(binding.etEmail);
        String hospitalName = getText(binding.etHospitalName);
        String specialist = getText(binding.etSpecialist);
        String hospitalPhone = getText(binding.etHospitalPhone);
//     TODO   三张证书
        String detail = getText(binding.etDetail);


        Doctor doctor = new Doctor();
        doctor.setAvatar(avatarImg);
        doctor.setTitleImg(titleImg);
        doctor.setCertifiedImg(certifiedImg);
        doctor.setPractitionerImg(practitionerImg);
        doctor.setName(name);
        doctor.setEmail(email);
        doctor.setHospitalName(hospitalName);
        doctor.setSpecialist(specialist);
        doctor.setHospitalPhone(hospitalPhone);
        doctor.setDetail(detail);
        doctor.setGender(getGender());
        if (isValid(doctor)) {
            return doctor;
        } else {
            return null;
        }
    }

    private boolean isPermissionsGranted(int[] grantResults) {
        if (grantResults.length > 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //授权成功
            ToastHelper.showMessage(this, "授权成功");
            return true;
        } else {
            EventHub.post(new CloseDialogEvent(true));
            TwoSelectorDialog.showTwoSelectorDialog(this, "需要获得相机和读文件的权限才能上传图片", "拒绝", "设置", new TwoSelectorDialog.GetActionButton() {
                @Override
                public void onClickPositiveButton(TwoSelectorDialog dialog) {
                    //去设置
                    PermissionsUtil.startAppSettings(EditDoctorInfoActivity.this);
                    dialog.dismiss();
                }

                @Override
                public void onClickNegativeButton(TwoSelectorDialog dialog) {
                    dialog.dismiss();
                }
            });
        }
        return false;
    }

    private boolean isValid(Doctor doctor) {
//        if (!Strings.isEmail(doctor.getEmail())) {
//            Toast.makeText(EditDoctorInfoActivity.this, "邮箱地址格式错误", Toast.LENGTH_SHORT).show();
//            return false;
//        }
//        if (Strings.isEmpty(doctor.getAvatar())) {
//            Toast.makeText(EditDoctorInfoActivity.this, "头像不能为空", Toast.LENGTH_SHORT).show();
//            return false;
//        }
        if (Strings.isEmpty(doctor.getName())) {
            Toast.makeText(EditDoctorInfoActivity.this, "姓名不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (Strings.isEmpty(doctor.getHospitalName())) {
            Toast.makeText(EditDoctorInfoActivity.this, "所属医院不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (Strings.isEmpty(doctor.getSpecialist())) {
            Toast.makeText(EditDoctorInfoActivity.this, "专科不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
//        if (Strings.isEmpty(doctor.getHospitalPhone()) && Strings.isMobile(doctor.getHospitalPhone())) {
//            Toast.makeText(EditDoctorInfoActivity.this, "医院电话号码格式错误", Toast.LENGTH_SHORT).show();
//            return false;
//        }
        if (doctor.getGender() != Doctor.MALE && doctor.getGender() != Doctor.FEMALE) {
            Toast.makeText(EditDoctorInfoActivity.this, "请选择性别", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public void pickImage(final int id) {
        new PickImageDialog(this, id).show();
    }

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        handleImageRequest(requestCode, resultCode, data);

    }

    private void handleImageRequest(final int requestCode, int resultCode, Intent intent) {
        File file;
        if (resultCode == RESULT_OK) {
            file = PickImageDialog.handleRequest(this, intent, requestCode);
            File to = PickImageDialog.compressImage(file);

            RequestBody body = RequestBody.create(MediaType.parse("multipart/form-intent"), to);
            api.uploadPhoto(body).enqueue(new Callback<ApiDTO<Photo>>() {
                @Override
                public void onResponse(Call<ApiDTO<Photo>> call, Response<ApiDTO<Photo>> response) {
                    if (!response.isSuccessful() || !response.body().getStatus().equals("200")) {
                        return;
                    }
                    String imgUrl = response.body().getData().getUrl();
                    switch (requestCode) {
                        case 1:
                            CustomBinding.loadImage(binding.ivAvatar, imgUrl, null);
                            binding.tvAvatar.setVisibility(View.GONE);
                            data.setAvatar(imgUrl);
                            avatarImg = imgUrl;
                            break;
                        case 2:
                            data.setTitleImg(imgUrl);
                            CustomBinding.loadImage(binding.ivTitle, imgUrl, null);
                            binding.tvTitle.setVisibility(View.GONE);
                            binding.titleImg.setVisibility(View.GONE);
                            titleImg = imgUrl;
                            break;
                        case 3:
                            data.setPractitionerImg(imgUrl);
                            CustomBinding.loadImage(binding.ivPractitioner, imgUrl, null);
                            binding.tvPractitioner.setVisibility(View.GONE);
                            binding.practitionerImg.setVisibility(View.GONE);
                            practitionerImg = imgUrl;
                            break;
                        case 4:
                            data.setCertifiedImg(imgUrl);
                            CustomBinding.loadImage(binding.ivCertified, imgUrl, null);
                            binding.tvCertified.setVisibility(View.GONE);
                            binding.certifiedImg.setVisibility(View.GONE);
                            certifiedImg = imgUrl;
                            break;
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    Log.e(TAG, "upload fail: " + t.getMessage());

                }
            });
        }
    }

    public int getGender() {
        switch (binding.rgGender.getCheckedRadioButtonId()) {
            case R.id.rb_male:
                gender = 1;
                break;
            case R.id.rb_female:
                gender = 2;
                break;
        }
        return gender;
    }
}