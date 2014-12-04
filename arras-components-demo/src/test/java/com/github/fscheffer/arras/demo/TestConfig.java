package com.github.fscheffer.arras.demo;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.github.fscheffer.arras.test.ArrasTestUtils;
import com.github.fscheffer.arras.test.DefaultTestContext;
import com.github.fscheffer.arras.test.TestContext;
import com.github.fscheffer.arras.test.TestContextFactory;

public class TestConfig implements TestContextFactory {

    @Override
    public TestContext build(Capabilities original) {

        DesiredCapabilities capabilities = new DesiredCapabilities(original);

        capabilities.setCapability("name", "arras-components");

        ArrasTestUtils.setCapabilityFromConfiguration(capabilities, "tunnel-identifier", "TRAVIS_JOB_NUMBER");
        ArrasTestUtils.setCapabilityFromConfiguration(capabilities, "build", "TRAVIS_BUILD_NUMBER");

        return new DefaultTestContext(ArrasTestUtils.createWebDrive(capabilities), "http://127.0.0.1:8080/arras",
                                      original);
    }
}
