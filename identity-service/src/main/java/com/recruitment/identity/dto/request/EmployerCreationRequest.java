package com.recruitment.identity.dto.request;

import java.time.LocalDate;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmployerCreationRequest {
    String userId;
    String firstName;
    String lastName;
    LocalDate dob;
    String city;
}
