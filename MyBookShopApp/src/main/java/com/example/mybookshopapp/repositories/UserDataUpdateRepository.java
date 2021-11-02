package com.example.mybookshopapp.repositories;

import com.example.mybookshopapp.data.user.UserDataUpdate;
import com.example.mybookshopapp.security.BookStoreUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDataUpdateRepository extends JpaRepository<UserDataUpdate, Integer> {

    UserDataUpdate findUserDataUpdateByToken(String token);
}
