package com.github.fscheffer.arras.cms.demo;

import org.testng.Assert;

import com.github.fscheffer.arras.test.ArrasTestCase;

public class ImageComponentModule extends ArrasTestCase {

    public void changeImage(String selector, int imageIdInLIghtbox) {

        String imageSelector = selector + " .content-image [data-container-type=lightbox]";

        hover(selector + " .content-image");

        waitUntil(visible(imageSelector));

        // use mouse action click here instead of click(element) or selenium thinks the element is not visible
        actions().click(element(imageSelector)).perform();

        waitUntil(visible("#cboxLoadedContent"));

        click("#cboxLoadedContent .row > div:nth-child(" + imageIdInLIghtbox + ") a");

        waitUntil(invisible("#cboxOverlay"));
    }

    public void assertImage(String selector, String expected) {
        String value = attr(selector, "src");
        Assert.assertTrue(value.endsWith(expected), "Expected \"" + expected + "\" but got \"" + value + "\"");
    }
}
