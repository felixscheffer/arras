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

package com.github.fscheffer.arras.demo.pages;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import javax.inject.Inject;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.StreamResponse;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Path;
import org.apache.tapestry5.ioc.Resource;
import org.apache.tapestry5.ioc.internal.util.InternalUtils;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.Response;
import org.apache.tapestry5.services.assets.ContentTypeAnalyzer;
import org.slf4j.Logger;

import com.github.fscheffer.arras.PlayerSource;
import com.github.fscheffer.arras.PlayerSourceImpl;

public class PlayerDemo {

    private static final String MIAOW_OGG = "miaowOgg";

    private static final String MIAOW_M4A = "miaowM4a";

    @Inject
    @Path("Miaow-07-Bubble.m4a")
    private Asset               miaowM4a;

    @Inject
    @Path("Miaow-07-Bubble.ogg")
    private Asset               miaowOgg;

    @Inject
    @Path("Big_Buck_Bunny_Trailer.m4v")
    private Asset               bigBuckBunnyM4v;

    @Inject
    @Path("Big_Buck_Bunny_Trailer.ogv")
    private Asset               bigBuckBunnyOgv;

    @Inject
    private ContentTypeAnalyzer contentTypeAnalyzer;

    @Inject
    private ComponentResources  resources;

    @Inject
    private Request             request;

    @Inject
    private Response            response;

    @Inject
    private Logger              log;

    public PlayerSource getAudio() {

        PlayerSource source = new PlayerSourceImpl();
        source.add("audio/ogg", toUrl(MIAOW_OGG));
        source.add("audio/mp4", toUrl(MIAOW_M4A));
        return source;
    }

    public PlayerSource getVideo() {

        PlayerSource source = new PlayerSourceImpl();
        source.add("video/m4v", this.bigBuckBunnyM4v.toClientURL());
        source.add("video/ogg", this.bigBuckBunnyOgv.toClientURL());
        return source;
    }

    private String toUrl(String eventName) {
        return this.resources.createEventLink(eventName).toURI();
    }

    @OnEvent(MIAOW_OGG)
    public Object onMiaowOgg() {

        Resource resource = PlayerDemo.this.miaowOgg.getResource();

        String range = this.request.getHeader("Range");

        return new MediaStreamingResponse(resource, 209.0f, range);
    }

    @OnEvent(MIAOW_M4A)
    public StreamResponse onMiaowM4a() {

        Resource resource = PlayerDemo.this.miaowM4a.getResource();

        String range = this.request.getHeader("Accept-Ranges");

        return new MediaStreamingResponse(resource, 209f, range);
    }

    private final class MediaStreamingResponse implements StreamResponse {

        private final Float       length;

        private final Resource    resource;

        private final InputStream is;

        private String            range;

        private MediaStreamingResponse(Resource resource, Float length, String range) {

            this.length = length;
            this.resource = resource;
            this.range = range;

            try {
                this.is = resource.openStream();
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void prepareResponse(Response response) {

            response.setHeader("X-Content-Duration", this.length.toString());
            response.setHeader("Accept-Ranges", "bytes");

            try {
                if (InternalUtils.isNonBlank(this.range)) {

                    String[] ranges = this.range.split("=")[1].split("-");

                    int from = Integer.parseInt(ranges[0]);
                    // TODO: limit to a max size (2MB or something like that)
                    int to = ranges.length == 2 ? Integer.parseInt(ranges[1]) : this.is.available() - 1;
                    int len = to - from + 1;

                    response.setStatus(206);
                    response.setContentLength(len);
                    response.setHeader("Accept-Ranges", "bytes");
                    response.setHeader("Content-Range", String.format("bytes %d-%d/%d", from, to, this.is.available()));
                    response.setDateHeader("Last-Modified", new Date().getTime());

                    this.is.skip(from);
                }
                else {
                    response.setContentLength(this.is.available());
                }
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }

        }

        @Override
        public InputStream getStream() throws IOException {
            // TODO: support "to" parameter
            return this.is;
        }

        @Override
        public String getContentType() {
            return PlayerDemo.this.contentTypeAnalyzer.getContentType(this.resource);
        }
    }
}
