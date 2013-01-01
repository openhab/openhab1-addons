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
package org.openhab.io.servicediscovery.internal;

import java.io.IOException;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceInfo;

import org.openhab.io.servicediscovery.DiscoveryService;
import org.openhab.io.servicediscovery.ServiceDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class starts the JmDNS and implements interface to register and unregister services.
 * 
 * @author Victor Belov
 * @since 1.0.0
 *
 */
public class DiscoveryServiceImpl implements DiscoveryService {

	private static Logger logger = LoggerFactory.getLogger(DiscoveryServiceImpl.class);
	private JmDNS jmdns;
	
	public DiscoveryServiceImpl() {
	}
	
	/**
	 * @{inheritDoc}
	 */
	public void registerService(ServiceDescription description) {
		ServiceInfo serviceInfo = ServiceInfo.create(description.serviceType, description.serviceName, description.servicePort,
				0, 0, description.serviceProperties);		
		try {
			logger.info("Registering new service " + description.serviceType + " at port " + 
					String.valueOf(description.servicePort));
			jmdns.registerService(serviceInfo);
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}

	/**
	 * @{inheritDoc}
	 */
	public void unregisterService(ServiceDescription description) {
		ServiceInfo serviceInfo = ServiceInfo.create(description.serviceType, description.serviceName, description.servicePort,
				0, 0, description.serviceProperties);
		logger.info("Unregistering service " + description.serviceType + " at port " + 
				String.valueOf(description.servicePort));
		jmdns.unregisterService(serviceInfo);
	}

	/**
	 * This method unregisters all services from Bonjour/MDNS
	 */
	protected void unregisterAllServices() {
		logger.info("Unregistering all services");
		jmdns.unregisterAllServices();
	}
	
	public void activate() {
		try {
			jmdns = JmDNS.create();
			logger.debug("Discovery service has been started.");		
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}
	
	public void deactivate() {
		unregisterAllServices();
		try {
			jmdns.close();
			logger.debug("Discovery service has been stopped.");
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}

}
