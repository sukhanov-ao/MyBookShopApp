package com.example.mybookshopapp.repositories;

import com.example.mybookshopapp.data.SmsCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SmsCodeRepository extends JpaRepository<SmsCode, Long> {

    public SmsCode findByCode(String code);


}
