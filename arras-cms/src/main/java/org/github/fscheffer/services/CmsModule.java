package org.github.fscheffer.services;

import org.apache.tapestry5.ioc.ServiceBinder;

public class CmsModule {

    public static void bind(ServiceBinder binder) {
        binder.bind(PageContentDao.class, PageContentDaoImpl.class);
    }
}
