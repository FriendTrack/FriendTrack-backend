package com.ciklon.friendtracker.common.auth.util;

import com.ciklon.friendtracker.common.auth.props.JwtProps;
import com.ciklon.friendtracker.common.auth.repository.TokenRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@EnableConfigurationProperties(JwtProps.class)
public class JwtUtils {
    private final TokenRepository tokenRepository;
    private final JwtProps jwtProps;
    private final JwtService jwtService;

    public void saveToken(String key, String value) {
        tokenRepository.save(key, value, jwtProps.getRefreshExpirationTime());
    }

    public void deleteToken(String key) {
        tokenRepository.delete(key);
    }

    public String getToken(String key) {
        return tokenRepository.getToken(key);
    }

    public String generateAccessToken(String login, UUID userId) {
        return jwtService.generateAccessToken(userId.toString(), login);
    }

    public String getOrGenerateRefreshToken(String login, UUID userId) {
        String token = tokenRepository.getToken(userId.toString());
        if (token != null) {
            return token;
        }
        return jwtService.generateRefreshToken(userId.toString(), login);
    }

    public Claims extractAllClaimsFromAccessToken(String accessToken) {
        return jwtService.extractAllClaimsFromAccessToken(accessToken);
    }

    public String getSubject(String token) {
        return jwtService.getSubject(token);
    }

    public String getLogin(String token) {
        return jwtService.extractLogin(token);
    }

    public boolean validateToken(String token) {
        return jwtService.validateToken(token);
    }
}
