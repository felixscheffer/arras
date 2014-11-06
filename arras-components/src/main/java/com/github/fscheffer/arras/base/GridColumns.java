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

package com.github.fscheffer.arras.base;

import java.util.List;

import org.apache.tapestry5.Block;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.PropertyOverrides;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.beaneditor.PropertyModel;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

@SupportsInformalParameters
public class GridColumns {

    /**
     * The object that provides access to bean and data models, which is typically the enclosing Grid component.
     */
    @Parameter
    private BeanModel          model;

    /**
     * If true, then the CSS class on each &lt;TH&gt; element will be omitted, which can reduce the amount of output
     * from the component overall by a considerable amount. Leave this as false, the default, when you are leveraging
     * the CSS to customize the look and feel of particular columns.
     */
    @Parameter
    private boolean            lean;

    /**
     * Where to look for informal parameter Blocks used to override column headers.  The default is to look for such
     * overrides in the GridColumns component itself, but this is usually overridden.
     */
    @Parameter("this")
    private PropertyOverrides  overrides;

    /**
     * If not null, then each link is output as a link to update the specified zone.
     */
    @Parameter
    private String             zone;

    @Parameter
    private Boolean            mode;

    @Inject
    private Messages           messages;

    @Inject
    private Block              standardHeader;

    @Inject
    private Block              standardFooter;

    /**
     * Optional output parameter that stores the current column index.
     */
    @Parameter(cache = false)
    @Property
    private int                index;

    /**
     * Caches the index of the last column.
     */
    private int                lastColumnIndex;

    @Property(write = false)
    private PropertyModel      columnModel;

    @Inject
    private ComponentResources resources;

    void setupRender() {
        this.lastColumnIndex = this.model.getPropertyNames().size() - 1;
    }

    public Object getColumnContext() {
        if (this.zone == null) {
            return this.columnModel.getId();
        }

        return new Object[] { this.columnModel.getId(), this.zone };
    }

    public List<String> getColumnNames() {
        return this.model.getPropertyNames();
    }

    public void setColumnName(String columnName) {
        this.columnModel = this.model.get(columnName);
    }

    public Block getBlockForColumn() {
        Block override = this.overrides.getOverrideBlock(this.columnModel.getId() + (this.mode ? "Header" : "Footer"));

        if (override != null) {
            return override;
        }

        return this.mode ? this.standardHeader : this.standardFooter;
    }

    public Boolean getMode() {
        return this.mode;
    }
}
