package com.example.mybookshopapp.security;

import com.example.mybookshopapp.security.jwt.CustomLogoutHandler;
import com.example.mybookshopapp.security.jwt.JWTRequestFilter;
import com.example.mybookshopapp.security.jwt.JwtBlackListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig  extends WebSecurityConfigurerAdapter {

    private final BookStoreUserDetailService bookStoreUserDetailService;
    private final JWTRequestFilter jwtRequestFilter;
    private final JwtBlackListService jwtBlackListService;

    public SecurityConfig(BookStoreUserDetailService bookStoreUserDetailService, JWTRequestFilter jwtRequestFilter, JwtBlackListService jwtBlackListService) {
        this.bookStoreUserDetailService = bookStoreUserDetailService;
        this.jwtRequestFilter = jwtRequestFilter;
        this.jwtBlackListService = jwtBlackListService;
    }

    @Bean
    PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(bookStoreUserDetailService)
                .passwordEncoder(getPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()
                .authorizeRequests()
                .antMatchers("/my", "/profile", "/books/changeBookStatus/vote/**", "/books/*/img/save", "/books/bookReview/**").authenticated()
                .antMatchers("/**").permitAll()
                .and().formLogin()
                .loginPage("/signin").failureUrl("/signin")//страница логина
                .and().logout().logoutUrl("/logout")
                .logoutSuccessHandler(new CustomLogoutHandler(jwtBlackListService))
                .logoutSuccessUrl("/signin").deleteCookies("token")
                .and().oauth2Login()
                .and().oauth2Client();

//        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        http.exceptionHandling().accessDeniedHandler(new AccessDeniedHandlerImpl());
    }
}
