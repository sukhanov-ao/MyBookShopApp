package com.example.mybookshopapp.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
public class AuthUserController {

    private final BookStoreUserRegister userRegister;

    @Autowired
    public AuthUserController(BookStoreUserRegister userRegister) {
        this.userRegister = userRegister;
    }

    @GetMapping("signin")
    public String handleSignIn() {
        return "signin";
    }

    @GetMapping("/signup")
    public String handleSignUp(Model model) {
        model.addAttribute("regForm", new RegistrationForm());
        return "signup";
    }

    @PostMapping("/requestContactConfirmation")
    @ResponseBody
    public ContactConfirmationResponse handleRequestContactConfirmation(@RequestBody ContactConfirmationPayload payload) {
        ContactConfirmationResponse response = new ContactConfirmationResponse();
        response.setResult("true");
        return response;
    }

    @PostMapping("/approveContact")
    @ResponseBody
    public ContactConfirmationResponse handleApproveContact(@RequestBody ContactConfirmationPayload payload) {
        ContactConfirmationResponse response = new ContactConfirmationResponse();
        response.setResult("true");
        return response;
    }

    @PostMapping("/reg")
    public String handleUserRegistration(RegistrationForm registrationForm, Model model) {
        userRegister.registerNewUser(registrationForm);
        model.addAttribute("regOk", true);
        return "signin";
    }

    @PostMapping("/login")
    @ResponseBody
    public ContactConfirmationResponse handleLogin(@RequestBody ContactConfirmationPayload payload,
                                                   HttpServletResponse httpServletResponse) {
        ContactConfirmationResponse loginResponse = userRegister.jwtLogin(payload);
        Cookie cookie = new Cookie("token", loginResponse.getResult());
        httpServletResponse.addCookie(cookie);
        return loginResponse;
    }

    @GetMapping("/my")
    public String handleMy(Model model) {
        model.addAttribute("currentUser", userRegister.getCurrentUser());
        return "my";
    }

    @GetMapping("/profile")
    public String handleProfile(Model model) {
        model.addAttribute("currentUser", userRegister.getCurrentUser());
        return "profile";
    }

//    @GetMapping("/logout")
//    public String handleLogout(HttpServletRequest request) {
//        HttpSession session = request.getSession();
//        SecurityContextHolder.clearContext();
//        if(session != null) {
//            session.invalidate();
//        }
//        for(Cookie cookie : request.getCookies()) {
//            cookie.setMaxAge(0);
//        }
//        return "redirect:/";
//    }
}
