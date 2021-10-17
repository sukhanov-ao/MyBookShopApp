package com.example.mybookshopapp.security.jwt;

import org.springframework.stereotype.Service;

@Service
public class JwtBlackListService {

    private final JwtBlackListRepository jwtBlackListRepository;

    public JwtBlackListService(JwtBlackListRepository jwtBlackListRepository) {
        this.jwtBlackListRepository = jwtBlackListRepository;
    }

    public JWTBlackList getByToken(String token) {
        return this.jwtBlackListRepository.findJwtBlacklistByToken(token);
    }

    public JWTBlackList saveToken(JWTBlackList jwtBlacklist) {
        return this.jwtBlackListRepository.save(jwtBlacklist);
    }
}
