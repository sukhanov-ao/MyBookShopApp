package com.example.mybookshopapp.selenium;

import org.openqa.selenium.chrome.ChromeDriver;

public class RecentPage extends AbstractPage{
    public RecentPage(ChromeDriver driver) {
        super(driver);
    }

    @Override
    protected AbstractPage callPage() {
        driver.get(super.url + "books/recent");
        return this;
    }
}
