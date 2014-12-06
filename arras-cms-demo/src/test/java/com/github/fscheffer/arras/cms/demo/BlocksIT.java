package com.github.fscheffer.arras.cms.demo;

import java.util.List;

import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.github.fscheffer.arras.test.ArrasTestCase;

public class BlocksIT extends ArrasTestCase {

    private ImageComponentModule module = new ImageComponentModule();

    @BeforeMethod
    public void before() {
        open("/Blocks");
    }

    @Test
    public void test() {

        Assert.assertEquals(numberOfContentBlocks("#fixedNumber"), 3);
        Assert.assertEquals(numberOfContentBlocks("#variableNumber"), 0);

        text("#username", "admin");
        click("#signIn");

        waitUntil(pageHasLoaded());

        // assert no add button for fixed number of blocks
        List<WebElement> noAddButton = elements("#fixedNumber [data-component-type=content-add]");
        Assert.assertEquals(noAddButton.size(), 0);

        this.module.changeImage("#fixedNumber .ui-sortable-handle:nth-child(1)", 3);

        WebElement addButton = element("#variableNumber [data-component-type=content-add]");

        addButton.click();
        sleep(100);
        addButton.click();
        sleep(100);
        addButton.click();
        sleep(100);
        addButton.click();
        sleep(100);

        waitUntil(count("#variableNumber .content-block", 4));
        waitUntil(visible("#variableNumber .content-block"));

        click("[data-container-type=remote-submit]");

        waitForAjaxRequestsToComplete();

        click("#logout");

        waitUntil(pageHasLoaded());

        Assert.assertEquals(numberOfContentBlocks("#fixedNumber"), 3);
        Assert.assertEquals(numberOfContentBlocks("#variableNumber"), 4);

        this.module.assertImage(".content-image > img", "/photos/landscape/pointarena_rockycliffs.jpg");
    }

    private int numberOfContentBlocks(String selector) {

        List<WebElement> blocks = elements(selector + " .content-block");

        return blocks.size();
    }

}
