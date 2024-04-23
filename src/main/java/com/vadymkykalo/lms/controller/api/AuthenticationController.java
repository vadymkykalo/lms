package com.vadymkykalo.lms.controller.api;

import com.vadymkykalo.lms.component.CustomUsrDetails;
import com.vadymkykalo.lms.dto.AuthRequest;
import com.vadymkykalo.lms.service.CustomUsrDetailsService;
import com.vadymkykalo.lms.service.TokenService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class AuthenticationController {

    private final TokenService tokenService;
    private final AuthenticationManager authManager;
    private final CustomUsrDetailsService customUsrDetailsService;

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public String create(@RequestBody @Valid AuthRequest authRequest) {

        var authentication = new UsernamePasswordAuthenticationToken(
                authRequest.getUsername(),
                authRequest.getPassword()
        );

        authManager.authenticate(authentication);

        CustomUsrDetails user = (CustomUsrDetails) customUsrDetailsService.loadUserByUsername(authRequest.getUsername());

        return tokenService.generateAccessToken(user);
    }
}
