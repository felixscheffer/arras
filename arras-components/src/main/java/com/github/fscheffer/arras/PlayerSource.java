package com.github.fscheffer.arras;

import org.apache.tapestry5.MarkupWriter;

public interface PlayerSource {

    void add(String mimetype, String path);

    void write(MarkupWriter writer);
}
