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

package com.github.fscheffer.arras.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.github.fscheffer.arras.test.ArrasTestCase;

public class RemoteSubmitIT extends ArrasTestCase {

    private final Logger logger = LoggerFactory.getLogger(RemoteSubmitIT.class);

    @BeforeMethod
    void before() {
        open("/RemoteSubmitDemo");
    }

    @Test
    public void testVisibleSubmit() {

        this.logger.info("Running testVisibleSubmit()");

        text("#visibleExample input[type=text]", "Hello World!");

        click("#visibleExample > button");

        waitUntil(pageHasLoaded());

        waitUntil(containsText(".alert > span",
            "Triggered form with visible submit button! Form content was: \"Hello World!\""));
    }

    @Test
    public void testInvisibleSubmit() {

        this.logger.info("Running testInvisibleSubmit()");

        // Note: sometimes selenium sends the keys to the textfield of the visible example above.
        //       maybe the dom was not loaded..so try waiting.
        //        sleep(500);

        text("#invisibleExampleTextfield", "Hello Tapestry!");

        click("#invisibleExample > button");

        waitUntil(pageHasLoaded());

        waitUntil(containsText(".alert > span",
            "Triggered form with invisible submit button! Form content was: \"Hello Tapestry!\""));
    }
}
