package org.github.fscheffer.arras.demo;

import org.openqa.selenium.By;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class MediumEditorIT extends ArrasTestCase {

    @BeforeMethod
    void before() {
        open("/MediumEditorDemo");
        click(By.linkText("Reset demo"));
        waitForPageToLoad();
    }

    // TODO: test the toolbar but I have absolutely no clue how to select a text using selenium webdriver api

    @Test
    void testNoToolbar() {

        By by = By.id("noToolbar");

        click(by);

        sendKeys(by, "foobar");

        clickSave();

        assertTextPresent(by, "foobar");
    }

    @Test
    void testDisabled() {

        By by = By.id("disabled");

        assertTextPresent(by, "Hello World!");

        click(by);

        sendKeys(by, " foobar");

        clickSave();

        assertTextNotPresent(by, "foobar");
    }

    private void clickSave() {

        click(By.id("submit_0"));

        waitForPageToLoad();
    }

}
