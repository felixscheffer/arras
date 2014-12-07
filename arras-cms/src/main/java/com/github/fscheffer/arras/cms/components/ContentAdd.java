package com.github.fscheffer.arras.cms.components;

import javax.inject.Inject;

import org.apache.tapestry5.Block;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.BeginRender;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.Environment;
import org.slf4j.Logger;

import com.github.fscheffer.arras.cms.ArrasCmsConstants;
import com.github.fscheffer.arras.cms.BlockContext;

@SupportsInformalParameters
@Import(module = "content-add")
public class ContentAdd {

    @Parameter
    private Object[]           context;

    @Parameter(required = true, allowNull = false)
    private Block              block;

    @Inject
    private ComponentResources resources;

    @Inject
    private Environment        environment;

    @Inject
    private Logger             log;

    @BeginRender
    void begin(MarkupWriter writer) {

        Link link = this.resources.createEventLink(ArrasCmsConstants.ADD_CONTENT, this.context);

        writer.element("div", "class", "content-add");

        writer.element("button", "type", "button", "data-component-type", "content-add", "data-url", link);
    }

    @AfterRender
    void after(MarkupWriter writer) {

        this.resources.renderInformalParameters(writer);

        writer.end();

        writer.end();
    }

    @OnEvent(ArrasCmsConstants.ADD_CONTENT)
    Block onAddContent() {

        this.log.debug("Adding new content");

        this.environment.push(BlockContext.class, new BlockContext(new JSONObject()));
        return this.block;
    }
}
