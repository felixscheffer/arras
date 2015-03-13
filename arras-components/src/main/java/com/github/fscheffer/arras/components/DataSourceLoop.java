package com.github.fscheffer.arras.components;

import java.util.Collections;
import java.util.List;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.Block;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.BeginRender;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.grid.SortConstraint;

public class DataSourceLoop {

    @Parameter(required = true, allowNull = false)
    private GridDataSource       source;

    @Parameter(required = true)
    private Object               value;

    @Parameter
    private int                  currentPage;

    // inclusive
    @Parameter(required = true)
    private int                  rowsPerPage;

    @Parameter
    private List<SortConstraint> sorting;

    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    private Block                empty;

    private int                  index, endIndex;

    @SetupRender
    public Object setupRender() {

        this.index = this.rowsPerPage * this.currentPage;
        this.endIndex = Math.min(this.rowsPerPage * (this.currentPage + 1), this.source.getAvailableRows()) - 1;

        this.source.prepare(this.index, this.endIndex, this.sorting != null ? this.sorting : emptySortConstraints());

        return this.index <= this.endIndex;
    }

    private List<SortConstraint> emptySortConstraints() {
        return Collections.emptyList();
    }

    @BeginRender
    public void begin(MarkupWriter writer) {

        this.value = this.source.getRowValue(this.index);

        this.index++;
    }

    @AfterRender
    public boolean after(MarkupWriter writer) {
        // false for repeating
        return this.index > this.endIndex;
    }

    Block cleanupRender() {
        return this.endIndex <= -1 ? this.empty : null;
    }
}
