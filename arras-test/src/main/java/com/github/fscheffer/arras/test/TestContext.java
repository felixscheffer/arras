package com.github.fscheffer.arras.test;

import org.openqa.selenium.WebDriver;

public interface TestContext {

    WebDriver getDriver();

    String getBaseUrl();
}
