package com.example.mybookshopapp.selenium;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MainPageSeleniumTests {

    private static ChromeDriver driver;

    @BeforeAll
    static void setup() {
        System.setProperty("webdriver.chrome.driver", "D:\\Projects\\chromedriver_win32\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().timeouts().pageLoadTimeout(5, TimeUnit.SECONDS);
        driver.manage().window().maximize();
    }

    @AfterAll
    static void tearDown() {
        driver.quit();
    }

    @Test
    void testMainPageAccess() throws InterruptedException {
        MainPage mainPage = new MainPage(driver);
        mainPage
                .callPage()
                .pause();

        assertTrue(driver.getPageSource().contains("BOOKSHOP"));
    }

    @Test
    void testMainPageSearchByQuery() throws InterruptedException {
        MainPage mainPage = new MainPage(driver);
        mainPage
                .callPage()
                .pause()
                .setUpSearchToken("Messenger")
                .pause()
                .submitById("search")
                .pause();

        assertTrue(driver.getPageSource().contains("Kill the Messenger"));
    }

    @Test
    void testHeadersAccess() throws InterruptedException {
        MainPage mainPage = new MainPage(driver);
        mainPage
                .callPage()
                .pause()
                .clickByXpath("//a[@href='/genres']")
                .pause();
        assertTrue(driver.getPageSource().contains("Жанры"));

        mainPage
                .clickByXpath("//a[@href='/books/recent']")
                .pause();
        assertTrue(driver.getPageSource().contains("Recent"));

        mainPage
                .clickByXpath("//a[@href='/books/popular']")
                .pause();
        assertTrue(driver.getPageSource().contains("Popular"));

        mainPage
                .clickByXpath("//a[@href='/authors']")
                .pause();
        assertTrue(driver.getPageSource().contains("Authors"));

        mainPage
                .clickByXpath("//a[@href='/authors']")
                .pause();
        assertTrue(driver.getPageSource().contains("Authors"));

        mainPage
                .clickByXpath("//a[@href='/books/postponed']")
                .pause();
        assertTrue(driver.getPageSource().contains("В отложенном ничего нет"));

        mainPage
                .clickByXpath("//a[@href='/books/cart']")
                .pause();
        assertTrue(driver.getPageSource().contains("Корзина пуста"));
    }

    @Test
    void genresAccess() throws InterruptedException {
        GenresPage genresPage = new GenresPage(driver);
        genresPage
                .callPage()
                .pause()
                .clickByXpath("//a[@href='/genres/1']")
                .pause();
        assertTrue(driver.getPageSource().contains("Лёгкое чтение"));
        genresPage
                .clickByXpath("//li[@class='breadcrumbs-item']//a[@href='/']")
                .pause();
        assertTrue(driver.getPageSource().contains("Books by tags"));
    }

    @Test
    void recentAccess() throws InterruptedException {
        RecentPage recentPage = new RecentPage(driver);
        recentPage
                .callPage()
                .pause();

        driver.executeScript("window.scrollTo(0, document.body.scrollHeight)");

        recentPage
                .pause()
                .clickByXpath("//a[@class='btn btn_primary']")
                .pause();
        assertTrue(driver.getPageSource().contains("скидка"));

        recentPage
                .callPage()
                .pause()
                .clickByXpath("//div[@class='Card-picture']//a[@href='/books/book-jvt-339']")
                .pause();
        assertTrue(driver.getPageSource().contains("Авторизуйтесь, чтобы проголосовать"));

        recentPage
                .clickByXpath("//li[@class='breadcrumbs-item']//a[@href='/']")
                .pause();
        assertTrue(driver.getPageSource().contains("Books by tags"));
    }

    @Test
    void popularAccess() throws InterruptedException {
        PopularPage popularPage = new PopularPage(driver);
        popularPage
                .callPage()
                .pause();

        driver.executeScript("window.scrollTo(0, document.body.scrollHeight)");

        popularPage
                .pause()
                .clickByXpath("//a[@class='btn btn_primary']")
                .pause();
        assertTrue(driver.getPageSource().contains("скидка"));

        popularPage
                .callPage()
                .pause()
                .clickByXpath("//div[@class='Card-picture']//a[@href='/books/book-yfm-574']")
                .pause();
        assertTrue(driver.getPageSource().contains("Авторизуйтесь, чтобы проголосовать"));

        popularPage
                .clickByXpath("//li[@class='breadcrumbs-item']//a[@href='/']")
                .pause();
        assertTrue(driver.getPageSource().contains("Books by tags"));
    }

    @Test
    void authorsAccess() throws InterruptedException {
        AuthorsPage authorsPage = new AuthorsPage(driver);
        authorsPage
                .callPage()
                .pause();
        assertTrue(driver.getPageSource().contains("Authors"));

        authorsPage
                .clickByXpath("//a[@href='#p']")
                .pause();
        assertTrue(driver.findElement(By.id("p")).isDisplayed());

        authorsPage
                .clickByXpath("//a[@href='/authors/slug/14']")
                .pause();
        assertTrue(driver.getPageSource().contains("Биография"));

        authorsPage
                .clickByXpath("//li[@class='breadcrumbs-item']//a[@href='/']")
                .pause();
        assertTrue(driver.getPageSource().contains("Books by tags"));
    }

}