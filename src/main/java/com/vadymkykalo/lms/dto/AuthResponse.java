package com.vadymkykalo.lms.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "Response model for authentication, containing message, status, access token, and refresh token")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthResponse {

    @Schema(description = "JWT access token")
    @JsonProperty("access_token")
    private String accessToken;

    @Schema(description = "JWT refresh token")
    @JsonProperty("refresh_token")
    private String refreshToken;

    @Schema(description = "Access token expiration time in epoch seconds")
    @JsonProperty("access_token_expiration")
    private long accessTokenExpiration;

    @Schema(description = "Refresh token expiration time in epoch seconds")
    @JsonProperty("refresh_token_expiration")
    private long refreshTokenExpiration;
}
