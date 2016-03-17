package com.doctor.sun.dto;

import com.doctor.sun.entity.Time;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.List;

/**
 * Created by lucas on 12/2/15.
 */
public class AllDateDTO {

    /**
     * status : 200
     * message : time list
     * date : [{"id":163,"doctor_id":13,"week":4,"type":1,"from":"16:19:00","to":"17:24:00","created_at":"2015-10-21 16:25:43","updated_at":"2015-10-21 16:25:43"}]
     */

    @JsonProperty("status")
    private int status;
    @JsonProperty("message")
    private String message;
    /**
     * id : 163
     * doctor_id : 13
     * week : 4
     * type : 1
     * from : 16:19:00
     * to : 17:24:00
     * created_at : 2015-10-21 16:25:43
     * updated_at : 2015-10-21 16:25:43
     */

    @JsonProperty("date")
    private HashMap<String, List<Time>> date;

    public void setStatus(int status) {
        this.status = status;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public HashMap<String, List<Time>> getDate() {
        return date;
    }

    public void setDate(HashMap<String, List<Time>> date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "DateDTO{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", date=" + date +
                '}';
    }
}
