package com.github.fscheffer.arras.demo;

import org.openqa.selenium.By;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.github.fscheffer.arras.test.ArrasTestCase;

public class PlayerIT extends ArrasTestCase {

    @BeforeMethod
    void before() {
        open("/PlayerDemo");
    }

    @Test
    void testVideoPlayer() {

        click(By.cssSelector("#video .vjs-big-play-button"));

        waitUntilVisible(By.cssSelector("#video .vjs-control-bar"));

        click(By.cssSelector("#video .vjs-playing"));

        assertTextPresent(By.cssSelector("#video .vjs-duration-display"), "0:33");
    }

    @Test
    void testAudioPlayer() {

        click(By.cssSelector("#audio .vjs-play-control"));

        waitUntilVisible(By.cssSelector("#audio .vjs-playing"));

        click(By.cssSelector("#audio .vjs-playing"));

        assertTextPresent(By.cssSelector("#audio .vjs-duration-display"), "3:24");
    }
}
