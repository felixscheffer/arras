package com.github.fscheffer.arras;

import java.util.Map;

public interface PlayerSource {

    void add(String mimetype, String path);

    Map<String, String> get();
}
