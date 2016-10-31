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

    @Value.Default
    public String getId() {
        return "";
    }

    public abstract String getType();

    /**
     * 不要用这个接口，这个只在用药信息里面会用到
     *
     * @return
     */
    @Deprecated
    @Value.Default
    public String getRecord_name() {
        return "";
    }

    /**
     * 不要用这个接口，这个只在用药信息里面会用到
     *
     * @return
     */
    @Deprecated
    @Value.Default
    public String getRelation() {
        return "";
    }

    @Value.Default
    public String getDisplay_type() {
        return "";
    }

    @Value.Default
    public String getBook_time() {
        return "";
    }
}
