package com.doctor.sun.dto;

import com.doctor.sun.entity.Questions2;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.List;

/**
 * Created by rick on 3/8/2016.
 */

public class QuestionDTO {
    @JsonProperty("questions")
    public List<Questions2> questions;
    @JsonProperty("scales")
    public List<HashMap<String, String>> scales;
}
