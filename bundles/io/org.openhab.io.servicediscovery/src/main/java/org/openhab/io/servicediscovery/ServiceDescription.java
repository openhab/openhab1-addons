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
package org.openhab.io.servicediscovery;

import java.util.Hashtable;

/**
 * This is a simple data container to keep all details of a service description together.
 *  
 * @author Kai Kreuzer
 * @since 1.0.0
 *
 */
public class ServiceDescription {

	public String serviceType;
	public String serviceName;
	public int servicePort;
	public Hashtable<String, String> serviceProperties;

	/**
	 * Constructor for a {@link ServiceDescription}, which takes all details as parameters
	 * 
	 * @param serviceType String service type, like "_openhab-server._tcp.local."
	 * @param serviceName String service name, like "openHAB"
	 * @param servicePort Int service port, like 8080
	 * @param serviceProperties Hashtable service props, like url = "/rest"
	 * @param serviceDescription String service description text, like "openHAB REST interface"
	 */
	 public ServiceDescription(String serviceType, String serviceName, int servicePort, 
			Hashtable<String, String> serviceProperties) {
		 this.serviceType = serviceType;
		 this.serviceName = serviceName;
		 this.servicePort = servicePort;
		 this.serviceProperties = serviceProperties;
	 }
	 
}
