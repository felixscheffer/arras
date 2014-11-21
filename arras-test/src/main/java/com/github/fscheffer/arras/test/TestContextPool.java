package com.github.fscheffer.arras.test;

import java.util.List;
import java.util.ServiceLoader;

import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.UnreachableBrowserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestContextPool {

    private Logger            logger = LoggerFactory.getLogger(TestContextPool.class);

    private List<TestContext> pool   = CollectionFactory.newList();

    public TestContextPool() {
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO: terminating the pool within a shutdown hook doesn't work very well.
                //       Selenium also uses a shutdown hook to clean up which can cause the browser to become unresponsive
                //       before we can actually call driver.quit() (see TemporaryFilesystem.deleteTemporaryFiles())
                terminate();
            }
        }));
    }

    public TestContext aquire() {

        int size = this.pool.size();

        if (size > 0) {

            synchronized (this.pool) {

                size = this.pool.size();
                if (size > 0) {
                    return this.pool.remove(size - 1);
                }
            }
        }

        return createTestContext();
    }

    public void release(TestContext context) {
        synchronized (this.pool) {
            this.pool.add(context);
        }
    }

    private TestContext createTestContext() {
        ServiceLoader<TestContext> loader = ServiceLoader.load(TestContext.class);

        for (TestContext context : loader) {
            return context;
        }

        LoggerFactory.getLogger(PerThreadTestContext.class)
                     .warn("No implementation of TestConfig was found. Falling back to DefaultTestContext!");

        return new DefaultTestContext();
    }

    protected void terminate() {

        synchronized (this.pool) {

            for (TestContext context : this.pool) {

                WebDriver driver = context.getDriver();

                try {
                    driver.quit();
                }
                catch (UnreachableBrowserException e) {
                    // ignore (see above)
                }
            }
            this.pool.clear();
        }
    }
}
