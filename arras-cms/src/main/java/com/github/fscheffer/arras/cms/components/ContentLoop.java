package com.github.fscheffer.arras.cms.components;

import javax.inject.Inject;

import org.apache.tapestry5.Block;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.BeginRender;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.json.JSONArray;
import org.apache.tapestry5.services.Environment;

import com.github.fscheffer.arras.cms.BlockContext;

public class ContentLoop {

    @Parameter(required = true, allowNull = false)
    private JSONArray   contents;

    @Parameter(required = true, allowNull = false)
    private Block       block;

    @Parameter
    private Integer     howMany;

    @Inject
    private Environment enviroment;

    private int         index, length;

    @SetupRender
    boolean setup() {

        this.index = 0;
        this.length = this.howMany != null ? this.howMany : this.contents.length();

        return this.index < this.length;
    }

    @BeginRender
    Block begin() {

        BlockContext context = new BlockContext(this.contents.getJSONObject(this.index));

        this.enviroment.push(BlockContext.class, context);

        return this.block;
    }

    @AfterRender
    boolean after() {

        this.enviroment.pop(BlockContext.class);

        this.index++;

        return this.index == this.length;
    }
}
