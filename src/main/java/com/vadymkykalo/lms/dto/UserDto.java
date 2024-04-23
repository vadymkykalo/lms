package com.vadymkykalo.lms.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Data transfer object for user mapping")
public class UserDto {
    private Long id;

    private String username;
    private String password;

    private String firstName;
    private String lastName;
    private Boolean isEnabled;
}
