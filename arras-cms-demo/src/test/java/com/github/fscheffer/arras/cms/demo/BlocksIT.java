package com.github.fscheffer.arras.cms.demo;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import com.github.fscheffer.arras.test.ArrasTestCase;

public class BlocksIT extends ArrasTestCase {

    @BeforeSuite
    void beforeSuite() {
        // TODO: find a better solution when running tests within eclipse
        System.setProperty("testing.path", "/arras-cms");
    }

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

        WebElement addButton = element(By.cssSelector("#variableNumber [data-component-type=content-add]"));
        addButton.click();
        addButton.click();
        addButton.click();
        addButton.click();

        click(By.cssSelector("[data-container-type=remote-submit]"));

        waitForAjaxRequestsToComplete();

        click(By.cssSelector("#logout"));

        waitForPageToLoad();

        Assert.assertEquals(numberOfContentBlocks("#fixedNumber"), 3);
        Assert.assertEquals(numberOfContentBlocks("#variableNumber"), 4);
    }

    private int numberOfContentBlocks(String selector) {

        List<WebElement> blocks = elements(By.cssSelector(selector + " .content-block"));

        return blocks.size();
    }
}
