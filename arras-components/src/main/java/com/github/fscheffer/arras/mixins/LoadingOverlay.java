package com.github.fscheffer.arras.mixins;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.BeginRender;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.MixinAfter;
import org.apache.tapestry5.annotations.Parameter;

@Import(module = "arras/loading-overlay", stylesheet = { "arras.css", "font-awesome/css/font-awesome.css" })
@MixinAfter
public class LoadingOverlay {

    @Parameter(defaultPrefix = BindingConstants.LITERAL, value = "fa fa-spinner fa-spin")
    private String icon;

    @BeginRender
    void begin(MarkupWriter writer) {

        writer.attributes("data-icon", this.icon);
        writer.attributes("class", "loading-zone");
    }
}
