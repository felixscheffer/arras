package org.github.fscheffer.arras.cms.services;

import org.apache.tapestry5.ioc.services.ThreadLocale;
import org.apache.tapestry5.services.RequestGlobals;
import org.github.fscheffer.arras.cms.entities.PageContentId;

public class PageContentIdSourceImpl implements PageContentIdSource {

    private RequestGlobals globals;

    private ThreadLocale   locale;

    public PageContentIdSourceImpl(RequestGlobals globals, ThreadLocale locale) {
        this.globals = globals;
        this.locale = locale;
    }

    @Override
    public PageContentId create(String contentId) {
        return create(contentId, 0);
    }

    @Override
    public PageContentId create(String contentId, int index) {

        return new PageContentId(this.globals.getActivePageName(), this.locale.getLocale(), contentId, index);
    }

}
