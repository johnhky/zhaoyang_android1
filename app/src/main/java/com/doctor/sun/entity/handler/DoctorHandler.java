package com.doctor.sun.entity.handler;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v4.app.ActivityOptionsCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.StackingBehavior;
import com.doctor.sun.R;
import com.doctor.sun.Settings;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.dto.PatientDTO;
import com.doctor.sun.entity.Doctor;
import com.doctor.sun.entity.MyPatient;
import com.doctor.sun.entity.constans.AppointmentType;
import com.doctor.sun.event.RefreshEvent;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.module.AfterServiceModule;
import com.doctor.sun.module.ProfileModule;
import com.doctor.sun.module.ToolModule;
import com.doctor.sun.ui.activity.DoctorDetailActivity2;
import com.doctor.sun.ui.activity.patient.AllowAfterServiceActivity;
import com.doctor.sun.ui.activity.patient.HospitalDetailActivity;
import com.doctor.sun.ui.adapter.SimpleAdapter;
import com.doctor.sun.ui.adapter.ViewHolder.BaseViewHolder;
import com.doctor.sun.ui.adapter.core.AdapterConfigKey;
import com.doctor.sun.ui.adapter.core.BaseListAdapter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.HashMap;

import io.ganguo.library.core.event.EventHub;

/**
 * Created by lucas on 1/8/16.
 */
public class DoctorHandler {
    private Doctor data;
    private boolean isSelected;
    private boolean hasSharedTransition;
    public ProfileModule api = Api.of(ProfileModule.class);
    private MyPatient mPatient;

    public DoctorHandler(MyPatient patient) {
        this.mPatient = patient;
    }

    public DoctorHandler(Doctor doctorDTO) {
        data = doctorDTO;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public void hasSharedTransition(boolean hasSharedTransition) {
        this.hasSharedTransition = hasSharedTransition;
    }

    public boolean isSelected(SimpleAdapter adapter, BaseViewHolder vh) {
        Doctor doctor = (Doctor) adapter.get(vh.getAdapterPosition());
        return doctor.isUserSelected();
    }


    public void detail(Context context, View view) {
        Intent intent = DoctorDetailActivity2.makeIntent(context, data);

        if (hasSharedTransition && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptionsCompat options = ActivityOptionsCompat.
                    makeSceneTransitionAnimation((Activity) view.getContext(), view, view.getTransitionName());
            context.startActivity(intent, options.toBundle());
        } else {
            context.startActivity(intent);
        }
    }

    @JsonIgnore
    public String getCareerInfo() {
        return data.getHospitalName() + "/" + data.getSpecialist() + "/" + data.getTitle();
    }

    @JsonIgnore
    public String getFee(@AppointmentType int type) {
        if (type == AppointmentType.PREMIUM) {
            return getDetailFee();
        } else {
            return getQuickFee();
        }
    }

    @JsonIgnore
    public String getDetailFee() {
        String fee = data.getMoney() + "元/次/15分钟";
        return fee;
    }

    @JsonIgnore
    public String getQuickFee() {
        String fee = data.getSecondMoney() + "元/次";
        return fee;
    }

    @JsonIgnore
    public String getSpecial() {
        String specialist;
        specialist = "专长病种:" + data.getSpecialist();
        return specialist;
    }


    public void itemClick(View v) {
        Intent intent = new Intent();
        intent.putExtra(Constants.DATA, data);
        Activity activity = (Activity) v.getContext();
        Messenger messenger = activity.getIntent().getParcelableExtra(Constants.HANDLER);
        if (messenger != null) {
            try {
                Message message = new Message();
                Bundle bundle = new Bundle();
                bundle.putParcelable(Constants.DATA, data);
                message.setData(bundle);
                message.what = Constants.DOCTOR_REQUEST_CODE;
                messenger.send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        activity.setResult(Activity.RESULT_OK, intent);
        activity.finish();
    }

    public void viewDetail(Context context) {
//        Intent intent = DoctorDetailActivity.makeIntent(context, data, type);
//        context.startActivity(intent);
        Intent intent = new Intent(context, DoctorDetailActivity2.class);
        intent.putExtra(Constants.DATA, data);
        context.startActivity(intent);
        /*
        if (hasSharedTransition && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptionsCompat options = ActivityOptionsCompat.
                    makeSceneTransitionAnimation((Activity) context, view, view.getTransitionName());
            context.startActivity(intent, options.toBundle());
        } else {
            context.startActivity(intent);
        }*/
    }

    public void viewDetailIfIsPatient(Context context) {
        if (!Settings.isDoctor()) {
            Intent intent = DoctorDetailActivity2.makeIntent(context, data);
            context.startActivity(intent);
        }
    }

//
//    public void pickDate(final Context context, final int type) {
//        SelectRecordDialog.showRecordDialog(context, new SelectRecordDialog.SelectRecordListener() {
//            @Override
//            public void onSelectRecord(SelectRecordDialog dialog, MedicalRecord record) {
//                data.setRecordId(String.valueOf(record.getMedicalRecordId()));
//                Intent intent = PickDateActivity.intentFor(context, data, type);
//                context.startActivity(intent);
//                dialog.dismiss();
//            }
//        });
//    }

    public int getType(SimpleAdapter adapter) {
//        n adapter.getType();
        return adapter.getInt(AdapterConfigKey.APPOINTMENT_TYPE);
    }

    public String getTypeLabel(SimpleAdapter adapter) {
        if (getType(adapter) == AppointmentType.PREMIUM) {
            return "VIP网诊";
        } else {
            return "简易复诊";
        }
    }


    @JsonIgnore
    public boolean getDetailVisible() {
        return !data.getDetail().equals("") && data.getDetail() != null;
    }

    public int getDefaultAvatar() {
        int result;
        if (data.getGender() == 0) {
            result = R.drawable.female_doctor_avatar;
        } else {
            result = R.drawable.male_doctor_avatar;
        }
        return result;
    }

    public void toggleFav(final Context context, final Doctor doctor) {
        ToolModule api = Api.of(ToolModule.class);
        if ("1".equals(doctor.getIsFav())) {
            api.unlikeDoctor(doctor.getId()).enqueue(new SimpleCallback<String>() {
                @Override
                protected void handleResponse(String response) {
                    //改状态,不然下次点击还是会跑到这里,但是医生已经取消收藏了.
                    doctor.setIsFav("0");
                    doctor.notifyChange();
                    Toast.makeText(context, "取消收藏医生", Toast.LENGTH_SHORT).show();
                }

            });
        } else {
            api.likeDoctor(doctor.getId()).enqueue(new SimpleCallback<String>() {
                @Override
                protected void handleResponse(String response) {
                    //改状态,不然下次点击还是会跑到这里,但是医生已经收藏过了.
                    doctor.setIsFav("1");
                    doctor.notifyChange();
                    Toast.makeText(context, "成功收藏医生", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void hospital(Context context, Doctor data) {
        Intent intent = HospitalDetailActivity.makeIntent(context, data);
        context.startActivity(intent);
    }

    public void acceptRelation(final SimpleAdapter adapter, String id, final Context context) {
        AfterServiceModule api = Api.of(AfterServiceModule.class);
        api.acceptBuildRelation(id).enqueue(new SimpleCallback<String>() {
            @Override
            protected void handleResponse(String response) {
                Toast.makeText(context, "成功建立随访关系", Toast.LENGTH_SHORT).show();
                ProfileModule api = Api.of(ProfileModule.class);
                api.patientProfile().enqueue(new SimpleCallback<PatientDTO>() {
                    @Override
                    protected void handleResponse(PatientDTO response) {
                        Settings.setPatientProfile(response);
                    }
                });
            }
        });
        EventHub.post(new RefreshEvent());
    }

    public View.OnClickListener allowAfterService(final BaseListAdapter adapter, final BaseViewHolder vh) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = AllowAfterServiceActivity.intentFor(v.getContext(), data);
                v.getContext().startActivity(intent);
            }
        };
    }

    public boolean canWritePrescription() {
        return "执业医师认证".equals(data.getLevel());
    }

    public HashMap<String, String> toHashMap() {
        HashMap<String, String> result = new HashMap<String, String>();
        result.put("name", data.getName() == null ? "" : data.getName());
        result.put("email", data.getEmail() == null ? "" : data.getEmail());
        result.put("gender", String.valueOf(data.getGender()));
        result.put("avatar", data.getAvatar() == null ? "" : data.getAvatar());
        result.put("specialist", data.getSpecialist() == null ? "" : data.getSpecialist());
        result.put("title", data.getTitle() == null ? "" : data.getTitle());
        result.put("titleImg", data.getTitleImg() == null ? "" : data.getTitleImg());
        result.put("practitionerImg", data.getPractitionerImg() == null ? "" : data.getPractitionerImg());
        result.put("certifiedImg", data.getCertifiedImg() == null ? "" : data.getCertifiedImg());
        result.put("hospitalPhone", data.getHospitalPhone() == null ? "" : data.getHospitalPhone());
        result.put("detail", data.getDetail() == null ? "" : data.getDetail());
        result.put("hospital", data.getHospitalName() == null ? "" : data.getHospitalName());
        return result;
    }

    public int isVisible(SimpleAdapter adapter) {
        if (adapter.getBoolean(AdapterConfigKey.IS_EDIT_MODE)) {
            return View.VISIBLE;
        } else {
            return View.GONE;
        }
    }

    public void select(SimpleAdapter adapter, BaseViewHolder vh) {
        int position = vh.getAdapterPosition();
        Doctor doctor = (Doctor) adapter.get(position);
        ((Doctor) adapter.get(position)).setUserSelected(!doctor.isUserSelected());
        adapter.notifyDataSetChanged();
    }

    public String getMyPatientName() {
        String name = "";
        if (null != mPatient.getPatient()) {
            if (!TextUtils.isEmpty(mPatient.getPatient().getName())) {
                name = mPatient.getPatient().getName();
            } else {
                if (mPatient.getPatient().getRecord_names().size() > 0) {
                    name = mPatient.getPatient().getRecord_names().get(0);
                }
            }
        }
        return name;
    }

    public String getReordName() {
        StringBuilder builder = new StringBuilder();
        if (null != mPatient.getPatient()) {
            if (mPatient.getPatient().getRecord_names().size() > 0) {
                for (int i = 0; i < mPatient.getPatient().getRecord_names().size(); i++) {
                    builder.append(mPatient.getPatient().getRecord_names().get(i));
                    builder.append("/");
                }
                builder.deleteCharAt(builder.length() - 1);
            } else {
                builder.append("暂未填写");
            }
        }
        return builder.toString();
    }

    public String getIntroType() {
        String typeText = "";
        if (mPatient.getType() == 1) {
            typeText = "扫码时间: ";
        } else {
            typeText = "订单支付时间: ";
        }
        return typeText;
    }

    public String getNetType() {
        String text = "";
        if (data.getIsOpen().isNetwork() == true) {
            text = "VIP网诊" + " ￥" + data.getMoney() + "/" + data.getNetworkMinute() + "分钟";
        } else {
            text = "VIP网诊";
        }
        return text;
    }

    public String getSimpleType(TextView view) {
        String text = "";
        if (data.getIsOpen().isSimple() == true) {
            if (null != data.getCoupons()) {
                if (null != data.getCoupons().getSimple()) {
                    if (data.getCoupons().getSimple().couponStatus.equals("available")) {
                        text = "￥" + data.getSecondMoney();
                        view.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                    } else {
                        text = "  ￥" + data.getSecondMoney();
                    }
                } else {
                    text = "  ￥" + data.getSecondMoney();
                }
            } else {
                text = "  ￥" + data.getSecondMoney();
            }
        }
        return text;
    }

    public String getSurfaceType() {
        String text = "";
        if (data.getIsOpen().isSurface() == true) {
            text = "诊所面诊" + " ￥" + data.getSurfaceMoney() + "/" + data.getSurfaceMinute() + "分钟";
        } else {
            text = "诊所面诊";
        }
        return text;
    }

    public int isVisiblity() {
        if (data.getIsOpen().isSimple() == true) {
            if (null != data.getCoupons()) {
                if (null != data.getCoupons().getSimple()) {
                    return View.VISIBLE;
                } else {
                    return View.GONE;
                }
            } else {
                return View.GONE;
            }
        } else {
            return View.GONE;
        }

    }

    public void showDoctorDetail(Context context) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(context);
        builder.stackingBehavior(StackingBehavior.ALWAYS)
                .btnStackedGravity(GravityEnum.CENTER)
                .titleGravity(GravityEnum.CENTER)
                .title("医生简介").content(data.getDetail())
                .neutralText("关闭")
                .show();
    }
}
