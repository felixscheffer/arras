package org.github.fscheffer.arras.cms.demo;

import org.github.fscheffer.arras.test.ArrasTestCase;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

public class SimpleIT extends ArrasTestCase {

    @BeforeSuite
    void beforeSuite() {
        System.setProperty("testing.path", "/arras-cms");
    }

    @BeforeMethod
    void before() {
        open("/Simple");
    }

    @Test
    void test() {

        assertTextPresent(By.cssSelector("h1"), "A Great Headline");
        assertTextPresent(By.cssSelector("h2"), "A good subtitle");
        assertTextPresent("A great way to catch your reader's attention is to tell a story");
        assertTextPresent("But as the junior mates were hurrying to execute the order");

        text(By.cssSelector("#username"), "admin");
        click(By.cssSelector("#signIn"));

        waitForPageToLoad();

        Assert.assertTrue(isDisplayed(By.cssSelector("#logout")));
        Assert.assertTrue(isDisplayed(By.cssSelector("#contentsubmit")));

        changeText("#contentH1 > .medium-editor", "An even better headline!");
        changeText("#contentH2 > .medium-editor", "And an amazing subtitle...");

        click(By.cssSelector("#contentsubmit"));

        waitForAjaxRequestsToComplete();

        click(By.cssSelector("#logout"));

        assertTextPresent(By.cssSelector("h1"), "An even better headline!");
        assertTextPresent(By.cssSelector("h2"), "And an amazing subtitle...");
    }

    private void changeText(String selector, String value) {

        By by = By.cssSelector(selector);

        int length = text(by).length();

        click(by);

        for (int i = 0; i < length; i++) {
            sendKeys(by, Keys.DELETE);
        }

        sendKeys(by, value);
    }
}
