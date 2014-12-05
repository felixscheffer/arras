package com.github.fscheffer.arras.test;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
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

    private Logger                             logger        = LoggerFactory.getLogger(TestContextPool.class);

    private Map<Capabilities, List<PoolEntry>> pool          = CollectionFactory.newMap();

    private Thread                             cleanupThread = new CleanupThread();

    private class CleanupThread extends Thread {

        @Override
        public void run() {

            long timeoutInMillis = TimeUnit.SECONDS.toMillis(10);

            while (true) {

                if (isInterrupted()) {
                    return;
                }

                synchronized (TestContextPool.this.pool) {

                    for (List<PoolEntry> entries : TestContextPool.this.pool.values()) {

                        for (Iterator<PoolEntry> iter = entries.iterator(); iter.hasNext();) {

                            PoolEntry entry = iter.next();

                            if (entry.isOutdated(timeoutInMillis)) {

                                TestContextPool.this.logger.info("Context timed out after {} ms without usage!",
                                                                 timeoutInMillis);

                                iter.remove();

                                terminateContext(entry.getContext());
                            }
                        }
                    }
                }

                try {
                    sleep(timeoutInMillis / 2);
                }
                catch (InterruptedException e) {
                    TestContextPool.this.logger.debug("CleanupThread was interrupted!");
                    return;
                }
            }
        }
    };

    public TestContextPool() {

        this.cleanupThread.start();

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

        List<PoolEntry> available = this.pool.get(capabilities);

        if (available != null && !available.isEmpty()) {

            synchronized (this.pool) {

                int size = available.size();
                if (size > 0) {
                    return available.remove(size - 1).getContext();
                }
            }
        }

        return createTestContext(capabilities);
    }

    public void release(TestContext context) {

        Capabilities key = context.getCapabilities();

        synchronized (this.pool) {
            InternalUtils.addToMapList(this.pool, key, new PoolEntry(context));
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

        this.logger.debug("Terminating TestContextPool!");

        // terminate contexts asap to prevent timeout errors for remote drivers.
        terminatePool();
    }

    private void terminatePool() {

        this.cleanupThread.interrupt();

        synchronized (this.pool) {

            for (List<PoolEntry> list : this.pool.values()) {

                for (PoolEntry entry : list) {

                    terminateContext(entry.getContext());
                }
            }
            this.pool.clear();
        }
    }

    private void terminateContext(TestContext context) {

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

    private static class PoolEntry {

        private final long        lastUsedInMillis;

        private final TestContext context;

        public PoolEntry(TestContext context) {
            this.lastUsedInMillis = new Date().getTime();
            this.context = context;
        }

        public boolean isOutdated(long timeoutInMillis) {
            return this.lastUsedInMillis + timeoutInMillis < new Date().getTime();
        }

        public TestContext getContext() {
            return this.context;
        }
    }
}
