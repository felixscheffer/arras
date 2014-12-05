package com.github.fscheffer.arras.test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ArrasConditions {

    private static Logger logger = LoggerFactory.getLogger(ArrasConditions.class);

    public static ExpectedCondition<List<WebElement>> visibiltyOfElementsLocated(final By locator) {
        return new ExpectedCondition<List<WebElement>>() {

            @Override
            public List<WebElement> apply(WebDriver driver) {
                try {
                    List<WebElement> elements = findElements(locator, driver);
                    for (WebElement element : elements) {

                        if (!element.isDisplayed()) {
                            return null;
                        }
                    }
                    return elements.size() > 0 ? elements : null;
                }
                // consider non existing elements invisible
                catch (StaleElementReferenceException e) {
                    return null;
                }
                catch (NoSuchElementException e) {
                    return null;
                }
            }

            @Override
            public String toString() {
                return "visibility of element located by " + locator;
            }
        };
    }

    public static final ExpectedCondition<List<WebElement>> presenceOfElementsLocated(final By locator) {
        return new ExpectedCondition<List<WebElement>>() {

            @Override
            public List<WebElement> apply(WebDriver driver) {
                try {
                    List<WebElement> elements = findElements(locator, driver);
                    return elements.size() > 0 ? elements : null;
                }
                // dont throw, wait instead
                catch (StaleElementReferenceException e) {
                    return null;
                }
                catch (NoSuchElementException e) {
                    return null;
                }
            }

            @Override
            public String toString() {
                return "presence of elements located by: " + locator;
            }
        };
    }

    public static ExpectedCondition<WebElement> focusOnElementLocated(final By locator) {

        return new ExpectedCondition<WebElement>() {

            @Override
            public WebElement apply(WebDriver driver) {

                WebElement focusedElement = driver.switchTo().activeElement();

                return focusedElement.equals(findElement(locator, driver)) ? focusedElement : null;
            }
        };
    }

    public static ExpectedCondition<Boolean> pageHasLoaded() {

        return new ExpectedCondition<Boolean>() {

            @Override
            public Boolean apply(WebDriver driver) {

                WebElement body;
                try {
                    body = findElement(By.cssSelector("body"), driver);
                }
                catch (NoSuchElementException e) {

                    // In a limited number of cases, a "page" is an container error page or raw HTML content
                    // that does not include the body element and data-page-initialized element. In those cases,
                    // there will never be page initialization in the Tapestry sense and we return immediately.

                    return true;
                }

                String initialized = body.getAttribute("data-page-initialized");

                if (initialized == null || "true".equals(initialized)) {
                    // if the attribute is missing, then this is probably not a tapestry page (see above)
                    return true;
                }

                return false;
            }
        };
    }

    public static ExpectedCondition<Boolean> presenceOfClasses(final By locator, final String[] classes) {

        return new ExpectedCondition<Boolean>() {

            @Override
            public Boolean apply(WebDriver driver) {

                List<WebElement> elements = findElements(locator, driver);

                for (WebElement element : elements) {

                    List<String> list = retrieveClasses(element);

                    for (String clazz : classes) {

                        if (!list.contains(clazz)) {
                            return false;
                        }
                    }
                }

                return true;
            }
        };
    }

    private static List<String> retrieveClasses(WebElement element) {

        String attr = element.getAttribute("class");

        return attr == null ? Collections.<String> emptyList() : Arrays.asList(attr.split(" "));
    }

    private static WebElement findElement(By by, WebDriver driver) {
        try {
            return driver.findElement(by);
        }
        catch (NoSuchElementException e) {
            throw e;
        }
        catch (WebDriverException e) {
            logger.warn(String.format("WebDriverException thrown by findElement(%s)", by), e);
            throw e;
        }
    }

    private static List<WebElement> findElements(By by, WebDriver driver) {
        try {
            return driver.findElements(by);
        }
        catch (WebDriverException e) {
            logger.warn(String.format("WebDriverException thrown by findElement(%s)", by), e);
            throw e;
        }
    }

    public static ExpectedCondition<WebElement> attributeHasValueOnElementLocated(final By locator, final String name,
                                                                                  final String expectedValue) {

        return new ExpectedCondition<WebElement>() {

            @Override
            public WebElement apply(WebDriver driver) {

                WebElement element = findElement(locator, driver);

                String actualValue = element.getAttribute(name);

                // Note: if null is the expected value, return true
                if (expectedValue == actualValue || expectedValue.equals(actualValue)) {
                    return element;
                }

                return null;
            }
        };
    }
}
