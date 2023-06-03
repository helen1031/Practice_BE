package com.example.practice.security;

import com.example.practice.entity.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Slf4j
@Service
// 유저 정보를 받아 JWT를 생성하는 역할
public class TokenProvider {
    public static final String SECRET_KEY = "abcd"; // 추후 수정 필요

    public String create(UserEntity userEntity) {
        Date expiryDate = Date.from(Instant.now().plus(1, ChronoUnit.DAYS));

        // JWT Token 생성
        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                // payload에 들어갈 내용
                .setSubject(userEntity.getId())
                .setIssuer("practice app")
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .compact();
    }

    public String create(final Authentication authentication) {
        ApplicationOAuth2User userPrincipal = (ApplicationOAuth2User) authentication.getPrincipal();
        Date expiryDate = Date.from(
                Instant.now()
                        .plus(1, ChronoUnit.DAYS));

        return Jwts.builder()
                .setSubject(userPrincipal.getName()) // id가 리턴됨.
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    public String validateAndGetUserId(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token) // token과 비교
                .getBody();

        return claims.getSubject(); // id 리턴
    }
}
