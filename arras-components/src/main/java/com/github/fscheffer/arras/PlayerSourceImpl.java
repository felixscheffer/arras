package com.github.fscheffer.arras;

import java.util.List;

import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;

public class PlayerSourceImpl implements PlayerSource {

    private List<Source> sources = CollectionFactory.newList();

    @Override
    public void add(String mimetype, String path) {
        this.sources.add(new Source(mimetype, path));
    }

    @Override
    public void write(MarkupWriter writer) {

        for (Source source : this.sources) {
            writer.element("source", "src", source.url, "type", source.mimetype);
            writer.end();
        }
    }

    private static class Source {

        public final String mimetype;

        public final String url;

        public Source(String mimetype, String url) {
            this.mimetype = mimetype;
            this.url = url;
        }
    }
}
