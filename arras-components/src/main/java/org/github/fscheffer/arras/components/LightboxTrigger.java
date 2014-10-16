package org.github.fscheffer.arras.components;

import javax.inject.Inject;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.BeginRender;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.github.fscheffer.arras.ArrasUtils;
import org.github.fscheffer.arras.base.AbstractLightbox;

@SupportsInformalParameters
@Import(module = "lightbox")
public class LightboxTrigger extends AbstractLightbox {

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
    private String             transition;

    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    private boolean            slideshow;

    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    private boolean            iframe;

    @Parameter(required = false, defaultPrefix = BindingConstants.LITERAL, value = "literal:-1")
    private int                innerWidth;

    @Parameter(required = false, defaultPrefix = BindingConstants.LITERAL, value = "literal:-1")
    private int                innerHeight;

    @Inject
    private ComponentResources resources;

    @BeginRender
    void begin(MarkupWriter writer) {

        writer.element("a", "href", this.href, "data-container-type", "lightbox");

        ArrasUtils.addOption(writer, "rel", this.rel);
        ArrasUtils.addOption(writer, "transition", this.transition);
        ArrasUtils.addOption(writer, "slideshow", this.slideshow);
        ArrasUtils.addOption(writer, "iframe", this.iframe);

        super.addOptions(writer);

        if (this.innerWidth >= 0) {
            ArrasUtils.addDataAttribute(writer, "innerWidth", this.innerWidth);
        }
        if (this.innerHeight >= 0) {
            ArrasUtils.addDataAttribute(writer, "innerHeight", this.innerHeight);
        }
    }

    @AfterRender
    void after(MarkupWriter writer) {

        this.resources.renderInformalParameters(writer);

        writer.end();
    }
}
