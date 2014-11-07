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

package com.github.fscheffer.arras.demo.services;

import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.annotations.ImportModule;

import com.github.fscheffer.arras.services.ArrasComponentsModule;
import com.github.fscheffer.arras.test.services.ArrasTestModule;

@ImportModule({ ArrasComponentsModule.class, ArrasTestModule.class })
public class AppModule {

    public static void contributeApplicationDefaults(MappedConfiguration<String, Object> conf) {

        conf.add(SymbolConstants.JAVASCRIPT_INFRASTRUCTURE_PROVIDER, "jquery");
        // disable production mode to enable live-reloading
        conf.add(SymbolConstants.PRODUCTION_MODE, false);
        conf.add(SymbolConstants.HMAC_PASSPHRASE, "dcc0ca4c-57b3-11e4-a536-5c260a602453");
    }
}
