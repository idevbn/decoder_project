package com.ead.authuser.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Log4j2
@Component
public class JwtProvider {

    @Value("${ead.auth.jwtSecret}")
    private String jwtSecret;

    @Value("${ead.auth.jwtExpirationMs}")
    private String jwtExpirationMs;

    public String generateJwt(final Authentication authentication) {
        final UserDetails userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        final Instant issuedAt = Instant.now();

        return Jwts.builder()
                .setSubject(userPrincipal.getUsername())
                .setIssuedAt(Date.from(issuedAt))
                .setExpiration(Date.from(issuedAt.plus(Long.parseLong(this.jwtExpirationMs), ChronoUnit.MILLIS)))
                .signWith(SignatureAlgorithm.HS512, this.jwtSecret)
                .compact();
    }

}
