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

package org.github.fscheffer.arras.components;

import java.util.List;

import javax.inject.Inject;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.PropertyOverrides;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.BeginRender;
import org.apache.tapestry5.annotations.CleanupRender;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.dom.Element;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.runtime.RenderCommand;
import org.apache.tapestry5.runtime.RenderQueue;
import org.apache.tapestry5.services.Environment;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import org.github.fscheffer.arras.TabGroupContext;

/**
 * The TabGroup component is a single content area with multiple panels, each
 * associated with a header. It servers as a container for {@link Tab} and
 * {@link TabDropdown} components.
 *
 * @tapestrydoc
 * @see Tab
 * @see TabDropdown
 * @see TabGroupContext
 */
@Import(module = "tabgroup")
public class TabGroup implements ClientElement {

    /**
     * Optional parameter to specify the id of the tab that should be shown. If
     * not set the first tab will be displayed by default.
     */
    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    private String               active;

    /**
     * Optional parameter to make tabs fade in and out
     */
    @Parameter
    private boolean              fade;

    @Parameter(value = "this", allowNull = false)
    private PropertyOverrides    overrides;

    @Inject
    private Environment          environment;

    @Inject
    private ComponentResources   resources;

    @Inject
    private JavaScriptSupport    support;

    private String               uniqueId;

    private Element              ul;

    private static RenderCommand RENDER_CLOSE_TAG = new RenderCommand() {

                                                      @Override
                                                      public void render(MarkupWriter writer, RenderQueue queue) {
                                                          writer.end();
                                                      }
                                                  };

    @BeginRender
    void begin(MarkupWriter writer) {
        Messages messages = this.overrides.getOverrideMessages();

        this.environment.push(TabGroupContext.class, new TabGroupContext(messages, this.active, this.fade));

        this.ul = writer.element("ul", "class", "nav nav-tabs", "role", "tablist");
    }

    @AfterRender
    RenderCommand after(MarkupWriter writer) {
        writer.end();

        final TabGroupContext context = this.environment.peek(TabGroupContext.class);

        return new RenderCommand() {

            @Override
            public void render(MarkupWriter writer, RenderQueue queue) {
                writer.element("div", "class", "tab-content");

                queue.push(TabGroup.RENDER_CLOSE_TAG);

                List<RenderCommand> contents = context.getContents();
                for (int i = contents.size() - 1; i >= 0; i--) {
                    queue.push(contents.get(i));
                }
            }
        };
    }

    @CleanupRender
    void cleanupRender() {
        this.environment.pop(TabGroupContext.class);
    }

    @Override
    public String getClientId() {
        if (this.uniqueId == null) {
            this.uniqueId = this.support.allocateClientId(this.resources);
            this.ul.forceAttributes("id", this.uniqueId);
        }

        return this.uniqueId;
    }

}
