package com.github.fscheffer.arras.components;

import javax.inject.Inject;

import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.BeginRender;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

@SupportsInformalParameters
public class LightboxBody implements ClientElement {

    @Inject
    private ComponentResources resources;

    @Inject
    private JavaScriptSupport  support;

    private String             clientId;

    @BeginRender
    void begin(MarkupWriter writer) {

        this.clientId = this.support.allocateClientId(this.resources);

        writer.element("div", "style", "display: none;");
        writer.element("div", "id", this.clientId);
    }

    @AfterRender
    void after(MarkupWriter writer) {

        this.resources.renderInformalParameters(writer);

        writer.end();
        writer.end();
    }

    @Override
    public String getClientId() {
        return this.clientId;
    }
}
