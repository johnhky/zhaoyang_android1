package com.doctor.sun.http;

/**
 * 可选
 * api code >= 300 返回的json错误
 * <p/>
 * Created by Tony on 10/23/15.
 */
public class HttpError {

    private int code;
    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
