package com.pm.security.dto.request;

import com.pm.security.validator.DobConstraint;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateRequest {
     String password;
     String firstName;
     String lastName;
     @DobConstraint(min = 18, message = "INVALID_DOB")
     LocalDate dateOfBirth;
     List<String> roles;
}
