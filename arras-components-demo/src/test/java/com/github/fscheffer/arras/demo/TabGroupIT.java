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
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.github.fscheffer.arras.test.ArrasTestCase;

public class TabGroupIT extends ArrasTestCase {

    @BeforeMethod
    void before() {
        open("/TabGroupDemo");
    }

    @Test
    void testTabNames() {

        // check tab names
        assertTabNamePresent("Simple tab");
        assertTabNamePresent("Ajax tab");
        assertTabNamePresent("Dropdown");
        assertTabNamePresent("Tab With Subtabs");
    }

    @Test
    void testTabContent() {

        // check tab content
        assertTabContentPresent("This is just a normal tab.");

        click("a[href='#tab2']");

        waitForAjaxRequestsToComplete();

        assertTabContentPresent("This is an ajax tab!");
    }

    @Test
    void testDropdownTabs() {

        element(By.xpath("//ul[@role='tablist']/li/a[@data-toggle='dropdown']")).click();

        // check dropdown tab names
        assertTabNamePresent("Tab3");
        assertTabNamePresent("Custom tab");

        click("a[href='#tab3']");

        // wait until the transition is complete. probably not the best solution
        sleep(300);

        assertTabContentPresent("This is a tab within a dropdown.");
    }

    @Test
    void testSubtabs() {

        String selector = ".tab-content > .active .tab-content";

        // check subtabs
        click("a[href='#TabWithSubtabs']");
        waitUntil(visible(selector));
        waitUntil(containsText(selector, "foo"));

        click("a[href='#barTab']");
        waitUntil(visible(selector));
        waitUntil(containsText(selector, "bar"));
    }

    @Test
    void testTabInZone() {

        element(By.linkText("trigger zone")).click();

        waitForAjaxRequestsToComplete();

        waitUntil(containsText("#tabgroupZone", "a tab in a zone"));

        element(By.linkText("Tab In Zone2")).click();

        waitUntil(containsText("#tabgroupZone", "another tab in the same zone"));
    }

    private void assertTabContentPresent(String value) {
        waitUntil(containsText(".tab-content > .active", value));
    }

    private void assertTabNamePresent(String tabname) {
        waitUntil(containsText(".nav-tabs", tabname));
    }
}
