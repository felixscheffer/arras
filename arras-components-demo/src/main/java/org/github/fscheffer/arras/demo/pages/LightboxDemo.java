package org.github.fscheffer.arras.demo.pages;

import java.util.Date;

import javax.inject.Inject;

import org.apache.tapestry5.Block;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.OnEvent;
import org.github.fscheffer.arras.components.LightboxContent;

public class LightboxDemo {

    @InjectComponent
    private LightboxContent content;

    @Inject
    private Block           zoneContent;

    public String getContentId() {
        return "#" + this.content.getClientId();
    }

    public Date getDate() {
        return new Date();
    }

    @OnEvent("someAjaxEvent")
    public Block onUpdateZone() {
        return this.zoneContent;
    }
}
