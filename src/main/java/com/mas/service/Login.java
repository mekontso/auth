package com.mas.service;

import lombok.Getter;

public class Login {
    @Getter
    private final Jwt accessToken;
    @Getter
    private final Jwt refreshToken;

    public Login(Jwt accessToken, Jwt refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public static Login of(Long userId, String accessSecret, String refreshSecret ){
        return new Login(
                Jwt.of(userId, 1L,accessSecret),
                Jwt.of(userId, 1440L,refreshSecret)
        );
    }
}
