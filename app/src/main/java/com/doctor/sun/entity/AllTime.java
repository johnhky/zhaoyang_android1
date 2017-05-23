package com.doctor.sun.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by heky on 17/5/23.
 */

public class AllTime {
    @JsonProperty("1")
    public List<Time> netWork = new ArrayList<>();
    @JsonProperty("2")
    public List<Time>simple = new ArrayList<>();
    @JsonProperty("4")
    public List<Time> surface = new ArrayList<>();

}
