package com.doctor.sun.immutables;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import org.immutables.value.Value;

/**
 * Created by rick on 25/10/2016.
 */

@Value.Immutable
@JsonSerialize(as = ImmutableSimpleAppointment.class)
@JsonDeserialize(as = ImmutableSimpleAppointment.class)
public abstract class SimpleAppointment {

    public abstract String getId();

    public abstract String getType();
}
