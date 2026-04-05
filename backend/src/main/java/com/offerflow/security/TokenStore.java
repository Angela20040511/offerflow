package com.offerflow.security;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TokenStore {
    private final Map<String, LoginUser> tokenMap = new ConcurrentHashMap<>();

    public LoginUser create(LoginUser loginUser) {
        String token = "token_" + UUID.randomUUID().toString().replace("-", "");
        loginUser.setToken(token);
        tokenMap.put(token, loginUser);
        return loginUser;
    }

    public LoginUser get(String token) {
        return tokenMap.get(token);
    }

    public void remove(String token) {
        tokenMap.remove(token);
    }
}
