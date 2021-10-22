package com.example.mybookshopapp.selenium;

import org.openqa.selenium.chrome.ChromeDriver;

public class PopularPage extends AbstractPage {

    public PopularPage(ChromeDriver driver) {
        super(driver);
    }

    @Override
    protected AbstractPage callPage() {
        driver.get(super.url + "books/popular");
        return this;
    }
}
