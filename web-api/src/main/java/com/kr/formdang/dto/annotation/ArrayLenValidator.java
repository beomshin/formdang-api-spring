package com.kr.formdang.dto.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ArrayLenValidator implements ConstraintValidator<ArrayLenValid, String[]> {
    @Override
    public boolean isValid(String[] value, ConstraintValidatorContext context) {
        if (value == null || value.length == 0) return true;

        int len = 0;

        for (String val : value) {
            len += val.length();
        }

        if (len > 512) {
            return false;
        }

        return true;
    }
}
