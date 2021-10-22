package com.example.mybookshopapp.security;

import com.example.mybookshopapp.security.jwt.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class BookStoreUserRegister {

    private final BookStoreUserRepository bookStoreUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final BookStoreUserDetailService bookStoreUserDetailService;
    private final JWTUtil jwtUtil;


    @Autowired
    public BookStoreUserRegister(BookStoreUserRepository bookStoreUserRepository, PasswordEncoder passwordEncoder,
                                 AuthenticationManager authenticationManager, BookStoreUserDetailService bookStoreUserDetailService,
                                 JWTUtil jwtUtil) {
        this.bookStoreUserRepository = bookStoreUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.bookStoreUserDetailService = bookStoreUserDetailService;
        this.jwtUtil = jwtUtil;
    }

    public BookStoreUser registerNewUser(RegistrationForm registrationForm) {
        if (bookStoreUserRepository.findBookStoreUserByEmail(registrationForm.getEmail()) == null) {
            BookStoreUser user = new BookStoreUser();
            user.setName(registrationForm.getName());
            user.setEmail(registrationForm.getEmail());
            user.setPhone(registrationForm.getPhone());
            user.setPassword(passwordEncoder.encode(registrationForm.getPassword()));
            bookStoreUserRepository.save(user);
            return user;
        }
        return null;
    }

    public ContactConfirmationResponse jwtLogin(ContactConfirmationPayload payload) {
        if (payload != null && !payload.getContact().startsWith("+")) {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(payload.getContact(), payload.getCode()));
        }
        BookStoreUserDetails userDetails = (BookStoreUserDetails) bookStoreUserDetailService.loadUserByUsername(payload.getContact());
        String jwtToken = jwtUtil.generateToken(userDetails);
        ContactConfirmationResponse response = new ContactConfirmationResponse();
        response.setResult(jwtToken);
        return response;
    }

    public ContactConfirmationResponse login(ContactConfirmationPayload payload) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(payload.getContact(), payload.getCode()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        ContactConfirmationResponse response = new ContactConfirmationResponse();
        response.setResult("true");
        return response;
    }

    public BookStoreUser getCurrentUser() {
        BookStoreUserDetails userDetails = (BookStoreUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getBookStoreUser();
    }
}
