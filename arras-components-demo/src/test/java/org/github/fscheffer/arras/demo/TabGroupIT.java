package org.github.fscheffer.arras.demo;

import org.openqa.selenium.By;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

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

        click(By.xpath("//a[@href='#tab2']"));

        waitForAjaxRequestsToComplete();

        assertTabContentPresent("This is an ajax tab!");
    }

    @Test
    void testDropdownTabs() {

        click(By.xpath("//ul[@role='tablist']/li/a[@data-toggle='dropdown']"));

        // check dropdown tab names
        assertTabNamePresent("Tab3");
        assertTabNamePresent("Custom tab");

        click(By.xpath("//a[@href='#tab3']"));

        // wait until the transition is complete. probably not the best solution
        sleep(300);

        assertTabContentPresent("This is a tab within a dropdown.");
    }

    @Test
    void testSubtabs() {

        // check subtabs
        click(By.xpath("//a[@href='#TabWithSubtabs']"));
        assertTextPresent(By.cssSelector(".tab-content > .active .tab-content"), "foo");

        click(By.xpath("//a[@href='#barTab']"));
        assertTextPresent(By.cssSelector(".tab-content > .active .tab-content"), "bar");
    }

    private void assertTabContentPresent(String value) {
        assertTextPresent(By.cssSelector(".tab-content > .active"), value);
    }

    private void assertTabNamePresent(String tabname) {
        assertTextPresent(By.cssSelector(".nav-tabs"), tabname);
    }
}
