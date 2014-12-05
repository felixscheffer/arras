package com.github.fscheffer.arras.test;

import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import org.apache.tapestry5.ioc.internal.util.InternalUtils;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.UnsupportedCommandException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.UnreachableBrowserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestContextPool {

    private Logger                               logger    = LoggerFactory.getLogger(TestContextPool.class);

    private Map<Capabilities, List<TestContext>> pool      = CollectionFactory.newMap();

    private ExecutorService                      executors = Executors.newFixedThreadPool(1);

    private Future<?>                            supplier;

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

    public TestContext aquire(final Capabilities capabilities) {

        // 250 ms * 1200 = 300sec / 5min
        for (int i = 0; i < 1200; i++) {

            TestContext context = poll(capabilities);
            if (context != null) {
                return context;
            }

            synchronized (this) {

                if (this.supplier == null || this.supplier.isDone()) {

                    this.supplier = this.executors.submit(new Runnable() {

                        @Override
                        public void run() {

                            TestContext context = createTestContext(capabilities);

                            release(context);
                        }
                    });
                }
            }

            try {
                Thread.sleep(250);
            }
            catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        throw new RuntimeException("TIMEOUT: Failed to create web driver for " + capabilities.toString());
    }

    private TestContext poll(Capabilities capabilities) {

        List<TestContext> available = this.pool.get(capabilities);

        if (available != null && !available.isEmpty()) {

            synchronized (this.pool) {

                int size = available.size();
                if (size > 0) {
                    return available.remove(size - 1);
                }
            }
        }

        return null;
    }

    public void release(TestContext context) {

        Capabilities key = context.getCapabilities();

        synchronized (this.pool) {
            InternalUtils.addToMapList(this.pool, key, context);
        }
    }

    private TestContext createTestContext(Capabilities capabilities) {

        ServiceLoader<TestContextFactory> factories = ServiceLoader.load(TestContextFactory.class);

        for (TestContextFactory factory : factories) {
            return factory.build(capabilities);
        }

        this.logger.warn("No implementation of TestConfig was found. Falling back to DefaultTestContext!");

        return new DefaultTestContext(capabilities);
    }

    protected void terminate() {

        this.logger.info("Terminating TestContextPool!");

        // terminate contexts asap to prevent timeout errors for remote drivers.
        terminatePool();

        try {
            this.executors.shutdown();
            // this may take some time
            this.executors.awaitTermination(60, TimeUnit.SECONDS);
        }
        catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        finally {
            // terminate all the drivers that are late.
            terminatePool();
        }
    }

    private void terminatePool() {
        synchronized (this.pool) {

            for (List<TestContext> list : this.pool.values()) {

                for (TestContext context : list) {

                    WebDriver driver = context.getDriver();

                    try {
                        driver.quit();
                    }
                    catch (UnreachableBrowserException e) {
                        // ignore (see above)
                        this.logger.debug("Ignoring exception: ", e);
                    }
                    catch (UnsupportedCommandException e) {
                        // sometimes the connection to the RemoteWebDriver times out
                        this.logger.debug("Ignoring exception: ", e);
                    }
                }
            }
            this.pool.clear();
        }
    }
}
