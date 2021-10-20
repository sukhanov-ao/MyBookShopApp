package com.example.mybookshopapp.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.sql.Driver;

public class GenresPage extends AbstractPage {

    public GenresPage(ChromeDriver driver) {
        super(driver);
    }

    @Override
    protected AbstractPage callPage() {
        driver.get(super.url + "genres");
        return this;
    }
}
