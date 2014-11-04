package com.github.fscheffer.arras.cms.components;

import javax.inject.Inject;

import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.BeginRender;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.dom.Element;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

@SupportsInformalParameters
public class ContentSubmit implements ClientElement {

    @Inject
    private ComponentResources resources;

    @Inject
    private JavaScriptSupport  support;

    private String             uniqueId;

    private Element            button;

    @BeginRender
    void begin(MarkupWriter writer) {
        this.button = writer.element("button", "type", "button", "data-container-type", "content-submit");
    }

    @AfterRender
    void after(MarkupWriter writer) {

        this.resources.renderInformalParameters(writer);

        writer.end();
    }

    @Override
    public String getClientId() {
        if (this.uniqueId == null) {
            this.uniqueId = this.support.allocateClientId(this.resources);
            this.button.forceAttributes("id", this.uniqueId);
        }

        return this.uniqueId;
    }
}
