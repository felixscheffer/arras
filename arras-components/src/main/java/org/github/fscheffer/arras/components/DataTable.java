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

import java.io.IOException;
import java.util.List;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.Block;
import org.apache.tapestry5.ComponentParameterConstants;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.PropertyConduit;
import org.apache.tapestry5.PropertyOverrides;
import org.apache.tapestry5.Translator;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.BeginRender;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.RequestParameter;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.beaneditor.PropertyModel;
import org.apache.tapestry5.dom.Element;
import org.apache.tapestry5.grid.ColumnSort;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.grid.GridSortModel;
import org.apache.tapestry5.internal.services.AjaxPartialResponseRenderer;
import org.apache.tapestry5.internal.services.PageRenderQueue;
import org.apache.tapestry5.internal.services.ajax.AjaxFormUpdateController;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import org.apache.tapestry5.ioc.internal.util.InternalUtils;
import org.apache.tapestry5.ioc.services.TypeCoercer;
import org.apache.tapestry5.json.JSONArray;
import org.apache.tapestry5.json.JSONLiteral;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.runtime.RenderCommand;
import org.apache.tapestry5.services.Environment;
import org.apache.tapestry5.services.PartialMarkupRenderer;
import org.apache.tapestry5.services.PartialMarkupRendererFilter;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.TranslatorSource;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import org.github.fscheffer.arras.ArrasUtils;
import org.github.fscheffer.arras.FilteringDataSource;
import org.github.fscheffer.arras.base.AbstractTable;
import org.github.fscheffer.arras.base.GridColumns;

public class DataTable extends AbstractTable {

    /**
     * Triggered by the DataTable component to give a chance to the developer to filter data on server-side when the
     * data source is not a {@link FilteringDataSource}
     */
    public static final String FILTER_DATA    = "filterData";

    /**
     * Triggered by the javascript component using ajax to indicate that the table's data need to be updated.
     */
    public static final String DATA           = "Data";

    public static final String SORT_DIR       = "sSortDir_";

    public static final String SORT_COL       = "iSortCol_";

    public static final String SORTING_COLS   = "iSortingCols";

    public static final String DISPLAY_START  = "iDisplayStart";

    public static final String DISPLAY_LENGTH = "iDisplayLength";

    public static final String SEARCH         = "sSearch";

    public static final String ECHO           = "sEcho";

    private final class PartialMarkupRendererFilterImplementation implements PartialMarkupRendererFilter {

        private final GridDataSource    source;

        private final GridSortModel     sortModel;

        private final int               records;

        private final int               startIndex;

        private final String            sEcho;

        private final int               totalRecords;

        private final int               rowsPerPage;

        private final PropertyOverrides overrides;

        private final BeanModel         model;

        private PartialMarkupRendererFilterImplementation(GridDataSource source,
                                                          GridSortModel sortModel,
                                                          int startIndex,
                                                          String sEcho,
                                                          int rowsPerPage,
                                                          PropertyOverrides overrides,
                                                          BeanModel model) {
            this.source = source;
            this.sortModel = sortModel;
            this.overrides = overrides;
            this.model = model;
            this.sEcho = sEcho;
            this.startIndex = startIndex;
            this.rowsPerPage = rowsPerPage;
            this.records = source.getAvailableRows();
            this.totalRecords = determineTotalRecords(source);
        }

        private int determineTotalRecords(GridDataSource source) {
            return source instanceof FilteringDataSource ? ((FilteringDataSource) source).getTotalRows()
                                                        : source.getAvailableRows();
        }

        @Override
        public void renderMarkup(MarkupWriter writer, JSONObject reply, PartialMarkupRenderer renderer) {

            JSONArray rows = new JSONArray();

            reply.put("sEcho", this.sEcho);
            reply.put("iTotalDisplayRecords", this.records);
            reply.put("iTotalRecords", this.totalRecords);
            reply.put("aaData", rows);

            if (this.records == 0) {
                return;
            }

            int endIndex = this.startIndex + this.rowsPerPage - 1;
            if (endIndex > this.records - 1) {
                endIndex = this.records - 1;
            }

            this.source.prepare(this.startIndex, endIndex, this.sortModel.getSortConstraints());

            for (int index = this.startIndex; index <= endIndex; index++) {
                //JSONArray cell = new JSONArray();
                JSONObject cell = new JSONObject();

                rows.put(cell);

                Object obj = this.source.getRowValue(index);

                List<String> names = this.model.getPropertyNames();
                int rowIndex = index % this.rowsPerPage;
                int columnIndex = 0;

                for (String name : names) {

                    Block override = this.overrides.getOverrideBlock(name + "Cell");

                    /**
                     * Is the property overridden as a block
                     * */
                    if (override != null) {
                        /**
                         * Render the block from server-side !
                         * */

                        // Create an element to contain the content for the zone. We give it a mnemonic
                        // element name and attribute just to help with debugging (the element itself is discarded).

                        Element zoneContainer = writer.element("ajax-partial");

                        DataTable.this.ajaxFormUpdateController.setupBeforePartialZoneRender(writer);

                        // Make sure the zone's actual rendering command is processed first, then the inline
                        // RenderCommand just above.

                        setRow(obj);

                        RenderCommand renderCommand = DataTable.this.typeCoercer.coerce(override, RenderCommand.class);
                        DataTable.this.pageRenderQueue.addPartialRenderer(renderCommand);
                        renderer.renderMarkup(writer, reply);

                        writer.end(); // the zoneContainer element

                        // Need to do this Ajax Form-related cleanup here, before we extract the zone content.

                        DataTable.this.ajaxFormUpdateController.cleanupAfterPartialZoneRender();

                        String zoneUpdateContent = zoneContainer.getChildMarkup();

                        /**
                         * Must check JSONArray's length because partialRenderer.renderPartialPageMarkup() is done twice (see AjaxComponentEventRequestHandler)!
                         * */
                        if (rows.length() > rowIndex) {
                            //rows.getJSONArray(rowIndex).put(columnIndex,zoneUpdateContent);
                            cell.put(name, zoneUpdateContent);
                        }
                        zoneContainer.remove();
                    }
                    else {

                        PropertyModel property = this.model.get(name);

                        PropertyConduit conduit = property.getConduit();

                        Object val = conduit.get(obj) != null ? conduit.get(obj) : "";

                        Class<? extends PropertyModel> propertyClass = property.getClass();

                        if (!String.class.equals(propertyClass) && !Number.class.isAssignableFrom(propertyClass)) {

                            Translator<Object> translator = DataTable.this.translatorSource.findByType(property.getPropertyType());
                            val = translator == null ? val.toString() : translator.toClient(val);
                        }
                        /**
                         * Render the value from server-side !
                         * */
                        cell.put(name, val);
                    }
                    columnIndex++;
                }
            }

            renderer.renderMarkup(writer, reply);
        }
    }

    /**
     * for ajax requests
     */
    @Parameter
    private Object                      context;

    /**
     * JSON options for the DataTable component
     */
    @Parameter(defaultPrefix = BindingConstants.PROP)
    private JSONObject                  options;

    @Component(parameters = { "index=inherit:columnIndex", "lean=inherit:lean", "overrides=overrides",
        "model=dataModel", "mode=true" })
    private GridColumns                 headers;

    @Component(parameters = { "index=inherit:columnIndex", "lean=inherit:lean", "overrides=overrides",
        "model=dataModel", "mode=false" })
    private GridColumns                 footers;

    /**
     * CSS class for the &lt;table&gt; element. In addition, informal parameters to the Grid are rendered in the table
     * element.
     */
    @Parameter(name = "class", defaultPrefix = BindingConstants.LITERAL, value = BindingConstants.SYMBOL
                                                                                 + ":"
                                                                                 + ComponentParameterConstants.GRID_TABLE_CSS_CLASS)
    @Property(write = false)
    private String                      tableClass;

    @Property
    private int                         columnIndex;

    @Parameter(value = "block:empty", defaultPrefix = BindingConstants.LITERAL)
    private Block                       empty;

    @Inject
    private ComponentResources          resources;

    @Inject
    private TypeCoercer                 typeCoercer;

    @Inject
    private TranslatorSource            translatorSource;

    @Environmental
    private JavaScriptSupport           support;

    @Inject
    private Request                     request;

    @Inject
    private Messages                    messages;

    @Inject
    private Environment                 environment;

    @Inject
    private PageRenderQueue             pageRenderQueue;

    @Inject
    private AjaxFormUpdateController    ajaxFormUpdateController;

    @Inject
    private AjaxPartialResponseRenderer partialRenderer;

    /**
     * Event method in order to get the data to display.
     * @throws IOException
     */
    @OnEvent(value = DATA)
    void onData(Object context, //
                @RequestParameter(value = DataTable.SORTING_COLS) String sortingCols,
                @RequestParameter(value = DataTable.SEARCH, allowBlank = true) String search,
                @RequestParameter(value = DataTable.ECHO) String sEcho, //
                @RequestParameter(value = DataTable.DISPLAY_START) int startIndex,
                @RequestParameter(value = DataTable.DISPLAY_LENGTH) int rowsPerPage) throws IOException {

        this.context = context;

        if (InternalUtils.isNonBlank(sortingCols)) {
            updateSortModel(sortingCols);
        }

        final GridDataSource source = getSource();
        BeanModel model = getDataModel();

        if (source instanceof FilteringDataSource) {
            ((FilteringDataSource) source).updateFilter(search, toFilterModel(model));
        }
        else {
            /**
             * Give a chance to the developer to update the GridDataSource to filter data server-side
             * */
            this.resources.triggerEvent(FILTER_DATA, new Object[] { search }, null);
        }

        GridSortModel sortModel = getSortModel();
        PropertyOverrides overrides = getOverrides();

        /**
         * Add a filter to initialize the data to be sent to the client
         * */
        this.pageRenderQueue.addPartialMarkupRendererFilter(new PartialMarkupRendererFilterImplementation(source,
                                                                                                          sortModel,
                                                                                                          startIndex,
                                                                                                          sEcho,
                                                                                                          rowsPerPage,
                                                                                                          overrides,
                                                                                                          model));

        /**
         * Even if it will be done once again in AjaxComponentEventRequestHandler, we must call
         * partialRenderer.renderPartialPageMarkup() here to "flush" the PartialMarkupRendererFilters that we've added
         * into the JSONArray!
         * It would be great if we could tell the partialRenderer that the job have already been done ...
         * */
        this.partialRenderer.renderPartialPageMarkup();
    }

    public void updateSortModel(String sortingCols) {

        int nbSortingCols = Integer.parseInt(sortingCols);
        if (nbSortingCols > 0) {

            String sord = this.request.getParameter(DataTable.SORT_DIR + "0");
            String sidx = this.request.getParameter(DataTable.SORT_COL + "0");

            List<String> names = getDataModel().getPropertyNames();

            int indexProperty = Integer.parseInt(sidx);

            String propName = names.get(indexProperty);

            GridSortModel sortModel = getSortModel();

            ColumnSort colSort = sortModel.getColumnSort(propName);

            if (!(InternalUtils.isNonBlank(colSort.name()) && colSort.name().startsWith(sord.toUpperCase()))) {
                getSortModel().updateSort(propName);
            }
        }

    }

    /**
     * This method will construct the JSON options and call the DataTable contructor
     */
    @AfterRender
    void setJS() {

        // NOTE: see datatables.js for more default values

        JSONObject setup = new JSONObject();

        setup.put("id", getClientId());
        // ensure compatibility with java's sort() which is case-sensitive
        // by default DataTable's sort method is case-INsensitive
        setup.put("bCaseSensitive", true);

        JSONObject dataTableParams = new JSONObject();

        if (isInPlace()) {
            dataTableParams.put("sAjaxSource", this.resources.createEventLink("data", this.context).toURI());
        }

        int rowsPerPage = getRowsPerPage();

        dataTableParams.put("iDisplayLength", rowsPerPage);
        dataTableParams.put("aLengthMenu", new JSONLiteral("[[" + rowsPerPage + "," + rowsPerPage * 2 + ","
                                                           + rowsPerPage * 4 + "," + rowsPerPage * 8 + "],["
                                                           + rowsPerPage + "," + rowsPerPage * 2 + "," + rowsPerPage
                                                           * 4 + "," + rowsPerPage * 8 + "]]"));

        //We set the bSortable parameters for each column. Cf : http://www.datatables.net/usage/columns
        //We set also the mDataProp parameters to handle ColReorder plugin. Cf : http://datatables.net/release-datatables/extras/ColReorder/server_side.html

        JSONArray sorting = new JSONArray();

        if (!isEmpty()) {

            int columnIndex = 0;

            JSONArray columnConfs = new JSONArray();
            for (String propertyName : getPropertyNames()) {

                boolean sortable = getModel().get(propertyName).isSortable();

                JSONObject confs = new JSONObject();
                confs.put("mDataProp", propertyName);
                confs.put("bSortable", sortable);
                confs.put("sClass", propertyName);

                columnConfs.put(confs);

                // find the first sortable column
                if (sorting.length() == 0 && sortable) {
                    sorting.put(new JSONArray(columnIndex, "asc"));
                }

                columnIndex++;
            }

            dataTableParams.put("aoColumns", columnConfs);
        }

        JSONObject language = new JSONObject();
        language.put("sProcessing", this.messages.get("datatable.sProcessing"));
        language.put("sLengthMenu", this.messages.get("datatable.sLengthMenu"));
        language.put("sZeroRecords", this.messages.get("datatable.sZeroRecords"));
        language.put("sEmptyTable", this.messages.get("datatable.sEmptyTable"));
        language.put("sLoadingRecords", this.messages.get("datatable.sLoadingRecords"));
        language.put("sInfo", this.messages.get("datatable.sInfo"));
        language.put("sInfoEmpty", this.messages.get("datatable.sInfoEmpty"));
        language.put("sInfoFiltered", this.messages.get("datatable.sInfoFiltered"));
        language.put("sInfoPostFix", this.messages.get("datatable.sInfoPostFix"));
        language.put("sSearch", this.messages.get("datatable.sSearch"));
        language.put("sUrl", this.messages.get("datatable.sUrl"));

        //JSONObject classes = new JSONObject();
        //        classes.put("sSortable", "ui-state-default sorting");
        //        classes.put("sSortAsc", "ui-state-default sorting");
        //        classes.put("sSortDesc", "ui-state-default sorting");
        //dataTableParams.put("oClasses", classes);

        JSONObject paginate = new JSONObject();
        paginate.put("sFirst", this.messages.get("datatable.sFirst"));
        paginate.put("sPrevious", this.messages.get("datatable.sPrevious"));
        paginate.put("sNext", this.messages.get("datatable.sNext"));
        paginate.put("sLast", this.messages.get("datatable.sLast"));

        language.put("oPaginate", paginate);

        dataTableParams.put("oLanguage", language);
        dataTableParams.put("aaSorting", sorting);

        ArrasUtils.merge(dataTableParams, this.options);
        setup.put("params", dataTableParams);

        this.support.require("arras/datatables:init").with(setup);
    }

    @SetupRender
    public Object setupRender() {

        return isEmpty() ? this.empty : null;
    }

    @BeginRender
    public Object beginRender(MarkupWriter writer) {
        // Skip rendering of component (template, body, etc.) when there's nothing to display.
        // The empty placeholder will already have rendered.

        return isEmpty() ? false : null;
    }

    private boolean isEmpty() {
        return getSource().getAvailableRows() == 0;
    }

    private List<PropertyModel> toFilterModel(BeanModel<?> model) {

        List<PropertyModel> list = CollectionFactory.newList();

        for (String name : model.getPropertyNames()) {

            PropertyModel property = model.get(name);
            if (property.getConduit() != null) {
                // add only properties to the list which we can access
                list.add(property);
            }
        }

        return list;
    }
}
