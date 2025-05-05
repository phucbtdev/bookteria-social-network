package com.recruitment.identity.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.reflect.Field;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        try {
            Field passwordField = value.getClass().getDeclaredField("password");
            Field confirmField = value.getClass().getDeclaredField("confirmPassword");

            passwordField.setAccessible(true);
            confirmField.setAccessible(true);

            Object password = passwordField.get(value);
            Object confirm = confirmField.get(value);

            boolean isValid = (password == null && confirm == null)
                    || (password != null && password.equals(confirm));

            if (!isValid) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("Mật khẩu và xác nhận mật khẩu không khớp!")
                        .addPropertyNode("confirmPassword")
                        .addConstraintViolation();
            }

            return isValid;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            return false;
        }
    }
}
