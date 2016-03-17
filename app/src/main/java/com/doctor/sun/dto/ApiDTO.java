package com.doctor.sun.dto;

import com.doctor.sun.entity.QuestionStats;

/**
 * Created by rick on 11/5/15.
 */
public class ApiDTO<T> {

    /**
     * data : {"per_page":"15","total":"1","data":[{}],"last_page":"1","next_page_url":"","from":"1","to":"1","prev_page_url":"","current_page":"1"}
     * status : 200
     */
    private int code;
    private String status;
    private Object message;
    private T data;
    // api 不规范才放这个在这里
    private QuestionStats count;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return String.valueOf(message);
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public QuestionStats getCount() {
        return count;
    }

    public void setCount(QuestionStats count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "ApiDTO{" +
                "code=" + code +
                ", status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
