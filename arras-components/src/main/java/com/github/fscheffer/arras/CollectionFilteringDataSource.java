// Copyright 2008, 2010 The Apache Software Foundation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

// Modified version of /tapestry-core/src/main/java/org/apache/tapestry5/internal/grid/CollectionGridDataSource.java

package com.github.fscheffer.arras;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.tapestry5.PropertyConduit;
import org.apache.tapestry5.beaneditor.PropertyModel;
import org.apache.tapestry5.grid.ColumnSort;
import org.apache.tapestry5.grid.SortConstraint;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import org.apache.tapestry5.ioc.internal.util.InternalUtils;
import org.apache.tapestry5.ioc.services.TypeCoercer;

public class CollectionFilteringDataSource<T> implements FilteringDataSource {

    private final List<T>       list;

    private List<Object>        preparedResults;

    private int                 startIndex;

    private List<PropertyModel> properties;

    private TypeCoercer         typeCoercer;

    private String[]            terms;

    public CollectionFilteringDataSource(Collection<T> collection, TypeCoercer typeCoercer) {
        this.typeCoercer = typeCoercer;
        this.list = CollectionFactory.newList(collection);
        this.preparedResults = CollectionFactory.newList(collection);
        this.terms = new String[] {};
    }

    /**
     * Returns the type of the first element in the list, or null if the list is empty.
     */
    @Override
    public Class getRowType() {
        return this.list.isEmpty() ? null : this.list.get(0).getClass();
    }

    @Override
    public int getTotalRows() {
        return this.list.size();
    }

    @Override
    public void updateFilter(String search, List<PropertyModel> properties) {
        this.properties = properties;
        this.terms = InternalUtils.isNonBlank(search) ? search.split(" ") : new String[] {};

        // case insensitve search
        for (int i = 0; i < this.terms.length; i++) {
            this.terms[i] = this.terms[i].toLowerCase();
        }
    }

    @Override
    public int getAvailableRows() {

        if (this.terms.length == 0) {
            return this.list.size();
        }

        int n = 0;
        for (Object obj : this.list) {

            if (contains(obj, this.terms)) {
                n++;
            }
        }
        return n;
    }

    @Override
    public void prepare(int startIndex, int endIndex, List<SortConstraint> sortConstraints) {

        for (SortConstraint constraint : sortConstraints) {

            final ColumnSort sort = constraint.getColumnSort();

            if (sort == ColumnSort.UNSORTED) {
                continue;
            }

            final PropertyConduit conduit = constraint.getPropertyModel().getConduit();

            final Comparator valueComparator = new Comparator<Comparable>() {

                @Override
                public int compare(Comparable o1, Comparable o2) {
                    // Simplify comparison, and handle case where both are nulls.

                    if (o1 == o2) {
                        return 0;
                    }

                    if (o2 == null) {
                        return 1;
                    }

                    if (o1 == null) {
                        return -1;
                    }

                    return o1.compareTo(o2);
                }
            };

            final Comparator rowComparator = new Comparator() {

                @Override
                public int compare(Object row1, Object row2) {
                    Comparable value1 = (Comparable) conduit.get(row1);
                    Comparable value2 = (Comparable) conduit.get(row2);

                    return valueComparator.compare(value1, value2);
                }
            };

            final Comparator reverseComparator = new Comparator() {

                @Override
                public int compare(Object o1, Object o2) {
                    int modifier = sort == ColumnSort.ASCENDING ? 1 : -1;

                    return modifier * rowComparator.compare(o1, o2);
                }
            };

            // We can freely sort this list because its just a copy.

            Collections.sort(this.list, reverseComparator);
        }

        if (this.terms.length > 0) {

            this.preparedResults.clear();
            this.startIndex = startIndex;

            int matches = 0;

            for (Object instance : this.list) {

                if (contains(instance, this.terms)) {

                    if (matches >= startIndex) {
                        this.preparedResults.add(instance);
                    }

                    if (matches == endIndex) {
                        break;
                    }

                    matches++;
                }
            }
        }
        else {
            this.startIndex = 0;
        }

    }

    @Override
    public Object getRowValue(int index) {

        List<?> list = this.terms.length == 0 ? this.list : this.preparedResults;
        return list.get(index - this.startIndex);
    }

    protected boolean contains(Object instance, String[] terms) {

        for (PropertyModel property : this.properties) {

            Object rawValue = property.getConduit().get(instance);

            String value = this.typeCoercer.coerce(rawValue, String.class);

            if (InternalUtils.isBlank(value)) {
                continue;
            }

            value = value.toLowerCase();

            for (String term : terms) {
                if (value.contains(term)) {
                    return true;
                }
            }
        }

        return false;
    }
}
