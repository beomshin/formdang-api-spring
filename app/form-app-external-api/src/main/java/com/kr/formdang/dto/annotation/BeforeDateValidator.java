package com.kr.formdang.dto.annotation;

import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Slf4j
public class BeforeDateValidator implements ConstraintValidator<BeforeDateValid, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.length() == 0) return true;

        try {
            LocalDate date = LocalDate.from(LocalDate.parse(value, DateTimeFormatter.ofPattern("yyyyMMdd")));
            if (date.isBefore(LocalDate.now())) {
                return false;
            }
        } catch (DateTimeParseException e) {
            return true;
        }

        return true;
    }
}
