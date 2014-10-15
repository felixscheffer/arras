package org.github.fscheffer.arras.components;

import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.BeginRender;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.dom.Element;

public class DropdownItem {

    @Parameter
    private boolean divider;

    @BeginRender
    boolean begin(MarkupWriter writer) {

        Element li = writer.element("li", "role", "presentation");

        if (this.divider) {
            li.forceAttributes("class", "divider");
        }

        return !this.divider;
    }

    @AfterRender
    void after(MarkupWriter writer) {
        writer.end();
    }
}
