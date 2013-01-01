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
package org.openhab.binding.networkhealth.internal;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Dictionary;

import org.openhab.binding.networkhealth.NetworkHealthBindingProvider;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.library.types.OnOffType;
import org.openhab.io.net.actions.Ping;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The RefreshService polls all configured hostnames with a configurable 
 * interval and post all values to the internal event bus. The interval is 1 
 * minute by default and can be changed via openhab.cfg. 
 * 
 * @author Thomas.Eichstaedt-Engelen
 * @author Kai Kreuzer
 * @since 0.6.0
 */
public class NetworkHealthBinding extends AbstractActiveBinding<NetworkHealthBindingProvider> implements ManagedService {

	private static final Logger logger = LoggerFactory.getLogger(NetworkHealthBinding.class);
	
	/** the port to use for connecting to a given host (defaults to 5000) */
	private int timeout = 5000;
	
	/** the refresh interval which is used to poll the vitality of the given hosts (defaults to 60000ms) */
	private long refreshInterval = 60000;
	
	
	@Override
	protected String getName() {
		return "NetworkHealth Refresh Service";
	}
	
	@Override
	protected long getRefreshInterval() {
		return refreshInterval;
	}
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	public boolean isProperlyConfigured() {
		return true;
	}
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	public void execute() {
		for (NetworkHealthBindingProvider provider : providers) {
			for (String itemName : provider.getItemNames()) {
				
				String hostname = provider.getHostname(itemName);
				int port = provider.getPort(itemName);

				if (provider.getTimeout(itemName) > 0) {
					timeout = provider.getTimeout(itemName);
				}
				
				boolean success = false;
				
				try {
					success = Ping.checkVitality(hostname, port, timeout);

					logger.debug("established connection [host '{}' port '{}' timeout '{}']", new Object[] {hostname, port, timeout});
				} 
				catch (SocketTimeoutException se) {
					logger.debug("timed out while connecting to host '{}' port '{}' timeout '{}'", new Object[] {hostname, port, timeout});
				}
				catch (IOException ioe) {
					logger.debug("couldn't establish network connection [host '{}' port '{}' timeout '{}']", new Object[] {hostname, port, timeout});
				}
				if(eventPublisher!=null) {
					eventPublisher.postUpdate(itemName, success ? OnOffType.ON : OnOffType.OFF);
				}
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("rawtypes")
	public void updated(Dictionary config) throws ConfigurationException {
		if (config != null) {
			String timeoutString = (String) config.get("timeout");
			if (timeoutString != null && !timeoutString.isEmpty()) {
				timeout = Integer.parseInt(timeoutString);
			}
			
			String refreshIntervalString = (String) config.get("refresh");
			if (refreshIntervalString != null && !refreshIntervalString.isEmpty()) {
				refreshInterval = Long.parseLong(refreshIntervalString);
			}
		}
	}
	
	
}
