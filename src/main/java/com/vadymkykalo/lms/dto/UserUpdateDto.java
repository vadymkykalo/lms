package com.vadymkykalo.lms.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Data transfer object for user update")
public class UserUpdateDto {
    @NotNull
    private String firstName;
}
