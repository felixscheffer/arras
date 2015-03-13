// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.github.fscheffer.arras.services;

import java.util.Collection;

import org.apache.tapestry5.annotations.Path;
import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.Resource;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.Contribute;
import org.apache.tapestry5.ioc.services.Coercion;
import org.apache.tapestry5.ioc.services.CoercionTuple;
import org.apache.tapestry5.services.BindingFactory;
import org.apache.tapestry5.services.LibraryMapping;
import org.apache.tapestry5.services.javascript.ExtensibleJavaScriptStack;
import org.apache.tapestry5.services.javascript.JavaScriptModuleConfiguration;
import org.apache.tapestry5.services.javascript.JavaScriptStack;
import org.apache.tapestry5.services.javascript.JavaScriptStackSource;
import org.apache.tapestry5.services.javascript.ModuleManager;
import org.apache.tapestry5.services.javascript.StackExtension;

import com.github.fscheffer.arras.Arras;
import com.github.fscheffer.arras.ArrasConstants;
import com.github.fscheffer.arras.CollectionFilteringDataSource;
import com.github.fscheffer.arras.FilteringDataSource;

public class ArrasComponentsModule {

    public static void contributeComponentClassResolver(Configuration<LibraryMapping> configuration) {
        configuration.add(new LibraryMapping("arras", "com.github.fscheffer.arras"));
    }

    public static void bind(ServiceBinder binder) {
        binder.bind(SubmissionProcessor.class, SubmissionProcessorImpl.class);
        binder.bind(JavaScriptStack.class, ExtensibleJavaScriptStack.class).withMarker(Arras.class)
              .withId("ArrasJavaScriptStack");
    }

    @Contribute(JavaScriptStackSource.class)
    public static void provideBuiltinJavaScriptStacks(MappedConfiguration<String, JavaScriptStack> conf,
                                                      @Arras JavaScriptStack stack) {
        conf.add("arras", stack);
    }

    public static void contributeFactoryDefaults(MappedConfiguration<String, String> conf) {

        conf.add(ArrasConstants.LIGHTBOX_CSS_PATH, "colorbox/example1/colorbox.css");
        conf.add(ArrasConstants.PLAYER_CSS_PATH, "video-js/video-js.css");
        conf.add(ArrasConstants.SELECT2_CSS_PATH, "select2.css");
    }

    @Contribute(JavaScriptStack.class)
    @Arras
    public static void setupJavaScriptStack(OrderedConfiguration<StackExtension> conf) {

        for (String module : new String[] { "datatables", "dropdown", "events", "lightbox", "loading-overlay",
            "medium-editor", "player", "remotesubmit", "select2", "sortable-jquery", "tabgroup", "transition" }) {

            conf.add("arras/" + module, StackExtension.module("arras/" + module));
        }

    }

    @Contribute(ModuleManager.class)
    public static void setupBaseModules(MappedConfiguration<String, Object> conf,
                                        @Path("META-INF/assets/arras/colorbox/jquery.colorbox.js") Resource colorbox,
                                        @Path("META-INF/assets/arras/medium-editor/medium-editor.js") Resource mediumEditor) {

        conf.add("shim/jquery.colorbox", new JavaScriptModuleConfiguration(colorbox).dependsOn("jquery"));
        conf.add("shim/medium-editor", new JavaScriptModuleConfiguration(mediumEditor).exports("MediumEditor"));
    }

    public static void contributeBindingSource(MappedConfiguration<String, BindingFactory> conf) {
        conf.add("clientid", new ClientIdBindingFactory());
    }

    public static void contributeTypeCoercer(Configuration<CoercionTuple> conf) {

        conf.add(CoercionTuple.create(Collection.class, FilteringDataSource.class,
                                      new Coercion<Collection, FilteringDataSource>() {

                                          @Override
                                          public FilteringDataSource coerce(Collection input) {

                                              return new CollectionFilteringDataSource(input);
                                          }
                                      }));
    }

    public static void contributeContentTypeAnalyzer(MappedConfiguration<String, String> conf) {
        conf.add("m4a", "audio/mp4");
        conf.add("acc", "audio/acc");
        conf.add("mp3", "audio/mpeg");
        conf.add("oga", "audio/ogg");
        conf.add("wav", "audio/wav");
        conf.add("m4v", "video/mp4");
        conf.add("ogv", "video/ogg");
        conf.add("ogg", "application/ogg");
    }

    public static void contributeCompressionAnalyzer(MappedConfiguration<String, Boolean> conf) {
        // compressing compressed files is waste of cpu power
        conf.add("audio/*", false);
        conf.add("video/*", false);
        conf.add("application/ogg", false);
    }
}
