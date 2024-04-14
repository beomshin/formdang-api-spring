package com.kr.formdang.dto.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = BeforeDateValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface BeforeDateValid {

    /**
     * 날짜 형식 체크를 위해 DateValid 같이 사용 권장
     * @return
     */

    String message() default "이전 날짜를 입력하였습니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
