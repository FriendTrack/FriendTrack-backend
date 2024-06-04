package com.ciklon.friendtracker.common.auth.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class TokenRepository {
    private final RedisTemplate<String, String> redisTemplate;

    public void save(String key, String value, long lifetime) {
        redisTemplate.opsForValue().set(key, value);
        redisTemplate.expire(key, lifetime, TimeUnit.MILLISECONDS);
    }

    public Boolean checkToken(String key) {
        return redisTemplate.hasKey(key);
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }

    public String getToken(String key) {
        return redisTemplate.opsForValue().get(key);
    }
}
