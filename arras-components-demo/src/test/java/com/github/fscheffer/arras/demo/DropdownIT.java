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
    public void testLabels() {

        // check labels
        waitUntil(containsText("#drop", "Dropdown1"));
        waitUntil(containsText("#drop_0", "My dropdown"));
        waitUntil(containsText("#drop_1", "Custom label"));
        // check icons
        waitUntil(classesPresent("#drop span", "caret"));
        waitUntil(classesPresent("#drop_0 span", "glyphicon", "glyphicon-plus"));
        waitUntil(classesPresent("#drop_1 span", "glyphicon", "glyphicon-arrow-down"));
    }

    @Test
    public void testMouseInteraction() {

        // check mouse clicks
        click("#drop");
        // assert tag name is div and is open
        waitUntil(classesPresent("div.dropdown", "open"));
        waitUntil(containsText("#main-content", "Index"));

        click("#drop");
        waitUntil(notContainsText("#main-content", "Index"));

        click("#drop_0");
        // assert tag name is li and is open
        waitUntil(classesPresent("ul.nav.nav-pills > li:first-child", "dropdown", "open"));
        waitUntil(containsText("#main-content", "A dropdown item"));

        // clicking outside of the dropdown should close the dropdown
        click("h3");
        waitUntil(notContainsText("#main-content", "A dropdown item"));
    }

    @Test
    public void testKeyInteraction() {

        // check keys
        click("#drop_1");
        sendKeys(Keys.ARROW_DOWN);
        waitUntil(focused(dropdownItem(1)));

        // cant move up. it's the first element. focus should stay on the first element
        sendKeys(Keys.ARROW_UP);
        waitUntil(focused(dropdownItem(1)));

        sendKeys(Keys.ARROW_DOWN);
        waitUntil(focused(dropdownItem(2)));

        // skip divider
        sendKeys(Keys.ARROW_DOWN);
        waitUntil(focused(dropdownItem(4)));

        // cant move down. we are at the last element. focus should stay on the last element
        sendKeys(Keys.ARROW_DOWN);
        waitUntil(focused(dropdownItem(4)));

        // skip divider
        sendKeys(Keys.ARROW_UP);
        waitUntil(focused(dropdownItem(2)));

        sendKeys(Keys.ESCAPE);
        waitUntil(notContainsText("BODY", "Item 1"));
        waitUntil(focused("ul.nav.nav-pills > li:nth-child(2) > a"));
    }

    @Test
    public void testDropdownInZone() {

        element(By.linkText("trigger zone")).click();

        waitForAjaxRequestsToComplete();

        waitUntil(containsText("#dropdownZone", "Dropdown In Zone"));

        element(By.linkText("Dropdown In Zone")).click();

        waitUntil(containsText(dropdownItem(1), "Apache HTTP Server"));
        waitUntil(containsText(dropdownItem(2), "Apache Tapestry 5"));
        waitUntil(containsText(dropdownItem(3), "Apache Felix"));
    }

    private String dropdownItem(int n) {
        return ".dropdown.open > ul > li:nth-child(" + n + ") > a";
    }
}
