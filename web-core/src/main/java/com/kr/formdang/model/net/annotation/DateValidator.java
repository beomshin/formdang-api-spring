package com.kr.formdang.model.net.annotation;

import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Slf4j
public class DateValidator implements ConstraintValidator<DateValid, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.length() == 0) return true;

        try {
            LocalDate.from(LocalDate.parse(value, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        } catch (DateTimeParseException e) {
            return false;
        }

        return true;
    }
}
