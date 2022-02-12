package com.todolist.demo.security;

import com.todolist.demo.model.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
@Slf4j
@Service
public class TokenProvider {
    private static final String SECRET_KEY = "Q4NSl604sgyHJj1qwEkR3ycUeR4uUAt7WJraD7EN3O9DVM4yyYuHxMEbSF4XXyYJkal13eqgB0F7Bq4HQ4NSl604sgyHJj1qwEkR3ycUeR4uUAt7WJraD7EN3O9DVM4yyYuHxMEbSF4XXyYJkal13eqgB0F7Bq4H";

    public String create(UserEntity userEntity) {
        // 기한 1일
        Date expiryDate = Date.from(Instant.now().plus(1, ChronoUnit.DAYS));
        // SECRET_KEY 문자열로 Secret Key 생성
        byte[] keyBytes = SECRET_KEY.getBytes();
        Key key = Keys.hmacShaKeyFor(keyBytes);
        // JWT Token 생성
        return Jwts.builder()
                // header
                .signWith(key, SignatureAlgorithm.HS512) // 구버전: .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                // payload
                .setSubject(userEntity.getId()) // sub
                .setIssuer("demo app") // iss
                .setIssuedAt(new Date()) // iat
                .setExpiration(expiryDate) // exp
                .compact();
        /*
		{ // header
		  "alg":"HS512"
		}.
		{ // payload
		  "sub":"40288093784915d201784916a40c0001",
		  "iss": "demo app",
		  "iat":1595733657,
		  "exp":1596597657
		}.
	    { // signature: Secret Key를 이용
	    Nn4d1MOVLZg79sfFACTIpCPKqWmpZMZQsbNrXdJJNWkRv50_l7bPLQPwhMobT4vBOG6Q3JYjhDrKFlBSaUxZOg
		}
        */
    }

    public String validateAndGetUserId(String token) {
        // parseClaimsJws(): Base64로 디코딩 및 파싱
        // setSigningKey(): 시크릿키로 서명한 후 token의 서명과 비교하여 페이로드(Claims) 리턴, 위조 됬다면 예외를 리턴
        // getBody(): Body안의 userId가 필요하므로 Body꺼냄
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }
}
