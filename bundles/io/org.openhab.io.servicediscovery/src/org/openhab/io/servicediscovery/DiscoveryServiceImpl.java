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
package org.openhab.io.servicediscovery;

import java.io.IOException;
import java.util.Hashtable;

import javax.jmdns.*;

import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class starts the JmDNS and implements interface to register and unregister services.
 * 
 * 
 * @author Victor Belov
 *
 */
public class DiscoveryServiceImpl implements DiscoveryService {

	private static Logger logger = LoggerFactory.getLogger(DiscoveryServiceImpl.class);
	private JmDNS jmdns;
	
	public DiscoveryServiceImpl() {
		logger.info("starting JmDNS");		
		try {
			jmdns = JmDNS.create();
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		logger.info("JmDNS started successfully");
	}
	
	public DiscoveryServiceImpl(final BundleContext context) {
		logger.debug("starting JmDNS");
		try {
			jmdns = JmDNS.create();
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		logger.info("JmDNS started successfully");
	}

	/**
	 * This method registers a service to be announced through Bonjour/MDNS
	 * 
	 * @param serviceType String service type, like "_openhab-server._tcp.local."
	 * @param serviceName String service name, like "openHAB"
	 * @param servicePort Int service port, like 8080
	 * @param uri URI service uri
	 * @param serviceDescription String service description text, like "openHAB REST interface"
	 */
	public void registerService(String serviceType, String serviceName, int servicePort, 
			Hashtable<String, String> serviceProperties) {
		ServiceInfo serviceInfo = ServiceInfo.create(serviceType, serviceName, servicePort,
				0, 0, serviceProperties);		
		try {
			logger.info("registering new service " + serviceType + " at port " + 
					String.valueOf(servicePort));
			jmdns.registerService(serviceInfo);
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}

	/**
	 * This method unregisters a service not to be announced through Bonjour/MDNS
	 * 
	 * @param serviceType String service type, like "_openhab-server._tcp.local."
	 * @param serviceName String service name, like "openHAB"
	 * @param servicePort Int service port, like 8080
	 * @param uri URI service uri
	 * @param serviceDescription String service description text, like "openHAB REST interface"
	 */
	public void unregisterService(String serviceType, String serviceName, int servicePort,
			Hashtable<String, String> serviceProperties) {
		ServiceInfo serviceInfo = ServiceInfo.create(serviceType, serviceName, servicePort,
				0, 0, serviceProperties);
		logger.info("unregistering new service " + serviceType + " at port " + 
				String.valueOf(servicePort));
		jmdns.unregisterService(serviceInfo);
	}

	/**
	 * This method unregisters all services from Bonjour/MDNS
	 * 
	 */
	public void unregisterAllServices() {
		logger.info("unregistering all services");
		jmdns.unregisterAllServices();
	}

	/**
	 * This method unregisters all services from Bonjour/MDNS and clearly shuts down JmDNS
	 * 
	 */
	public void shutdown() {
		logger.info("unregistering all services");
		jmdns.unregisterAllServices();
		logger.info("closing JmDNS service");
		try {
			jmdns.close();
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		logger.info("JmDNS closed");
	}
	
	public void activate() {
		logger.info("Discovery service has been started.");		
	}
	
	public void deactivate() {
		this.shutdown();
		logger.info("Discovery service has been stopped.");
	}

}
