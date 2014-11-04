package com.github.fscheffer.arras.cms.services;

import java.util.List;
import java.util.Locale;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.tapestry5.ioc.services.PerThreadValue;
import org.apache.tapestry5.ioc.services.PerthreadManager;
import org.apache.tapestry5.ioc.services.ThreadLocale;
import org.apache.tapestry5.json.JSONArray;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.RequestGlobals;

import com.github.fscheffer.arras.cms.entities.PageContent;
import com.github.fscheffer.arras.cms.entities.PageContentId;

public class PageContentDaoImpl implements PageContentDao {

    private EntityManager           entityManager;

    private RequestGlobals          globals;

    private ThreadLocale            locale;

    private PerThreadValue<Boolean> cacheInitialized;

    public PageContentDaoImpl(EntityManager entityManager,
                              PerthreadManager perthreadManager,
                              RequestGlobals globals,
                              ThreadLocale locale) {
        this.entityManager = entityManager;
        this.globals = globals;
        this.locale = locale;
        this.cacheInitialized = perthreadManager.createValue();
    }

    @Override
    public JSONObject getContentAsObject(String contentId) {

        JSONArray array = getContent(contentId);

        return array.length() == 0 ? new JSONObject() : array.getJSONObject(0);
    }

    @Override
    public JSONArray getContent(String contentId) {

        PageContentId id = createId(contentId);

        if (!this.cacheInitialized.get(false)) {

            // batch load the page content
            retrievePageContents(id.getPageName(), id.getLocale());

            this.cacheInitialized.set(true);
        }

        PageContent content = this.entityManager.find(PageContent.class, id);

        return content == null ? new JSONArray() : new JSONArray(content.getContent());
    }

    @Override
    public void save(String contentId, JSONObject obj) {
        save(contentId, new JSONArray(obj));
    }

    @Override
    public void save(String contentId, JSONArray obj) {

        PageContentId id = createId(contentId);

        if (!this.cacheInitialized.get(false)) {

            // batch load the page content
            retrievePageContents(id.getPageName(), id.getLocale());

            this.cacheInitialized.set(true);
        }

        PageContent content = this.entityManager.find(PageContent.class, id);

        if (content == null) {

            content = new PageContent(id);

            this.entityManager.persist(content);
        }

        content.setContent(obj.toCompactString());
    }

    private List<?> retrievePageContents(String pagename, Locale locale) {

        Query query = this.entityManager.createQuery("select c from PageContent c where c.id.pageName = :pagename and c.id.locale = :locale");
        query.setParameter("pagename", pagename);
        query.setParameter("locale", locale);
        return query.getResultList();
    }

    public PageContentId createId(String contentId) {
        return new PageContentId(this.globals.getActivePageName(), this.locale.getLocale(), contentId);
    }
}
