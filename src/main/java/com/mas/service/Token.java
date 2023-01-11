package com.mas.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class Token {
    @Getter
    private final String token;


    private Token(String token) {
        this.token = token;
    }

    /**
     * Token generator
     * @param userId
     * @param validityInMinutes
     * @param secretKey
     * @return
     */
    public static Token of(Long userId, Long validityInMinutes, String secretKey){
        var issueDate = Instant.now();
        var expiration = issueDate.plus(validityInMinutes, ChronoUnit.MINUTES);
        var token = Jwts.builder()
                .claim("user_id", userId)
                .setIssuedAt(Date.from(issueDate))
                .setExpiration(Date.from(expiration))
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)))
                .compact();
        return new Token(token);
    }
}
