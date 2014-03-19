/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.action.mail.internal;

import java.util.Dictionary;

import org.apache.commons.lang.StringUtils;
import org.openhab.core.scriptengine.action.ActionService;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
	

/**
 * This class registers an OSGi service for the Mail action.
 * 
 * @author Kai Kreuzer
 * @since 1.3.0
 */
public class MailActionService implements ActionService, ManagedService {

	/**
	 * Indicates whether this action is properly configured which means all
	 * necessary configurations are set. This flag can be checked by the
	 * action methods before executing code.
	 */
	/* default */ static boolean isProperlyConfigured = false;
	
	public MailActionService() {
	}
	
	public void activate() {
	}
	
	public void deactivate() {
		// deallocate Resources here that are no longer needed and 
		// should be reset when activating this binding again
	}

	@Override
	public String getActionClassName() {
		return Mail.class.getCanonicalName();
	}

	@Override
	public Class<?> getActionClass() {
		return Mail.class;
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
			if (StringUtils.isBlank(Mail.hostname) || StringUtils.isBlank(Mail.from)) {
				throw new ConfigurationException("mail", "Parameters mail:hostname and mail:from are mandatory and must be configured. Please check your openhab.cfg!");
			}
			
			// set defaults for optional settings
			if(Mail.port==null) {
				Mail.port = Mail.tls ? 587 : 25;
			}
			
			isProperlyConfigured = true;
		}
	}
	
}
