package com.recruitment.identity.validator;

import com.recruitment.identity.repository.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

    private final UserRepository userRepository;

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (email == null || email.isEmpty()) {
            return true;
        }
        if (userRepository.existsByEmail(email)) {
            context.disableDefaultConstraintViolation();
            context
                    .buildConstraintViolationWithTemplate("Email đã tồn tại trong hệ thống")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}

