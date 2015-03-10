package com.github.fscheffer.arras.email.services;

import org.apache.commons.mail.Email;

public interface MailService {

    void send(Email email);
}
