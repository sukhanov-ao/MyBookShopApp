package com.example.mybookshopapp.security.jwt;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JwtBlackListRepository extends JpaRepository<JWTBlackList, Integer> {
    JWTBlackList findJwtBlacklistByToken(String token);
}
