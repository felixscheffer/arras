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

package org.github.fscheffer.arras.demo;

import org.github.fscheffer.arras.test.ArrasTestCase;
import org.openqa.selenium.By;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class RemoteSubmitIT extends ArrasTestCase {

    @BeforeMethod
    void before() {
        open("/RemoteSubmitDemo");
    }

    @Test
    void testVisibleSubmit() {

        text(By.cssSelector("#visibleExample input[type=text]"), "Hello World!");

        click(By.cssSelector("#visibleExample > button"));

        waitForPageToLoad();

        assertTextPresent(By.cssSelector(".alert > span"),
                          "Triggered form with visible submit button! Form content was: \"Hello World!\"");
    }

    @Test
    void testInvisibleSubmit() {

        text(By.cssSelector("#invisibleExample input[type=text]"), "Hello Tapestry!");

        click(By.cssSelector("#invisibleExample > button"));

        waitForPageToLoad();

        sleep(800);

        assertTextPresent(By.cssSelector(".alert > span"),
            "Triggered form with invisible submit button! Form content was: \"Hello Tapestry!\"");
    }
}
