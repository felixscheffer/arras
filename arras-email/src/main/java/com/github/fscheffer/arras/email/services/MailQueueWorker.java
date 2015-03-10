package com.github.fscheffer.arras.email.services;

import java.util.concurrent.TimeUnit;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.tapestry5.ioc.internal.util.InternalUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MailQueueWorker implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(MailQueueWorker.class);

    private Email               email;

    public MailQueueWorker(Email email) {
        this.email = email;
    }

    @Override
    public void run() {

        String debugInfo = debugInfo();

        MailQueueWorker.log.info("Sending e-mail: {}", debugInfo);

        try {
            long nanoStart = System.nanoTime();
            this.email.send();
            long nanoStop = System.nanoTime();
            MailQueueWorker.log.info("Sending e-mail took {} ms", TimeUnit.NANOSECONDS.toMillis(nanoStop - nanoStart));
        }
        catch (EmailException e) {
            MailQueueWorker.log.error("FAILED not send e-mail: " + debugInfo, e);

            try {
                // maybe there is a issue with the mail server, so wait before attempting to send more mails
                Thread.sleep(TimeUnit.SECONDS.toMillis(10));

                // TODO: retry?
            }
            catch (InterruptedException e1) {

            }
        }
    }

    private String debugInfo() {
        String subject = this.email.getSubject();
        String from = this.email.getFromAddress().getAddress();

        String to = InternalUtils.join(this.email.getToAddresses(), ",");
        String cc = InternalUtils.join(this.email.getCcAddresses(), ",");
        String bcc = InternalUtils.join(this.email.getBccAddresses(), ",");

        return String.format("Subject: '%s', From: '%s', To: '%s', CC: '%s', BCC: '%s'", subject, from, to, cc, bcc);
    }

}
