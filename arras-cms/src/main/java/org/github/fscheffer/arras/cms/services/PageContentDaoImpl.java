package org.github.fscheffer.arras.cms.services;

import java.util.List;
import java.util.Locale;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.tapestry5.ioc.services.PerThreadValue;
import org.apache.tapestry5.ioc.services.PerthreadManager;
import org.apache.tapestry5.json.JSONArray;
import org.github.fscheffer.arras.cms.entities.PageContent;
import org.github.fscheffer.arras.cms.entities.PageContentId;

public class PageContentDaoImpl implements PageContentDao {

    private EntityManager           entityManager;

    private PerThreadValue<Boolean> cacheInitialized;

    public PageContentDaoImpl(EntityManager entityManager, PerthreadManager perthreadManager) {
        this.entityManager = entityManager;
        this.cacheInitialized = perthreadManager.createValue();
    }

    @Override
    public PageContent getContent(PageContentId id, String defaultContent) {

        if (!this.cacheInitialized.get(false)) {

            // batch load the page content
            retrievePageContents(id.getPageName(), id.getLocale());

            this.cacheInitialized.set(true);
        }

        PageContent content = this.entityManager.find(PageContent.class, id);

        if (content == null) {

            content = new PageContent(id);

            content.setContent(defaultContent);

            this.entityManager.persist(content);
        }

        return content;
    }

    @Override
    public JSONArray getContent(PageContentId id) {

        if (!this.cacheInitialized.get(false)) {

            // batch load the page content
            retrievePageContents(id.getPageName(), id.getLocale());

            this.cacheInitialized.set(true);
        }

        PageContent content = this.entityManager.find(PageContent.class, id);

        return content == null ? new JSONArray() : new JSONArray(content.getContent());
    }

    @Override
    public void save(PageContentId id, JSONArray obj) {

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

    @Override
    public int count(String contentId) {

        // TODO:
        return 1;
        /*
        int count = 0;

        while (true) {

            id = id.withIndex(count);

            if (this.entityManager.find(PageContentId.class, id) == null) {
                return count;
            }

            count++;
        }
         */
    }

    private List<?> retrievePageContents(String pagename, Locale locale) {

        Query query = this.entityManager.createQuery("select c from PageContent c where c.id.pageName = :pagename and c.id.locale = :locale");
        query.setParameter("pagename", pagename);
        query.setParameter("locale", locale);
        return query.getResultList();
    }
}
