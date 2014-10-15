// Copyright 2014 The Apache Software Foundation
//
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

import javax.inject.Inject;

import org.apache.tapestry5.Block;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.runtime.RenderCommand;
import org.github.fscheffer.arras.ArrasUtils;
import org.github.fscheffer.arras.TabGroupContext;

@Import(module = "dropdown")
public class TabDropdown {

    @Environmental
    private TabGroupContext    context;

    @Inject
    private ComponentResources resources;

    /**
     * A renderable (usually a {@link Block}) that can render the label for a
     * tab.
     */
    @Parameter(value = "block:defaultLabel")
    @Property(write = false)
    private RenderCommand      label;

    public String getName() {
        return ArrasUtils.getPresentableComponentName(this.context.getMessages(), this.resources);
    }
}
