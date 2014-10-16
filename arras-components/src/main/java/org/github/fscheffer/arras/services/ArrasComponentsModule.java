package org.github.fscheffer.arras.services;

import org.apache.tapestry5.annotations.Path;
import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.Resource;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.Contribute;
import org.apache.tapestry5.services.LibraryMapping;
import org.apache.tapestry5.services.javascript.JavaScriptModuleConfiguration;
import org.apache.tapestry5.services.javascript.ModuleManager;

public class ArrasComponentsModule {

    public static void contributeComponentClassResolver(Configuration<LibraryMapping> configuration) {
        configuration.add(new LibraryMapping("arras", "org.github.fscheffer.arras"));
    }

    public static void bind(ServiceBinder binder) {}

    public static void contributeFactoryDefaults(MappedConfiguration<String, String> conf) {

        conf.add(Lightbox.SYMBOL_CSS_PATH, "colorbox/example1/colorbox.css");
    }

    @Contribute(ModuleManager.class)
    public static void setupBaseModules(MappedConfiguration<String, Object> conf,
                                        @Path("META-INF/assets/arras/colorbox/jquery.colorbox.js") Resource colorbox) {

        conf.add("jquery.colorbox", new JavaScriptModuleConfiguration(colorbox).dependsOn("jquery"));
    }
}
