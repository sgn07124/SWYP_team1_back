package com.example.swyp_team1_back.global.jwt;

public interface JwtProperties {
    String SECRET = "{}"; //(2)
    int EXPIRATION_TIME =  864000000; //(3)
    String TOKEN_PREFIX = "Bearer "; //(4)
    String HEADER_STRING = "Authorization"; //(5)
}
