package com.example.mybookshopapp.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class MainPage extends AbstractPage {

    public MainPage(ChromeDriver driver) {
        super(driver);
    }

    @Override
    protected AbstractPage callPage() {
        return super.callPage();
    }

    @Override
    protected AbstractPage pause() throws InterruptedException {
        return super.pause();
    }

    @Override
    public AbstractPage submitById(String id) {
        return super.submitById(id);
    }

    @Override
    public AbstractPage clickByXpath(String xpath) {
        return super.clickByXpath(xpath);
    }

    @Override
    public AbstractPage setUpSearchToken(String token) {
        return super.setUpSearchToken(token);
    }
}
