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

package org.github.fscheffer.arras.demo.services;

import java.net.URL;
import java.util.List;
import java.util.Map;

import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.annotations.ImportModule;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import org.github.fscheffer.arras.demo.Track;
import org.github.fscheffer.arras.services.ArrasComponentsModule;
import org.slf4j.Logger;

@ImportModule(ArrasComponentsModule.class)
public class AppModule {

    public static void contributeApplicationDefaults(MappedConfiguration<String, Object> conf) {

        conf.add(SymbolConstants.JAVASCRIPT_INFRASTRUCTURE_PROVIDER, "jquery");
        // disable production mode to enable live-reloading
        conf.add(SymbolConstants.PRODUCTION_MODE, false);
    }

    // from tapestry5-core
    public MusicLibrary buildMusicLibrary(Logger log) {

        URL library = getClass().getResource("iTunes.xml");

        final List<Track> tracks = new MusicLibraryParser(log).parseTracks(library);

        final Map<Long, Track> idToTrack = CollectionFactory.newMap();

        for (Track t : tracks) {
            idToTrack.put(t.getId(), t);
        }

        return new MusicLibrary() {

            @Override
            public Track getById(long id) {
                Track result = idToTrack.get(id);

                if (result != null) {
                    return result;
                }

                throw new IllegalArgumentException(String.format("No track with id #%d.", id));
            }

            @Override
            public List<Track> getTracks() {
                return tracks;
            }

            @Override
            public List<Track> findByMatchingTitle(String title) {
                String titleLower = title.toLowerCase();

                List<Track> result = CollectionFactory.newList();

                for (Track t : tracks) {
                    if (t.getTitle().toLowerCase().contains(titleLower)) {
                        result.add(t);
                    }
                }

                return result;
            }
        };
    }
}
