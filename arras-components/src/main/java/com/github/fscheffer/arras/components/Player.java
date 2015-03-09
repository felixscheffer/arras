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

import java.util.Map;

import javax.inject.Inject;

import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.BeginRender;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.dom.Element;
import org.apache.tapestry5.ioc.internal.util.InternalUtils;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

import com.github.fscheffer.arras.ArrasConstants;
import com.github.fscheffer.arras.PlayerSource;

@SupportsInformalParameters
@Import(module = "arras/player", stylesheet = ArrasConstants.PLAYER_CSS_PATH_VALUE)
public class Player implements ClientElement {

    @Parameter(required = true, allowNull = false, autoconnect = true)
    private PlayerSource       source;

    @Parameter(value = "true")
    private boolean            video;

    @Parameter
    private String             poster;

    @Parameter(value = "true")
    private boolean            controls;

    @Parameter
    private boolean            autoplay;

    @Parameter(value = "literal:auto")
    private String             preload;

    @Parameter
    private boolean            repeat;

    @Parameter
    private String             width;

    @Inject
    private ComponentResources resources;

    @Inject
    private JavaScriptSupport  support;

    private Element            tag;

    private String             uniqueId;

    @BeginRender
    void begin(MarkupWriter writer) {

        this.tag = writer.element("div", "data-component-type", "player");

        writer.element(this.video ? "video" : "audio");

        writer.attributes("class", "video-js vjs-default-skin", "data-setup", "{}");

        if (InternalUtils.isNonBlank(this.poster)) {
            writer.attributes("poster", this.poster);
        }

        // behave like a div and resize when the grid changes its size (aka responsive)
        writer.attributes("width", InternalUtils.isBlank(this.width) ? "100%" : this.width);

        addOption(writer, this.controls, "controls");
        addOption(writer, this.autoplay, "autoplay");
        addOption(writer, this.repeat, "loop");

        writer.attributes("preload", this.preload);

        for (Map.Entry<String, String> source : this.source.get().entrySet()) {
            writer.element("source", "type", source.getKey(), "src", source.getValue());
            writer.end();
        }
    }

    private void addOption(MarkupWriter writer, boolean condition, String value) {

        if (condition) {
            writer.attributes(value, value);
        }
    }

    @AfterRender
    void after(MarkupWriter writer) {

        this.resources.renderInformalParameters(writer);

        writer.end(); // video or audio
        writer.end(); // data-component-type=player
    }

    @Override
    public String getClientId() {

        if (this.uniqueId == null) {
            this.uniqueId = this.support.allocateClientId(this.resources);
            this.tag.forceAttributes("id", this.uniqueId);
        }

        return this.uniqueId;
    }
}