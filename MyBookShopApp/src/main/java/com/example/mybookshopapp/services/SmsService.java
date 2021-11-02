package com.example.mybookshopapp.services;

import com.example.mybookshopapp.data.SmsCode;
import com.example.mybookshopapp.repositories.SmsCodeRepository;
import com.example.mybookshopapp.security.ContactConfirmationPayload;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class SmsService {

    @Value("${twilio.ACCOUNT_SID}")
    private String ACCOUNT_SID;

    @Value("${twilio.AUTH_TOKEN}")
    private String AUTH_TOKEN;

    @Value("${twilio.TWILIO_NUMBER}")
    private String TWILIO_NUMBER;

    private final SmsCodeRepository smsCodeRepository;
    private final JavaMailSender javaMailSender;

    @Autowired
    public SmsService(SmsCodeRepository smsCodeRepository, JavaMailSender javaMailSender) {
        this.smsCodeRepository = smsCodeRepository;
        this.javaMailSender = javaMailSender;
    }

    public String sendSecretCodeSms(String contact) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        String formattedContact = contact.replaceAll("[()-]", "");
        String generateCode = generateCode();
        Message.creator(
                new PhoneNumber(formattedContact),
                new PhoneNumber(TWILIO_NUMBER),
                "Your secret code is: " + generateCode
        ).create();
        return generateCode;
    }

    public String generateCode() {
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();
        while (stringBuilder.length() < 6) {
            stringBuilder.append(random.nextInt(9));
        }
        stringBuilder.insert(3, " ");
        return stringBuilder.toString();
    }

    public void saveNewCode(SmsCode smsCode) {
        if (smsCodeRepository.findByCode(smsCode.getCode()) == null) {
            smsCodeRepository.save(smsCode);
        }
    }

    public Boolean verifyCode(String code) {
        SmsCode smsCode = smsCodeRepository.findByCode(code);
        return (smsCode != null && !smsCode.isExpired());
    }

    public void sendCodeByEmail(ContactConfirmationPayload payload) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("alex_under91@mail.ru");
        message.setTo(payload.getContact());
        SmsCode smsCode = new SmsCode(generateCode(), 300); //expire in 5 minutes
        saveNewCode(smsCode);
        message.setSubject("Bookstore email verification");
        message.setText("Verification code is " + smsCode.getCode());
        javaMailSender.send(message);
    }
}
