package com.kr.formdang.net.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ArrayLenValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ArrayLenValid {

    /**
     * 배열 길이를 체크
     * @return
     */

    String message() default "내용 길이가 초과하였습니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
