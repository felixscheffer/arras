package org.github.fscheffer.arras;

import java.util.List;

import org.apache.tapestry5.beaneditor.PropertyModel;
import org.apache.tapestry5.grid.GridDataSource;

public interface FilteringDataSource extends GridDataSource {

    /**
     * @param search        The search term entered by the user
     * @param properties    A list of properties which should (or can) be used for filtering
     */
    void updateFilter(String search, List<PropertyModel> properties);

    /**
     * @return the total amount of rows without filtering
     */
    int getTotalRows();
}
