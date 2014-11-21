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
    public void before() {
        open("/MediumEditorDemo");
        element(By.linkText("Reset demo")).click();
        waitUntil(pageHasLoaded());
    }

    // TODO: test the toolbar but I have absolutely no clue how to select a text using selenium webdriver api

    @Test
    public void testNoToolbar() {

        String selector = "#noToolbar";

        click(selector);

        waitUntil(focused(selector));

        sendKeys(selector, " foobar");

        waitUntil(containsText(selector, "foobar"));

        clickSave();

        waitUntil(containsText(selector, "foobar"));
    }

    @Test
    public void testDisabled() {

        String selector = "#disabled";

        waitUntil(containsText(selector, "Hello World!"));

        click(selector);

        sendKeys(selector, " foobar");

        clickSave();

        waitUntil(notContainsText(selector, "foobar"));
    }

    private void clickSave() {

        click("#submit_0");

        waitUntil(pageHasLoaded());

        // reload the page...just to be sure
        open("/MediumEditorDemo");

        waitUntil(pageHasLoaded());
    }

}
