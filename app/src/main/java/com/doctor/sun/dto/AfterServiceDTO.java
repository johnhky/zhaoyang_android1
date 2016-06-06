package com.doctor.sun.dto;

import com.doctor.sun.entity.AfterService;
import com.doctor.sun.entity.Answer;
import com.doctor.sun.entity.Question;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by rick on 4/6/2016.
 */
public class AfterServiceDTO  {

    @JsonProperty("followUpInfo")
    public List<AfterService> followUpInfo;

    @JsonProperty("questions")
    public List<Answer> questions;

}
