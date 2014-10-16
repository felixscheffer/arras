package org.github.fscheffer.services;

import java.util.Locale;

public interface PageContentDao {

    String getContent(String pagename, String contentId, Locale locale);
}
