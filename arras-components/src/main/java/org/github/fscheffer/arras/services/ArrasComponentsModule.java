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
import org.github.fscheffer.arras.ArrasConstants;

public class ArrasComponentsModule {

    public static void contributeComponentClassResolver(Configuration<LibraryMapping> configuration) {
        configuration.add(new LibraryMapping("arras", "org.github.fscheffer.arras"));
    }

    public static void bind(ServiceBinder binder) {}

    public static void contributeFactoryDefaults(MappedConfiguration<String, String> conf) {

        conf.add(ArrasConstants.LIGHTBOX_CSS_PATH, "colorbox/example1/colorbox.css");
    }

    @Contribute(ModuleManager.class)
    public static void setupBaseModules(MappedConfiguration<String, Object> conf,
                                        @Path("META-INF/assets/arras/colorbox/jquery.colorbox.js") Resource colorbox,
                                        @Path("META-INF/assets/arras/medium-editor/medium-editor.js") Resource mediumEditor) {

        conf.add("shim/jquery.colorbox", new JavaScriptModuleConfiguration(colorbox).dependsOn("jquery"));
        conf.add("shim/medium-editor", new JavaScriptModuleConfiguration(mediumEditor).exports("MediumEditor"));
    }
}
