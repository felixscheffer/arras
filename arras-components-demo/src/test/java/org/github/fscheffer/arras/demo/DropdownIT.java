package org.github.fscheffer.arras.demo;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class DropdownIT extends ArrasTestCase {

    @BeforeMethod
    void before() {
        open("/DropdownDemo");
    }

    @Test
    void testLabels() {

        // check labels
        assertTextPresent(By.id("drop"), "Dropdown1");
        assertTextPresent(By.id("drop_0"), "My dropdown");
        assertTextPresent(By.id("drop_1"), "Custom label");

        // check icons
        assertClassPresent(By.cssSelector("#drop span"), "caret");
        assertClassPresent(By.cssSelector("#drop_0 span"), "glyphicon", "glyphicon-plus");
        assertClassPresent(By.cssSelector("#drop_1 span"), "glyphicon", "glyphicon-arrow-down");
    }

    @Test
    void testMouseInteraction() {

        // check mouse clicks
        click(By.id("drop"));
        // assert tag name is div and is open
        assertClassPresent(By.cssSelector("div.dropdown"), "open");
        assertTextPresent("Index");

        click(By.id("drop"));
        assertTextNotPresent("Index");

        click(By.id("drop_0"));
        // assert tag name is li and is open
        assertClassPresent(By.cssSelector("ul.nav.nav-pills > li:first-child"), "dropdown", "open");
        assertTextPresent("A dropdown item");

        // clicking outside of the dropdown should close the dropdown
        click(By.cssSelector("h3"));
        assertTextNotPresent("A dropdown item");
    }

    @Test
    void testKeyInteraction() {

        // check keys
        click(By.id("drop_1"));
        sendKeys(Keys.ARROW_DOWN);
        assertFocused(dropdownItem(1));

        // cant move up. it's the first element. focus should stay on the first element
        sendKeys(Keys.ARROW_UP);
        assertFocused(dropdownItem(1));

        sendKeys(Keys.ARROW_DOWN);
        assertFocused(dropdownItem(2));

        // skip divider
        sendKeys(Keys.ARROW_DOWN);
        assertFocused(dropdownItem(4));

        // cant move down. we are at the last element. focus should stay on the last element
        sendKeys(Keys.ARROW_DOWN);
        assertFocused(dropdownItem(4));

        // skip divider
        sendKeys(Keys.ARROW_UP);
        assertFocused(dropdownItem(2));

        sendKeys(Keys.ESCAPE);
        assertTextNotPresent("Item 1");
        assertFocused(By.cssSelector("ul.nav.nav-pills > li:nth-child(2) > a"));
    }

    private By dropdownItem(int n) {
        return By.cssSelector(".dropdown.open > ul > li:nth-child(" + n + ") > a");
    }
}
