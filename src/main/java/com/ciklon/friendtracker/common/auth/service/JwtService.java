package com.ciklon.friendtracker.common.auth.service;

import com.ciklon.friendtracker.common.auth.props.JwtProps;
import com.ciklon.friendtracker.common.exception.CustomException;
import com.ciklon.friendtracker.common.exception.ExceptionType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.function.Function;

import static com.ciklon.friendtracker.api.constant.ApiPaths.LOGIN;


@Slf4j
@Service
@RequiredArgsConstructor
@EnableConfigurationProperties(JwtProps.class)
public class JwtService {

    private final JwtProps jwtProps;

    public String generateAccessToken(String userId, String login) {
        return Jwts.builder()
                .setSubject(userId)
                .claim(LOGIN, login)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtProps.getAccessExpirationTime()))
                .signWith(getAccessSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(String userId, String login) {
        return Jwts.builder()
                .setSubject(userId)
                .claim(LOGIN, login)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtProps.getRefreshExpirationTime()))
                .signWith(getRefreshSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims extractAllClaimsFromAccessToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getAccessSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            log.error("Token is not valid: {}", e.getMessage());
            throw new CustomException(ExceptionType.UNAUTHORIZED, "Token is not valid");
        }
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaimsFromAccessToken(token);
        return claimsResolver.apply(claims);
    }

    private Key getAccessSigningKey() {
        return Keys.hmacShaKeyFor(jwtProps.getAccessSecret().getBytes(StandardCharsets.UTF_8));
    }

    private Key getRefreshSigningKey() {
        return Keys.hmacShaKeyFor(jwtProps.getRefreshSecret().getBytes(StandardCharsets.UTF_8));
    }


    public String getSubject(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public boolean validateToken(String token) {
        try {
            Date expiration = extractExpiration(token);
            return expiration.after(new Date());
        } catch (Exception e) {
            log.error("Token is not valid. Check his expiration: {}", e.getMessage());
            throw new CustomException(ExceptionType.UNAUTHORIZED, "Token is expired or invalid");
        }
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public String extractLogin(String token) {
        return extractClaim(token, claims -> claims.get(LOGIN, String.class));
    }
}
