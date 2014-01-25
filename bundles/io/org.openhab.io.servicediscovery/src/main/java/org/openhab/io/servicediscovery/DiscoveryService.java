/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.servicediscovery;


/**
 * This interface defines how to use JmDNS based service discovery
 * to register and unregister services on Bonjour/MDNS
 * 
 * @author Victor Belov
 * @author Kai Kreuzer
 * @since 1.0.0
 */
public interface DiscoveryService {
	
	/**
	 * This method registers a service to be announced through Bonjour/MDNS
	 * 
	 * @param serviceDescription the {@link ServiceDescription} instance with all details to identify the service
	 */
	public void registerService(ServiceDescription description);
	
	/**
	 * This method unregisters a service not to be announced through Bonjour/MDNS
	 * 
	 * @param serviceDescription the {@link ServiceDescription} instance with all details to identify the service
	 */
	public void unregisterService(ServiceDescription description);
	
}
