package com.doctor.sun.entity;

import com.doctor.sun.immutables.Prescription;
import com.doctor.sun.immutables.Subsidy;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;

// FIXME generate failure  field _$Prescription3178

/**
 * Created by rick on 15/2/2017.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class SubsidyDetail {
    public List<Subsidy> detail = new ArrayList<>();
    public List<Prescription> prescription = new ArrayList<>();
}
