package com.doctor.sun.entity;

import android.content.Context;
import android.widget.Toast;

import com.doctor.sun.entity.constans.Gender;
import com.doctor.sun.entity.handler.AppointmentHandler2;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.module.ProfileModule;
import com.doctor.sun.vo.BaseItem;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;

/**
 * Created by rick on 2/9/2016.
 */

public class FollowUpInfo extends BaseItem {

    /**
     * record_id : 122
     * created_at : 2016-09-02 11:45:51
     * status : doing
     * doctor_name : 兽医
     * address :
     * record_name : 蜘蛛精
     * gender : 1
     * age : 24
     * relation : 同事
     * patient_name : 二百
     */

    @JsonProperty("record_id")
    public int recordId;
    @JsonProperty("created_at")
    public String createdAt;
    @JsonProperty("status")
    public int status;
    @JsonProperty("doctor_name")
    public String doctorName;
    @JsonProperty("address")
    public String address;
    @JsonProperty("record_name")
    public String recordName;
    @JsonProperty("gender")
    public int gender;
    @JsonProperty("age")
    public String age;
    @JsonProperty("relation")
    public String relation;
    @JsonProperty("patient_name")
    public String patientName;
    @JsonProperty("birthday")
    public String birthday;


    public boolean isFinished(int status) {
        return AppointmentHandler2.Status.FINISHED == status;
    }


    public String physiologicalInfo() {
        return age + "岁/" + (gender == Gender.MALE ? "男" : "女");
    }


    public void updateAddress(final Context context, String address, int id) {
        ProfileModule api = Api.of(ProfileModule.class);
        HashMap<String, String> medicalRecord = new HashMap<>();
        medicalRecord.put("recordId", String.valueOf(id));
        medicalRecord.put("name", recordName);
        medicalRecord.put("address", address);
        medicalRecord.put("gender", String.valueOf(gender));
        medicalRecord.put("birthday", birthday);
        medicalRecord.put("relation", relation);
        api.editMedicalRecord(medicalRecord).enqueue(new SimpleCallback<MedicalRecord>() {
            @Override
            protected void handleResponse(MedicalRecord response) {
                Toast.makeText(context, "保存地址成功", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
