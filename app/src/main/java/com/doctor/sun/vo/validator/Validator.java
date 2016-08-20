package com.doctor.sun.vo.validator;

/**
 * Created by rick on 19/8/2016.
 */
public interface Validator {
    boolean isValid(String input);

    String errorMsg();
}
