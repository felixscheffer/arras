package org.github.fscheffer.arras.cms.components;

import javax.inject.Inject;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import org.github.fscheffer.arras.ArrasConstants;
import org.github.fscheffer.arras.cms.services.AvailableImages;
import org.github.fscheffer.arras.cms.services.PermissionManager;
import org.github.fscheffer.arras.components.LightboxContent;
import org.slf4j.Logger;

public class Image {

    @Parameter(required = true, allowNull = false)
    @Property(write = false)
    private String             value;

    @Inject
    private PermissionManager  permissionManager;

    @Inject
    private Logger             logger;

    @Inject
    private ComponentResources resources;

    @Inject
    private JavaScriptSupport  support;

    @Inject
    private AvailableImages    availableImages;

    @InjectComponent
    private LightboxContent    imageSelection;

    @Property
    private String             image;

    @Property(write = false)
    private String             clientId;

    @Property(write = false)
    private boolean            editing;

    @SetupRender
    void setup() {
        this.clientId = this.support.allocateClientId(this.resources);
        this.editing = this.permissionManager.hasPermissionToEdit();

        if (this.editing) {
            this.support.require("content-image");
        }
    }

    public String getCompleteId() {
        return this.resources.getCompleteId();
    }

    public String getImageSelectionId() {
        return "#" + this.imageSelection.getClientId();
    }

    public Iterable<String> getImages() {
        return this.availableImages.getImageUrls();
    }

    @OnEvent(ArrasConstants.UPDATE_CONTENT)
    boolean onContentSubmit(String newValue) {

        if (!this.permissionManager.hasPermissionToEdit()) {
            throw new RuntimeException("You are not allowed to edit this page!");
        }

        this.logger.info("Saving {}", newValue);

        this.value = newValue;

        return true;
    }
}
