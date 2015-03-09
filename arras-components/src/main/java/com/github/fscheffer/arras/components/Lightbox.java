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

package com.github.fscheffer.arras.components;

import javax.inject.Inject;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.BeginRender;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.ioc.internal.util.InternalUtils;

import com.github.fscheffer.arras.ArrasConstants;
import com.github.fscheffer.arras.ArrasUtils;

@Import(module = "arras/lightbox", stylesheet = ArrasConstants.LIGHTBOX_CSS_PATH_VALUE)
@SupportsInformalParameters
public class Lightbox {

    /**
     * a id of an element or a path to a site which should be opened within the lightbox
     *
     * if blank the lightbox will try to ready the href from the triggering <a>-tag, ie. <a href="http://www.google.com">
     */
    @Parameter(defaultPrefix = BindingConstants.LITERAL, required = true)
    private String             href;

    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    private String             rel;

    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    private boolean            inline;

    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    private boolean            fixed;

    @Parameter(defaultPrefix = BindingConstants.LITERAL, value = "true")
    private boolean            scrolling;

    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    private String             width;

    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    private String             height;

    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    private String             transition;

    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    private boolean            slideshow;

    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    private boolean            iframe;

    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    private String             title;

    @Parameter(required = false, defaultPrefix = BindingConstants.LITERAL, value = "literal:-1")
    private int                innerWidth;

    @Parameter(required = false, defaultPrefix = BindingConstants.LITERAL, value = "literal:-1")
    private int                innerHeight;

    /**
     * If true, the lightbox will be shown after the page was loaded
     */
    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    private boolean            open;

    /**
     * The client of the zone (without the #)
     */
    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    private String             zone;

    @Inject
    private ComponentResources resources;

    @BeginRender
    void begin(MarkupWriter writer) {
        writer.element("a", "data-container-type", "lightbox");
    }

    @AfterRender
    void after(MarkupWriter writer) {

        writer.attributes("href", this.href);

        // can't use inline AND iframe, so choose one (or none if none is set, .e.g. for slideshows)
        if (this.inline) {
            ArrasUtils.addOption(writer, "inline", true);
        }
        else {
            ArrasUtils.addOption(writer, "iframe", this.iframe);
        }

        // TODO: not sure if colorbox supports slideshows of inline (or iframe) contents
        ArrasUtils.addOption(writer, "rel", this.rel);
        ArrasUtils.addOption(writer, "slideshow", this.slideshow);
        ArrasUtils.addOption(writer, "transition", this.transition);

        ArrasUtils.addOption(writer, "open", this.open);
        ArrasUtils.addOption(writer, "zone", this.zone);

        ArrasUtils.addOption(writer, "fixed", this.fixed);
        ArrasUtils.addOption(writer, "height", this.height);
        ArrasUtils.addOption(writer, "width", this.width);

        if (!this.scrolling) {
            ArrasUtils.addDataAttribute(writer, "scrolling", false);
        }

        if (this.innerWidth >= 0) {
            ArrasUtils.addDataAttribute(writer, "innerWidth", this.innerWidth);
        }

        if (this.innerHeight >= 0) {
            ArrasUtils.addDataAttribute(writer, "innerHeight", this.innerHeight);
        }

        if (InternalUtils.isNonBlank(this.title)) {
            writer.attributes("title", this.title);
        }

        this.resources.renderInformalParameters(writer);

        writer.end();
    }
}
