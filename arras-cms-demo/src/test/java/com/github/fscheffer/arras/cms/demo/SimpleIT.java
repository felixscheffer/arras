package com.github.fscheffer.arras.cms.demo;

import org.openqa.selenium.Keys;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.github.fscheffer.arras.test.ArrasTestCase;

public class SimpleIT extends ArrasTestCase {

    private ImageComponentModule module = new ImageComponentModule();

    @BeforeMethod
    public void before() {
        open("/Simple");
    }

    @Test
    public void test() {

        waitUntil(containsText("h1", "A Great Headline"));
        waitUntil(containsText("h2", "A good subtitle"));
        waitUntil(containsText("BODY", "A great way to catch your reader's attention is to tell a story"));
        waitUntil(containsText("BODY", "But as the junior mates were hurrying to execute the order"));

        this.module.assertImage(".content-image > img", "/photos/landscape/man_point-arena-stornetta.jpg");

        text("#username", "admin");
        click("#signIn");

        waitUntil(pageHasLoaded());

        waitUntil(visible("#logout"));
        waitUntil(visible("#contentsubmit"));

        changeText("#contentH1 > .medium-editor", "An even better headline!");
        changeText("#contentH2 > .medium-editor", "And an amazing subtitle...");

        this.module.changeImage("", 5);

        click("#contentsubmit");

        waitForAjaxRequestsToComplete();

        click("#logout");

        waitUntil(containsText("h1", "An even better headline!"));
        waitUntil(containsText("h2", "And an amazing subtitle..."));

        this.module.assertImage(".content-image > img", "/photos/paris/eiffel-tower.jpg");
    }

    private void changeText(String selector, String value) {

        int length = text(selector).length();

        click(selector);

        waitUntil(focused(selector));

        String keys = buildKeys(length, value);

        sendKeys(selector, keys);

        waitUntil(containsText(selector, value));
    }

    private String buildKeys(int length, String value) {

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < length; i++) {
            sb.append(Keys.DELETE);
        }

        sb.append(value);

        return sb.toString();
    }
}
