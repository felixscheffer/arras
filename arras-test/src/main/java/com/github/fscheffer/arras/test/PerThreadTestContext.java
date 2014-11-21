package com.github.fscheffer.arras.test;


public class PerThreadTestContext {

    private static final ThreadLocal<TestContext> CONTEXT = new ThreadLocal<TestContext>();

    public TestContext get() {
        return CONTEXT.get();
    }

    public void set(TestContext newContext) {
        CONTEXT.set(newContext);
    }

    public void destroy() {

        TestContext context = CONTEXT.get();

        CONTEXT.set(null);

        if (context != null) {
            // Note: We can't close the driver in a shutdown hook
            context.getDriver().close();
        }
    }
}
