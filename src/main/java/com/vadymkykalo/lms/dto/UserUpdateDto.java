package com.vadymkykalo.lms.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateDto {
    @NotNull
    private String firstName;
}
