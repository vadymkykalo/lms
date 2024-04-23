package com.vadymkykalo.lms.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Data transfer object for user generate jwt")
public class AuthRequest {
    private String username;
    private String password;
}
