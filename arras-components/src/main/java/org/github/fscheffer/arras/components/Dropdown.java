package org.github.fscheffer.arras.components;

import javax.inject.Inject;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.Block;
import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.PropertyOverrides;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.BeginRender;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.dom.Element;
import org.apache.tapestry5.runtime.RenderCommand;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import org.github.fscheffer.arras.ArrasUtils;

@Import(module = "dropdown")
@SupportsInformalParameters
public class Dropdown implements ClientElement {

    @Parameter(defaultPrefix = BindingConstants.LITERAL, value = "div")
    private String             element;

    @Parameter(defaultPrefix = BindingConstants.LITERAL, value = "caret")
    @Property(write = false)
    private String             icon;

    @Parameter(value = "this", allowNull = false)
    private PropertyOverrides  overrides;

    @Inject
    private ComponentResources resources;

    @Inject
    private JavaScriptSupport  support;

    /**
     * A renderable (usually a {@link Block}) that can render the label for a
     * dropdown.
     */
    @Parameter(value = "block:defaultLabel")
    @Property(write = false)
    private RenderCommand      label;

    private String             uniqueId;

    @Property(write = false)
    private String             anchorId;

    private Element            root;

    @BeginRender
    void begin(MarkupWriter writer) {

        this.anchorId = this.support.allocateClientId("drop");

        // <div class="dropdown">
        this.root = writer.element(this.element, "class", "dropdown");

        this.resources.renderInformalParameters(writer);
    }

    @AfterRender
    void after(MarkupWriter writer) {

        writer.end(); // root;
    }

    @Override
    public String getClientId() {

        if (this.uniqueId == null) {
            this.uniqueId = this.support.allocateClientId(this.resources);
            this.root.forceAttributes("id", this.uniqueId);
        }

        return this.uniqueId;
    }

    public String getName() {
        return ArrasUtils.getPresentableComponentName(this.overrides.getOverrideMessages(), this.resources);
    }
}
