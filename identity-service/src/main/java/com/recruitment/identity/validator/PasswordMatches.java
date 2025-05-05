package com.recruitment.identity.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordMatchesValidator.class)
@Documented
public @interface PasswordMatches {
    String message() default "Mật khẩu và xác nhận mật khẩu không khớp!";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

