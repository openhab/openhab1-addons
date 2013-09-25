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
package org.openhab.action.prowl.internal;

import java.util.Dictionary;

import org.apache.commons.lang.StringUtils;
import org.openhab.core.scriptengine.action.ActionService;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
	

/**
 * This class registers an OSGi service for the Prowl action.
 * 
 * @author Kai Kreuzer
 * @since 1.3.0
 */
public class ProwlActionService implements ActionService, ManagedService {

	/**
	 * Indicates whether this action is properly configured which means all
	 * necessary configurations are set. This flag can be checked by the
	 * action methods before executing code.
	 */
	/* default */ static boolean isProperlyConfigured = false;
	
	public ProwlActionService() {
	}
	
	public void activate() {
	}
	
	public void deactivate() {
		// deallocate Resources here that are no longer needed and 
		// should be reset when activating this binding again
	}

	@Override
	public String getActionClassName() {
		return Prowl.class.getCanonicalName();
	}

	@Override
	public Class<?> getActionClass() {
		return Prowl.class;
	}

	@SuppressWarnings("rawtypes")
	public void updated(Dictionary config) throws ConfigurationException {
		if (config!=null) {
			Prowl.url = (String) config.get("url");
			
			Prowl.apiKey = (String) config.get("apikey");
			if (StringUtils.isBlank(Prowl.apiKey)) {
				throw new ConfigurationException("prowl.apikey", "The parameter 'apikey' must be configured. Please refer to your 'openhab.cfg'");
			}
			
			String priorityString = (String) config.get("defaultpriority");
			if (StringUtils.isNotBlank(priorityString)) {
				Prowl.priority = Integer.valueOf(priorityString);
			}
			
			isProperlyConfigured = true;
		}
	}
	
}
