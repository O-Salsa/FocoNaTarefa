package com.heilsalsa.foconatarefa.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

public class JwtUtil {

    private static final SecretKey SECRET_KEY =
            Keys.hmacShaKeyFor("MinhaSuperSenhaSecretaJwt1234567890123456".getBytes(StandardCharsets.UTF_8));

    private static final Duration EXPIRATION = Duration.ofHours(24);

    public static String generateToken(String username) {
        Instant now = Instant.now();
        return Jwts.builder()
                .subject(username)                         // (novo estilo 0.12.x)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(EXPIRATION)))
                .signWith(SECRET_KEY)                      // HS256 inferido pela chave HMAC
                .compact();
    }

    public static String getUsernameFromToken(String token) {
        return Jwts.parser()
                .verifyWith(SECRET_KEY)                    // (novo estilo 0.12.x)
                .build()
                .parseSignedClaims(token)                  // em vez de parseClaimsJws(...)
                .getPayload()
                .getSubject();
    }
}
