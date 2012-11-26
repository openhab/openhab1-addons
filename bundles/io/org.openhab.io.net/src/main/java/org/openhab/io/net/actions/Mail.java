/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2012, openHAB.org <admin@openhab.org>
 *
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or
 * combining it with Eclipse (or a modified version of that library),
 * containing parts covered by the terms of the Eclipse Public License
 * (EPL), the licensors of this Program grant you additional permission
 * to convey the resulting work.
 */
package org.openhab.io.net.actions;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Dictionary;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;
import org.apache.commons.mail.SimpleEmail;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** 
 * This class provides static methods that can be used in automation rules
 * for sending emails via SMTP. 
 * 
 * @author Kai Kreuzer
 * @since 0.4.0
 *
 */
public class Mail implements ManagedService {
	
	static private final Logger logger = LoggerFactory.getLogger(Mail.class);
	
	private static boolean initialized = false;
	
	private static String hostname;
	private static Integer port;
	private static String username;
	private static String password;
	private static String from;

	private static boolean tls;
	private static boolean popBeforeSmtp = false;

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
	static public boolean sendMail(String to, String subject, String message) {
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
	static public boolean sendMail(String to, String subject, String message, String attachmentUrl) {
		boolean success = false;
		if(initialized) {
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

	@SuppressWarnings("rawtypes")
	public void updated(Dictionary config) throws ConfigurationException {
		if (config != null) {
			Mail.hostname = (String) config.get("hostname");
			
			String portString = (String) config.get("port");
			if (portString != null) {
				Mail.port = Integer.valueOf(portString);
			}
			
			Mail.username = (String) config.get("username");
			Mail.password = (String) config.get("password");
			Mail.from = (String) config.get("from");
			
			String tlsString = (String) config.get("tls");
			if (StringUtils.isNotBlank(tlsString)) {
				 Mail.tls = tlsString.equalsIgnoreCase("true");
			 }
			String popBeforeSmtpString = (String) config.get("popbeforesmtp");
			if (StringUtils.isNotBlank(popBeforeSmtpString)) {
				Mail.popBeforeSmtp = popBeforeSmtpString.equalsIgnoreCase("true"); 
			}
			
			// check mandatory settings
			if (StringUtils.isBlank(hostname) || StringUtils.isBlank(from)) {
				throw new ConfigurationException("mail", "Parameters mail:hostname and mail:from are mandatory and must be configured. Please check your openhab.cfg!");
			}
			
			// set defaults for optional settings
			if(port==null) {
				port = tls ? 587 : 25;
			}
			
			initialized = true;
		}
	}
}
