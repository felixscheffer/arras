package org.github.fscheffer.entities;

import java.util.Locale;

import javax.persistence.Embeddable;

@Embeddable
public class PageContentId {

    private String pageName;

    private String contentId;

    private Locale locale;

    public PageContentId(String pageName, String contentId, Locale locale) {
        this.pageName = pageName;
        this.contentId = contentId;
        this.locale = locale;
    }

    public String getPageName() {
        return this.pageName;
    }

    public String getContentId() {
        return this.contentId;
    }

    public Locale getLocale() {
        return this.locale;
    }
}
