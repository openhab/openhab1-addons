/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2013, openHAB.org <admin@openhab.org>
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
