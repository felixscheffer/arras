package com.github.fscheffer.arras.cms.entities;

import java.io.Serializable;
import java.util.Locale;

import javax.persistence.Embeddable;

@Embeddable
public class PageContentId implements Serializable {

    private static final long serialVersionUID = -872438096544296683L;

    private String            pageName;

    private Locale            locale;

    private String            contentId;

    public PageContentId() {}

    public PageContentId(String pageName, Locale locale, String contentId) {
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (this.contentId == null ? 0 : this.contentId.hashCode());
        result = prime * result + (this.locale == null ? 0 : this.locale.hashCode());
        result = prime * result + (this.pageName == null ? 0 : this.pageName.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof PageContentId)) {
            return false;
        }
        PageContentId other = (PageContentId) obj;
        if (this.contentId == null) {
            if (other.contentId != null) {
                return false;
            }
        }
        else if (!this.contentId.equals(other.contentId)) {
            return false;
        }
        if (this.locale == null) {
            if (other.locale != null) {
                return false;
            }
        }
        else if (!this.locale.equals(other.locale)) {
            return false;
        }
        if (this.pageName == null) {
            if (other.pageName != null) {
                return false;
            }
        }
        else if (!this.pageName.equals(other.pageName)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "PageContentId [pageName=" + this.pageName + ", locale=" + this.locale + ", contentId=" + this.contentId
               + "]";
    }

}
