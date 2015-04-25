package com.github.fscheffer.arras.components;

import javax.inject.Inject;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.corelib.components.Select;

import com.github.fscheffer.arras.ArrasConstants;
import com.github.fscheffer.arras.ArrasUtils;

@Import(module = "arras/select2", stylesheet = ArrasConstants.SELECT2_CSS_PATH_VALUE)
@SupportsInformalParameters
public class Select2 extends Select {

    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    private String             placeholder;

    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    private Integer            minimumInputLength;

    @Inject
    private ComponentResources resources;

    @AfterRender
    void afterRender2(MarkupWriter writer) {

        writer.attributes("data-component-type", "select2");

        ArrasUtils.addNonNullOption(writer, "placeholder", this.placeholder);
        ArrasUtils.addNonNullOption(writer, "minimum-input-length", this.minimumInputLength);

        this.resources.renderInformalParameters(writer);
    }
}
