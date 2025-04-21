package com.pm.security.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {
    @Size(min = 3, message = "USERNAME_INVALID")
    @NotEmpty
    String username;

    @Size(min = 8, message = "Password must be at least 8 character")
    String password;
    String firstName;
    String lastName;
    LocalDate dateOfBirth;
}
