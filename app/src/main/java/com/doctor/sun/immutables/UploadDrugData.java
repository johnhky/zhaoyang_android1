package com.doctor.sun.immutables;

import com.doctor.sun.vm.BaseItem;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import org.immutables.value.Value;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by heky on 17/5/4.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Value.Immutable
@Value.Style(jdkOnly = true)
@JsonSerialize(as = ImmutableUploadDrugData.class)
@JsonDeserialize(as = ImmutableUploadDrugData.class)
@Value.Modifiable
public class UploadDrugData extends BaseItem {

    @Value.Default
    public String getAppointment_id(){
        return "";
    }
    @Value.Default
    public String getName(){
        return "";
    }
    @Value.Default
    public String getCreated_at(){
        return "";
    }
    @Value.Default
    public List<Prescription>getDrug_data(){
        return  new ArrayList<>();
    }

    @Override
    public String toString() {
        return "DATA[ name: "+getName()+"create_at:"+getCreated_at()+"appointment_id:"+getAppointment_id()+",getDrug_data:"+getDrug_data()+"]";
    }
}
