package com.github.fscheffer.arras;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class PlayerSourceImpl implements PlayerSource {

    private Map<String, String> sources = new LinkedHashMap<>();

    @Override
    public void add(String mimetype, String path) {
        this.sources.put(mimetype, path);
    }

    @Override
    public Map<String, String> get() {
        return Collections.unmodifiableMap(this.sources);
    }
}
