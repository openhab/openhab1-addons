#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
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
package ${artifactId}.internal;

import java.util.Dictionary;

import ${artifactId}.${binding-name}BindingProvider;

import org.apache.commons.lang.StringUtils;
import org.openhab.core.binding.AbstractActiveBinding;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
	

/**
 * Implement this class if you are going create an actively polling service
 * like querying a Website/Device.
 * 
 * @author ${author}
 * @since ${version}
 */
public class ${binding-name}ActiveBinding extends AbstractActiveBinding<${binding-name}BindingProvider> implements ManagedService {

	private static final Logger logger = 
		LoggerFactory.getLogger(${binding-name}ActiveBinding.class);

	private boolean isProperlyConfigured = false;

	/** the refresh interval which is used to poll values from the ${binding-name} server (optional, defaults to 60000ms) */
	private long refreshInterval = 60000;
	
	
	public ${binding-name}ActiveBinding() {
	}
		
	
	public void activate() {
	}
	
	public void deactivate() {
		// deallocate Resources here that are no longer needed and 
		// should be reset when activating this binding again
	}

	
	/**
	 * @{inheritDoc}
	 */
	@Override
	protected long getRefreshInterval() {
		return refreshInterval;
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	protected String getName() {
		return "${binding-name} Refresh Service";
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	public boolean isProperlyConfigured() {
		return isProperlyConfigured;
	}
	
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	protected void execute() {
		// the frequently executed code goes here ...
	}
		
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	public void updated(Dictionary<String, ?> config) throws ConfigurationException {
		if (config != null) {
			String refreshIntervalString = (String) config.get("refresh");
			if (StringUtils.isNotBlank(refreshIntervalString)) {
				refreshInterval = Long.parseLong(refreshIntervalString);
			}
			
			// read further config parameters here ...

			isProperlyConfigured = true;
		}
	}
	

}
