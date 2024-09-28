package com.vadymkykalo.lms.service.auth;

import jakarta.validation.constraints.NotNull;

public interface TokenService {

    long getAccessTokenExpiration();

    long getRefreshTokenExpiration();

    String generateAccessToken(@NotNull CustomUserDetails usrDetails);

    String generateRefreshToken(@NotNull CustomUserDetails usrDetails);

    String parseToken(@NotNull String token);

    boolean validateToken(String token);
}
