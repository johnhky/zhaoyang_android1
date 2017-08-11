package com.doctor.sun.dto;

import com.doctor.sun.entity.FollowUpInfo;
import com.doctor.sun.entity.Questions2;
import com.doctor.sun.entity.Scales;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by rick on 3/8/2016.
 */

public class QuestionDTO {
    @JsonProperty("questions")
    public List<Questions2> questions;
    @JsonProperty("scales")
    public List<Scales> scales;
    @JsonProperty("followUpInfo")
    public FollowUpInfo followUpInfo;

    @Override
    public String toString() {
        return "QuestionDTO:{ questions:"+questions +", scales:"+scales+"followInfo:"+followUpInfo+ "}";
    }
}
