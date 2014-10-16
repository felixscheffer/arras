package org.github.fscheffer.services;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.tapestry5.ioc.services.PerThreadValue;
import org.apache.tapestry5.ioc.services.PerthreadManager;
import org.github.fscheffer.entities.PageContent;

public class PageContentDaoImpl implements PageContentDao {

    private EntityManager                       entityManager;

    private PerThreadValue<Map<String, String>> cache;

    public PageContentDaoImpl(EntityManager entityManager, PerthreadManager perthreadManager) {
        this.entityManager = entityManager;
        this.cache = perthreadManager.createValue();
    }

    public String getContent(String pagename, String contentId, Locale locale) {

        Map<String, String> contents = getContentsForPage(pagename, locale);

        return contents.get(contentId);
    }

    private Map<String, String> getContentsForPage(String pagename, Locale locale) {

        Map<String, String> contents = this.cache.get();

        if (contents == null) {

            contents = retrievePageContents(pagename, locale);

            this.cache.set(contents);
        }

        return contents;
    }

    private Map<String, String> retrievePageContents(String pagename, Locale locale) {

        Query query = this.entityManager.createQuery("select c from PageContent c where c.pagename = :pagename and c.locale = :locale");
        query.setParameter("pagename", pagename);
        query.setParameter("locale", locale);
        List<PageContent> result = query.getResultList();

        return transformQueryResult(result);
    }

    private Map<String, String> transformQueryResult(List<PageContent> list) {

        Map<String, String> contents = new HashMap<String, String>(list.size());
        for (PageContent content : list) {
            contents.put(content.getContentId(), content.getContent());
        }

        return contents;
    }
}
