package com.doctor.sun.vm.validator;

import com.google.common.base.Strings;

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
        return !(Strings.isNullOrEmpty(input));
    }

    @Override
    public String errorMsg() {
        return errorMsg;
    }
}
