package com.github.fscheffer.arras.bootstrap.services;

import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.annotations.Contribute;
import org.apache.tapestry5.services.Core;
import org.apache.tapestry5.services.javascript.JavaScriptStack;
import org.apache.tapestry5.services.javascript.StackExtension;

public class ArrasBootstrapModule {

    @Contribute(JavaScriptStack.class)
    @Core
    public static void overrideBootstrapCSS(OrderedConfiguration<StackExtension> conf) {
        conf.override("bootstrap.css", StackExtension.stylesheet("META-INF/assets/sharelinks.less"),
                      "before:tapestry.css");
    }
}
