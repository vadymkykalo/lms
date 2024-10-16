package com.vadymkykalo.lms.controller.api;

import com.vadymkykalo.lms.dto.ApiResponse;
import com.vadymkykalo.lms.exception.InvalidTokenException;
import com.vadymkykalo.lms.service.auth.CustomUserDetails;
import com.vadymkykalo.lms.dto.AuthRequest;
import com.vadymkykalo.lms.dto.AuthResponse;
import com.vadymkykalo.lms.service.auth.TokenServiceImpl;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1")
public class AuthenticationController {

    private final TokenServiceImpl tokenService;
    private final AuthenticationManager authManager;
    private final UserDetailsService customUsrDetailsService;

    @PostMapping("/auth")
    public ResponseEntity<?> auth(@RequestBody @Valid AuthRequest authRequest) {
        try {
            var authentication = new UsernamePasswordAuthenticationToken(
                    authRequest.getUsername(),
                    authRequest.getPassword()
            );

            authManager.authenticate(authentication);

            CustomUserDetails user = (CustomUserDetails) customUsrDetailsService.loadUserByUsername(authRequest.getUsername());

            String accessToken = tokenService.generateAccessToken(user);
            String refreshToken = tokenService.generateRefreshToken(user);

            AuthResponse response = AuthResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .accessTokenExpiration(tokenService.getAccessTokenExpiration())
                    .refreshTokenExpiration(tokenService.getRefreshTokenExpiration())
                    .build();

            return ResponseEntity.ok(ApiResponse.success(response));

        } catch (Exception e) {
            log.error(e.getMessage(), e);

            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(HttpStatus.UNAUTHORIZED, "Authentication failed"));
        }
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody String refreshToken) {
        try {
            if (!tokenService.validateToken(refreshToken)) {
                throw new InvalidTokenException("Invalid refresh token");
            }

            String username = tokenService.parseToken(refreshToken);
            CustomUserDetails user = (CustomUserDetails) customUsrDetailsService.loadUserByUsername(username);

            String newAccessToken = tokenService.generateAccessToken(user);
            String newRefreshToken = tokenService.generateRefreshToken(user);

            AuthResponse response = AuthResponse.builder()
                    .accessToken(newAccessToken)
                    .refreshToken(newRefreshToken)
                    .accessTokenExpiration(tokenService.getAccessTokenExpiration())
                    .refreshTokenExpiration(tokenService.getRefreshTokenExpiration())
                    .build();

            return ResponseEntity.ok(ApiResponse.success(response));

        } catch (Exception e) {
            log.error("Error refreshing token", e);

            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(HttpStatus.UNAUTHORIZED, "Refresh token failed"));
        }
    }
}
