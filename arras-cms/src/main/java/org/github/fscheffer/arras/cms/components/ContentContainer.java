package org.github.fscheffer.arras.cms.components;

import javax.inject.Inject;

import org.apache.tapestry5.Block;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.BeginRender;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.Request;
import org.github.fscheffer.arras.cms.ArrasCmsConstants;
import org.github.fscheffer.arras.cms.services.PermissionManager;
import org.github.fscheffer.arras.cms.services.SubmissionProcessor;

@Import(module = "content-container")
public class ContentContainer {

    @Inject
    private ComponentResources  resources;

    @Inject
    private PermissionManager   permissionManager;

    @Inject
    private Request             request;

    @Inject
    private SubmissionProcessor submissionProcessor;

    @Property(write = false)
    private boolean             hasPermissionToEdit;

    @SetupRender
    void setup() {
        this.hasPermissionToEdit = hasPermissionToEdit();
    }

    @BeginRender
    void begin(MarkupWriter writer) {

        if (this.hasPermissionToEdit) {

            Link link = this.resources.createEventLink(ArrasCmsConstants.SUBMIT_CONTENT);

            writer.element("div", "data-container-type", "content-container", "data-url", link);
        }
    }

    @AfterRender
    void after(MarkupWriter writer) {

        if (this.hasPermissionToEdit) {
            writer.end();
        }
    }

    @OnEvent(ArrasCmsConstants.SUBMIT_CONTENT)
    void submitContent() {

        if (!hasPermissionToEdit()) {
            return;
        }

        String parameter = this.request.getParameter("content");

        JSONObject data = new JSONObject(parameter);

        this.submissionProcessor.process(data);
    }

    public Block getBody() {
        return this.resources.getBody();
    }

    private boolean hasPermissionToEdit() {
        return this.permissionManager.hasPermissionToEdit();
    }
}
