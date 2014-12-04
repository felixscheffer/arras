package com.github.fscheffer.arras.test;

import org.apache.tapestry5.ioc.internal.util.InternalUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.saucelabs.saucerest.SauceREST;

public class SauceLabsListener implements ITestListener {

    private static Logger        log           = LoggerFactory.getLogger(SauceLabsListener.class);

    private PerThreadTestContext threadContext = new PerThreadTestContext();

    private SauceREST            sauceREST;

    @Override
    public void onStart(ITestContext context) {

        String username = ArrasTestUtils.getConfiguration("SAUCE_USERNAME");
        String accessKey = ArrasTestUtils.getConfiguration("SAUCE_ACCESS_KEY");

        if (InternalUtils.isBlank(username) || InternalUtils.isBlank(accessKey)) {
            return;
        }

        this.sauceREST = new SauceREST(username, accessKey);
    }

    @Override
    public void onTestStart(ITestResult result) {}

    @Override
    public void onTestSuccess(ITestResult result) {

        if (this.sauceREST != null) {

            WebDriver driver = this.threadContext.get().getDriver();

            if (driver instanceof RemoteWebDriver) {

                RemoteWebDriver remoteDriver = RemoteWebDriver.class.cast(driver);

                this.sauceREST.jobPassed(remoteDriver.getSessionId().toString());
            }
        }
    }

    @Override
    public void onTestFailure(ITestResult result) {

        if (this.sauceREST != null) {

            WebDriver driver = this.threadContext.get().getDriver();

            if (driver instanceof RemoteWebDriver) {

                RemoteWebDriver remoteDriver = RemoteWebDriver.class.cast(driver);

                this.sauceREST.jobFailed(remoteDriver.getSessionId().toString());

                try {
                    remoteDriver.quit();
                }
                catch (Exception e) {
                    log.debug("Ignoring exception: ", e);
                }

                this.threadContext.set(null);
            }

        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onFinish(ITestContext context) {
        // TODO Auto-generated method stub

    }

}
