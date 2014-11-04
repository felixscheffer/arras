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

import org.openqa.selenium.By;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.github.fscheffer.arras.test.ArrasTestCase;

public class MediumEditorIT extends ArrasTestCase {

    @BeforeMethod
    void before() {
        open("/MediumEditorDemo");
        click(By.linkText("Reset demo"));
        waitForPageToLoad();
    }

    // TODO: test the toolbar but I have absolutely no clue how to select a text using selenium webdriver api

    @Test
    void testNoToolbar() {

        By by = By.id("noToolbar");

        click(by);

        sendKeys(by, "foobar");

        clickSave();

        assertTextPresent(by, "foobar");
    }

    @Test
    void testDisabled() {

        By by = By.id("disabled");

        assertTextPresent(by, "Hello World!");

        click(by);

        sendKeys(by, " foobar");

        clickSave();

        assertTextNotPresent(by, "foobar");
    }

    private void clickSave() {

        click(By.id("submit_0"));

        waitForPageToLoad();

        // reload the page...just to be sure
        open("/MediumEditorDemo");

        waitForPageToLoad();
    }

}
