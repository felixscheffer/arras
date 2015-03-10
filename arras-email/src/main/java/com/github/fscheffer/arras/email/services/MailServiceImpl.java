package com.github.fscheffer.arras.email.services;

import java.net.IDN;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailConstants;
import org.apache.commons.mail.EmailException;
import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.ioc.internal.util.InternalUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MailServiceImpl implements MailService {

    private final Logger                logger = LoggerFactory.getLogger(MailServiceImpl.class);

    private final String                mailerUserAgent;

    private final boolean               productionMode;

    private Collection<InternetAddress> receipientOverrides;

    private final ExecutorService       queue;

    private String                      defaultSenderAddress;

    private Session                     jndi;

    private String                      smtpHost;

    private String                      smtpUser;

    private String                      smtpPassword;

    private int                         smtpPort;

    private boolean                     useSSL;

    private boolean                     useTLS;

    public MailServiceImpl(@Inject @Symbol(SymbolConstants.PRODUCTION_MODE) boolean productionMode,
                           @Inject @Symbol(EmailSymbols.JNDI) String jndiName,
                           @Inject @Symbol(EmailSymbols.SMTP_HOST) String smtpHost,
                           @Inject @Symbol(EmailSymbols.SMTP_PORT) int smtpPort,
                           @Inject @Symbol(EmailSymbols.SMTP_USER) String smtpUser,
                           @Inject @Symbol(EmailSymbols.SMTP_PASSWORD) String smtpPassword,
                           @Inject @Symbol(EmailSymbols.SMTP_USESSL) boolean useSSL,
                           @Inject @Symbol(EmailSymbols.SMTP_USETLS) boolean useTLS,
                           @Inject @Symbol(EmailSymbols.USERAGENT) String mailerUserAgent,
                           @Inject @Symbol(EmailSymbols.DEFAULT_SENDER_ADDRESS) String defaultSenderAddress,
                           @Inject @Symbol(EmailSymbols.RECEIPIENT_OVERRIDE) String receipientOverride,
                           @Inject @Symbol(EmailSymbols.NUM_THREADS) int numThreads) {

        this.productionMode = productionMode;
        this.smtpHost = smtpHost;
        this.smtpPort = smtpPort;
        this.smtpUser = smtpUser;
        this.smtpPassword = smtpPassword;
        this.useSSL = useSSL;
        this.useTLS = useTLS;
        this.mailerUserAgent = mailerUserAgent;

        if (InternalUtils.isNonBlank(jndiName)) {

            this.logger.info("Using JNDI: 'java:comp/env/{}'", jndiName);

            this.jndi = Session.class.cast(lookupJNDI("java:comp/env/" + jndiName));

            this.defaultSenderAddress = this.jndi.getProperty(EmailConstants.MAIL_SMTP_FROM);

            if (InternalUtils.isBlank(this.defaultSenderAddress)) {
                throw new IllegalArgumentException(
                                                   "Please provide a value for javax.mail property 'mail.from' in your JNDI configuration.");
            }
        }
        else {
            this.defaultSenderAddress = defaultSenderAddress;

            if (InternalUtils.isBlank(this.defaultSenderAddress)) {
                throw new IllegalArgumentException("Please provide a value for " + EmailSymbols.DEFAULT_SENDER_ADDRESS);
            }
        }

        if (!this.productionMode) {

            if (InternalUtils.isBlank(receipientOverride)) {
                throw new IllegalArgumentException(
                                                   "Please set an email address for receipient override to ensure no email is sent accidently to a customer!");
            }

            this.receipientOverrides = toInternetAddresses(receipientOverride);
        }

        // limit the number of emails that are passed to the server at the same time (default is 1)
        this.queue = new ThreadPoolExecutor(0, numThreads, 1000L, TimeUnit.MILLISECONDS,
                                            new LinkedBlockingQueue<Runnable>());;
    }

    public static Object lookupJNDI(String jndiName) {
        try {
            return new InitialContext().lookup(jndiName);
        }
        catch (NamingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void send(Email email) {

        email.setCharset("UTF-8");
        email.addHeader("User-Agent", this.mailerUserAgent);

        if (!this.productionMode) {
            overrideReceipients(email);
        }

        setSmtpServer(email);

        this.queue.execute(new MailQueueWorker(email));

        this.logger.info("Mail '{}' for '{}' was added to queue!", email.getSubject(), email.getToAddresses());
    }

    private void setSmtpServer(Email email) {

        if (this.jndi == null) {
            email.setHostName(this.smtpHost);

            if (InternalUtils.isNonBlank(this.smtpUser)) {
                email.setAuthentication(this.smtpUser, this.smtpPassword);
            }

            if (this.smtpPort > 0) {
                email.setSmtpPort(this.smtpPort);
            }

            email.setStartTLSEnabled(this.useTLS);
            email.setSSLOnConnect(this.useSSL);
        }
        else {
            email.setMailSession(this.jndi);
        }

        // Email does not read the from-address from the JNDI mail session, so set it manually
        if (email.getFromAddress() == null) {
            setSenderAddress(email, this.defaultSenderAddress);
        }
    }

    private void setSenderAddress(Email email, String senderAddress) {
        try {
            // TODO: do IDN conversion here??
            email.setFrom(senderAddress);
        }
        catch (EmailException e) {
            throw new RuntimeException(e);
        }
    }

    private void overrideReceipients(Email email) {
        email.setSubject(buildReceipientOverrideSubject(email));
        try {
            email.setTo(this.receipientOverrides);
        }
        catch (EmailException e) {
            throw new RuntimeException(e);
        }
    }

    private String buildReceipientOverrideSubject(Email email) {

        String to = InternalUtils.join(email.getToAddresses(), ", ");
        String cc = InternalUtils.join(email.getCcAddresses(), ", ");
        String bcc = InternalUtils.join(email.getBccAddresses(), ", ");

        return String.format("[To: %s, CC: %s, BCC: %s] %s", to, cc, bcc, email.getSubject());
    }

    private Collection<InternetAddress> toInternetAddresses(String emails) {

        Collection<InternetAddress> addresses = new ArrayList<InternetAddress>();

        for (String email : emails.split(",")) {
            if (InternalUtils.isBlank(email)) {
                continue;
            }

            addresses.add(toInternetAddress(email));
        }
        return addresses;
    }

    private InternetAddress toInternetAddress(String email) {
        String idnCompliantEmail = toIdnCompliantEmail(email);
        try {
            return new InternetAddress(idnCompliantEmail);
        }
        catch (AddressException e) {
            throw new RuntimeException(e);
        }
    }

    private String toIdnCompliantEmail(String email) {

        int idx = email.indexOf('@');

        if (!(idx > 0)) {
            throw new IllegalArgumentException("Invalid email address '" + email
                                               + "'. Starts with @ or does not contain an @.");
        }

        String localpart = email.substring(0, idx + 1);
        String domain = email.substring(idx + 1);

        return localpart + IDN.toASCII(domain);
    }
}
