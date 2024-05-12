package com.vadymkykalo.lms.controller.api;

import com.vadymkykalo.lms.component.CustomUserDetails;
import com.vadymkykalo.lms.dto.AuthRequest;
import com.vadymkykalo.lms.service.CustomUsrDetailsService;
import com.vadymkykalo.lms.service.TokenService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1")
public class AuthenticationController {

    private final TokenService tokenService;
    private final AuthenticationManager authManager;
    private final CustomUsrDetailsService customUsrDetailsService;

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> create(@RequestBody @Valid AuthRequest authRequest) {

        var authentication = new UsernamePasswordAuthenticationToken(
                authRequest.getUsername(),
                authRequest.getPassword()
        );

        authManager.authenticate(authentication);

        CustomUserDetails user = (CustomUserDetails) customUsrDetailsService.loadUserByUsername(authRequest.getUsername());

        return ResponseEntity.status(HttpStatus.OK).body(tokenService.generateAccessToken(user));
    }
}
