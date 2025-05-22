package com.recruitment.job_service.validation;

import com.recruitment.job_service.repository.JobRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UniqueJobSlugValidator implements ConstraintValidator<UniqueJobSlug, String> {

    private final JobRepository jobRepository;

    @Override
    public boolean isValid(String slug, ConstraintValidatorContext context) {
        if (slug == null || slug.trim().isEmpty()) {
            return true;
        }
        return !jobRepository.existsBySlug(slug);
    }
}

