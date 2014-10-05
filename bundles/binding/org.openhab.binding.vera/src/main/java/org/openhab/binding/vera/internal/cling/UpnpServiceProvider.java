/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.vera.internal.cling;

import org.fourthline.cling.UpnpService;
import org.fourthline.cling.UpnpServiceImpl;

/**
 * Static (singleton) provider of the {@link UpnpService}.
 *
 * @author Matthew Bowman
 * @since 1.6.0
 */
public class UpnpServiceProvider {

	/**
	 * The singleton instance.
	 */
	private static UpnpService service;
	
	static {
		service = new UpnpServiceImpl(new JettyUpnpServiceConfiguration());
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				service.shutdown();
			}
		});
	}
	
	/**
	 * Singleton; private constructor.
	 */
	private UpnpServiceProvider() {
	}
	
	/**
	 * Gets the default {@link UpnpService}.
	 * @return the default {@link UpnpService}
	 */
	public static UpnpService getDefaultService() {
		return service;
	}
	
}
