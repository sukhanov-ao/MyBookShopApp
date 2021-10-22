package com.example.mybookshopapp.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public abstract class AbstractPage {
    protected String url = "http://www.localhost:8085/";
    protected ChromeDriver driver;

    public AbstractPage(ChromeDriver driver) {
        this.driver = driver;
    }

    protected AbstractPage callPage() {
        driver.get(url);
        return this;
    }

    protected AbstractPage pause() throws InterruptedException {
        Thread.sleep(2000);
        return this;
    }

    public AbstractPage submitById(String id) {
        WebElement element = driver.findElement(By.id(id));
        element.submit();
        return this;
    }

    public AbstractPage clickByXpath(String xpath) {
        WebElement element = driver.findElement(By.xpath(xpath));
        element.click();
        return this;
    }

    public AbstractPage clickById(String id) {
        WebElement element = driver.findElement(By.id(id));
        element.click();
        return this;
    }

    public AbstractPage setUpSearchToken(String token) {
        WebElement element = driver.findElement(By.id("query"));
        element.sendKeys(token);
        return this;
    }
}
