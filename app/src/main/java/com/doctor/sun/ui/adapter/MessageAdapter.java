package com.doctor.sun.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.view.View;

import com.doctor.sun.AppContext;
import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.ItemPrescription2Binding;
import com.doctor.sun.databinding.MsgPrescriptionListBinding;
import com.doctor.sun.dto.PatientDTO;
import com.doctor.sun.dto.PrescriptionDTO;
import com.doctor.sun.entity.Appointment;
import com.doctor.sun.entity.Avatar;
import com.doctor.sun.entity.Doctor;
import com.doctor.sun.entity.Prescription;
import com.doctor.sun.entity.im.TextMsg;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.http.callback.TokenCallback;
import com.doctor.sun.module.AuthModule;
import com.doctor.sun.module.ImModule;
import com.doctor.sun.ui.activity.ImagePreviewActivity;
import com.doctor.sun.ui.activity.patient.MedicineStoreActivity;
import com.doctor.sun.ui.adapter.ViewHolder.BaseViewHolder;
import com.doctor.sun.ui.adapter.ViewHolder.LayoutId;
import com.doctor.sun.util.JacksonUtils;

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
    private boolean shouldUpdate;
    private long finishedTime;

    public MessageAdapter(Context context, Appointment data) {
        super(context);
        initData(data);
    }

    public MessageAdapter(MedicineStoreActivity context, String my, String your) {
        super(context);
        initData(my, your);
    }

    /**
     * 初始化头像数据
     *
     * @param data
     */
    private void initData(Appointment data) {
        this.finishedTime = data.getHandler().getFinishedTime();
        switch (Config.getInt(Constants.USER_TYPE, -1)) {
            case AuthModule.PATIENT_TYPE: {
                Doctor doctor = data.getDoctor();
                String json = Config.getString(Constants.PATIENT_PROFILE);
                PatientDTO dto = JacksonUtils.fromJson(json, PatientDTO.class);
                yourAvatar = doctor.getAvatar();
                if (dto != null) {
                    myAvatar = dto.getInfo().getAvatar();
                } else {
                    myAvatar = "";
                }
                break;
            }
            default: {
                Doctor doctor = TokenCallback.getDoctorProfile();
                yourAvatar = data.getAvatar();
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
        ImModule api = Api.of(ImModule.class);
        api.avatar(my, "").enqueue(new SimpleCallback<Avatar>() {
            @Override
            protected void handleResponse(Avatar response) {
                myAvatar = response.getAvatar();
                if (shouldUpdate) {
                    notifyDataSetChanged();
                } else {
                    shouldUpdate = true;
                }
            }
        });
        api.avatar(your, "").enqueue(new SimpleCallback<Avatar>() {
            @Override
            protected void handleResponse(Avatar response) {
                yourAvatar = response.getAvatar();
                if (shouldUpdate) {
                    notifyDataSetChanged();
                } else {
                    shouldUpdate = true;
                }
            }
        });
    }

    public String getMyAvatar() {
        return myAvatar;
    }

    public String getYourAvatar() {
        return yourAvatar;
    }

    @Override
    public void onBindViewBinding(BaseViewHolder<ViewDataBinding> vh, int position) {
        super.onBindViewBinding(vh, position);
        switch (vh.getItemViewType()) {
            case R.layout.msg_prescription_list: {
                MsgPrescriptionListBinding binding = (MsgPrescriptionListBinding) vh.getBinding();
                binding.prescription.removeAllViews();
                TextMsg textMsg = (TextMsg) get(position);
                String body = textMsg.attachmentData("body");
                if (body == null || body.equals("")) {
                    return;
                }
                PrescriptionDTO prescriptionDTO = JacksonUtils.fromJson(body, PrescriptionDTO.class);
                if (prescriptionDTO == null) return;
                Appointment appointment = prescriptionDTO.getAppointmentInfo();
                if (appointment != null) {
                    binding.name.setText(String.format("%s  %s", appointment.getRecordName(), appointment.getRelation()));
                    binding.time.setText(String.format("%s  %s", appointment.getBookTime(), appointment.getDisplayType()));
                }

                if (prescriptionDTO.getDrug() == null) return;
                for (Prescription prescription : prescriptionDTO.getDrug()) {
                    ItemPrescription2Binding item = ItemPrescription2Binding.inflate(getInflater(), binding.prescription, true);
                    item.setData(prescription);
                }
            }
        }
    }

    public String selectedTime(long time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTimeInMillis(time);
        String format = simpleDateFormat.format(calendar.getTime());
        return "用户于" + format + "选择用药";
    }

    @Override
    protected int getItemLayoutId(int position) {
        return super.getItemLayoutId(position);
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
        if (AppContext.isDoctor()) {
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
