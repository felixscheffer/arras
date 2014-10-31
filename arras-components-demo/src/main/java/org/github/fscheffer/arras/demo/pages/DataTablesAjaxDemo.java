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

package org.github.fscheffer.arras.demo.pages;

import javax.inject.Inject;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.services.TypeCoercer;
import org.github.fscheffer.arras.CollectionFilteringDataSource;
import org.github.fscheffer.arras.FilteringDataSource;
import org.github.fscheffer.arras.test.Track;
import org.github.fscheffer.arras.test.services.MusicLibrary;

public class DataTablesAjaxDemo {

    @Inject
    private MusicLibrary library;

    @Inject
    private TypeCoercer  coercer;

    @Property
    private Track        track;

    public FilteringDataSource getTracks() {
        return new CollectionFilteringDataSource<Track>(this.library.getTracks(), this.coercer);
    }
}
