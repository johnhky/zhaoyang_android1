package com.doctor.sun.immutables;

import com.doctor.sun.vm.BaseItem;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import org.immutables.value.Value;

// FIXME generate failure  field _$Prescription3178

/**
 * Created by rick on 15/2/2017.
 */
@Value.Immutable
@Value.Style(jdkOnly = true)
@JsonSerialize(as = ImmutableSubsidy.class)
@JsonDeserialize(as = ImmutableSubsidy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract   class Subsidy extends BaseItem {

    public abstract String getFee();

    public abstract String getItem();

    public abstract String getRemark();

    public abstract String getSubsidy();
}
