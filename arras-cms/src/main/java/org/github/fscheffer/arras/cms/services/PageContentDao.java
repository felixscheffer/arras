package org.github.fscheffer.arras.cms.services;

import org.apache.tapestry5.json.JSONArray;

public interface PageContentDao {

    JSONArray getContent(String contentId);

    void save(String contentId, JSONArray obj);
}
