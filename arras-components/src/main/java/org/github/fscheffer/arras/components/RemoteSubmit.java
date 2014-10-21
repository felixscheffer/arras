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

@SupportsInformalParameters
@Import(module = { "arras/remotesubmit" })
public class RemoteSubmit {

    @Parameter(required = true, allowNull = false, defaultPrefix = BindingConstants.LITERAL)
    private String             selector;

    @Inject
    private ComponentResources resources;

    @BeginRender
    void before(MarkupWriter writer) {
        writer.element("button", "type", "submit", "data-container-type", "remote-submit", "data-selector",
                       this.selector);
    }

    @AfterRender
    void after(MarkupWriter writer) {

        this.resources.renderInformalParameters(writer);

        writer.end();
    }

}
