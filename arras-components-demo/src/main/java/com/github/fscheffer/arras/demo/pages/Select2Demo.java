package com.github.fscheffer.arras.demo.pages;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.services.ValueEncoderSource;

public class Select2Demo {

    @Persist(PersistenceConstants.FLASH)
    @Property
    private String             select;

    @Persist(PersistenceConstants.FLASH)
    @Property
    private List<String>       selectMultiple;

    @Inject
    private ValueEncoderSource encoderSource;

    void onActivate() {
        if (this.selectMultiple == null) {
            this.selectMultiple = new ArrayList<String>();
        }
    }

    public ValueEncoder<String> getStringEncoder() {
        return this.encoderSource.getValueEncoder(String.class);
    }
}
