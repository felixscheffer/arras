package org.github.fscheffer.arras.cms.components;

import javax.inject.Inject;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.Block;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.json.JSONArray;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.Environment;
import org.github.fscheffer.arras.ArrasConstants;
import org.github.fscheffer.arras.cms.BlockContext;
import org.github.fscheffer.arras.cms.services.ContentBlocks;
import org.github.fscheffer.arras.cms.services.PageContentDao;
import org.github.fscheffer.arras.cms.services.PermissionManager;
import org.github.fscheffer.arras.services.SubmissionProcessor;

public class ContentBlock {

    @Parameter(required = true, allowNull = false)
    @Property(write = false)
    private String              blockId;

    @Parameter(required = true, allowNull = false)
    @Property(write = false)
    private String              contentId;

    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    @Property(write = false)
    private String              sizeClass;

    @Parameter
    @Property(write = false)
    private Integer             howMany;

    @Inject
    private ContentBlocks       blocks;

    @Inject
    private PermissionManager   permissionManager;

    @Inject
    private PageContentDao      contentDao;

    @Inject
    private ComponentResources  resources;

    @Inject
    private SubmissionProcessor submissionProcessor;

    @Inject
    private Environment         environment;

    public Block getBlock() {

        Block block = this.blocks.getBlock(this.blockId);
        if (block == null) {
            throw new IllegalStateException("Missing block for id \"" + this.blockId + "\"");
        }

        return block;
    }

    public boolean isEditing() {
        return this.permissionManager.hasPermissionToEdit();
    }

    public String getContext() {
        return this.resources.getCompleteId();
    }

    public JSONArray getContents() {

        JSONArray contents = this.contentDao.getContent(this.contentId);

        if (this.howMany != null) {

            while (contents.length() < this.howMany) {
                contents.put(new JSONObject());
            }
        }

        return contents;
    }

    @OnEvent(ArrasConstants.UPDATE_CONTENT)
    boolean onUpdateContent(JSONArray array) {

        validate(array);

        JSONArray contents = new JSONArray();

        for (int i = 0; i < array.length(); i++) {

            JSONObject content = new JSONObject();

            this.environment.push(BlockContext.class, new BlockContext(content));

            JSONObject submission = array.getJSONObject(i);

            this.submissionProcessor.process(submission);

            this.environment.pop(BlockContext.class);

            contents.put(content);
        }

        this.contentDao.save(this.contentId, contents);

        return true;
    }

    private void validate(JSONArray array) {

        if (this.howMany != null && array.length() != this.howMany) {

            throw new IllegalStateException("The submitted data does have the expected length. It should be \""
                                            + this.howMany + "\" but was \"" + array.length() + "\"!");
        }
    }

    public boolean isAllowRemoval() {
        return this.howMany == null;
    }

}
