package com.jazasoft.mtdb.service;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Created by mdzahidraza on 27/08/17.
 */
public interface IEmailService {

    boolean sendSimpleEmail(String[] to, String subject, String text);

    /**
     * Send Email with attachment
     * @param to
     * @param subject
     * @param text
     * @param paths paths to attachement
     * @return
     */
    boolean sendEmailWithAttachment(String[] to, String subject, String text, Iterable<String> paths);
}
