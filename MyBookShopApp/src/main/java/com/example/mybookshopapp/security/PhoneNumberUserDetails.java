package com.example.mybookshopapp.security;

public class PhoneNumberUserDetails extends BookStoreUserDetails {

    public PhoneNumberUserDetails(BookStoreUser bookStoreUser) {
        super(bookStoreUser);
    }

    @Override
    public String getUsername() {
        return getBookStoreUser().getPhone();
    }
}
