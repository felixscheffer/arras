package org.github.fscheffer.arras.demo.pages;

import java.util.List;

import javax.inject.Inject;

import org.apache.tapestry5.annotations.Property;
import org.github.fscheffer.arras.demo.Track;
import org.github.fscheffer.arras.demo.services.MusicLibrary;

public class DataTablesDemo {

    @Inject
    private MusicLibrary library;

    @Property
    private Track        track;

    public List<Track> getTracks() {
        return this.library.getTracks();
    }
}
