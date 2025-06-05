package com.recruiment.payment_service.exception;

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
    RECORD_NOT_EXISTED(1005, "Record not existed", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1006, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1007, "You do not have permission", HttpStatus.FORBIDDEN),
    INVALID_DOB(1008, "Your age must be at least {min}", HttpStatus.BAD_REQUEST),
    EMPLOYER_NOT_FOUND(2001, "Employer not found", HttpStatus.NOT_FOUND),
    REVIEW_NOT_FOUND(2002, "Review not found", HttpStatus.NOT_FOUND),
    REQUEST_JON_BODY_NOT_READABLE(2003, "Request JSON body not readable", HttpStatus.BAD_REQUEST),
    METHOD_ARGUMENT_TYPE_MISMATCH(2004, "Method argument type mismatch", HttpStatus.BAD_REQUEST),
    GENERATE_FAILED(2005, "Failed to generate HMAC SHA256 signature",HttpStatus.BAD_REQUEST),
    CREATE_FAIL_MOMO_PAYMENT_URL( 2006,"Failed to create MoMo payment URL",HttpStatus.BAD_REQUEST)
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
