package com.example.mybookshopapp.security;

import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class BookStoreUserDetailService implements UserDetailsService {

    private final BookStoreUserRepository bookStoreUserRepository;

    @Autowired
    public BookStoreUserDetailService(BookStoreUserRepository bookStoreUserRepository) {
        this.bookStoreUserRepository = bookStoreUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        BookStoreUser bookStoreUser = bookStoreUserRepository.findBookStoreUserByEmail(s);
        if (bookStoreUser != null) {
            return new BookStoreUserDetails(bookStoreUser);
        } else {
            throw new UsernameNotFoundException("user not found!");
        }
    }
}