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

import java.util.Hashtable;

/**
 * This interface defines how to use JmDNS based service discovery
 * to register and unregister services on Bonjour/MDNS
 * 
 * @author Victor Belov
 *
 */
public interface DiscoveryService {
	/**
	 * This method registers a service to be announced through Bonjour/MDNS
	 * 
	 * @param serviceType String service type, like "_openhab-server._tcp.local."
	 * @param serviceName String service name, like "openHAB"
	 * @param servicePort Int service port, like 8080
	 * @param serviceProperties Hashtable service props, like url = "/rest"
	 * @param serviceDescription String service description text, like "openHAB REST interface"
	 */
	public void registerService(String serviceType, String serviceName, int servicePort, 
			Hashtable<String, String> serviceProperties);
	/**
	 * This method unregisters a service not to be announced through Bonjour/MDNS
	 * 
	 * @param serviceType String service type, like "_openhab-server._tcp.local."
	 * @param serviceName String service name, like "openHAB"
	 * @param servicePort Int service port, like 8080
	 * @param serviceProperties Hashtable service props, like url = "/rest"
	 * @param serviceDescription String service description text, like "openHAB REST interface"
	 */
	public void unregisterService(String serviceType, String serviceName, int servicePort, 
			Hashtable<String, String> serviceProperties);
	/**
	 * This method unregisters all services from Bonjour/MDNS
	 * 
	 */
	public void unregisterAllServices();
	/**
	 * This method unregisters all services from Bonjour/MDNS and clearly shuts down JmDNS
	 * 
	 */
	public void shutdown();
}
