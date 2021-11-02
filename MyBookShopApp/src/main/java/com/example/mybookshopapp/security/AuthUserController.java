package com.example.mybookshopapp.security;

import com.example.mybookshopapp.data.SmsCode;
import com.example.mybookshopapp.data.user.UserDataUpdate;
import com.example.mybookshopapp.services.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Controller
public class AuthUserController {

    private final BookStoreUserRegister userRegister;
    private final SmsService smsService;
    private final JavaMailSender javaMailSender;

    @Autowired
    public AuthUserController(BookStoreUserRegister userRegister, SmsService smsService, JavaMailSender javaMailSender) {
        this.userRegister = userRegister;
        this.smsService = smsService;
        this.javaMailSender = javaMailSender;
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
        if (!payload.getContact().contains("@")) {
            String smsCodeString = smsService.sendSecretCodeSms(payload.getContact());
            smsService.saveNewCode(new SmsCode(smsCodeString, 60)); //expires in one minute
        }

        response.setResult("true");
        return response;
    }

    @PostMapping("/requestEmailConfirmation")
    @ResponseBody
    public ContactConfirmationResponse handleRequestEmailConfirmation(@RequestBody ContactConfirmationPayload payload) {
        ContactConfirmationResponse response = new ContactConfirmationResponse();
        smsService.sendCodeByEmail(payload);
        response.setResult("true");

        return response;
    }

    @PostMapping("/approveContact")
    @ResponseBody
    public ContactConfirmationResponse handleApproveContact(@RequestBody ContactConfirmationPayload payload) {
        ContactConfirmationResponse response = new ContactConfirmationResponse();

        if (smsService.verifyCode(payload.getCode())) {
            response.setResult("true");
            return response;
        }

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

    @PostMapping("/login-by-phone-number")
    @ResponseBody
    public ContactConfirmationResponse handleLoginByPhoneNumber(@RequestBody ContactConfirmationPayload payload,
                                                                HttpServletResponse httpServletResponse) {
        if (smsService.verifyCode(payload.getCode())) {
            ContactConfirmationResponse loginResponse = userRegister.jwtLoginByPhoneNumber(payload);
            Cookie cookie = new Cookie("token", loginResponse.getResult());
            httpServletResponse.addCookie(cookie);
            return loginResponse;
        } else {
            return null;
        }
    }

    @GetMapping("/my")
    public String handleMy(Model model) {
        model.addAttribute("currentUser", userRegister.getCurrentUser());
        return "my";
    }

    @GetMapping("/token_verification/{token}")
    public String handleTokenVerification(@PathVariable("token") String token) {
        UserDataUpdate userDataUpdate = userRegister.findUserDataUpdate(token);
        if (userDataUpdate != null) {
            BookStoreUser bookStoreUser = userRegister.findUserById(userDataUpdate.getUserId());
            bookStoreUser.setName(userDataUpdate.getName());
            bookStoreUser.setEmail(userDataUpdate.getEmail());
            bookStoreUser.setPhone(userDataUpdate.getPhone());
            bookStoreUser.setPassword(userDataUpdate.getPassword());
            userRegister.updateUserData(bookStoreUser);
            return "redirect:/profile";
        }
        return "redirect:/signin";
    }
}
