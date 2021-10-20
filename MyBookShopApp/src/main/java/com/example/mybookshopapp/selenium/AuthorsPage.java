package com.example.mybookshopapp.selenium;

import org.openqa.selenium.chrome.ChromeDriver;

public class AuthorsPage extends AbstractPage {

    public AuthorsPage(ChromeDriver driver) {
        super(driver);
    }

    @Override
    protected AbstractPage callPage() {
        driver.get(super.url + "authors");
        return this;
    }
}
