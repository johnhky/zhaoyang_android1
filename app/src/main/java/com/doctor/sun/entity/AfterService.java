package com.doctor.sun.entity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.StringDef;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.doctor.sun.R;
import com.doctor.sun.Settings;
import com.doctor.sun.entity.constans.AppointmentType;
import com.doctor.sun.entity.constans.Gender;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.module.AfterServiceModule;
import com.doctor.sun.ui.activity.doctor.AfterServiceDoingActivity;
import com.doctor.sun.ui.activity.doctor.AfterServiceDoneActivity;
import com.doctor.sun.ui.activity.doctor.ChattingActivity;
import com.doctor.sun.ui.adapter.ViewHolder.LayoutId;
import com.doctor.sun.ui.adapter.core.BaseAdapter;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by rick on 2/6/2016.
 */
public class AfterService implements LayoutId {

    /**
     * id : 1
     * doctor_id : 2
     * record_id : 1
     * patient_id : 6
     * deadline_time : 2016-07-01
     * status : todo
     * doctor : {"id":"2","name":"waymen医生","email":"waymen@waymen.com","level":"咨询/治疗师认证","gender":"1","city":"","hospital_id":"2","specialist":"妇科","title":"副主任医师","money":"50","second_money":"20","hospital_phone":"","detail":"专业治疗各种敏感疑难杂症很多经济学家肯德基细菌学家顶焦度计先进典型经济学家大家都觉得看看书忽东忽西是解决自身建设计划下进行教学基地进行冬季新款夏季丢盔卸甲继续亟待解决的星级大酒店小姐持续下降的禁吸戒毒独具匠心。","avatar":"http://7xkt51.com2.z0.glb.qiniucdn.com/FlVGcfPFlwXY12_NjDDulah-nOqq","need_review":"0","title_img":"http://pic.baike.soso.com/p/20140401/20140401145749-1321596626.jpg","practitioner_img":"http://pic.baike.soso.com/p/20140401/20140401145749-1321596626.jpg","certified_img":"http://pic.baike.soso.com/p/20140401/20140401145749-1321596626.jpg","point":"3","hospital_name":"私人诊所","voipAccount":"8009850000000002","phone":"15918748284","yunxin_accid":"2"}
     * questionnaire_name : 常规随访问卷
     * questionnaire_progress : 0/10
     * load_patient : 1
     * created_at : 2016-06-03 18:48:59
     */

    @JsonProperty("id")
    public String id;
    @JsonProperty("doctor_id")
    public String doctorId;
    @JsonProperty("record_id")
    public String recordId;
    @JsonProperty("patient_id")
    public String patientId;
    @JsonProperty("deadline_time")
    public String deadlineTime;
    @JsonProperty("status")
    public String status;
    @JsonProperty("questionnaire_name")
    public String questionnaireName;
    @JsonProperty("questionnaire_progress")
    public String questionnaireProgress;
    @JsonProperty("load_patient")
    public int loadPatient;
    @JsonProperty("created_at")
    public String createdAt;

    @JsonProperty("tid")
    public int tid;

    @JsonProperty("doctor")
    public Doctor doctor;

    @JsonProperty("record")
    public MedicalRecord record;


    @Override
    public int getItemLayoutId() {
        return R.layout.item_after_service;
    }

    public String getStatusLabel() {
        switch (status) {
            case Status.TODO: {
                return "申请中";
            }
            case Status.DOING: {
                return "进行中";
            }
            case Status.REJECTED: {
                return "已拒绝";
            }
            case Status.CLOSED: {
                return "已关闭";
            }
            case Status.FINISHED: {
                return "已完成";
            }
            case Status.LOCKED: {
                return "问卷已锁定";
            }
        }
        return status;
    }


    @ColorInt
    public int getStatusTextColor() {
        switch (status) {
            case Status.TODO: {
                return Color.parseColor("#ff8e43");
            }
            case Status.DOING: {
                return Color.parseColor("#88cb5a");
            }
            case Status.REJECTED: {
                return Color.parseColor("#898989");
            }
            case Status.CLOSED: {
                return Color.parseColor("#898989");
            }
            case Status.FINISHED: {
                return Color.parseColor("#363636");
            }
            case Status.LOCKED: {
                return Color.parseColor("#FFFFFF");
            }
        }
        return 0;
    }


    public int getBackgroundColor() {
        switch (status) {

            case Status.LOCKED: {
                return R.drawable.shape_status;
            }
            default: {
                return R.drawable.bg_transparent;
            }
        }
    }

    public String physiologicalInfo() {
        return record.getAge() + "岁/" + (record.getGender() == Gender.MALE ? "男" : "女");
    }

    public String relation() {
        return record.getName() + "(" + record.getPatientName() + "的" + record.getRelation() + ")";
    }

    public boolean isTodoBtnVisible() {
        return Status.TODO.equals(status);
    }

    public boolean isDoingBtnVisible() {
        return true;
    }

    public boolean isBtnVisible() {
        return isTodoBtnVisible() || isDoingBtnVisible();
    }

    public int getDividerColor() {
        if (isBtnVisible()) {
            return R.color.divider_color;
        } else {
            return R.color.transparent;
        }
    }

    public void updateAddress(final Context context, String address, int id) {
        AfterServiceModule api = Api.of(AfterServiceModule.class);
        api.updateAddress(address, id).enqueue(new SimpleCallback<String>() {
            @Override
            protected void handleResponse(String response) {
                Toast.makeText(context, "保存地址成功", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void reject(final BaseAdapter adapter, final RecyclerView.ViewHolder vh) {
        performAction(Actions.REJECT, new ItemChangedCallback(adapter, vh, Status.REJECTED));
    }

    public void accept(final BaseAdapter adapter, final RecyclerView.ViewHolder vh) {
        performAction(Actions.ACCEPT, new ItemChangedCallback(adapter, vh, Status.DOING));
    }

    public void fillForum(Context context, String id) {
        int position = 0;
        if (Settings.isDoctor()) {
            position = 1;
        }
        switch (status) {
            case Status.DOING: {
                Intent intent = AfterServiceDoingActivity.intentFor(context, id, recordId, position);
                context.startActivity(intent);
                break;
            }
            default: {
                Intent intent = AfterServiceDoneActivity.intentFor(context, id, position);
                context.startActivity(intent);
            }
        }
    }

    public void chatting(Context context) {
        Appointment appointment = new Appointment();
        appointment.setId(Integer.parseInt(id));
        String statusLabel = getStatusLabel();
        appointment.setStatus(statusLabel);
        appointment.setOrderStatus(statusLabel);
        appointment.setTid(tid);
        if (Settings.isDoctor()) {
            appointment.setUrgentRecord(record);
            appointment.setRecordId(record.getMedicalRecordId());
            appointment.setDoctor(doctor);
        } else {
            appointment.setUrgentRecord(record);
            appointment.setRecordId(record.getMedicalRecordId());
            appointment.setDoctor(doctor);
        }
        appointment.setDisplayStatus(statusLabel);
        appointment.setOrderStatus(statusLabel);
        appointment.setDisplayType("诊后随访");
        appointment.setAppointmentType(AppointmentType.AFTER_SERVICE);
        Intent intent = ChattingActivity.makeIntent(context, appointment);
        context.startActivity(intent);
    }

    public void viewDetail(Context context, String id) {
        Intent intent = AfterServiceDoneActivity.intentFor(context, id, 0);
        context.startActivity(intent);
    }

    private class ItemChangedCallback extends SimpleCallback<Void> {

        private final BaseAdapter adapter;
        private final RecyclerView.ViewHolder vh;
        private String targetStatus;

        public ItemChangedCallback(BaseAdapter adapter, RecyclerView.ViewHolder vh, String target) {
            this.vh = vh;
            this.adapter = adapter;
            this.targetStatus = target;
        }

        @Override
        protected void handleResponse(Void response) {
            status = targetStatus;
            adapter.notifyItemChanged(vh.getAdapterPosition());
        }
    }

    public void performAction(@Actions String action, SimpleCallback<Void> callback) {
        Api.of(AfterServiceModule.class).perform(id, action).enqueue(callback);
    }

    public boolean isFinished(String status) {
        return !Status.DOING.equals(status);
    }

    @StringDef
    public @interface Status {
        String ALL = "disableIfOneSelected";
        String TODO = "todo";
        String DOING = "doing";
        String REJECTED = "rejected";
        String CLOSED = "closed";
        String FINISHED = "finished";
        String LOCKED = "locked";
    }

    @StringDef
    public @interface Actions {
        String ACCEPT = "accept";
        String REJECT = "reject";
    }

}

