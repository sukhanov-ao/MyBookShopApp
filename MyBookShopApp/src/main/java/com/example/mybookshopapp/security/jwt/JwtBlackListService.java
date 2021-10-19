package com.example.mybookshopapp.security.jwt;

import org.springframework.stereotype.Service;

@Service
public class JwtBlackListService {

    private final JwtBlackListRepository jwtBlackListRepository;

    public JwtBlackListService(JwtBlackListRepository jwtBlackListRepository) {
        this.jwtBlackListRepository = jwtBlackListRepository;
    }

    public Boolean getByToken(String token) {
        return jwtBlackListRepository.findJwtBlacklistByToken(token) != null;
    }

    public void saveToken(JWTBlackList jwtBlacklist) {
        jwtBlackListRepository.save(jwtBlacklist);
    }
}
