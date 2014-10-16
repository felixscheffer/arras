package org.github.fscheffer.components;

import java.util.Locale;

import javax.inject.Inject;

import org.apache.tapestry5.Block;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.github.fscheffer.services.PageContentDao;
import org.github.fscheffer.services.UserManager;

public class Content {

    @Parameter(value = "pageName")
    private String             pageName;

    @Parameter(required = true, allowNull = false, autoconnect = true)
    private String             contentId;

    @Inject
    private Locale             locale;

    @Inject
    private ComponentResources resources;

    @Inject
    private PageContentDao     contentDao;

    @Inject
    private UserManager        userManager;

    @Inject
    private Block              contentBlock;

    @Property
    private boolean            editMode;

    @Property
    private String             content;

    public void setuprender(MarkupWriter writer) {

        this.editMode = this.userManager.hasPermissionToEdit(this.pageName);
        this.content = this.contentDao.getContent(this.pageName, this.contentId, this.locale);
    }

    public String getPageName() {
        return this.resources.getPageName();
    }

    @OnEvent(value = EventConstants.SUCCESS)
    public Block onSuccess() {
        return this.contentBlock;
    }
}
