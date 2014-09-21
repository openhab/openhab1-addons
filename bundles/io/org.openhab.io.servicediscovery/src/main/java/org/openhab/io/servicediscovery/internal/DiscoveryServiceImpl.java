/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.servicediscovery.internal;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Dictionary;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceInfo;

import org.apache.commons.lang.StringUtils;
import org.openhab.io.servicediscovery.DiscoveryService;
import org.openhab.io.servicediscovery.ServiceDescription;
import org.osgi.service.cm.ManagedService;
import org.osgi.service.cm.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class starts the JmDNS and implements interface to register and unregister services.
 * 
 * @author Victor Belov
 * @since 1.0.0
 *
 */
public class DiscoveryServiceImpl implements DiscoveryService, ManagedService {

	private static Logger logger = LoggerFactory.getLogger(DiscoveryServiceImpl.class);
	private JmDNS jmdns;
	
	public DiscoveryServiceImpl() {
	}

	@Override
	public void updated(Dictionary<String, ?> config) throws ConfigurationException {
		try {
			if (config != null) {
				String bindAddress = (String)config.get("bind_address");
				if (bindAddress != null && StringUtils.isNotBlank(bindAddress)) {
					jmdns = JmDNS.create(InetAddress.getByName(bindAddress));
					logger.info("Service Discovery initialization completed (bound to address: {}).", bindAddress);
					return;
				}
			}
			jmdns = JmDNS.create();
			logger.info("Service Discovery initialization completed.");
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}

	/**
	 * @{inheritDoc}
	 */
	public void registerService(ServiceDescription description) {
		ServiceInfo serviceInfo = ServiceInfo.create(description.serviceType, description.serviceName, description.servicePort,
				0, 0, description.serviceProperties);		
		try {
			logger.debug("Registering new service " + description.serviceType + " at port " + 
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
		logger.debug("Unregistering service " + description.serviceType + " at port " + 
				String.valueOf(description.servicePort));
		jmdns.unregisterService(serviceInfo);
	}

	/**
	 * This method unregisters all services from Bonjour/MDNS
	 */
	protected void unregisterAllServices() {
		jmdns.unregisterAllServices();
	}
	
	public void activate() {
		logger.info("mDNS service has been started");
	}
	
	public void deactivate() {
		unregisterAllServices();
		try {
			jmdns.close();
			logger.info("mDNS service has been stopped");
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}

}
