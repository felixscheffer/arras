package org.github.fscheffer.arras.components;

import javax.inject.Inject;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.ioc.internal.util.InternalUtils;
import org.github.fscheffer.arras.IconEffect;

@SupportsInformalParameters
@Import(stylesheet = "font-awesome/css/font-awesome.css")
public class Icon {

    // for a complete list: http://fortawesome.github.io/Font-Awesome/icons/
    @Parameter(required = true, allowNull = false, defaultPrefix = BindingConstants.LITERAL)
    private String             icon;

    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    private String             size;

    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    private IconEffect         effect;

    @Inject
    private ComponentResources resources;

    @SetupRender
    void render(MarkupWriter writer) {

        writer.element("i", "class", "fa fa-" + this.icon);

        if (InternalUtils.isNonBlank(this.size)) {
            writer.attributes("class", "fa-" + this.size);
        }

        if (this.effect != null) {
            writer.attributes("class", "fa-" + this.effect.value);
        }

        this.resources.renderInformalParameters(writer);

        writer.end();
    }
}
