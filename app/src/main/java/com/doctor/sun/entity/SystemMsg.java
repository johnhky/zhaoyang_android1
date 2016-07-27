package com.doctor.sun.entity;

import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.support.annotation.NonNull;

import com.doctor.sun.R;
import com.doctor.sun.Settings;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.entity.constans.AppointmentType;
import com.doctor.sun.entity.im.TextMsg;
import com.doctor.sun.ui.activity.doctor.AfterServiceActivity;
import com.doctor.sun.ui.activity.doctor.AppointmentListActivity;
import com.doctor.sun.ui.activity.doctor.ConsultingActivity;
import com.doctor.sun.ui.activity.doctor.EditDoctorInfoActivity;
import com.doctor.sun.ui.activity.patient.MedicineStoreActivity;
import com.doctor.sun.ui.activity.patient.PAfterServiceActivity;
import com.doctor.sun.ui.activity.patient.PAppointmentListActivity;
import com.doctor.sun.ui.activity.patient.PConsultingActivity;
import com.doctor.sun.ui.activity.patient.SearchDoctorActivity;
import com.doctor.sun.ui.activity.patient.handler.SystemMsgHandler;
import com.doctor.sun.ui.adapter.ViewHolder.LayoutId;
import com.doctor.sun.ui.adapter.ViewHolder.SortedItem;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.ganguo.library.Config;
import io.ganguo.library.core.event.Event;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.annotations.Ignore;

/**
 * Created by lucas on 1/29/16.
 * 系统信息
 */
public class SystemMsg extends BaseObservable implements LayoutId, SortedItem, Event {


    /**
     * type : 23
     */

    @JsonProperty("type")
    public int type;
    /**
     * title : 【昭阳医生】提醒:刘医生提醒您完善问卷，请及时登录处理。
     * doctor_name : 刘医生
     * doctor_avatar : http://7xkt51.com2.z0.glb.qiniucdn.com/FjOdeBEi-6FnkhgIx8wMtUq475gZ
     * patient_avatar : null
     */

    @JsonProperty("title")
    private String title;
    @JsonProperty("doctor_name")
    private String doctorName;
    @JsonProperty("doctor_avatar")
    private String doctorAvatar;
    @JsonProperty("patient_avatar")
    private String patientAvatar;
    @JsonProperty("created_at")
    private String createdAt;
    @JsonProperty("patient_name")
    private Object patientName;

    @Ignore
    private int itemLayoutId = R.layout.p_item_system_msg;

    @Override
    public int getItemLayoutId() {
        return itemLayoutId;
    }

    public void setItemLayoutId(int itemLayoutId) {
        this.itemLayoutId = itemLayoutId;
    }
    /*@Override
    public int getItemLayoutId() {
        return R.layout.p_item_system_tip;
    }*/

    @Ignore
    private SystemMsgHandler handler = new SystemMsgHandler(this);


    public SystemMsgHandler getHandler() {
        return handler;
    }

    public void setHandler(SystemMsgHandler handler) {
        this.handler = handler;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public void setDoctorAvatar(String doctorAvatar) {
        this.doctorAvatar = doctorAvatar;
    }

    public void setPatientAvatar(String patientAvatar) {
        this.patientAvatar = patientAvatar;
    }

    public String getTitle() {
        return title;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public String getDoctorAvatar() {
        return doctorAvatar;
    }

    public String getPatientAvatar() {
        return patientAvatar;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }


    public void setPatientName(Object patientName) {
        this.patientName = patientName;
    }

    public Object getPatientName() {
        return patientName;
    }

    public void itemClick(Context context) {
        Intent i = null;
        boolean isDoctor = Settings.isDoctor();
        switch (type) {
            case 1: {
                if (isDoctor) {
                    i = ConsultingActivity.makeIntent(context);
                } else {
                    i = PConsultingActivity.makeIntent(context);
                }
                break;
            }
            case 6:
            case 8:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 19:
            case 23:
            case 20: {
                if (isDoctor) {
                    i = AppointmentListActivity.makeIntent(context);
                } else {
                    i = PAppointmentListActivity.makeIntent(context);
                }
                break;
            }
            case 7: {
                i = MedicineStoreActivity.makeIntent(context);
                break;
            }
            case 21: {
                if (isDoctor) {
                    i = EditDoctorInfoActivity.makeIntent(context, Settings.getDoctorProfile());
                }
                break;
            }
            case 22: {
                if (!isDoctor) {
                    i = SearchDoctorActivity.makeIntent(context, AppointmentType.PREMIUM);
                }
                break;
            }
            case 24: {
                if (isDoctor) {
                    i = AfterServiceActivity.intentFor(context);
                } else {
                    i = PAfterServiceActivity.intentFor(context);
                }
                break;
            }
            default: {
                if (isDoctor) {
                    i = ConsultingActivity.makeIntent(context);
                } else {
                    i = PConsultingActivity.makeIntent(context);
                }
            }
        }
        if (i != null) {
            context.startActivity(i);
        }
    }

    @Override
    public int getLayoutId() {
        return getItemLayoutId();
    }

    @Override
    public long getCreated() {
        return Long.MAX_VALUE;
    }

    @Override
    public String getKey() {
        return "SYSTEM_MSG";
    }

    public static String getConfigKey() {
        return "SYSTEM_MSG" + Config.getString(Constants.VOIP_ACCOUNT);
    }

    public static long getUnreadMsgCount() {
        return getAllMsg(Realm.getDefaultInstance()).equalTo("haveRead", false).count();
    }

    public static String getLastMsg() {
        RealmResults<TextMsg> all = getAllMsg(Realm.getDefaultInstance()).findAll();
        if (all.isEmpty()) {
            return "";
        }
        return all.last().getBody();
    }

    public static void setLastMsg(final String msg, final int notificationId) {
        Realm.getDefaultInstance().executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                TextMsg msg1 = new TextMsg();
                msg1.setMsgId(String.valueOf(notificationId));
                msg1.setSessionId(getConfigKey());
                msg1.setHaveRead(false);
                msg1.setBody(msg);
                msg1.setTime(System.currentTimeMillis());
                Realm.getDefaultInstance().copyToRealm(msg1);
            }
        });
    }

    public static void reset() {
        Realm.getDefaultInstance().executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                for (TextMsg msg : getAllMsg(realm).findAll()) {
                    msg.setHaveRead(true);
                }
            }
        });
    }

    @NonNull
    public static RealmQuery<TextMsg> getAllMsg(Realm realm) {
        return realm.where(TextMsg.class).equalTo("sessionId", getConfigKey());
    }


    public void registerMsgsChangedListener() {
        getAllMsg(Realm.getDefaultInstance()).findAll().addChangeListener(new RealmChangeListener<RealmResults<TextMsg>>() {
            @Override
            public void onChange(RealmResults<TextMsg> element) {
                notifyChange();
            }
        });
    }
}
