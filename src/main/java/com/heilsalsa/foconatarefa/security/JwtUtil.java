package com.heilsalsa.foconatarefa.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;

public class JwtUtil {
	private static final Key SECRET_KEY = Keys.hmacShaKeyFor("MinhaSuperSenhaSecretaJwt1234567890123456".getBytes());
	private static final long EXPIRATION_TIME = 86400000; //24h em milissegundos
	
    public static String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }
    public static String getUsernameFromToken(String token) {
        return Jwts.parser() // PARSER na versão antiga, o compilador nao deixou eu dar o comando parserBuilder
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}

