package org.github.fscheffer.arras.demo;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.github.fscheffer.arras.ArrasUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;

public abstract class ArrasTestCase {

    private static final Logger logger = LoggerFactory.getLogger(ArrasTestCase.class);

    private WebDriver           driver;

    private String              baseUrl;

    @BeforeClass
    public void configureWebdriver(ITestContext context) {

        String host = System.getProperty("testing.hostname", "localhost");
        String port = System.getProperty("testing.port", "8080");
        String path = System.getProperty("testing.path", "/arras");

        this.baseUrl = ArrasUtils.buildUrl(host, port, path);

        this.driver = getWebdriver(context);
        if (this.driver != null) {
            return;
        }

        String driverName = System.getProperty("testing.driver", "firefox");

        // Note: use explicit wait if you need to wait (see waitUntil)
        this.driver = buildDriverByName(driverName);
        this.driver.manage().timeouts().implicitlyWait(400, TimeUnit.MILLISECONDS);

        setWebdriver(context, this.driver);
    }

    private WebDriver buildDriverByName(String driverName) {

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

    @AfterSuite(alwaysRun = true)
    public void destroyWebdriver(ITestContext context) {

        WebDriver wd = getWebdriver(context);
        if (wd != null) {
            wd.quit();
        }
    }

    private void setWebdriver(ITestContext context, WebDriver webdriver) {
        context.setAttribute("webdriver", webdriver);
    }

    private WebDriver getWebdriver(ITestContext context) {
        return (WebDriver) context.getAttribute("webdriver");
    }

    protected final void open(String url) {

        String completeUrl = ArrasUtils.appendPath(this.baseUrl, url);
        this.driver.get(completeUrl);
    }

    protected final void click(By by) {
        element(by).click();
    }

    protected final void sendKeys(By by, CharSequence... keysToSend) {
        element(by).sendKeys(keysToSend);
    }

    protected final void sendKeys(CharSequence... keysToSend) {
        this.driver.switchTo().activeElement().sendKeys(keysToSend);
    }

    protected final void text(By by, CharSequence text) {

        WebElement element = element(by);
        element.click();
        element.clear();
        element.sendKeys(text);
    }

    protected final String text(By by) {
        return element(by).getText();
    }

    protected final void select(By by, String value) {

        Select select = new Select(element(by));
        select.selectByValue(value);
    }

    protected final WebElement element(By by) {
        return this.driver.findElement(by);
    }

    protected final List<WebElement> elements(By by) {
        return this.driver.findElements(by);
    }

    protected final String attr(By by, String attribute) {
        return element(by).getAttribute(attribute);
    }

    protected final String title() {
        return this.driver.getTitle();
    }

    protected final boolean isDisplayed(By by) {
        return element(by).isDisplayed();
    }

    protected final void assertClassPresent(By by, String... classes) {

        WebElement element = element(by);

        String attr = element.getAttribute("class");

        List<String> list = attr == null ? Collections.<String> emptyList() : Arrays.asList(attr.split(" "));

        for (String clazz : classes) {

            if (!list.contains(clazz)) {
                reportAndThrowAssertionError("Element '" + element.getTagName() + "' is missing class '" + clazz + "'.");
            }
        }
    }

    protected final void assertTextPresent(String... text) {
        assertTextPresent(By.cssSelector("BODY"), text);
    }

    protected final void assertTextPresent(By by, String... text) {

        WebElement element = element(by);

        String content = element.getText();

        for (String item : text) {

            if (content.contains(item)) {
                continue;
            }

            reportAndThrowAssertionError("Element '" + element.getTagName() + "' did not contain '" + item + "'.");
        }
    }

    protected final void assertTextNotPresent(String... text) {
        assertTextNotPresent(By.cssSelector("BODY"), text);
    }

    protected final void assertTextNotPresent(By by, String... text) {

        WebElement element = element(by);

        String content = element.getText();

        for (String item : text) {

            if (!content.contains(item)) {
                continue;
            }

            reportAndThrowAssertionError("Element '" + element.getTagName() + "' contains '" + item
                                         + "' but it should not.");
        }
    }

    protected final void assertFocused(By by) {

        WebElement element = element(by);
        WebElement focusedElement = this.driver.switchTo().activeElement();

        if (!element.equals(focusedElement)) {
            reportAndThrowAssertionError("Element '" + element + "' is not focused. '" + focusedElement
                                         + "' is currently focused.");
        }
    }

    protected final void waitUntilInvisible(By by) {
        waitUntil(ExpectedConditions.invisibilityOfElementLocated(by));
    }

    protected final void waitUntilVisible(By by) {
        waitUntil(ExpectedConditions.visibilityOfElementLocated(by));
    }

    protected final <T> void waitUntil(ExpectedCondition<T> condition) {
        new WebDriverWait(this.driver, 10).until(condition);
    }

    /**
     * Waits until all active XHR requests (as noted by the t5/core/dom module)
     * have completed.
     *
     * @since 5.4
     */
    protected final void waitForAjaxRequestsToComplete() {
        // Ugly but necessary. Give the Ajax operation sufficient time to execute normally, then start
        // polling to see if it has complete.
        sleep(250);

        // The t5/core/dom module tracks how many Ajax requests are active
        // and body[data-ajax-active] as appropriate.

        for (int i = 0; i < 10; i++) {
            if (i > 0) {
                sleep(100);
            }

            // TODO: body[data-ajax-active=false] is broken in Prototype
            //       This was fixed in tapestry-5.4-beta-23 which is a non-public beta
            //       Once Tapesty releases a new beta change the selector to
            //
            //          body[data-ajax-active='0']
            if (getCssCount("body[data-ajax-active=false]").equals(1)) {
                return;
            }
        }

        reportAndThrowAssertionError("Body 'data-ajax-active' attribute never reverted to '0'.");
    }

    protected final Number getCssCount(String selector) {
        return this.driver.findElements(By.cssSelector(selector)).size();
    }

    /**
     * Waits for page  to load, then waits for initialization to finish, which is recognized by the {@code data-page-initialized} attribute
     * being set to true on the body element. Polls at increasing intervals, for up-to 30 seconds (that's extraordinarily long, but helps sometimes
     * when manually debugging a page that doesn't have the floating console enabled)..
     */
    protected final void waitForPageToLoad() {

        // In a limited number of cases, a "page" is an container error page or raw HTML content
        // that does not include the body element and data-page-initialized element. In those cases,
        // there will never be page initialization in the Tapestry sense and we return immediately.

        if (!isElementPresent("body[data-page-initialized]")) {
            return;
        }

        final long pollingStartTime = System.currentTimeMillis();

        long sleepTime = 20;

        while (true) {
            if (isElementPresent("body[data-page-initialized='true']")) {
                return;
            }

            if (System.currentTimeMillis() - pollingStartTime > 30000) {
                reportAndThrowAssertionError("Page did not finish initializing after 30 seconds.");
            }

            sleep(sleepTime);

            sleepTime *= 2;
        }
    }

    protected final boolean isElementPresent(String locator) {
        return this.driver.findElements(By.cssSelector(locator)).size() != 0;
    }

    /**
     * Sleeps for the indicated number of seconds.
     *
     * @since 5.3
     */
    protected final void sleep(long millis) {
        try {
            Thread.sleep(millis);
        }
        catch (InterruptedException ex) {
            // Ignore.
        }
    }

    /**
     * Formats a message from the provided arguments, which is written to System.err. In addition,
     * captures the AUT's markup, screenshot, and a report to the output directory.
     *
     * @param message
     * @param arguments
     * @since 5.4
     */
    protected final void reportAndThrowAssertionError(String message, Object... arguments) {
        StringBuilder builder = new StringBuilder(5000);

        String formatted = String.format(message, arguments);

        builder.append(formatted);

        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

        StringBuilder buffer = new StringBuilder(5000);

        boolean enabled = false;

        for (StackTraceElement e : stackTrace) {
            if (enabled) {
                buffer.append("\n- ");
                buffer.append(e);
                continue;
            }

            if (e.getMethodName().equals("reportAndThrowAssertionError")) {
                enabled = true;
            }
        }

        writeErrorReport(builder.toString());

        throw new AssertionError(formatted);
    }

    /**
     * Delegates to {@link ErrorReporter#writeErrorReport(String)} to capture the current page markup in a
     * file for later analysis.
     */
    protected void writeErrorReport(String reportText) {
        logger.error(reportText);
    }
}
