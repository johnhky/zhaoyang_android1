package com.doctor.sun.ui.adapter;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.View;

import com.doctor.sun.R;
import com.doctor.sun.Settings;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.ItemRLegacyPrescriptionBinding;
import com.doctor.sun.databinding.ItemRPrescriptionBinding;
import com.doctor.sun.databinding.MsgPrescriptionListBinding;
import com.doctor.sun.dto.PrescriptionDTO;
import com.doctor.sun.entity.Doctor;
import com.doctor.sun.entity.LegacyPrescriptionDTO;
import com.doctor.sun.entity.Patient;
import com.doctor.sun.entity.handler.AppointmentHandler2;
import com.doctor.sun.entity.im.TextMsg;
import com.doctor.sun.entity.im.TextMsgFactory;
import com.doctor.sun.im.cache.NimUserInfoCache;
import com.doctor.sun.immutables.Appointment;
import com.doctor.sun.immutables.Prescription;
import com.doctor.sun.immutables.SimpleAppointment;
import com.doctor.sun.module.AuthModule;
import com.doctor.sun.ui.activity.ImagePreviewActivity;
import com.doctor.sun.ui.adapter.ViewHolder.BaseViewHolder;
import com.doctor.sun.util.JacksonUtils;
import com.doctor.sun.vm.LayoutId;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import io.ganguo.library.Config;

/**
 * Created by rick on 12/15/15.
 */
public class MessageAdapter extends SimpleAdapter<LayoutId, ViewDataBinding> {
    private String myAvatar;
    private String yourAvatar;
    private String yourName;
    private boolean shouldUpdate;
    private long finishedTime;

    public MessageAdapter(Appointment data) {
        super();
        initData(data);
    }

    public MessageAdapter(String my, String your) {
        super();
        initData(my, your);
    }

    /**
     * 初始化头像数据
     *
     * @param data
     */
    private void initData(Appointment data) {
        this.finishedTime = AppointmentHandler2.getFinishedTime(data);
        switch (Config.getInt(Constants.USER_TYPE, -1)) {
            case AuthModule.PATIENT_TYPE: {
                Doctor doctor = data.getDoctor();
                Patient dto = Settings.getPatientProfile();
                yourAvatar = doctor.getAvatar();
                yourName = doctor.getName();
                if (dto != null) {
                    myAvatar = dto.getAvatar();
                } else {
                    myAvatar = "";
                }
                break;
            }
            default: {
                Doctor doctor = Settings.getDoctorProfile();
                yourAvatar = data.getRecord().getPatientAvatar();
                yourName = data.getRecord().getPatientName();
                if (doctor != null) {
                    myAvatar = doctor.getAvatar();
                } else {
                    myAvatar = "";
                }
            }
        }
    }

    /**
     * 初始化头像数据
     */
    private void initData(String my, String your) {
        this.finishedTime = Long.MAX_VALUE;
        Patient patientProfile = Settings.getPatientProfile();
        if (patientProfile != null) {
            myAvatar = patientProfile.getAvatar();
        }else {
            myAvatar = "";
        }

        NimUserInfoCache.getInstance().getUserInfoFromRemote(your, new RequestCallback<NimUserInfo>() {
            @Override
            public void onSuccess(NimUserInfo nimUserInfo) {
                yourAvatar = nimUserInfo.getAvatar();
                yourName = nimUserInfo.getName();
                notifyDataSetChanged();
            }

            @Override
            public void onFailed(int i) {

            }

            @Override
            public void onException(Throwable throwable) {

            }
        });
    }

    public String getMyAvatar() {
        return myAvatar;
    }

    public String getYourAvatar() {
        return yourAvatar;
    }

    public String getYourName() {
        return yourName;
    }

    public void setYourName(String yourName) {
        this.yourName = yourName;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder vh, int position) {
        super.onBindViewHolder(vh, position);
        switch (vh.getItemViewType()) {
            case R.layout.msg_prescription_list: {
                MsgPrescriptionListBinding binding = (MsgPrescriptionListBinding) vh.getBinding();
                binding.prescription.removeAllViews();
                TextMsg textMsg = (TextMsg) get(position);
                String body = textMsg.attachmentData(TextMsgFactory.BODY);
                if (body == null || body.equals("")) {
                    return;
                }
                String attachmentType = textMsg.attachmentData(TextMsgFactory.ATTACHMENT_TYPE);
                if (attachmentType.equals(String.valueOf(TextMsg.DrugV2))) {
                    PrescriptionDTO prescriptionDTO = JacksonUtils.fromJson(body, PrescriptionDTO.class);
                    if (prescriptionDTO == null) return;
                    SimpleAppointment appointment = prescriptionDTO.getAppointmentInfo();
                    if (appointment != null) {
                        binding.name.setText(String.format("%s  %s", appointment.getRecord_name(), appointment.getRelation()));
                        binding.time.setText(String.format("%s  %s", appointment.getBook_time(), appointment.getDisplay_type()));
                    }

                    if (prescriptionDTO.getDrug() == null) return;
                    for (Prescription prescription : prescriptionDTO.getDrug()) {
                        ItemRPrescriptionBinding item = DataBindingUtil.inflate(getInflater(vh.itemView.getContext()), R.layout.item_r_prescription, binding.prescription, true);
                        item.setData(prescription);
                    }
                } else if (attachmentType.equals(String.valueOf(TextMsg.Drug))) {
                    LegacyPrescriptionDTO prescriptionDTO = JacksonUtils.fromJson(body, LegacyPrescriptionDTO.class);
                    if (prescriptionDTO == null) return;
                    LegacyPrescriptionDTO.AppointmentInfoEntity appointment = prescriptionDTO.getAppointment_info();
                    if (appointment != null) {
                        binding.name.setText(String.format("%s  %s", appointment.getRecord_name(), appointment.getRelation()));
                        binding.time.setText(String.format("%s  %s", appointment.getBook_time(), appointment.getDisplay_type()));
                    }

                    if (prescriptionDTO.getDrug() == null) return;
                    for (LegacyPrescriptionDTO.Prescription prescription : prescriptionDTO.getDrug()) {
                        ItemRLegacyPrescriptionBinding item = DataBindingUtil.inflate(getInflater(vh.itemView.getContext()), R.layout.item_r_legacy_prescription, binding.prescription, true);
                        item.setData(prescription);
                    }
                }

            }
        }
    }

    public String selectedTime(long time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTimeInMillis(time);
        String format = simpleDateFormat.format(calendar.getTime());
        return "用户于" + format + "选择处方";
    }

    public View.OnClickListener previewImage(final String url) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = ImagePreviewActivity.makeIntent(v.getContext(), url);
                v.getContext().startActivity(intent);
            }
        };
    }

    public boolean timeVisible(BaseViewHolder vh) {
        int adapterPosition = vh.getAdapterPosition();
        if (adapterPosition == 0) return false;
        if (adapterPosition + 1 >= size()) return true;

        TextMsg thisMsg = (TextMsg) get(adapterPosition);
        TextMsg otherMsg = (TextMsg) get(adapterPosition + 1);

        return thisMsg.getTime() - otherMsg.getTime() > 1000 * 60 * 5;
    }

    public boolean isFinished(long msgTime) {
        if (Settings.isDoctor()) {
            return false;
        } else {
            return false;
//            return finishedTime < System.currentTimeMillis() && msgTime > finishedTime;
        }
    }

    public void setFinishedTime(long finishedTime) {
        this.finishedTime = finishedTime;
    }
}
