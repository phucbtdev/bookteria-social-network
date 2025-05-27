package com.recruitment.job_service.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, "Uncategorized error", HttpStatus.BAD_REQUEST),
    USER_EXISTED(1002, "User existed", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(1003, "Username must be at least {min} characters", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(1004, "Password must be at least {min} characters", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1005, "User not existed", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1006, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1007, "You do not have permission", HttpStatus.FORBIDDEN),
    INVALID_DOB(1008, "Your age must be at least {min}", HttpStatus.BAD_REQUEST),
    CANNOT_SEND_EMAIL(1008, "Cannot send email", HttpStatus.BAD_REQUEST),
    INDUSTRY_NOT_EXISTED(1009, "Industry not existed", HttpStatus.BAD_REQUEST),
    JOB_NOT_EXISTED(1010, "Job not existed", HttpStatus.BAD_REQUEST),
    JOB_LEVEL_NOT_EXISTED(1010, "Job level not existed", HttpStatus.BAD_REQUEST),
    EXPERIENCE_LEVEL_NOT_EXISTED(1011, "Experience level not existed", HttpStatus.BAD_REQUEST),
    SALARY_RANGE_NOT_EXISTED(1012, "Salary range not existed", HttpStatus.BAD_REQUEST),
    WORK_TYPE_NOT_EXISTED(1013, "Work type not existed", HttpStatus.BAD_REQUEST),
    INVALID_REQUEST(1014, "Invalid request", HttpStatus.BAD_REQUEST),
    MISSING_BODY(1015, "Missing body", HttpStatus.BAD_REQUEST),
    SLUG_EXISTED(1016, "Slug existed", HttpStatus.BAD_REQUEST),
    SLUG_NOT_EXISTED(1017, "Slug not existed", HttpStatus.BAD_REQUEST),
    APPLICATION_ALREADY_EXISTS(1018, "Application already exists for this candidate and job", HttpStatus.BAD_REQUEST),
    JOB_APPLICATION_NOT_EXISTED(1019, "Job application not existed", HttpStatus.BAD_REQUEST),
    ;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;
}
