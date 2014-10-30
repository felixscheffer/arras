package org.github.fscheffer.arras.cms.services;

import org.github.fscheffer.arras.cms.entities.PageContentId;

public interface PageContentIdSource {

    PageContentId create(String contentId);

    PageContentId create(String contentId, int index);
}
