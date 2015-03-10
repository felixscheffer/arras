package com.github.fscheffer.arras.email.services;

import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;

public class ArrasEmailModule {

    public static void contributeFactoryDefaults(MappedConfiguration<String, Object> conf) {

        // assume mail server on localhost
        conf.add(EmailSymbols.USERAGENT, "Arras mailer");
        conf.add(EmailSymbols.SMTP_HOST, "localhost");
        conf.add(EmailSymbols.SMTP_USER, "");
        conf.add(EmailSymbols.SMTP_PASSWORD, "");
        conf.add(EmailSymbols.SMTP_PORT, "25");
        conf.add(EmailSymbols.SMTP_USETLS, false);
        conf.add(EmailSymbols.SMTP_USESSL, false);
        conf.add(EmailSymbols.NUM_THREADS, 1);

        // needs to be set by the user
        conf.add(EmailSymbols.JNDI, "");
        conf.add(EmailSymbols.DEFAULT_SENDER_ADDRESS, "");
        conf.add(EmailSymbols.RECEIPIENT_OVERRIDE, "");
    }

    public static void bind(ServiceBinder binder) {
        binder.bind(MailService.class, MailServiceImpl.class);
    }
}
