package com.github.fscheffer.arras.test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.tapestry5.ioc.internal.util.InternalUtils;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;
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

    public static String getConfiguration(String key) {
        return getConfiguration(key, null);
    }

    public static final String getConfiguration(String key, String defaultValue) {

        String value = System.getProperty(key);

        if (value != null) {
            return value;
        }

        value = System.getenv(key);

        return value != null ? value : defaultValue;
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

    protected static WebDriver createLocalWebDriver(Capabilities capabilities) {

        log.info("Creating local webdriver with {}", capabilities);

        String driverName = capabilities.getBrowserName();

        if (BrowserType.CHROME.equals(driverName)) {
            // capabilities.setCapability(ChromeOptions.CAPABILITY, (Object) new ChromeOptions());
            return new ChromeDriver();
        }

        if (BrowserType.SAFARI.equals(driverName)) {
            return new SafariDriver(SafariOptions.fromCapabilities(capabilities));
        }

        if (BrowserType.IE.equals(driverName) || BrowserType.IEXPLORE.equals(driverName)) {
            // capabilities.setCapability(CapabilityType.ForSeleniumServer.ENSURING_CLEAN_SESSION, true);
            return new InternetExplorerDriver();
        }

        return new FirefoxDriver(new FirefoxBinary(), null, capabilities);
    }

    protected static WebDriver createRemoteWebDriver(URL remoteAddress, Capabilities capabilities) {

        log.info("Requesting remote webdriver with {} at {}", capabilities, new Date());

        RemoteWebDriver driver = new RemoteWebDriver(remoteAddress, capabilities);

        log.info("Received driver at {}", new Date());

        return driver;
    }

    public static WebDriver createWebDrive(Capabilities capabilities) {

        URL remoteUrl = toUrl(ArrasTestUtils.getConfiguration(TestConstants.REMOTE_URL));

        WebDriver driver = remoteUrl != null ? ArrasTestUtils.createRemoteWebDriver(remoteUrl, capabilities)
                                             : ArrasTestUtils.createLocalWebDriver(capabilities);

        // Note: use explicit wait if you need to wait (see waitUntil)
        driver.manage().timeouts().implicitlyWait(0, TimeUnit.MILLISECONDS);

        return driver;
    }

    public static void setCapabilityFromConfiguration(DesiredCapabilities capabilities, String capability,
                                                      String configKey) {

        String value = ArrasTestUtils.getConfiguration(configKey);

        if (InternalUtils.isNonBlank(value)) {

            capabilities.setCapability(capability, value);
        }

    }

    public static final DesiredCapabilities getDesiredCapabilities() {
        String browser = getConfiguration(TestConstants.BROWSER, "firefox");
        String version = getConfiguration(TestConstants.VERSION, "");
        Platform platform = Platform.valueOf(getConfiguration(TestConstants.PLATFORM, "ANY"));

        return new DesiredCapabilities(browser, version, platform);
    }
}
