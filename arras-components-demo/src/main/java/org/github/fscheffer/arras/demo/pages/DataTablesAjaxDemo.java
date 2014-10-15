package org.github.fscheffer.arras.demo.pages;

import javax.inject.Inject;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.services.TypeCoercer;
import org.github.fscheffer.arras.CollectionFilteringDataSource;
import org.github.fscheffer.arras.FilteringDataSource;
import org.github.fscheffer.arras.demo.Track;
import org.github.fscheffer.arras.demo.services.MusicLibrary;

public class DataTablesAjaxDemo {

    @Inject
    private MusicLibrary library;

    @Inject
    private TypeCoercer  coercer;

    @Property
    private Track        track;

    public FilteringDataSource getTracks() {
        return new CollectionFilteringDataSource<Track>(this.library.getTracks(), this.coercer);
    }
}
