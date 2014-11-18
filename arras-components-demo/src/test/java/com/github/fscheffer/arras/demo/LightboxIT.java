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
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.github.fscheffer.arras.test.ArrasTestCase;

public class LightboxIT extends ArrasTestCase {

    @BeforeMethod
    void before() {
        open("/LightboxDemo");
    }

    @Test
    void testBasic() {

        openLightbox(By.linkText("Bridge over river"));

        assertImageInLightbox("/arras/assets/meta/b9626ecd/photos/landscape/bridge-over-river.jpg");

        waitUntil(containsText("#cboxTitle", "Bridge over river"));

        closeLightbox();
    }

    @Test
    void testGroup() {

        // group example
        openLightbox(By.linkText("Rocky cliffs"));

        assertImageInLightbox("/arras/assets/meta/d45fddff/photos/landscape/pointarena_rockycliffs.jpg");

        click("#cboxLoadedContent > img");

        assertImageInLightbox("/arras/assets/meta/3aad432/photos/landscape/san-joaquin-river-view.jpg");

        click("#cboxLoadedContent > img");

        assertImageInLightbox("/arras/assets/meta/dedf595e/photos/landscape/man_point-arena-stornetta.jpg");

        click("#cboxLoadedContent > img");

        assertImageInLightbox("/arras/assets/meta/d45fddff/photos/landscape/pointarena_rockycliffs.jpg");

        closeLightbox();
    }

    @Test
    void testFixedSize() {

        // fixed size
        openLightbox(By.linkText("San Joaquin River (Fixed size)"));

        WebElement wrapper = element(By.cssSelector("#cboxWrapper"));

        Assert.assertEquals(wrapper.getCssValue("height"), "700px");
        Assert.assertEquals(wrapper.getCssValue("width"), "500px");

        closeLightbox();

        Dimension viewport = viewport();

        // fixed size in percent
        openLightbox(By.linkText("Rocky cliffs (Fixed size in percent)"));

        Assert.assertEquals(wrapper.getCssValue("width"), toPixel(viewport.width * 0.5));
        Assert.assertEquals(wrapper.getCssValue("height"), toPixel(viewport.height * 0.75));

        closeLightbox();
    }

    private String toPixel(double a) {
        return (int) Math.ceil(a) + "px";
    }

    @Test
    void testInlined() {

        // inline
        openLightbox(By.linkText("Show Kafka!"));

        waitUntil(containsText("#cboxLoadedContent h3", "Kafka"));

        closeLightbox();
    }

    @Test
    void testZoneUpdate() {

        openLightbox(By.linkText("Some ajax event"));

        waitUntil(containsText("#cboxLoadedContent h3", "Updated zone at"));

        closeLightbox();
    }

    @Test
    void testLightboxInZone() {

        element(By.linkText("Trigger zone")).click();

        waitForAjaxRequestsToComplete();

        waitUntil(containsText("#lightboxZone", "Stornetta in a Zone"));
        waitUntil(containsText("#lightboxZone", "Show content with zone"));

        // check Lightbox
        openLightbox(By.linkText("Stornetta in a Zone"));

        assertImageInLightbox("/arras/assets/meta/dedf595e/photos/landscape/man_point-arena-stornetta.jpg");

        closeLightbox();

        // check LightboxBody
        openLightbox(By.linkText("Show content with zone"));

        waitUntil(containsText("#cboxLoadedContent h3", "Content loaded by Zone"));

        closeLightbox();
    }

    private void openLightbox(By by) {

        element(by).click();

        waitUntil(visible("#cboxLoadedContent"));
    }

    private void closeLightbox() {

        click("#cboxClose");

        waitUntil(invisible("#cboxOverlay"));
    }

    private void assertImageInLightbox(String expected) {

        String selector = "#cboxLoadedContent > img";

        waitUntil(visible(selector));

        String attr = attr(selector, "src");

        // ignore base url
        Assert.assertTrue(attr.endsWith(expected), "Expected \"" + expected + "\", but got \"" + attr + "\"");
    }

}
