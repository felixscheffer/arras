package com.github.fscheffer.arras.demo.pages;

import java.util.Date;

import org.apache.tapestry5.Block;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.corelib.components.Zone;

public class LoadingOverlayDemo {

    @InjectComponent
    private Zone zone;

    @OnEvent("trigger")
    Block onTrigger() {

        try {
            Thread.sleep(20000);
        }
        catch (InterruptedException e) {}

        return this.zone.getBody();
    }

    public Date getDate() {
        return new Date();
    }
}
