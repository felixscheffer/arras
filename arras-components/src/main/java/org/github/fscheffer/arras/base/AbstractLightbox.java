package org.github.fscheffer.arras.base;

import javax.inject.Inject;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Path;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import org.github.fscheffer.arras.ArrasConstants;
import org.github.fscheffer.arras.ArrasUtils;

public class AbstractLightbox {

    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    private boolean           inline;

    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    private boolean           fixed;

    @Parameter(defaultPrefix = BindingConstants.LITERAL, value = "true")
    private boolean           scrolling;

    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    private String            width;

    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    private String            height;

    @Environmental
    private JavaScriptSupport javaScriptSupport;

    @Inject
    @Path(ArrasConstants.LIGHTBOX_CSS_PATH_VALUE)
    private Asset             cssFile;

    @SetupRender
    void setup() {
        this.javaScriptSupport.importStylesheet(this.cssFile);
    }

    protected void addOptions(MarkupWriter writer) {

        ArrasUtils.addOption(writer, "height", this.height);
        ArrasUtils.addOption(writer, "width", this.width);
        ArrasUtils.addOption(writer, "inline", this.inline);
        ArrasUtils.addOption(writer, "fixed", this.fixed);

        if (!this.scrolling) {
            ArrasUtils.addDataAttribute(writer, "scrolling", false);
        }
    }
}
