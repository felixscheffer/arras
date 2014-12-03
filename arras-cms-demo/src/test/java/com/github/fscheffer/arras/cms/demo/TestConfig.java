package com.github.fscheffer.arras.cms.demo;

import org.apache.tapestry5.ioc.internal.util.InternalUtils;
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

        String travisJobNumber = System.getenv("TRAVIS_JOB_NUMBER");
        if (InternalUtils.isNonBlank(travisJobNumber)) {
            capabilities.setCapability("tunnel-identifier", travisJobNumber);
        }

        return new DefaultTestContext(ArrasTestUtils.createWebDrive(capabilities), "localhost:8080/arras-cms", original);
    }
}
