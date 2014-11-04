package com.github.fscheffer.arras.cms.entities;

import java.util.Locale;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
public class PageContent {

    @Id
    private PageContentId id;

    @Lob
    private String        content;

    public PageContent() {}

    public PageContent(PageContentId id) {
        this.id = id;
    }

    public PageContentId getId() {
        return this.id;
    }

    public String getPagename() {
        return this.id.getPageName();
    }

    public String getContentId() {
        return this.id.getContentId();
    }

    public Locale getLocale() {
        return this.id.getLocale();
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
