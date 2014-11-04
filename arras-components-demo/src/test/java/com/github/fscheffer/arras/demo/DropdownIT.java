// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.github.fscheffer.arras.demo;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.github.fscheffer.arras.test.ArrasTestCase;

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
        assertTextPresent(By.cssSelector("#main-content"), "Index");

        click(By.id("drop"));
        assertTextNotPresent(By.cssSelector("#main-content"), "Index");

        click(By.id("drop_0"));
        // assert tag name is li and is open
        assertClassPresent(By.cssSelector("ul.nav.nav-pills > li:first-child"), "dropdown", "open");
        assertTextPresent(By.cssSelector("#main-content"), "A dropdown item");

        // clicking outside of the dropdown should close the dropdown
        click(By.cssSelector("h3"));
        assertTextNotPresent(By.cssSelector("#main-content"), "A dropdown item");
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

    @Test
    void testDropdownInZone() {

        click(By.linkText("trigger zone"));

        waitForAjaxRequestsToComplete();

        assertTextPresent(By.cssSelector("#dropdownZone"), "Dropdown In Zone");

        click(By.linkText("Dropdown In Zone"));

        assertTextPresent(dropdownItem(1), "Apache HTTP Server");
        assertTextPresent(dropdownItem(2), "Apache Tapestry 5");
        assertTextPresent(dropdownItem(3), "Apache Felix");
    }

    private By dropdownItem(int n) {
        return By.cssSelector(".dropdown.open > ul > li:nth-child(" + n + ") > a");
    }
}
