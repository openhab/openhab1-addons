package org.openhab.core.rules.actions;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Dictionary;

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

public class Mail implements ManagedService {
	
	static private final Logger logger = LoggerFactory.getLogger(Mail.class);
	
	private static boolean initialized = false;
	
	private static String hostname;
	private static Integer port;
	private static String username;
	private static String password;
	private static String from;

	private static boolean tls;

	static public void sendMail(String to, String subject, String message) {
		sendMail(to, subject, message, null);
	}

	static public void sendMail(String to, String subject, String message, String attachmentUrl) {
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
			} else {
			}

			email.setHostName(hostname);
			email.setSmtpPort(port);
			email.setTLS(tls);
			if(username!=null && !username.isEmpty()) {
				email.setAuthenticator(new DefaultAuthenticator(username, password));
			}
			try {
				email.setFrom(from);
				email.addTo(to);
				email.setSubject(subject);
				email.setMsg(message);
				email.send();
			} catch (EmailException e) {
				logger.error("Could not send e-mail to '{}‘.", to, e);
			}
		} else {
			logger.error("Cannot send e-mail because of missing configuration settings. The current settings are: " +
					"Host: '{}', port '{}', from '{}', useTLS: {}, username: '{}', password '{}'",
					new String[] { hostname, String.valueOf(port), from, String.valueOf(tls), username, password} );
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void updated(Dictionary config) throws ConfigurationException {
		if(config!=null) {
			Mail.hostname = (String) config.get("hostname");
			Mail.port = Integer.valueOf((String) config.get("port"));
			Mail.username = (String) config.get("username");
			Mail.password = (String) config.get("password");
			Mail.from = (String) config.get("from");
			Mail.tls = ((String) config.get("tls")).equalsIgnoreCase("true");
			
			// check mandatory settings
			if(hostname==null || hostname.isEmpty()) return;
			if(from==null || from.isEmpty()) return;
			
			// set defaults for optional settings
			if(port==null || hostname.isEmpty()) {
				port = tls ? 587 : 25;
			}
			initialized = true;
		}
	}
}
