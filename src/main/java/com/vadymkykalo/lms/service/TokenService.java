package com.vadymkykalo.lms.service;

import com.nimbusds.jwt.SignedJWT;
import com.vadymkykalo.lms.component.CustomUserDetails;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

@Slf4j
public class TokenService  {

    private final JwtEncoder jwtEncoder;

    public TokenService(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    public String generateAccessToken(@NotNull CustomUserDetails usrDetails) {
        Instant now = Instant.now();
        String scope = usrDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(1, ChronoUnit.HOURS))
                .subject(usrDetails.getUsername())
                .claim("scope", scope)
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    public String parseToken(@NotNull String token) {
        try {
            SignedJWT decodedJWT = SignedJWT.parse(token);
            return decodedJWT.getJWTClaimsSet().getSubject();
        } catch (ParseException e) {
            log.error("Error parsing token", e);
        }
        return null;
    }
}
