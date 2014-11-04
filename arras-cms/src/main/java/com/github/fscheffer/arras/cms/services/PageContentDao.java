package com.github.fscheffer.arras.cms.services;

import org.apache.tapestry5.json.JSONArray;
import org.apache.tapestry5.json.JSONObject;

public interface PageContentDao {

    JSONArray getContent(String contentId);

    JSONObject getContentAsObject(String contentId);

    void save(String contentId, JSONArray obj);

    void save(String contentId, JSONObject obj);
}
