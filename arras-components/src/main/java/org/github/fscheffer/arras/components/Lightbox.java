package org.github.fscheffer.arras.components;

import javax.inject.Inject;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.BeginRender;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Path;
import org.apache.tapestry5.ioc.internal.util.InternalUtils;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

/**
 * For documentation of options:
 * http://www.jacklmoore.com/colorbox
 *
 * Note: we're using a modified version of jquery.colorbox.js which implements
 *       https://github.com/jackmoore/colorbox/pull/262
 * Note: change settings.scrolling into settings.get('scrolling')
 *
 * @author fscheffer
 */

// TODO: change LightboxComponent into a mixin??
@Import(module = "jquery.colorbox")
public class Lightbox {

    public static final String SYMBOL_CSS_PATH = "arras.lightbox.css-path";

    /**
     * css selector, i.e. id or class which should trigger the lightbox
     *
     * if the selector parameter is blank, the lightbox will open immediately
     */
    @Parameter
    private String             selector;

    /**
     * client id of a zone if the content of the lightbox is a zone
     *
     * you can use a normal eventlink to trigger the zone and the lightbox will open AFTER the zone has been updated.
     */
    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    private String             zone;

    /**
     * a id of an element or a path to a site which should be opened within the lightbox
     *
     * if blank the lightbox will try to ready the href from the triggering <a>-tag, ie. <a href="http://www.google.com">
     */
    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    private String             href;

    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    private String             rel;

    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    private String             transition;

    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    private String             slideshow;

    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    private boolean            fixed;

    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    private String             title;

    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    private boolean            iframe;

    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    private boolean            inline;

    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    private boolean            scrolling;

    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    private String             width;

    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    private String             height;

    @Parameter(required = false, defaultPrefix = BindingConstants.LITERAL, value = "literal:-1")
    private int                innerWidth;

    @Parameter(required = false, defaultPrefix = BindingConstants.LITERAL, value = "literal:-1")
    private int                innerHeight;

    @Parameter(value = "defaultSelectorId")
    private String             selectorClientId;

    @Environmental
    private JavaScriptSupport  javaScriptSupport;

    @Inject
    @Path(Lightbox.SYMBOL_CSS_PATH)
    private Asset              cssFile;

    @Inject
    private ComponentResources componentResources;

    @BeginRender
    public void beginRender(MarkupWriter writer) {

        if (this.inline && this.componentResources.hasBody()) {

            writer.element("div", "style", "display: none");
            writer.element("div", "id", this.selectorClientId);

            // auto-connect lightbox to body content
            if (InternalUtils.isBlank(this.href)) {
                this.href = "#" + this.selectorClientId;
            }
        }
    }

    @AfterRender
    public void afterRender(MarkupWriter writer) {

        if (this.inline && this.componentResources.hasBody()) {

            writer.end();
            writer.end();
        }

        this.javaScriptSupport.importStylesheet(this.cssFile);

        JSONObject options = buildLightboxOptions();

        if (InternalUtils.isBlank(this.zone)) {
            this.javaScriptSupport.require("lightbox:init").with(this.selector, options);
        }
        else {
            this.javaScriptSupport.require("lightbox:attachToZone").with(this.zone,
                                                                         this.selector == null ? JSONObject.NULL
                                                                                              : this.selector, options);
        }
    }

    public String getDefaultSelectorId() {
        return this.javaScriptSupport.allocateClientId("selector");
    }

    private JSONObject buildLightboxOptions() {

        JSONObject options = new JSONObject();

        addIfNotBlank(options, "href", this.href);
        addIfNotBlank(options, "rel", this.rel);
        addIfNotBlank(options, "transition", this.transition);
        addIfNotBlank(options, "slideshow", this.slideshow);
        addIfNotBlank(options, "title", this.title);

        options.put("iframe", this.iframe);
        options.put("inline", this.inline);
        options.put("scrolling", this.scrolling);

        addIfNotBlank(options, "height", this.height);
        addIfNotBlank(options, "width", this.width);

        if (this.innerWidth >= 0) {
            options.put("innerWidth", this.innerWidth);
        }
        if (this.innerHeight >= 0) {
            options.put("innerHeight", this.innerHeight);
        }

        if (this.fixed) {
            options.put("fixed", true);
        }

        return options;
    }

    private void addIfNotBlank(JSONObject options, String name, String object) {

        if (!InternalUtils.isBlank(object)) {
            options.put(name, object);
        }
    }
}
