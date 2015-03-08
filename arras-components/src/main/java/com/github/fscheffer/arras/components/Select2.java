package com.github.fscheffer.arras.components;

import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.corelib.components.Select;

@Import(module = "arras/select2", stylesheet = "select2.css")
public class Select2 extends Select {

    @AfterRender
    void afterRender2(MarkupWriter writer) {
        writer.attributes("data-component-type", "select2");
    }
}
