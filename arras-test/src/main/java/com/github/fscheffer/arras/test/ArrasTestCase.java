package com.github.fscheffer.arras.test;

import java.util.List;
import java.util.Map;

import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.Platform;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;

public abstract class ArrasTestCase {

    private static final Map<String, Map<String, String>> cssFallbacks  = CollectionFactory.newMap();

    {
        // TODO: support browser versions
        // TODO: support more properties
        Map<String, String> safari = CollectionFactory.newMap();
        safari.put("transform", "-webkit-transform");

        cssFallbacks.put("safari", safari);
    }

    private static final Logger                           logger        = LoggerFactory.getLogger(ArrasTestCase.class);

    private static final TestContextPool                  pool          = new TestContextPool();

    private PerThreadTestContext                          threadContext = new PerThreadTestContext();

    @BeforeMethod
    protected void setup() {

        if (this.threadContext.get() != null) {
            throw new IllegalStateException();
        }

        String browser = ArrasTestUtils.getConfiguration(TestConstants.BROWSER, "firefox");
        String version = ArrasTestUtils.getConfiguration(TestConstants.VERSION, "");
        Platform platform = Platform.valueOf(ArrasTestUtils.getConfiguration(TestConstants.PLATFORM, "ANY"));

        Capabilities capabilities = new DesiredCapabilities(browser, version, platform);

        this.threadContext.set(pool.aquire(capabilities));
    }

    @AfterMethod(alwaysRun = true)
    protected void cleanup() {

        TestContext context = this.threadContext.get();
        if (context == null) {
            throw new IllegalStateException();
        }

        pool.release(context);
        this.threadContext.set(null);
    }

    @AfterSuite(alwaysRun = true)
    protected void terminate() {
        pool.terminate();
    }

    protected final WebDriver driver() {
        return this.threadContext.get().getDriver();
    }

    protected final void open(String url) {

        String baseUrl = this.threadContext.get().getBaseUrl();

        String completeUrl = ArrasTestUtils.appendPath(baseUrl, url);
        driver().get(completeUrl);

        waitUntil(pageHasLoaded());
    }

    protected final void click(String cssSelector) {
        element(cssSelector).click();
    }

    protected final void sendKeys(String cssSelector, CharSequence... keysToSend) {
        element(cssSelector).sendKeys(keysToSend);
    }

    protected final void sendKeys(CharSequence keysToSend) {
        driver().switchTo().activeElement().sendKeys(keysToSend);
    }

    protected final void text(String cssSelector, String text) {

        WebElement element = element(cssSelector);

        element.click();

        // ensure that the right element is focused before we change the value
        waitUntil(focused(cssSelector));

        element.clear();
        element.sendKeys(text.replace(" ", Keys.SPACE));

        // remove non printable characters
        waitUntil(valueContainsText(cssSelector, text.replaceAll("\\p{C}", "")));
    }

    protected final String text(String cssSelector) {
        return element(cssSelector).getText();
    }

    protected final void select(String cssSelector, String value) {

        Select select = new Select(element(cssSelector));
        select.selectByValue(value);
    }

    protected final WebElement element(String cssSelector) {
        return element(By.cssSelector(cssSelector));
    }

    protected final WebElement element(By by) {
        return driver().findElement(by);
    }

    protected final List<WebElement> elements(String cssSelector) {
        return driver().findElements(By.cssSelector(cssSelector));
    }

    protected final String attr(String cssSelector, String attribute) {
        return element(cssSelector).getAttribute(attribute);
    }

    protected final String title() {
        return driver().getTitle();
    }

    protected final Dimension viewport() {

        JavascriptExecutor executor = JavascriptExecutor.class.cast(driver());

        Number clientWidth = (Number) executor.executeScript("return document.documentElement.clientWidth");
        Number clientHeight = (Number) executor.executeScript("return document.documentElement.clientHeight");

        return new Dimension(clientWidth.intValue(), clientHeight.intValue());
    }

    protected final void hover(String cssSelector) {
        new Actions(driver()).moveToElement(element(cssSelector)).perform();
    }

    protected final String css(String cssSelector, String property) {

        WebElement element = element(cssSelector);

        String mappedProperty = findBrowserSpecificProperty(property);

        return element.getCssValue(mappedProperty);
    }

    private String findBrowserSpecificProperty(String property) {

        String browserName = this.threadContext.get().getCapabilities().getBrowserName();

        Map<String, String> fallbacks = cssFallbacks.get(browserName);

        if (fallbacks != null) {

            String fallback = fallbacks.get(property);

            if (fallback != null) {
                return fallback;
            }
        }

        return property;
    }

    protected static final ExpectedCondition<Boolean> pageHasLoaded() {
        return ArrasConditions.pageHasLoaded();
    }

    protected static final ExpectedCondition<WebElement> focused(String cssSelector) {
        return ArrasConditions.focusOnElementLocated(By.cssSelector(cssSelector));
    }

    protected static final ExpectedCondition<List<WebElement>> present(String cssSelector) {
        return ArrasConditions.presenceOfElementsLocated(By.cssSelector(cssSelector));
    }

    protected static final ExpectedCondition<List<WebElement>> visible(String cssSelector) {
        return ArrasConditions.visibiltyOfElementsLocated(By.cssSelector(cssSelector));
    }

    protected static final ExpectedCondition<Boolean> invisible(String cssSelector) {
        return ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(cssSelector));
    }

    protected static final ExpectedCondition<Boolean> containsText(String cssSelector, String text) {
        return ExpectedConditions.textToBePresentInElementLocated(By.cssSelector(cssSelector), text);
    }

    protected static final ExpectedCondition<Boolean> notContainsText(String cssSelector, String text) {
        return ExpectedConditions.not(containsText(cssSelector, text));
    }

    protected ExpectedCondition<Boolean> valueContainsText(String cssSelector, String text) {
        return ExpectedConditions.textToBePresentInElementValue(By.cssSelector(cssSelector), text);
    }

    protected ExpectedCondition<Boolean> classesPresent(String cssSelector, String... classes) {
        return ArrasConditions.presenceOfClasses(By.cssSelector(cssSelector), classes);
    }

    protected final <T> T waitUntil(ExpectedCondition<T> condition) {
        // Note: 10 sec is sometimes not enough
        int timeOutInSeconds = 30;
        int sleepInMillis = 200;
        return new WebDriverWait(driver(), timeOutInSeconds, sleepInMillis).until(condition);
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

        try {
            // TODO: body[data-ajax-active=false] is broken in Prototype
            //       This was fixed in tapestry-5.4-beta-23 which is a non-public beta
            //       Once Tapesty releases a new beta change the selector to
            //
            //          body[data-ajax-active='0']
            new WebDriverWait(driver(), 30, 250).until(present("body[data-ajax-active=false]"));
        }
        catch (TimeoutException e) {
            throw new AssertionError("Ajax request did not complete within 30 seconds.");
        }
    }

    /**
     * Sleeps for the indicated number of seconds.
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

    protected void writeErrorReport(String reportText) {
        logger.error(reportText);
    }
}
