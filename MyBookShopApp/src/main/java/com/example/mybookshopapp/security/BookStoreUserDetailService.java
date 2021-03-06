package com.example.mybookshopapp.security;

import org.springframework.beans.factory.annotation.Autowired;
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
    public CustomUserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        BookStoreUser bookStoreUser = bookStoreUserRepository.findBookStoreUserByEmail(s);

        if (bookStoreUser != null) {
            return new BookStoreUserDetails(bookStoreUser);
        }

        bookStoreUser = bookStoreUserRepository.findBookStoreUserByPhone(s);

        if (bookStoreUser != null) {
            return new PhoneNumberUserDetails(bookStoreUser);
        } else {
            throw new UsernameNotFoundException("user not found");
        }
    }
}
