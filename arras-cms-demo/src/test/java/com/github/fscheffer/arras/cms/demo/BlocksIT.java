package com.github.fscheffer.arras.cms.demo;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.github.fscheffer.arras.test.ArrasTestCase;

public class BlocksIT extends ArrasTestCase {

    @BeforeMethod
    void before() {
        open("/Blocks");
    }

    @Test
    void test() {

        Assert.assertEquals(numberOfContentBlocks("#fixedNumber"), 3);
        Assert.assertEquals(numberOfContentBlocks("#variableNumber"), 0);

        text(By.cssSelector("#username"), "admin");
        click(By.cssSelector("#signIn"));

        waitForPageToLoad();

        // assert no add button for fixed number of blocks
        List<WebElement> noAddButton = elements(By.cssSelector("#fixedNumber [data-component-type=content-add]"));
        Assert.assertEquals(noAddButton.size(), 0);

        changeImage(3);

        WebElement addButton = element(By.cssSelector("#variableNumber [data-component-type=content-add]"));
        addButton.click();
        addButton.click();
        addButton.click();
        addButton.click();

        // TODO: far from perfect. this waits until the first content block is present, but there should be 4!
        waitUntilPresent(By.cssSelector("#variableNumber .medium-editor"));

        click(By.cssSelector("[data-container-type=remote-submit]"));

        waitForAjaxRequestsToComplete();

        click(By.cssSelector("#logout"));

        waitForPageToLoad();

        Assert.assertEquals(numberOfContentBlocks("#fixedNumber"), 3);
        Assert.assertEquals(numberOfContentBlocks("#variableNumber"), 4);

        assertImage(By.cssSelector(".content-image > img"), "/photos/landscape/pointarena_rockycliffs.jpg");
    }

    private int numberOfContentBlocks(String selector) {

        List<WebElement> blocks = elements(By.cssSelector(selector + " .content-block"));

        return blocks.size();
    }

    // TODO: move to ImageModule
    // TODO: add a selector parameter
    private void changeImage(int imageIdInLIghtbox) {
        hover(By.cssSelector(".content-image"));
        click(By.cssSelector(".content-image [data-container-type=lightbox]"));

        waitUntilPresent(By.cssSelector("#cboxLoadedContent"));
        waitUntilVisible(By.cssSelector("#cboxLoadedContent"));

        click(By.cssSelector("#cboxLoadedContent .row > div:nth-child(" + imageIdInLIghtbox + ") a"));

        waitUntilInvisible(By.cssSelector("#cboxOverlay"));
    }

    // TODO: move to ImageModule
    private void assertImage(By by, String expected) {
        String value = attr(by, "src");
        Assert.assertTrue(value.endsWith(expected), "Expected \"" + expected + "\" but got \"" + value + "\"");
    }
}
