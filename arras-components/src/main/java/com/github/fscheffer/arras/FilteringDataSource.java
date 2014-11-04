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

package com.github.fscheffer.arras;

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
