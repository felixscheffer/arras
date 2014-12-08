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

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.tapestry5.Binding;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.Block;
import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.PropertyConduit;
import org.apache.tapestry5.PropertyOverrides;
import org.apache.tapestry5.Translator;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.beaneditor.PropertyModel;
import org.apache.tapestry5.corelib.data.GridPagerPosition;
import org.apache.tapestry5.grid.ColumnSort;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.grid.GridSortModel;
import org.apache.tapestry5.grid.SortConstraint;
import org.apache.tapestry5.internal.TapestryInternalUtils;
import org.apache.tapestry5.internal.beaneditor.BeanModelUtils;
import org.apache.tapestry5.internal.bindings.AbstractBinding;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import org.apache.tapestry5.ioc.internal.util.InternalUtils;
import org.apache.tapestry5.ioc.services.TypeCoercer;
import org.apache.tapestry5.services.BeanModelSource;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.TranslatorSource;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

import com.github.fscheffer.arras.FilteringDataSource;

@SupportsInformalParameters
public class AbstractTable implements ClientElement {

    /**
     * The source of data for the Grid to display. This will usually be a List
     * or array but can also be an explicit {@link GridDataSource}. For Lists
     * and object arrays, a GridDataSource is created automatically as a wrapper
     * around the underlying List.
     */
    @Parameter(required = true, autoconnect = true)
    private FilteringDataSource source;

    /**
     * Defines where the pager (used to navigate within the "pages" of results)
     * should be displayed: "top", "bottom", "both" or "none".
     */
    @Parameter(value = "top", defaultPrefix = BindingConstants.LITERAL)
    private GridPagerPosition   pagerPosition;

    /**
     * The model used to identify the properties to be presented and the order
     * of presentation. The model may be omitted, in which case a default model
     * is generated from the first object in the data source (this implies that
     * the objects provided by the source are uniform). The model may be
     * explicitly specified to override the default behavior, say to reorder or
     * rename columns or add additional columns. The add, include, exclude and
     * reorder parameters are <em>only</em> applied to a default model, not an
     * explicitly provided one.
     */
    @Parameter
    private BeanModel<?>        model;

    /**
     * The number of rows of data displayed on each page. If there are more rows
     * than will fit, the Grid will divide up the rows into "pages" and
     * (normally) provide a pager to allow the user to navigate within the
     * overall result set.
     */
    @Parameter(value = "25")
    private int                 rowsPerPage;

    /**
     * The model used to handle sorting of the Grid. This is generally not
     * specified, and the built-in model supports only single column sorting.
     * The sort constraints (the column that is sorted, and ascending vs.
     * descending) is stored as persistent fields of the Grid component.
     */
    @Parameter
    private GridSortModel       sortModel;

    /**
     * A comma-seperated list of property names to be added to the
     * {@link org.apache.tapestry5.beaneditor.BeanModel}. Cells for added
     * columns will be blank unless a cell override is provided. This parameter
     * is only used when a default model is created automatically.
     */
    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    private String              add;

    /**
     * Defines where block and label overrides are obtained from. By default,
     * the Grid component provides block overrides (from its block parameters).
     */
    @Parameter(value = "this", allowNull = false)
    private PropertyOverrides   overrides;

    /**
     * A comma-separated list of property names to be retained from the
     * {@link org.apache.tapestry5.beaneditor.BeanModel}. Only these properties
     * will be retained, and the properties will also be reordered. The names
     * are case-insensitive. This parameter is only used when a default model is
     * created automatically.
     */
    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    private String              include;

    /**
     * A comma-separated list of property names to be removed from the
     * {@link org.apache.tapestry5.beaneditor.BeanModel} . The names are
     * case-insensitive. This parameter is only used when a default model is
     * created automatically.
     */
    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    private String              exclude;

    /**
     * A comma-separated list of property names indicating the order in which
     * the properties should be presented. The names are case insensitive. Any
     * properties not indicated in the list will be appended to the end of the
     * display order. This parameter is only used when a default model is
     * created automatically.
     */
    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    private String              reorder;

    /**
     * The current value, set before the component renders its body.
     */
    @Parameter
    private Object              value;

    /**
     * If true, then the Grid will be wrapped in an element that acts like a
     * {@link org.apache.tapestry5.corelib.components.Zone}; all the paging and
     * sorting links will refresh the zone, repainting the entire grid within
     * it, but leaving the rest of the page (outside the zone) unchanged.
     */
    @Parameter
    private boolean             inPlace;

    /**
     * The model parameter after modification due to the add, include, exclude
     * and reorder parameters.
     */
    private BeanModel<?>        dataModel;

    @Inject
    private JavaScriptSupport   javaScriptSupport;

    @Inject
    private ComponentResources  resources;

    @Inject
    private BeanModelSource     modelSource;

    @Inject
    private TranslatorSource    translatorSource;

    @Persist
    private Boolean             sortAscending;

    @Persist
    private String              sortColumnId;

    private String              clientId;

    @Property
    private Integer             index;

    @Property
    private String              cellModel;

    @Inject
    private TypeCoercer         typeCoercer;

    @Inject
    private Request             request;

    @Override
    public String getClientId() {
        if (InternalUtils.isBlank(this.clientId)) {
            this.clientId = InternalUtils.isNonBlank(this.resources.getInformalParameter("id", String.class))
                ? this.resources.getInformalParameter("id",
                                                      String.class)
                                                      : this.javaScriptSupport.allocateClientId(this.resources);
        }
        return this.clientId;
    }

    public int getRowsPerPage() {
        return this.rowsPerPage;
    }

    public GridSortModel getSortModel() {
        return this.sortModel;
    }

    public boolean getSortAscending() {
        return this.sortAscending != null && this.sortAscending.booleanValue();
    }

    public void setSortAscending(boolean sortAscending) {
        this.sortAscending = sortAscending;
    }

    public BeanModel<?> getModel() {
        return this.model;
    }

    public BeanModel<?> getDataModel() {
        if (this.dataModel == null) {
            this.dataModel = getModel();

            BeanModelUtils.modify(this.dataModel, this.add, this.include, this.exclude, this.reorder);
        }

        return this.dataModel;
    }

    public FilteringDataSource getSource() {
        return this.source;
    }

    /**
     * Default implementation that only allows a single column to be the sort
     * column, and stores the sort information as persistent fields of the Grid
     * component.
     */
    class DefaultGridSortModel implements GridSortModel {

        @Override
        public ColumnSort getColumnSort(String columnId) {
            if (!TapestryInternalUtils.isEqual(columnId, AbstractTable.this.sortColumnId)) {
                return ColumnSort.UNSORTED;
            }

            return this.getColumnSort();
        }

        private ColumnSort getColumnSort() {
            return getSortAscending() ? ColumnSort.ASCENDING : ColumnSort.DESCENDING;
        }

        @Override
        public void updateSort(String columnId) {
            assert InternalUtils.isNonBlank(columnId);
            if (columnId.equals(AbstractTable.this.sortColumnId)) {
                setSortAscending(!getSortAscending());
                return;
            }

            AbstractTable.this.sortColumnId = columnId;
            setSortAscending(true);
        }

        @Override
        public List<SortConstraint> getSortConstraints() {
            if (AbstractTable.this.sortColumnId == null) {
                return Collections.emptyList();
            }

            PropertyModel sortModel = getDataModel().getById(AbstractTable.this.sortColumnId);

            SortConstraint constraint = new SortConstraint(sortModel, this.getColumnSort());

            return Collections.singletonList(constraint);
        }

        @Override
        public void clear() {
            AbstractTable.this.sortColumnId = null;
        }
    }

    GridSortModel defaultSortModel() {
        return new DefaultGridSortModel();
    }

    public PropertyOverrides getOverrides() {
        return this.overrides;
    }

    /**
     * Returns a {@link org.apache.tapestry5.Binding} instance that attempts to
     * identify the model from the source parameter (via
     * {@link org.apache.tapestry5.grid.GridDataSource#getRowType()}. Subclasses
     * may override to provide a different mechanism. The returning binding is
     * variant (not invariant).
     *
     * @see BeanModelSource#createDisplayModel(Class,
     *      org.apache.tapestry5.ioc.Messages)
     */
    protected Binding defaultModel() {
        return new AbstractBinding() {

            @Override
            public Object get() {
                // Get the default row type from the data source

                GridDataSource gridDataSource = (GridDataSource) AbstractTable.this.getSource();

                Class<?> rowType = gridDataSource.getRowType();

                if (rowType == null) {
                    throw new RuntimeException(
                                               String.format("Unable to determine the bean type for rows from %s. You should bind the model parameter explicitly.",
                                                             gridDataSource));
                }

                // Properties do not have to be read/write

                return AbstractTable.this.modelSource.createDisplayModel(rowType,
                                                                         AbstractTable.this.overrides.getOverrideMessages());
            }

            /**
             * Returns false. This may be overkill, but it basically exists
             * because the model is inherently mutable and therefore may contain
             * client-specific state and needs to be discarded at the end of the
             * request. If the model were immutable, then we could leave
             * invariant as true.
             */
            @Override
            public boolean isInvariant() {
                return false;
            }
        };
    }

    /**
     * A version of GridDataSource that caches the availableRows property. This
     * addresses TAPESTRY-2245.
     */
    static class CachingDataSource implements GridDataSource {

        private final GridDataSource delegate;

        private boolean              availableRowsCached;

        private int                  availableRows;

        CachingDataSource(GridDataSource delegate) {
            this.delegate = delegate;
        }

        @Override
        public int getAvailableRows() {
            if (!this.availableRowsCached) {
                this.availableRows = this.delegate.getAvailableRows();
                this.availableRowsCached = true;
            }

            return this.availableRows;
        }

        @Override
        public void prepare(int startIndex, int endIndex, List<SortConstraint> sortConstraints) {
            this.delegate.prepare(startIndex, endIndex, sortConstraints);
        }

        @Override
        public Object getRowValue(int index) {
            return this.delegate.getRowValue(index);
        }

        @Override
        public Class<?> getRowType() {
            return this.delegate.getRowType();
        }

    }

    @Parameter(required = true)
    //    @Property(write = false)
    private Object row;

    public Object getRow() {
        return this.row;
    }

    public void setRow(Object row) {
        this.row = row;
    }

    @Parameter
    private int    rowIndex;

    @Parameter(cache = false)
    private String rowClass;

    /**
     * In order to get the css of a specific row
     */
    public String getRowClass() {
        List<String> classes = CollectionFactory.newList();

        // Not a cached parameter, so careful to only access it once.

        String rc = this.rowClass;

        if (rc != null) {
            classes.add(rc);
        }

        return TapestryInternalUtils.toClassAttributeValue(classes);
    }

    /**
     * In order to get the value of a specific cell
     */
    public Object getCellValue() {

        this.rowIndex = this.index;

        Object obj = getSource().getRowValue(this.index);

        if (obj == null) { //rows can be null, as stated in getRowValue docs
            return "";
        }

        PropertyConduit conduit = getDataModel().get(this.cellModel).getConduit();

        Class<?> type = conduit.getPropertyType();

        Object val = conduit.get(obj);

        if (val == null) { //cells should be able to have null values
            return "";
        }

        PropertyModel m = getDataModel().get(this.cellModel);

        if (!String.class.equals(m.getClass()) && !Number.class.isAssignableFrom(m.getClass())) {

            Translator<Object> translator = this.translatorSource.findByType(m.getPropertyType());
            if (translator != null) {
                val = translator.toClient(val);
            }
            else {
                val = val.toString();
            }
        }

        return val;
    }

    /**
     * source of the seconf loop component, in order to loop on each cells
     */
    public List<String> getPropertyNames() {
        return (List<String>) getDataModel().getPropertyNames();
    }

    /**
     * Iterator for the look component in order to loop to each rows
     */
    public Iterable<Integer> getLoopSource() {

        // Issue #284 : call prepared() before calling getRowValue()
        int startIndex = 0;
        int endIndex = getSource().getAvailableRows() - 1;
        getSource().prepare(startIndex, endIndex, this.sortModel.getSortConstraints());

        return new Iterable<Integer>() {

            @Override
            public Iterator<Integer> iterator() {

                return new Iterator<Integer>() {

                    Integer i = new Integer(0);

                    @Override
                    public boolean hasNext() {

                        return this.i < AbstractTable.this.getSource().getAvailableRows();
                    }

                    @Override
                    public Integer next() {
                        AbstractTable.this.row = AbstractTable.this.getSource().getRowValue(this.i);
                        return this.i++;
                    }

                    @Override
                    public void remove() {
                        this.i = 0;
                    }
                };

            }
        };
    }

    @Inject
    private Block cell;

    public Block getCellBlock() {
        Block override = this.overrides.getOverrideBlock(getDataModel().get(this.cellModel).getPropertyName() + "Cell");
        if (override != null) {
            return override;
        }
        return this.cell;
    }

    public boolean isInPlace() {
        return this.inPlace;
    }
}
