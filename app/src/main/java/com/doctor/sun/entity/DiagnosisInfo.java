package com.doctor.sun.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.doctor.sun.immutables.Prescription;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by rick on 12/23/15.
 */
public class DiagnosisInfo implements Parcelable {

    /**
     * id : 108
     * doctor_id : 13
     * appointment_id : 398
     * is_diagnosis : 1
     * perception : {"0":1,"1":0,"2":0,"3":0,"4":0,"otherContent":""}
     * thinking : {"0":1,"1":0,"2":0,"3":0,"4":0,"5":0,"otherContent":""}
     * pipedream : {"0":1,"1":0,"2":0,"3":0,"4":0,"5":0,"otherContent":""}
     * emotion : {"0":1,"1":0,"2":0,"3":0,"4":0,"5":0,"otherContent":""}
     * memory : {"0":1,"1":0,"2":0,"otherContent":""}
     * insight : {"0":0,"1":1,"2":0,"3":0,"4":0,"5":0,"otherContent":"有人要阴我"}
     * description :
     * diagnosis_record : null
     * current_status : 0
     * recovered : 0
     * treatment : 0
     * effect : 0
     * prescription : null
     * doctor_advince : 坚持服药，定期复诊
     * return : 1
     * return_type : 3
     * return_paid : 0
     * is_accept : 1
     * return_time : 0
     * money : 0
     * return_appointment_id : 0
     * doctor_require : 15
     * comment :
     * status : 2
     * has_pay : 1
     * date : 1970-01-01
     * time : 08:00-08:30
     * doctor_info : {"id":15,"name":"一些企业主","level":"咨询/治疗师认证","hospital_id":8,"avatar":"http://7xkt51.com2.z0.glb.qiniucdn.com/FtfM0a2249WI1e90kLcLpaCLsTVS","detail":"但不要放弃你自己想","money":1,"title":"主任医师","specialist":"一起","point":4.3,"hospital_name":"一起","voipAccount":"8002293600000034","phone":"15917748283"}
     */

    @JsonProperty("id")
    private int id;
    @JsonProperty("doctor_id")
    private int doctorId;
    @JsonProperty("appointment_id")
    private int appointmentId;
    @JsonProperty("is_diagnosis")
    private int isDiagnosis;
    @JsonProperty("perception")
    private HashMap<String, String> perception;
    @JsonProperty("thinking")
    private HashMap<String, String> thinking;
    @JsonProperty("pipedream")
    private HashMap<String, String> pipedream;
    @JsonProperty("emotion")
    private HashMap<String, String> emotion;
    @JsonProperty("memory")
    private HashMap<String, String> memory;
    @JsonProperty("insight")
    private HashMap<String, String> insight;
    @JsonProperty("description")
    private String description;
    @JsonProperty("diagnosis_record")
    private String diagnosisRecord;
    @JsonProperty("current_status")
    private int currentStatus;
    @JsonProperty("recovered")
    private int recovered;
    @JsonProperty("treatment")
    private int treatment;
    @JsonProperty("effect")
    private int effect;
    @JsonProperty("prescription")
    private ArrayList<Prescription> prescription = new ArrayList<>();
    @JsonProperty("doctor_advince")
    private String doctorAdvince;
    @JsonProperty("return")
    private int returnX;
    @JsonProperty("return_type")
    private int returnType;
    @JsonProperty("return_paid")
    private int returnPaid;
    @JsonProperty("is_accept")
    private int isAccept;
    @JsonProperty("return_time")
    private int returnTime;
    @JsonProperty("money")
    private int money;
    @JsonProperty("return_appointment_id")
    private int returnAppointmentId;
    @JsonProperty("doctor_require")
    private int doctorRequire;
    @JsonProperty("comment")
    private String comment;
    @JsonProperty("status")
    private int status;
    @JsonProperty("has_pay")
    private int hasPay;
    @JsonProperty("date")
    private String date;
    @JsonProperty("time")
    private String time;
    @JsonProperty("doctor_info")
    private Doctor doctorInfo;
    @JsonProperty("reminder")
    public List<Reminder> reminderList;
    @JsonProperty("can_edit")
    public int canEdit = 0;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public int getIsDiagnosis() {
        return isDiagnosis;
    }

    public void setIsDiagnosis(int isDiagnosis) {
        this.isDiagnosis = isDiagnosis;
    }

    public HashMap<String, String> getPerception() {
        return perception;
    }

    public void setPerception(HashMap<String, String> perception) {
        this.perception = perception;
    }

    public HashMap<String, String> getThinking() {
        return thinking;
    }

    public void setThinking(HashMap<String, String> thinking) {
        this.thinking = thinking;
    }

    public HashMap<String, String> getPipedream() {
        return pipedream;
    }

    public void setPipedream(HashMap<String, String> pipedream) {
        this.pipedream = pipedream;
    }

    public HashMap<String, String> getEmotion() {
        return emotion;
    }

    public void setEmotion(HashMap<String, String> emotion) {
        this.emotion = emotion;
    }

    public HashMap<String, String> getMemory() {
        return memory;
    }

    public void setMemory(HashMap<String, String> memory) {
        this.memory = memory;
    }

    public HashMap<String, String> getInsight() {
        return insight;
    }

    public void setInsight(HashMap<String, String> insight) {
        this.insight = insight;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDiagnosisRecord() {
        return diagnosisRecord;
    }

    public void setDiagnosisRecord(String diagnosisRecord) {
        this.diagnosisRecord = diagnosisRecord;
    }

    public int getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(int currentStatus) {
        this.currentStatus = currentStatus;
    }

    public int getRecovered() {
        return recovered;
    }

    public void setRecovered(int recovered) {
        this.recovered = recovered;
    }

    public int getTreatment() {
        return treatment;
    }

    public void setTreatment(int treatment) {
        this.treatment = treatment;
    }

    public int getEffect() {
        return effect;
    }

    public void setEffect(int effect) {
        this.effect = effect;
    }

    public ArrayList<Prescription> getPrescription() {
        return prescription;
    }

    public void setPrescription(ArrayList<Prescription> prescription) {
        this.prescription = prescription;
    }

    public String getDoctorAdvince() {
        return doctorAdvince;
    }

    public void setDoctorAdvince(String doctorAdvince) {
        this.doctorAdvince = doctorAdvince;
    }

    public int getReturnX() {
        return returnX;
    }

    public void setReturnX(int returnX) {
        this.returnX = returnX;
    }

    public int getReturnType() {
        return returnType;
    }

    public void setReturnType(int returnType) {
        this.returnType = returnType;
    }

    public int getReturnPaid() {
        return returnPaid;
    }

    public void setReturnPaid(int returnPaid) {
        this.returnPaid = returnPaid;
    }

    public int getIsAccept() {
        return isAccept;
    }

    public void setIsAccept(int isAccept) {
        this.isAccept = isAccept;
    }

    public int getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(int returnTime) {
        this.returnTime = returnTime;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getReturnAppointmentId() {
        return returnAppointmentId;
    }

    public void setReturnAppointmentId(int returnAppointmentId) {
        this.returnAppointmentId = returnAppointmentId;
    }

    public int getDoctorRequire() {
        return doctorRequire;
    }

    public void setDoctorRequire(int doctorRequire) {
        this.doctorRequire = doctorRequire;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getHasPay() {
        return hasPay;
    }

    public void setHasPay(int hasPay) {
        this.hasPay = hasPay;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Doctor getDoctorInfo() {
        return doctorInfo;
    }

    public void setDoctorInfo(Doctor doctorInfo) {
        this.doctorInfo = doctorInfo;
    }

    @Override
    public String toString() {
        return "DiagnosisInfo{" +
                "id=" + id +
                ", doctorId=" + doctorId +
                ", id=" + appointmentId +
                ", isDiagnosis=" + isDiagnosis +
                ", perception=" + perception +
                ", thinking=" + thinking +
                ", pipedream=" + pipedream +
                ", emotion=" + emotion +
                ", memory=" + memory +
                ", insight=" + insight +
                ", description='" + description + '\'' +
                ", diagnosisRecord=" + diagnosisRecord +
                ", currentStatus=" + currentStatus +
                ", recovered=" + recovered +
                ", treatment=" + treatment +
                ", effect=" + effect +
                ", prescription=" + prescription +
                ", doctorAdvince='" + doctorAdvince + '\'' +
                ", returnX=" + returnX +
                ", returnType=" + returnType +
                ", returnPaid=" + returnPaid +
                ", isAccept=" + isAccept +
                ", returnTime=" + returnTime +
                ", money=" + money +
                ", returnAppointmentId=" + returnAppointmentId +
                ", doctorRequire=" + doctorRequire +
                ", comment='" + comment + '\'' +
                ", status=" + status +
                ", hasPay=" + hasPay +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", doctorInfo=" + doctorInfo +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.doctorId);
        dest.writeInt(this.appointmentId);
        dest.writeInt(this.isDiagnosis);
        dest.writeSerializable(this.perception);
        dest.writeSerializable(this.thinking);
        dest.writeSerializable(this.pipedream);
        dest.writeSerializable(this.emotion);
        dest.writeSerializable(this.memory);
        dest.writeSerializable(this.insight);
        dest.writeString(this.description);
        dest.writeString(this.diagnosisRecord);
        dest.writeInt(this.currentStatus);
        dest.writeInt(this.recovered);
        dest.writeInt(this.treatment);
        dest.writeInt(this.effect);
        dest.writeString(this.doctorAdvince);
        dest.writeInt(this.returnX);
        dest.writeInt(this.returnType);
        dest.writeInt(this.returnPaid);
        dest.writeInt(this.isAccept);
        dest.writeInt(this.returnTime);
        dest.writeInt(this.money);
        dest.writeInt(this.returnAppointmentId);
        dest.writeInt(this.doctorRequire);
        dest.writeString(this.comment);
        dest.writeInt(this.status);
        dest.writeInt(this.hasPay);
        dest.writeString(this.date);
        dest.writeString(this.time);
        dest.writeParcelable(this.doctorInfo, flags);
        dest.writeList(this.reminderList);
        dest.writeInt(this.canEdit);
    }

    public DiagnosisInfo() {
    }

    protected DiagnosisInfo(Parcel in) {
        this.id = in.readInt();
        this.doctorId = in.readInt();
        this.appointmentId = in.readInt();
        this.isDiagnosis = in.readInt();
        this.perception = (HashMap<String, String>) in.readSerializable();
        this.thinking = (HashMap<String, String>) in.readSerializable();
        this.pipedream = (HashMap<String, String>) in.readSerializable();
        this.emotion = (HashMap<String, String>) in.readSerializable();
        this.memory = (HashMap<String, String>) in.readSerializable();
        this.insight = (HashMap<String, String>) in.readSerializable();
        this.description = in.readString();
        this.diagnosisRecord = in.readString();
        this.currentStatus = in.readInt();
        this.recovered = in.readInt();
        this.treatment = in.readInt();
        this.effect = in.readInt();
        this.doctorAdvince = in.readString();
        this.returnX = in.readInt();
        this.returnType = in.readInt();
        this.returnPaid = in.readInt();
        this.isAccept = in.readInt();
        this.returnTime = in.readInt();
        this.money = in.readInt();
        this.returnAppointmentId = in.readInt();
        this.doctorRequire = in.readInt();
        this.comment = in.readString();
        this.status = in.readInt();
        this.hasPay = in.readInt();
        this.date = in.readString();
        this.time = in.readString();
        this.doctorInfo = in.readParcelable(Doctor.class.getClassLoader());
        this.reminderList = new ArrayList<Reminder>();
        in.readList(this.reminderList, Reminder.class.getClassLoader());
        this.canEdit = in.readInt();
    }

    public static final Parcelable.Creator<DiagnosisInfo> CREATOR = new Parcelable.Creator<DiagnosisInfo>() {
        @Override
        public DiagnosisInfo createFromParcel(Parcel source) {
            return new DiagnosisInfo(source);
        }

        @Override
        public DiagnosisInfo[] newArray(int size) {
            return new DiagnosisInfo[size];
        }
    };
}
