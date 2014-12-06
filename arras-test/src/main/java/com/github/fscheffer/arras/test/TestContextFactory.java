package com.github.fscheffer.arras.test;

import org.openqa.selenium.Capabilities;

public interface TestContextFactory {

    TestContext build(Capabilities capabilities);
}
