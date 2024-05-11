package com.vadymkykalo.lms.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Schema(description = "Data transfer object for user update")
public class UserUpdateDto {
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
}
