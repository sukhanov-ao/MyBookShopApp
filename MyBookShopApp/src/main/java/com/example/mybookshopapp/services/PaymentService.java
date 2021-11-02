package com.example.mybookshopapp.services;

import com.example.mybookshopapp.data.book.Book;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Service
public class PaymentService {
    @Value("${robokassa.merchant.login}")
    private String merchantLogin;

    @Value("${robokassa.pass.first.test}")
    private String passFirstTest;

    public String getPaymentUrl(Integer sum, Integer invId) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.update((merchantLogin + ":" + sum + ":" + invId + ":" + passFirstTest).getBytes());
        return "https://auth.robokassa.ru/Merchant/Index.aspx" +
                "?MerchantLogin=" + merchantLogin +
                "&InvId=" + invId +
                "&Culture=ru" +
                "&Encoding=utf-8" +
                "&OutSum=" + sum +
                "&SignatureValue=" + DatatypeConverter.printHexBinary(messageDigest.digest()).toUpperCase() +
                "&IsTest=1";
    }

    public String getPaymentUrl(List<Book> booksFromCookieSlug) throws NoSuchAlgorithmException {
        Double paymentSumTotal = booksFromCookieSlug.stream().mapToDouble(Book::discountPrice).sum();
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        String invId = "5"; //test purposes
        messageDigest.update((merchantLogin + ":" + paymentSumTotal + ":" + invId + ":" + passFirstTest).getBytes());
        return "https://auth.robokassa.ru/Merchant/Index.aspx" +
                "?MerchantLogin=" + merchantLogin +
                "&InvId=" + invId +
                "&Culture=ru" +
                "&Encoding=utf-8" +
                "&OutSum=" + paymentSumTotal +
                "&SignatureValue=" + DatatypeConverter.printHexBinary(messageDigest.digest()).toUpperCase() +
                "&IsTest=1";
    }
}
