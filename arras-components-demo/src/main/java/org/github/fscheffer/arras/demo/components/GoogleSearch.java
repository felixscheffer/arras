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

package org.github.fscheffer.arras.demo.components;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.BeginRender;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.ioc.internal.util.InternalUtils;
import org.github.fscheffer.arras.demo.Track;

public class GoogleSearch {

    @Parameter(required = true)
    private Track track;

    @BeginRender
    void begin(MarkupWriter writer) {

        String url = buildUrl();

        writer.element("a", "href", url, "target", "_blank");
        writer.write("Search");
        writer.end();
    }

    private String buildUrl() {

        String artist = this.track.getArtist();
        String title = this.track.getTitle();

        StringBuilder sb = new StringBuilder();
        sb.append("https://www.google.com/search?q=");

        try {
            if (InternalUtils.isNonBlank(artist)) {
                sb.append(URLEncoder.encode(artist, "UTF-8"));
                sb.append(URLEncoder.encode(" ", "UTF-8"));
            }

            if (InternalUtils.isNonBlank(title)) {
                sb.append(URLEncoder.encode(title, "UTF-8"));
            }
        }
        catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        return sb.toString();
    }
}
