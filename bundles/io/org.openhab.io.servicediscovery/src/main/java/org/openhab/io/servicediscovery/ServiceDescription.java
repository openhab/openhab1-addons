/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
