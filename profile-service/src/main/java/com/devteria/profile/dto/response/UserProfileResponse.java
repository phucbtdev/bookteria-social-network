package com.devteria.profile.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfileResponse {
    String id;
    String firstName;
    String lastName;
    LocalDate dateOfBirth;
    String city;
}
