package com.github.fscheffer.arras.test;

import org.openqa.selenium.WebDriver;

public class DefaultTestContext implements TestContext {

    private final WebDriver driver;

    private final String    baseUrl;

    public DefaultTestContext() {
        this(ArrasTestUtils.createWebDriverFromSystemProperties(), System.getProperty(TestingConstants.BASE_URL, "localhost:8080"));
    }

    public DefaultTestContext(String baseUrl) {
        this(ArrasTestUtils.createWebDriverFromSystemProperties(), baseUrl);
    }

    public DefaultTestContext(WebDriver driver, String baseUrl) {
        this.driver = driver;
        this.baseUrl = baseUrl;
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
