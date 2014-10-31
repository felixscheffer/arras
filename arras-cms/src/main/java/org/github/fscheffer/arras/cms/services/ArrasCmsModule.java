package org.github.fscheffer.arras.cms.services;

import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.Contribute;
import org.apache.tapestry5.ioc.annotations.ImportModule;
import org.apache.tapestry5.jpa.JpaEntityPackageManager;
import org.apache.tapestry5.services.LibraryMapping;
import org.github.fscheffer.arras.cms.ContentBlockContribution;
import org.github.fscheffer.arras.cms.entities.PageContent;
import org.github.fscheffer.arras.cms.pages.DefaultContentBlocks;
import org.github.fscheffer.arras.services.ArrasComponentsModule;

@ImportModule(ArrasComponentsModule.class)
public class ArrasCmsModule {

    public static void contributeComponentClassResolver(Configuration<LibraryMapping> configuration) {
        configuration.add(new LibraryMapping("arras", "org.github.fscheffer.arras.cms"));
    }

    public static void bind(ServiceBinder binder) {
        binder.bind(PageContentDao.class, PageContentDaoImpl.class);
        binder.bind(ContentBlocks.class, ContentBlocksImpl.class);
        binder.bind(SubmissionProcessor.class, SubmissionProcessorImpl.class);
    }

    @Contribute(JpaEntityPackageManager.class)
    public static void providePackages(Configuration<String> configuration) {
        configuration.add(PageContent.class.getPackage().getName());
    }

    public static void contributeContentBlocks(MappedConfiguration<String, ContentBlockContribution> conf) {
        conf.add("article", defaultBlocks("article"));
        conf.add("feature", defaultBlocks("feature"));
        conf.add("teaser", defaultBlocks("teaser"));
    }

    private static ContentBlockContribution defaultBlocks(String blockId) {
        return new ContentBlockContribution("arras/" + DefaultContentBlocks.class.getSimpleName(), blockId);
    }
}
