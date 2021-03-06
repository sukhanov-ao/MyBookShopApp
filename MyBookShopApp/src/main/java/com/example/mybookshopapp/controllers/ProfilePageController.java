package com.example.mybookshopapp.controllers;

import com.example.mybookshopapp.data.payments.BalanceTransaction;
import com.example.mybookshopapp.data.payments.PaymentPayload;
import com.example.mybookshopapp.errors.InvalidPasswordException;
import com.example.mybookshopapp.security.BookStoreUser;
import com.example.mybookshopapp.security.BookStoreUserRegister;
import com.example.mybookshopapp.services.BalanceTransactionService;
import com.example.mybookshopapp.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import java.security.NoSuchAlgorithmException;
import java.util.List;

@Controller
public class ProfilePageController {

    private final BookStoreUserRegister userRegister;
    private final BalanceTransactionService balanceTransactionService;
    private final PaymentService paymentService;

    @Autowired
    public ProfilePageController(BookStoreUserRegister userRegister, BalanceTransactionService balanceTransactionService, PaymentService paymentService) {
        this.userRegister = userRegister;
        this.balanceTransactionService = balanceTransactionService;
        this.paymentService = paymentService;
    }

    @GetMapping("/profile")
    public String handleProfile(Model model) {
        List<BalanceTransaction> balanceTransactionList = balanceTransactionService
                .findBalanceTransactionByUserId(userRegister.getCurrentUser());
        model.addAttribute("currentUser", userRegister.getCurrentUser());
        model.addAttribute("userTransactions", balanceTransactionList);
        return "profile";
    }

    @PostMapping("/profile/edit")
    public String editProfile(@RequestParam("phone") String phone,
                              @RequestParam("mail") String email,
                              @RequestParam("name") String name,
                              @RequestParam("password") String password,
                              @RequestParam("passwordReply") String passwordReply,
                              Model model) throws InvalidPasswordException {
        BookStoreUser bookStoreUser = userRegister.getCurrentUser();
        model.addAttribute("currentUser", bookStoreUser);
        checkPassword(password, passwordReply, model);
        try {
            userRegister.editProfile(bookStoreUser, phone, email, name, password, passwordReply);
        } catch (MailException e) {
            model.addAttribute("mailDelivery", "???? ?????????????? ?????????????????? ?????????????????? ???? ?????????????????? ??????????");
        }
        model.addAttribute("emailNotification", "?????? ?????????????????????????? ?????????????????? ???????????????????? ?????????????? ???? ????????????, ???????????????????????? ?????? ???? ??????????");
        return "/profile";

    }

    private void checkPassword(String password, String passwordReply, Model model) throws InvalidPasswordException {
        if (password.length() < 6 || password.trim().isEmpty()) {
            model.addAttribute("passwordSizeError", "???????????? ???????????? ???????? ???????????? ?????????? ????????????????");
            model.addAttribute("successfulSave", false);
            throw new InvalidPasswordException("???????????? ???????????? ???????? ???????????? ?????????? ????????????????");
        } else if (!password.equals(passwordReply)) {
            model.addAttribute("passwordError", "???????????? ???? ??????????????????");
            model.addAttribute("successfulSave", false);
            throw new InvalidPasswordException("???????????? ???? ??????????????????");
        } else {
            model.addAttribute("successfulSave", true);
        }
    }

    @PostMapping(value = "/payment")
    public RedirectView handleFillAccountBalance(@RequestBody PaymentPayload payload) throws NoSuchAlgorithmException {
        BookStoreUser bookStoreUser = userRegister.getCurrentUser();
        Integer invId = balanceTransactionService.saveTransaction(payload, bookStoreUser);
        String paymentUrl = paymentService.getPaymentUrl(payload.getSum(), invId);
        return new RedirectView(paymentUrl);
    }

}
