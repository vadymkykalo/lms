package com.vadymkykalo.lms.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springdoc.core.annotations.ParameterObject;

import java.util.Set;

@Builder
@Data
@ParameterObject
@Schema(description = "Data transfer object for user mapping")
public class UserDto {

    @Schema(description = "Unique identifier of the User", example = "1", required = true)
    private Long id;

    @Schema(description = "Username of the User, typically the email", example = "user@example.com")
    private String username;

    @Schema(description = "First name of the User", example = "John")
    private String firstName;

    @Schema(description = "Last name of the User", example = "Doe")
    private String lastName;

    @Schema(description = "Indicates whether the User is enabled", example = "true")
    private Boolean isEnabled;

    @Schema(description = "Set of roles assigned to the User", example = "[\"ADMIN\", \"USER\"]")
    private Set<String> roles;
}
