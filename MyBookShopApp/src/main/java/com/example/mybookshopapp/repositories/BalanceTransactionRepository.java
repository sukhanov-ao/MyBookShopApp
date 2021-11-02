package com.example.mybookshopapp.repositories;

import com.example.mybookshopapp.data.payments.BalanceTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BalanceTransactionRepository extends JpaRepository<BalanceTransaction, Integer> {

    List<BalanceTransaction> findBalanceTransactionByUserId(Integer id);
}
