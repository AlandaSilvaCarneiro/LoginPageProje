package com.AuthProjeto.Service;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    private final SecretKey secretKey;
    private final long expiracaoMs;

    public JwtService(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration-ms}") long expiracaoMs) {

        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
        this.expiracaoMs = expiracaoMs;
    }

    public String gerarToken(UserDetails usuario) {

        Date agora = new Date();
        Date expiracao = new Date(
            agora.getTime() + expiracaoMs
        );

        return Jwts.builder()
            .subject(usuario.getUsername())
            .issuedAt(agora)
            .expiration(expiracao)
            .signWith(secretKey)
            .compact();
    }

    public String extrairEmail(String token) {
        return extrairClaims(token).getSubject();
    }

    public boolean tokenValido(
            String token,
            UserDetails usuario) {

        Claims claims = extrairClaims(token);

        return claims.getSubject().equals(usuario.getUsername())
            && claims.getExpiration().after(new Date());
    }

    private Claims extrairClaims(String token) {
        return Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }
}