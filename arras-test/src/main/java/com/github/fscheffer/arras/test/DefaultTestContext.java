package com.github.fscheffer.arras.test;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;

public class DefaultTestContext implements TestContext {

    private final WebDriver driver;

    private final String    baseUrl;

    public DefaultTestContext() {
        this(createWebDriver(), System.getProperty("testing.baseUrl", "localhost:8080"));
    }

    public DefaultTestContext(String baseUrl) {
        this(createWebDriver(), baseUrl);
    }

    public DefaultTestContext(WebDriver driver, String baseUrl) {
        this.driver = driver;
        this.baseUrl = baseUrl;
    }

    private static WebDriver createWebDriver() {

        String driverName = System.getProperty("testing.driver", "firefox");

        WebDriver driver = ArrasTestUtils.getWebDriverInstanceByName(driverName);

        // Note: use explicit wait if you need to wait (see waitUntil)
        driver.manage().timeouts().implicitlyWait(0, TimeUnit.MILLISECONDS);

        return driver;
    }

    @Override
    public WebDriver getDriver() {
        return this.driver;
    }

    @Override
    public String getBaseUrl() {
        return this.baseUrl;
    }

}
