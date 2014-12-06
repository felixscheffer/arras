package com.github.fscheffer.arras.test;

import org.openqa.selenium.remote.DesiredCapabilities;

public interface TestContextFactory {

    TestContext build(DesiredCapabilities capabilities);
}
