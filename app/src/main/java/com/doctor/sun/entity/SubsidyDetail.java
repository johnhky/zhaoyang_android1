package com.doctor.sun.entity;

import com.doctor.sun.immutables.Prescription;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

// FIXME generate failure  field _$Prescription3178

/**
 * Created by rick on 15/2/2017.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class SubsidyDetail {


    public List<DetailEntity> detail;
    public List<Prescription> prescription;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DetailEntity {
        public String fee;
        public String item;
        public String remark;
        public String subsidy;
    }
}
