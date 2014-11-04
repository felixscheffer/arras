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

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.BeginRender;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.ioc.internal.util.InternalUtils;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

import com.github.fscheffer.arras.ArrasUtils;
import com.github.fscheffer.arras.base.AbstractLightbox;

public class LightboxBody extends AbstractLightbox implements ClientElement {

    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    private boolean            open;

    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    private String             zone;

    @Inject
    private JavaScriptSupport  support;

    @Inject
    private ComponentResources resources;

    private String             id;

    @BeginRender
    void begin(MarkupWriter writer) {

        this.id = this.support.allocateClientId(this.resources);

        writer.element("div", "style", "display: none;");
        writer.element("div", "id", this.id, "data-container-type", "lightbox-content");

        if (this.open || InternalUtils.isNonBlank(this.zone)) {

            ArrasUtils.addOption(writer, "open", this.open);
            ArrasUtils.addOption(writer, "zone", this.zone);

            super.addOptions(writer);
        }
    }

    @AfterRender
    void after(MarkupWriter writer) {
        writer.end();
        writer.end();
    }

    @Override
    public String getClientId() {
        return this.id;
    }

}
