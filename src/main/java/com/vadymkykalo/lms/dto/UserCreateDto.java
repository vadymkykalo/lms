package com.vadymkykalo.lms.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Data transfer object for user creation")
public class UserCreateDto {
    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @Email
    @Schema(description = "Email of the user", example = "john.doe@example.com")
    private String email;

    @Size(min = 3, max = 100)
    private String password;
}
