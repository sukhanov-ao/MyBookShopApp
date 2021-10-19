package com.example.mybookshopapp.security.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

public class CustomLogoutHandler implements LogoutSuccessHandler {
    private JwtBlackListService jwtBlackListService;

    @Autowired
    public CustomLogoutHandler(JwtBlackListService jwtBlackListService) {
        this.jwtBlackListService = jwtBlackListService;
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
                                Authentication authentication) throws IOException {
        String token = null;
        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals("token")) {
                token = cookie.getValue();
            }
        }
        if (token != null && jwtBlackListService.getByToken(token)) {
            JWTBlackList jwtBlacklist = new JWTBlackList();
            jwtBlacklist.setToken(token);
            jwtBlackListService.saveToken(jwtBlacklist);

        }
        SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
        securityContextLogoutHandler.logout(request, response, authentication);

        response.sendRedirect("/");
    }
}
