package org.github.fscheffer.arras.cms.services;

import java.util.Map;

import org.apache.tapestry5.Block;
import org.apache.tapestry5.internal.services.RequestPageCache;
import org.apache.tapestry5.internal.structure.Page;
import org.github.fscheffer.arras.cms.ContentBlockContribution;

public class ContentBlocksImpl implements ContentBlocks {

    private RequestPageCache                      pageCache;

    private Map<String, ContentBlockContribution> conf;

    public ContentBlocksImpl(Map<String, ContentBlockContribution> conf, RequestPageCache pageCache) {
        this.conf = conf;
        this.pageCache = pageCache;
    }

    @Override
    public Block getBlock(String id) {

        return toBlock(this.conf.get(id));
    }

    private Block toBlock(ContentBlockContribution contribution) {

        if (contribution == null) {
            return null;
        }

        Page page = this.pageCache.get(contribution.getPageName());

        return page.getRootElement().getBlock(contribution.getBlockId());
    }
}
