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
