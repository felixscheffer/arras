package org.github.fscheffer.arras.cms;

import org.apache.tapestry5.json.JSONObject;

public class BlockContext {

    public final JSONObject data;

    public BlockContext(JSONObject data) {
        this.data = data;
    }

}
