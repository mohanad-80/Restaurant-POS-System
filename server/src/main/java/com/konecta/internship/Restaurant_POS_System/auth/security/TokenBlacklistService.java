package com.konecta.internship.Restaurant_POS_System.auth.security;

import com.konecta.internship.Restaurant_POS_System.auth.security.jwt.utils.JwtUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
public class TokenBlacklistService {

    private final Set<String> blacklistedTokens = ConcurrentHashMap.newKeySet();
    private final JwtUtils jwtUtils;

    public TokenBlacklistService(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    public void blacklistToken(String token) {
        blacklistedTokens.add(token);
        scheduleTokenCleanup(token);
    }

    public boolean isTokenBlacklisted(String token) {
        return blacklistedTokens.contains(token);
    }

    private void scheduleTokenCleanup(String token) {
        Date expiration = jwtUtils.extractExpiration(token);
        long delay = expiration.getTime() - System.currentTimeMillis();

        if (delay > 0) {
            CompletableFuture.delayedExecutor(delay, TimeUnit.MILLISECONDS)
                    .execute(() -> blacklistedTokens.remove(token));
        }
    }
}