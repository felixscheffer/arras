package com.github.fscheffer.arras.demo;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.github.fscheffer.arras.test.ArrasTestCase;

public class PlayerIT extends ArrasTestCase {

    @BeforeMethod
    public void before() {
        open("/PlayerDemo");
    }

    @Test
    public void testVideoPlayer() {

        click("#video .vjs-big-play-button");

        // wait until we loaded the video
        waitUntil(classesPresent("#video > div", "vjs-playing"));

        // make sure the controls are visible
        hover("#video");

        waitUntil(visible("#video .vjs-control-bar"));

        click("#video .vjs-play-control");

        if (!isLive()) {

            waitUntil(containsText("#video .vjs-duration-display", "0:33"));
        }
    }

    @Test
    public void testAudioPlayer() {

        click("#audio .vjs-play-control");

        waitUntil(classesPresent("#audio > div", "vjs-playing"));

        click("#audio .vjs-play-control");

        if (!isLive()) {
            waitUntil(containsText("#audio .vjs-duration-display", "3:29"));
        }
    }

    protected boolean isLive() {
        return element(".vjs-live-display").isDisplayed();
    }
}
