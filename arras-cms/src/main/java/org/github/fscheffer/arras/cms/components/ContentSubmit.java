package org.github.fscheffer.arras.cms.components;

import javax.inject.Inject;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.BeginRender;
import org.apache.tapestry5.annotations.SupportsInformalParameters;

@SupportsInformalParameters
public class ContentSubmit {

    @Inject
    private ComponentResources resources;

    @BeginRender
    void begin(MarkupWriter writer) {
        writer.element("button", "type", "button", "data-container-type", "content-submit");
    }

    @AfterRender
    void after(MarkupWriter writer) {

        this.resources.renderInformalParameters(writer);

        writer.end();
    }
}
