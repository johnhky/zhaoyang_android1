package com.doctor.sun.entity;

import com.doctor.sun.immutables.Prescription;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by heky on 17/6/13.
 */

public class MyPrescription {
    private List<Prescription> prescription;
    public Perception perception;
    public Thinking thinking;
    public Pipedream pipedream;
    public Emotion emotion;
    public Memory memory;
    public Insight insight;
    private int id;
    private int doctorId;
    private int appointmentId;
    private int isDiagnosis;
    private String description;
    public ArrayList<String> diagnosisRecord = new ArrayList<>();
    private int currentStatus;
    private int recovered;
    private int treatment;
    private int effect;
    private String doctorAdvince;
    private int returnX;
    private int returnType;
    private int returnPaid;
    private int isAccept;
    private int returnTime;
    private int money;
    private int returnAppointmentId;
    private int doctorRequire;
    private String comment;
    private int status;
    private int hasPay;
    private String date;
    private String time;
    private Doctor doctorInfo;
    public List<Reminder> reminderList;
    public int canEdit = 0;
    public int isFinish = 0;

    public List<Prescription> getPrescription() {
        return prescription;
    }

    public void setPrescription(List<Prescription> prescription) {
        this.prescription = prescription;
    }

    class Perception {
        @JsonProperty("0")
        String id;

        @Expose
        String otherContent;
    }

    class Thinking {
        @JsonProperty("0")
        String id;
        @Expose
        String otherContent;
    }

    class Pipedream {
        @JsonProperty("0")
        String id;
        @Expose
        String otherContent;
    }

    class Emotion {
        @JsonProperty("0")
        String id;
        @Expose
        String otherContent;
    }

    class Memory {
        @JsonProperty("0")
        String id;
        @Expose
        String otherContent;
    }

    class Insight {
        @JsonProperty("0")
        String id;
        @Expose
        String otherContent;
    }

    @Override
    public String toString() {
        return "prescription:"+prescription;
    }
}
