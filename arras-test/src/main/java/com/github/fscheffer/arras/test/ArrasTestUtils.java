package com.github.fscheffer.arras.test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.apache.tapestry5.ioc.internal.util.InternalUtils;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ArrasTestUtils {

    private static Logger log = LoggerFactory.getLogger(ArrasTestUtils.class);

    public static boolean isBlank(String input) {
        return input == null || input.length() == 0 || input.trim().length() == 0;
    }

    public static final String appendPath(String baseUrl, String path) {
        return path.startsWith("/") ? baseUrl + path : baseUrl + "/" + path;
    }

    public static URL toUrl(String rawUrl) {

        if (rawUrl == null) {
            return null;

        }
        try {
            return new URL(rawUrl);
        }
        catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Platform toPlatform(String platform) {

        if (InternalUtils.isBlank(platform)) {
            return Platform.ANY;
        }

        return Platform.valueOf(platform);
    }

    public static WebDriver createWebDriverFromSystemProperties() {

        URL remoteUrl = toUrl(System.getProperty(TestingConstants.REMOTE_URL, null));
        String browserName = System.getProperty(TestingConstants.BROWSER, BrowserType.FIREFOX);
        String version = System.getProperty(TestingConstants.VERSION, "");
        Platform platform = toPlatform(System.getProperty(TestingConstants.PLATFORM, null));

        WebDriver driver = remoteUrl != null ? ArrasTestUtils.createRemoteWebDriver(remoteUrl, browserName, version,
                                                                                    platform)
                                                                                    : ArrasTestUtils.createLocalWebDriver(browserName);

        // Note: use explicit wait if you need to wait (see waitUntil)
        driver.manage().timeouts().implicitlyWait(0, TimeUnit.MILLISECONDS);

        return driver;
    }

    protected static WebDriver createLocalWebDriver(String driverName) {

        log.info("Creating local webdriver for {}", driverName);

        if (BrowserType.CHROME.equals(driverName)) {
            return new ChromeDriver();
        }

        if (BrowserType.SAFARI.equals(driverName)) {
            return new SafariDriver();
        }

        if (BrowserType.IE.equals(driverName) || BrowserType.IEXPLORE.equals(driverName)) {
            return new InternetExplorerDriver();
        }

        return new FirefoxDriver();
    }

    protected static WebDriver createRemoteWebDriver(URL remoteAddress, String browserName, String version,
                                                     Platform platform) {

        DesiredCapabilities desiredCapabilities = new DesiredCapabilities(browserName, version, platform);

        String travisJobNumber = System.getProperty("TRAVIS_JOB_NUMBER");
        if (InternalUtils.isNonBlank(travisJobNumber)) {
            desiredCapabilities.setCapability("tunnel-identifier", travisJobNumber);
        }

        log.info("Creating remote webdriver with {}", desiredCapabilities);

        return new RemoteWebDriver(remoteAddress, desiredCapabilities);
    }
}
