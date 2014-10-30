package org.github.fscheffer.arras.cms.services;

import java.util.Collections;
import java.util.List;

public class AvailableImagesImpl implements AvailableImages {

    private List<String> conf;

    public AvailableImagesImpl(List<String> conf) {
        this.conf = conf;
    }

    @Override
    public Iterable<String> getImageUrls() {
        return Collections.unmodifiableList(this.conf);
    }

}
