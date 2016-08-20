package com.doctor.sun.vo.validator;

import java.util.regex.Pattern;

/**
 * Created by rick on 19/8/2016.
 */

public class RegexValidator implements Validator {

    private final Pattern pattern;
    private final String errorMsg;

    public RegexValidator(Pattern pattern, String errorMsg) {
        this.pattern = pattern;
        this.errorMsg = errorMsg;
    }

    @Override
    public boolean isValid(String input) {
        if (input == null || input.equals("")) {
            return false;
        }
        return pattern.matcher(input).matches();
    }

    @Override
    public String errorMsg() {
        return errorMsg;
    }

}
