// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.github.fscheffer.arras.components;

import javax.inject.Inject;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.BeginRender;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Path;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.dom.Element;
import org.apache.tapestry5.ioc.internal.util.InternalUtils;
import org.apache.tapestry5.json.JSONArray;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import org.slf4j.Logger;

import com.github.fscheffer.arras.ArrasConstants;
import com.github.fscheffer.arras.ArrasUtils;

// TODO: replaced with MediumEditor with Medium.js because it seems to be more sophisticated and easier to extend
//       also supports AMD out of the box
//       http://jakiestfu.github.io/Medium.js/docs/
@Import(module = "arras/medium-editor", stylesheet = { "medium-editor/medium-editor.css",
                                                      "medium-editor/themes/default.css" })
@SupportsInformalParameters
public class MediumEditor implements ClientElement {

    @Parameter(autoconnect = true)
    private String             value;

    // default value is "bold,italic,underline,anchor,header1,header2,quote"
    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    private String             buttons;

    @Parameter
    private boolean            disabled;

    @Parameter(value = "true")
    private boolean            toolbar;

    @Parameter(value = "true")
    private boolean            fontawesome;

    @Inject
    private JavaScriptSupport  support;

    @Inject
    @Path("font-awesome/css/font-awesome.css")
    private Asset              fontAwesomeCss;

    @Inject
    private ComponentResources resources;

    @Inject
    private Logger             logger;

    private String             uniqueId;

    private Element            div;

    @BeginRender
    void begin(MarkupWriter writer) {

        this.div = writer.element("div", "data-container-type", "medium-editor", "data-context",
                                  this.resources.getCompleteId());

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

        if (InternalUtils.isNonBlank(this.value)) {
            writer.writeRaw(this.value);
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

        writer.end(); // div[medium-editor]
    }

    @OnEvent(ArrasConstants.UPDATE_CONTENT)
    boolean onContentSubmit(String newValue) {

        this.logger.debug("Saving {}", newValue);

        this.value = newValue;

        return true;
    }

    @Override
    public String getClientId() {
        if (this.uniqueId == null) {
            this.uniqueId = this.support.allocateClientId(this.resources);
            this.div.forceAttributes("id", this.uniqueId);
        }

        return this.uniqueId;
    }

}