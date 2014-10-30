package org.github.fscheffer.arras.mixins;

import javax.inject.Inject;

import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.annotations.BeginRender;
import org.apache.tapestry5.annotations.MixinAfter;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import org.github.fscheffer.arras.ArrasUtils;

@MixinAfter
public class Sortable {

    @Parameter
    private String            handle;

    @Parameter
    private String            cancel;

    @Parameter
    private String            placeholder;

    @Inject
    @Symbol(SymbolConstants.JAVASCRIPT_INFRASTRUCTURE_PROVIDER)
    String                    provider;

    @Inject
    private JavaScriptSupport support;

    @BeginRender
    void begin(MarkupWriter writer) {

        writer.attributes("data-sortable", "sortable");

        ArrasUtils.addOption(writer, "handle", this.handle);
        ArrasUtils.addOption(writer, "cancel", this.cancel);
        ArrasUtils.addOption(writer, "placeholder", this.placeholder);

        if ("jquery".equals(this.provider)) {
            this.support.require("arras/sortable-jquery");
        }

        // TODO: prototype
    }

}
