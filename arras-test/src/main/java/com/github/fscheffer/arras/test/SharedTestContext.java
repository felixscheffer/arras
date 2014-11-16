package com.github.fscheffer.arras.test;

import java.util.ServiceLoader;

import org.slf4j.LoggerFactory;

public class SharedTestContext {

    private static final ThreadLocal<TestContext> CONTEXT = new LocalTestContext();

    public TestContext get() {

        return CONTEXT.get();
    }

    public void destroy() {

        TestContext context = CONTEXT.get();

        CONTEXT.set(null);

        if (context != null) {
            // Note: We can't close the driver in a shutdown hook
            context.getDriver().close();
        }
    }

    private static final class LocalTestContext extends ThreadLocal<TestContext> {

        @Override
        protected TestContext initialValue() {

            ServiceLoader<TestContext> loader = ServiceLoader.load(TestContext.class);

            for (TestContext context : loader) {
                return context;
            }

            LoggerFactory.getLogger(SharedTestContext.class)
            .warn("No implementation of TestConfig was found. Falling back to DefaultTestContext!");

            return new DefaultTestContext();
        }
    }
}
