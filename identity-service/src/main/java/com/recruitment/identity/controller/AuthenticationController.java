package com.recruitment.identity.controller;

import java.text.ParseException;

import com.recruitment.common.dto.response.ApiResponse;
import com.recruitment.identity.dto.request.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.recruitment.identity.dto.response.AuthenticationResponse;
import com.recruitment.identity.dto.response.IntrospectResponse;
import com.recruitment.identity.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AuthenticationService authenticationService;

    @PostMapping("/token")
    ApiResponse<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        var result = authenticationService.authenticate(request);
        return ApiResponse.<AuthenticationResponse>builder().result(result).build();
    }

    @PostMapping("/introspect")
    ApiResponse<IntrospectResponse> authenticate(
            @RequestBody IntrospectRequest request
    )
            throws ParseException, JOSEException
    {
        return ApiResponse.<IntrospectResponse>builder()
                .result(authenticationService.introspect(request)).build();
    }

    @PostMapping("/refresh")
    ApiResponse<AuthenticationResponse> authenticate(
            @RequestBody RefreshRequest request
    )
            throws ParseException, JOSEException
    {
        var result = authenticationService.refreshToken(request);
        return ApiResponse.<AuthenticationResponse>builder().result(result).build();
    }

    @PostMapping("/logout")
    ApiResponse<Void> logout(
            @RequestBody LogoutRequest request
    )
            throws ParseException, JOSEException
    {
        authenticationService.logout(request);
        return ApiResponse.<Void>builder().build();
    }
}
