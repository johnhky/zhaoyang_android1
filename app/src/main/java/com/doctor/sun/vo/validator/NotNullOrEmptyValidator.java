package com.doctor.sun.vo.validator;

/**
 * Created by rick on 22/8/2016.
 */

public class NotNullOrEmptyValidator implements Validator {

    private final String errorMsg;

    public NotNullOrEmptyValidator(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    @Override
    public boolean isValid(String input) {
        return !(input == null || input.equals(""));
    }

    @Override
    public String errorMsg() {
        return errorMsg;
    }
}
