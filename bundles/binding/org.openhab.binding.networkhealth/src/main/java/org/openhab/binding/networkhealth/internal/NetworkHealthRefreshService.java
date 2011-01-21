/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010, openHAB.org <admin@openhab.org>
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
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.util.Collection;
import java.util.Dictionary;
import java.util.HashSet;

import org.openhab.binding.networkhealth.NetworkHealthBindingProvider;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.library.types.OnOffType;
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
 * @since 0.6.0
 */
public class NetworkHealthRefreshService extends Thread implements ManagedService {

	private static final Logger logger = 
		LoggerFactory.getLogger(NetworkHealthRefreshService.class);
	
	// for interrupting this thread
	private boolean interrupted = false;

	/** to keep track of all binding providers */
	private Collection<NetworkHealthBindingProvider> providers = new HashSet<NetworkHealthBindingProvider>();
	
	private EventPublisher eventPublisher = null;
	
	/** the port to use for connecting to a given host (defaults to 5000) */
	private static int timeout = 5000;
	
	/** the refresh interval which is used to poll the vitality of the given hosts (defaults to 60000ms) */
	private static long refreshInterval = 60000;
	

	public NetworkHealthRefreshService() {
		super("NetworkHealth Refresh Service");
	}
	
	public void activate() {
	}
	
	public void deactivate() {
		setInterrupted(true);
	}
	
	public void setInterrupted(boolean interrupted) {
		this.interrupted = interrupted;
	}
	
	
	public void setEventPublisher(EventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
	}
	
	public void unsetEventPublisher(EventPublisher eventPublisher) {
		this.eventPublisher = null;
	}	
	
	public void addBindingProvider(NetworkHealthBindingProvider provider) {
		this.providers.add(provider);
	}

	public void removeBindingProvider(NetworkHealthBindingProvider provider) {
		this.providers.remove(provider);
		
		// if there are no binding providers there is no need for running
		// the refreshservice any longer
		if (this.providers.size() == 0) {
			setInterrupted(true);
		}
	}	
	
		
	@Override
	public void run() {
		
		logger.info("refresh thread started [refresh interval {}ms]", NetworkHealthRefreshService.refreshInterval);
		
		while (!interrupted) {

			for (NetworkHealthBindingProvider provider : providers) {
				for (String itemName : provider.getItemNames()) {
					
					String hostname = provider.getHostname(itemName);
					int port = provider.getPort(itemName);

					if (provider.getTimeout(itemName) > 0) {
						timeout = provider.getTimeout(itemName);
					}
					
					boolean success = false;
					
					try {
						success = checkReachability(hostname, port, timeout);

						logger.debug("established connection [host '{}' port '{}' timeout '{}']", new Object[] {hostname, port, timeout});
					} 
					catch (SocketTimeoutException se) {
						logger.debug("timed out while connecting to host '{}' port '{}' timeout '{}'", new Object[] {hostname, port, timeout});
					}
					catch (IOException ioe) {
						logger.debug("couldn't establish network connection [host '{}' port '{}' timeout '{}']", new Object[] {hostname, port, timeout});
					}

					eventPublisher.postUpdate(itemName, success ? OnOffType.ON : OnOffType.OFF);
				}
			}
			
			// sleep for a while ...
			pause(NetworkHealthRefreshService.refreshInterval);
		}
	}
	
	/**
	 * Checks the reachability of <code>host</code>. If <code>port</code> '0'
	 * is specified (which is the default when configuring just the host), a
	 * regular ping is issued. If other ports are specified we try open a new
	 * Socket with the given <code>timeout</code>.
	 * 
	 * @param host
	 * @param port
	 * @param timeout
	 * 
	 * @return <code>true</code> when <code>host</code> is reachable on <code>port</code>
	 * within the given <code>timeout</code> and <code>false</code> in all other
	 * cases.
	 * 
	 * @throws IOException
	 * @throws SocketTimeoutException
	 */
	private boolean checkReachability(String host, int port, int timeout) throws IOException, SocketTimeoutException {

		boolean success = false;
		
		if (host != null && timeout > 0) {

			if (port == 0) {
				success = InetAddress.getByName(host).isReachable(timeout);
			}
			else {
				SocketAddress socketAddress = new InetSocketAddress(host, port);
				
				Socket socket = new Socket();
				socket.connect(socketAddress, timeout);
				success = true;
			}

		}
		else {
			logger.warn("Configuration of Host isn't sufficient [Host '{}' Port '{}' Timeout '{}'].", new Object[] {host, port, timeout});
		}
		
		return success;
	}
	

	/**
	 * Pause host polling for the given <code>refreshInterval</code>. Possible
	 * {@link InterruptedException} is logged with no further action.
	 *  
	 * @param refreshInterval 
	 */
	private void pause(long refreshInterval) {
		
		try {
			Thread.sleep(refreshInterval);
		}
		catch (InterruptedException e) {
			logger.error("pausing NetworkHealth-Thread throws exception", e);
		}
	}
	
	
	@Override
	public void updated(Dictionary config) throws ConfigurationException {
		
		if (config != null) {
			String timeoutString = (String) config.get("timeout");
			if (timeoutString != null && !timeoutString.isEmpty()) {
				NetworkHealthRefreshService.timeout = Integer.parseInt(timeoutString);
			}
			
			String refreshIntervalString = (String) config.get("refresh");
			if (refreshIntervalString != null && !refreshIntervalString.isEmpty()) {
				NetworkHealthRefreshService.refreshInterval = Long.parseLong(refreshIntervalString);
			}
		}

		// teichsta: TODO: find the right to place to call start() 
		// and check whether there are any bindingConfigs for this binding
		start();

	}

	/**
	 * @return <code>true</code> if any of the <code>providers</code> contains
	 * a wol-binding
	 */
	private boolean shouldStartRefreshService(Collection<NetworkHealthBindingProvider> providers) {
		boolean activate = false;
		for (NetworkHealthBindingProvider provider : providers) {
			activate |= provider.containsBinding();
		}
		return activate;
	}

	
}
