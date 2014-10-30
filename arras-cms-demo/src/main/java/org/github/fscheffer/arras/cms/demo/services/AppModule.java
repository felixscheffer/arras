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

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.annotations.Path;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.ImportModule;
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

    public static void contributeAvailableImages(OrderedConfiguration<String> conf,
                                                 @Path("META-INF/assets/photos/landscape/bridge-over-river.jpg") Asset image1,
                                                 @Path("META-INF/assets/photos/landscape/man_point-arena-stornetta.jpg") Asset image2,
                                                 @Path("META-INF/assets/photos/landscape/pointarena_rockycliffs.jpg") Asset image3,
                                                 @Path("META-INF/assets/photos/landscape/san-joaquin-river-view.jpg") Asset image4) {
        conf.add("image1", image1.toClientURL());
        conf.add("image2", image2.toClientURL());
        conf.add("image3", image3.toClientURL());
        conf.add("image4", image4.toClientURL());

        conf.add("image5", image1.toClientURL());
        conf.add("image6", image2.toClientURL());
        conf.add("image7", image3.toClientURL());
        conf.add("image8", image4.toClientURL());
    }
}
