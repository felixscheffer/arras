package com.github.fscheffer.arras.demo;

import org.apache.tapestry5.ioc.internal.util.InternalUtils;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.github.fscheffer.arras.test.ArrasTestUtils;
import com.github.fscheffer.arras.test.DefaultTestContext;
import com.github.fscheffer.arras.test.TestConstants;
import com.github.fscheffer.arras.test.TestContext;
import com.github.fscheffer.arras.test.TestContextFactory;

public class TestConfig implements TestContextFactory {

    @Override
    public TestContext build(Capabilities original) {

        DesiredCapabilities capabilities = new DesiredCapabilities(original);

        capabilities.setCapability("name", "arras-components");

        if (BrowserType.CHROME.equals(capabilities.getBrowserName())
        /*&& Platform.LINUX.equals(capabilities.getPlatform())*/) {

            if (InternalUtils.isNonBlank(capabilities.getVersion()) && Integer.valueOf(capabilities.getVersion()) >= 29) {
                capabilities.setCapability("chromedriver-version", "2.4");
            }

            if (InternalUtils.isNonBlank(capabilities.getVersion()) && Integer.valueOf(capabilities.getVersion()) >= 37) {
                capabilities.setCapability("chromedriver-version", "2.11");
            }
        }

        ArrasTestUtils.setCapabilityFromConfiguration(capabilities, "tunnel-identifier", "TRAVIS_JOB_NUMBER");
        ArrasTestUtils.setCapabilityFromConfiguration(capabilities, "build", "TRAVIS_BUILD_NUMBER");

        String baseUrl = ArrasTestUtils.getConfiguration(TestConstants.BASE_URL, "http://127.0.0.1:8080/arras");

        return new DefaultTestContext(ArrasTestUtils.createWebDrive(capabilities), baseUrl, original);
    }
}
