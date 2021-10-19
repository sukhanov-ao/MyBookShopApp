package com.example.mybookshopapp.security;

import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Entity
@Table(name = "users")
public class BookStoreUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String login;
    private String email;
    private String phone;
    private String password;

    @Column(name = "is_oauth2")
    @ColumnDefault("false")
    private Boolean isOAuth2;

    @Column(name = "id_oauth")
    private Integer idOAuth;

    public Boolean getOAuth2() {
        return isOAuth2;
    }

    public void setOAuth2(Boolean OAuth2) {
        isOAuth2 = OAuth2;
    }

    public Integer getIdOAuth() {
        return idOAuth;
    }

    public void setIdOAuth(Integer idOAuth) {
        this.idOAuth = idOAuth;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}
