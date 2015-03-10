package com.github.fscheffer.arras.components;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.corelib.components.Select;

import com.github.fscheffer.arras.ArrasConstants;

@Import(module = "arras/select2", stylesheet = ArrasConstants.SELECT2_CSS_PATH_VALUE)
public class Select2 extends Select {

    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    private String placeholder;

    @AfterRender
    void afterRender2(MarkupWriter writer) {
        writer.attributes("data-component-type", "select2");

        if (this.placeholder != null) {
            writer.attributes("data-placeholder", this.placeholder);
        }
    }
}
