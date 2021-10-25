package com.example.mybookshopapp.services;

import com.example.mybookshopapp.data.payments.BalanceTransaction;
import com.example.mybookshopapp.data.payments.PaymentPayload;
import com.example.mybookshopapp.repositories.BalanceTransactionRepository;
import com.example.mybookshopapp.security.BookStoreUser;
import com.example.mybookshopapp.security.BookStoreUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BalanceTransactionService {

    private final BalanceTransactionRepository balanceTransactionRepository;

    @Autowired
    public BalanceTransactionService(BalanceTransactionRepository balanceTransactionRepository) {
        this.balanceTransactionRepository = balanceTransactionRepository;
    }

    public Integer saveTransaction(PaymentPayload payload, BookStoreUserDetails bookStoreUser) {
        BalanceTransaction balanceTransaction = new BalanceTransaction();
        balanceTransaction.setTime(payload.getTime());
        balanceTransaction.setValue(payload.getSum());
        balanceTransaction.setUserId(bookStoreUser.getBookStoreUser().getId());
        balanceTransaction.setDescription("Пополнение счета через ROBOKASSA");
        balanceTransaction = balanceTransactionRepository.save(balanceTransaction);
        return balanceTransaction.getId();
    }

    public List<BalanceTransaction> findBalanceTransactionByUserId(BookStoreUser bookStoreUser) {
        return balanceTransactionRepository.findBalanceTransactionByUserId(bookStoreUser.getId());
    }
}
