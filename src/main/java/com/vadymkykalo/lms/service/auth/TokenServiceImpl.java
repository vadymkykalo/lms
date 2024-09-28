package com.vadymkykalo.lms.service.auth;

import com.nimbusds.jwt.SignedJWT;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

    private final JwtEncoder jwtEncoder;

    @Value("${jwt.access-token-expiration-minutes}")
    private long accessTokenExpirationMinutes;

    @Value("${jwt.refresh-token-expiration-hours}")
    private long refreshTokenExpirationHours;

    public long getAccessTokenExpiration() {
        return Instant.now().plus(accessTokenExpirationMinutes, ChronoUnit.MINUTES).getEpochSecond();
    }

    public long getRefreshTokenExpiration() {
        return Instant.now().plus(refreshTokenExpirationHours, ChronoUnit.HOURS).getEpochSecond();
    }

    public String generateAccessToken(@NotNull CustomUserDetails usrDetails) {
        Instant now = Instant.now();
        String scope = usrDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(accessTokenExpirationMinutes, ChronoUnit.MINUTES))
                .subject(usrDetails.getUsername())
                .claim("scope", scope)
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    public String generateRefreshToken(@NotNull CustomUserDetails usrDetails) {
        Instant now = Instant.now();
        String scope = usrDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(refreshTokenExpirationHours, ChronoUnit.HOURS))
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

    public boolean validateToken(String token) {
        try {
            SignedJWT decodedJWT = SignedJWT.parse(token);
            Instant expirationTime = decodedJWT.getJWTClaimsSet().getExpirationTime().toInstant();
            return Instant.now().isBefore(expirationTime);
        } catch (ParseException e) {
            log.error("Error validating token", e);
            return false;
        }
    }
}
