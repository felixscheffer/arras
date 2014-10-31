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

package org.github.fscheffer.arras.cms.demo.services;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.ImportModule;
import org.apache.tapestry5.services.AssetSource;
import org.github.fscheffer.arras.cms.services.ArrasCmsModule;
import org.github.fscheffer.arras.cms.services.AvailableImages;
import org.github.fscheffer.arras.cms.services.AvailableImagesImpl;

@ImportModule(ArrasCmsModule.class)
public class AppModule {

    public static void contributeApplicationDefaults(MappedConfiguration<String, Object> conf) {

        conf.add(SymbolConstants.JAVASCRIPT_INFRASTRUCTURE_PROVIDER, "jquery");
        // disable production mode to enable live-reloading
        conf.add(SymbolConstants.PRODUCTION_MODE, false);
        conf.add(SymbolConstants.HMAC_PASSPHRASE, "896c29e0-55fb-11e4-8dad-5c260a602453");
    }

    public static void bind(ServiceBinder binder) {
        binder.bind(UserManager.class, SimpleUserManager.class);
        binder.bind(AvailableImages.class, AvailableImagesImpl.class);
    }

    public static void contributeAvailableImages(OrderedConfiguration<String> conf, AssetSource assetSource)
        throws URISyntaxException {

        addFolder(conf, assetSource, "META-INF/assets/photos/landscape/");
        addFolder(conf, assetSource, "META-INF/assets/photos/paris/");
    }

    private static void addFolder(OrderedConfiguration<String> conf, AssetSource source, String path)
        throws URISyntaxException {

        URL baseUrl = Thread.currentThread().getContextClassLoader().getResource(path);

        File folder = new File(baseUrl.toURI());

        for (File file : folder.listFiles()) {

            if (file.isDirectory()) {
                continue;
            }

            String classpath = toClasspath(folder, file);

            Asset asset = source.getClasspathAsset(classpath);

            conf.add(file.getName(), asset.toClientURL());
        }
    }

    private static String toClasspath(File folder, File file) {

        String absolutePath = file.getAbsolutePath();

        int idx = absolutePath.indexOf("/META-INF/");

        return absolutePath.substring(idx + 1);
    }
}