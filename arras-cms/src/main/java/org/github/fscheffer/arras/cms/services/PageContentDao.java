package org.github.fscheffer.arras.cms.services;

import org.apache.tapestry5.json.JSONArray;
import org.github.fscheffer.arras.cms.entities.PageContent;
import org.github.fscheffer.arras.cms.entities.PageContentId;

public interface PageContentDao {

    PageContent getContent(PageContentId id, String defaultContent);

    int count(String contentId);

    JSONArray getContent(PageContentId id);

    void save(PageContentId id, JSONArray obj);
}
