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

import java.net.URISyntaxException;

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
import org.github.fscheffer.arras.test.services.ArrasTestModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ImportModule({ ArrasCmsModule.class, ArrasTestModule.class })
public class AppModule {

    private static Logger logger = LoggerFactory.getLogger(AppModule.class);

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

        addFile(conf, assetSource, "META-INF/assets/photos/landscape/", "bridge-over-river.jpg");
        addFile(conf, assetSource, "META-INF/assets/photos/landscape/", "man_point-arena-stornetta.jpg");
        addFile(conf, assetSource, "META-INF/assets/photos/landscape/", "pointarena_rockycliffs.jpg");
        addFile(conf, assetSource, "META-INF/assets/photos/landscape/", "san-joaquin-river-view.jpg");

        addFile(conf, assetSource, "META-INF/assets/photos/paris/", "eiffel-tower.jpg");
        addFile(conf, assetSource, "META-INF/assets/photos/paris/", "musee-de-orsay.jpg");
        addFile(conf, assetSource, "META-INF/assets/photos/paris/", "museum-clock.jpg");
        addFile(conf, assetSource, "META-INF/assets/photos/paris/", "notre_dames-view.jpg");
        addFile(conf, assetSource, "META-INF/assets/photos/paris/", "museum-clock.jpg");
        addFile(conf, assetSource, "META-INF/assets/photos/paris/", "paris_night.jpg");
        addFile(conf, assetSource, "META-INF/assets/photos/paris/", "river-seine.jpg");
        addFile(conf, assetSource, "META-INF/assets/photos/paris/", "tricolour-flag.jpg");
    }

    private static void addFile(OrderedConfiguration<String> conf, AssetSource source, String folder, String filename) {
        // TODO Auto-generated method stub
        Asset asset = source.getClasspathAsset(folder + filename);

        conf.add(filename, asset.toClientURL());

    }
}