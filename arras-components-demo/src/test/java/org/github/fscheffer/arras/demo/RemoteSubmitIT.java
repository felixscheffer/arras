package org.github.fscheffer.arras.demo;

import org.openqa.selenium.By;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class RemoteSubmitIT extends ArrasTestCase {

    @BeforeMethod
    void before() {
        open("/RemoteSubmitDemo");

        waitForPageToLoad();
    }

    @Test
    void testVisibleSubmit() {

        text(By.cssSelector("#visibleExample input[type=text]"), "Hello World!");

        click(By.cssSelector("#visibleExample > button"));

        waitForPageToLoad();

        assertTextPresent(By.cssSelector(".alert > span"),
            "Triggered form with visible submit button! Form content was: \"Hello World!\"");
    }

    @Test
    void testInvisibleSubmit() {

        text(By.cssSelector("#invisibleExample input[type=text]"), "Hello Tapestry!");

        click(By.cssSelector("#invisibleExample > button"));

        waitForPageToLoad();

        assertTextPresent(By.cssSelector(".alert > span"),
                          "Triggered form with invisible submit button! Form content was: \"Hello Tapestry!\"");
    }
}
