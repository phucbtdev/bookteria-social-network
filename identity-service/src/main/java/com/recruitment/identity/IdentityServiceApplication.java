package com.recruitment.identity;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@SecurityScheme(
        name = "bearerAuth", // Tên tham chiếu cho Security Requirement
        description = "JWT auth description - Enter 'Bearer' [space] and then your token",
        scheme = "bearer", // Scheme là "bearer" cho JWT
        type = SecuritySchemeType.HTTP, // Loại là HTTP
        bearerFormat = "JWT", // Định dạng của Bearer Token
        in = SecuritySchemeIn.HEADER // Token được gửi trong Header (thường là Authorization)
)
@EnableFeignClients
public class IdentityServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(IdentityServiceApplication.class, args);
    }
}
