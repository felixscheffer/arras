package org.github.fscheffer.arras.components;

import javax.inject.Inject;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Path;
import org.apache.tapestry5.corelib.base.AbstractTextField;
import org.apache.tapestry5.ioc.internal.util.InternalUtils;
import org.apache.tapestry5.json.JSONArray;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import org.github.fscheffer.arras.ArrasUtils;

@Import(module = "arras/medium-editor", stylesheet = { "medium-editor/medium-editor.css",
                                                      "medium-editor/themes/default.css" })
public class MediumEditor extends AbstractTextField {

    // default value is "bold,italic,underline,anchor,header1,header2,quote"
    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    private String            buttons;

    @Parameter(value = "true")
    private boolean           toolbar;

    @Parameter(value = "true")
    private boolean           fontawesome;

    @Inject
    private JavaScriptSupport support;

    @Inject
    @Path("font-awesome/css/font-awesome.css")
    private Asset             fontAwesomeCss;

    @Override
    protected void writeFieldTag(MarkupWriter writer, String value) {

        writer.element("div", "data-container-type", "medium-editor");

        writer.element("input", "type", "hidden", "name", getControlName());
        writer.end();

        writer.element("div", "name", getControlName(), "id", getClientId());

        ArrasUtils.addOption(writer, "disable-editing", this.disabled);
        ArrasUtils.addOption(writer, "buttons", splitIntoJsonArray(this.buttons));

        if (!this.toolbar || this.disabled) {
            ArrasUtils.addDataAttribute(writer, "disable-toolbar", true);
        }

        if (this.fontawesome) {
            this.support.importStylesheet(this.fontAwesomeCss);
        }
        else {
            ArrasUtils.addDataAttribute(writer, "fontawesome", false);
        }

        if (InternalUtils.isNonBlank(value)) {
            writer.writeRaw(value);
        }
    }

    private static String splitIntoJsonArray(String rawstring) {

        if (rawstring == null) {
            return null;
        }

        Object[] values = rawstring.split(",");

        String s = new JSONArray(values).toCompactString();
        return s.replace("\"", "'");
    }

    @AfterRender
    void after(MarkupWriter writer) {

        this.resources.renderInformalParameters(writer);

        writer.end(); // div
        writer.end(); // div[medium-editor]
    }
}