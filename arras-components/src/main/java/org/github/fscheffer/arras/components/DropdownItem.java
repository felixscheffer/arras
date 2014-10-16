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
