package com.example.mybookshopapp.services;

//import com.example.mybookshopapp.data.user.UserUpdateData;

import com.example.mybookshopapp.data.user.UserDataUpdate;
import com.example.mybookshopapp.repositories.UserDataUpdateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UserDataUpdateService {
    private final JavaMailSender javaMailSender;
    private final UserDataUpdateRepository userDataUpdateRepository;

    @Autowired
    public UserDataUpdateService(JavaMailSender javaMailSender, UserDataUpdateRepository userDataUpdateRepository) {
        this.javaMailSender = javaMailSender;
        this.userDataUpdateRepository = userDataUpdateRepository;
    }

    public void sendEmailConfirmation(String phone, String mail, String name, String password, Integer userId) throws MailException {
        String token = generateCode();
        UserDataUpdate userDataUpdate = new UserDataUpdate();
        userDataUpdate.setToken(token);
        userDataUpdate.setUserId(userId);
        userDataUpdate.setEmail(mail);
        userDataUpdate.setPhone(phone);
        userDataUpdate.setPassword(password);
        userDataUpdate.setName(name);
        userDataUpdate.setExpireTime(LocalDateTime.now());
        userDataUpdateRepository.save(userDataUpdate);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("alex_under91@mail.ru");
        message.setTo(userDataUpdate.getEmail());
        String messageText = setMessageText(userDataUpdate, token);
        message.setSubject("Bookstore user data change confirmation");
        message.setText("Verification code is " + messageText);
        javaMailSender.send(message);
    }

    private String setMessageText(UserDataUpdate userDataUpdate, String token) {
        StringBuilder messageContent = new StringBuilder();
        messageContent
                .append("Ваша учетная запись была изменена!\n")
                .append("Name: " + userDataUpdate.getName() + "\n")
                .append("Email: " + userDataUpdate.getEmail() + "\n")
                .append("Phone: " + userDataUpdate.getPhone() + "\n")
                .append("New Password: " + userDataUpdate.getPassword() + "\n")
                .append("Для подтверждения изменение перейдите по ссылке\n" + createLink(token));
        return messageContent.toString();

    }

    private String createLink(String token) {
        return "http://localhost:8085/token_verification/" + token;
    }

    private String generateCode() {
        return UUID.randomUUID().toString();
    }

    public UserDataUpdate findUserDataUpdateByToken(String token){
        return userDataUpdateRepository.findUserDataUpdateByToken(token);
    }

    public void deleteUpdateData(UserDataUpdate updateData) {
        userDataUpdateRepository.delete(updateData);
    }
}
