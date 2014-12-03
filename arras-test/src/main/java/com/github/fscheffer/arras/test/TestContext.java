package com.github.fscheffer.arras.test;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;

public interface TestContext {

    Capabilities getCapabilities();

    WebDriver getDriver();

    String getBaseUrl();
}
