package com.devteria.profile.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserProfileCreation {
    String firstName;
    String lastName;
    LocalDate dateOfBirth;
    String city;
}
