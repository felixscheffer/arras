package com.github.fscheffer.arras.cms;

public class ContentBlockContribution {

    private final String pageName;

    private final String blockId;

    public ContentBlockContribution(String pageName, String blockId) {
        this.pageName = pageName;
        this.blockId = blockId;
    }

    public String getPageName() {
        return this.pageName;
    }

    public String getBlockId() {
        return this.blockId;
    }

}
