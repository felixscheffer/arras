package com.github.fscheffer.arras.test;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.safari.SafariDriver;

public class ArrasTestUtils {

    public static boolean isBlank(String input) {
        return input == null || input.length() == 0 || input.trim().length() == 0;
    }

    public static final String appendPath(String baseUrl, String path) {
        return path.startsWith("/") ? baseUrl + path : baseUrl + "/" + path;
    }

    public static WebDriver getWebDriverInstanceByName(String driverName) {
    
        if ("chrome".equals(driverName)) {
            return new ChromeDriver();
        }
    
        if ("safari".equals(driverName)) {
            return new SafariDriver();
        }
    
        if ("ie".equals(driverName) || "internetexplorer".equals(driverName)) {
            return new InternetExplorerDriver();
        }
    
        return new FirefoxDriver();
    }
}
