package org.github.fscheffer.arras;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.runtime.RenderCommand;

public class TabGroupContext {

    private Messages            messages;

    private String              active;

    private boolean             fade;

    private List<RenderCommand> contents = new ArrayList<RenderCommand>();

    private String              current;

    public TabGroupContext(Messages messages, String active, boolean fade) {
        this.messages = messages;
        this.active = active;
        this.fade = fade;
    }

    public boolean isActive(String id) {
        return id.equals(this.active);
    }

    public boolean isFade() {
        return this.fade;
    }

    public List<RenderCommand> getContents() {
        return this.contents;
    }

    public void addContent(RenderCommand body) {
        this.contents.add(body);
    }

    public Messages getMessages() {
        return this.messages;
    }

    public String getCurrent() {
        return this.current;
    }

    public void setCurrent(String current) {

        if (this.active == null) {
            // mark the first tab as the active tab if the "active" parameter
            // was not set
            this.active = current;
        }

        this.current = current;
    }

}
