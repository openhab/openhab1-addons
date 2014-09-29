/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.action.mail.internal;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;
import org.apache.commons.mail.SimpleEmail;
import org.openhab.core.scriptengine.action.ActionDoc;
import org.openhab.core.scriptengine.action.ParamDoc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/** 
 * This class provides static methods that can be used in automation rules
 * for sending emails via SMTP. 
 * 
 * @author Kai Kreuzer
 * @since 0.4.0
 *
 */public class Mail {

	private static final Logger logger = LoggerFactory.getLogger(Mail.class);

	static String hostname;
	static Integer port;
	static String username;
	static String password;
	static String from;

	static boolean tls;
	static boolean popBeforeSmtp = false;

	/**
	 * Sends an email via SMTP
	 * 
	 * @param to the email address of the recipient
	 * @param subject the subject of the email
	 * @param message the body of the email
	 * 
	 * @return <code>true</code>, if sending the email has been successful and 
	 * <code>false</code> in all other cases.
	 */
	@ActionDoc(text="Sends an email via SMTP")
	static public boolean sendMail(
		@ParamDoc(name="to") String to, 
		@ParamDoc(name="subject") String subject, 
		@ParamDoc(name="message") String message) {
		return sendMail(to, subject, message, null);
	}

	/**
	 * Sends an email with attachment via SMTP
	 * 
	 * @param to the email address of the recipient
	 * @param subject the subject of the email
	 * @param message the body of the email
	 * @param attachmentUrl a URL string of the content to send as an attachment
	 * 
	 * @return <code>true</code>, if sending the email has been successful and 
	 * <code>false</code> in all other cases.
	 */
	@ActionDoc(text="Sends an email with attachment via SMTP")
	static public boolean sendMail(
			@ParamDoc(name="to") String to, 
			@ParamDoc(name="subject") String subject, 
			@ParamDoc(name="message") String message,
			@ParamDoc(name="attachmentUrl") String attachmentUrl) {
		boolean success = false;
		if(MailActionService.isProperlyConfigured) {
			Email email = new SimpleEmail();
			if(attachmentUrl!=null) {
				// Create the attachment
				  try {
					  email = new MultiPartEmail();
					  EmailAttachment attachment = new EmailAttachment();
					  attachment.setURL(new URL(attachmentUrl));
					  attachment.setDisposition(EmailAttachment.ATTACHMENT);
					  attachment.setName("Attachment");
					  ((MultiPartEmail) email).attach(attachment);
				} catch (MalformedURLException e) {
					logger.error("Invalid attachment url.", e);
				} catch (EmailException e) {
					logger.error("Error adding attachment to email.", e);
				}
			}

			email.setHostName(hostname);
			email.setSmtpPort(port);
			email.setTLS(tls);
			
			if (StringUtils.isNotBlank(username)) {
				if (popBeforeSmtp) {
					email.setPopBeforeSmtp(true, hostname, username, password);
				} else {
					email.setAuthenticator(new DefaultAuthenticator(username, password));
				}
			}

			try {
				email.setFrom(from);
				email.addTo(to);
				if(!StringUtils.isEmpty(subject)) email.setSubject(subject);
				if(!StringUtils.isEmpty(message)) email.setMsg(message);
				email.send();
				logger.debug("Sent email to '{}' with subject '{}'.", to, subject);
				success = true;
			} catch (EmailException e) {
				logger.error("Could not send e-mail to '" + to + "‘.", e);
			}
		} else {
			logger.error("Cannot send e-mail because of missing configuration settings. The current settings are: " +
					"Host: '{}', port '{}', from '{}', useTLS: {}, username: '{}', password '{}'",
					new String[] { hostname, String.valueOf(port), from, String.valueOf(tls), username, password} );
		}
		
		return success;
	}

}
