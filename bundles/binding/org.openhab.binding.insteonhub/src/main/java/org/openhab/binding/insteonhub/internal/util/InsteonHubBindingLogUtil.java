/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.insteonhub.internal.util;

import org.openhab.binding.insteonhub.internal.hardware.InsteonHubProxy;
import org.slf4j.Logger;

/**
 * Utility functions for common log statements
 * 
 * @author Eric Thill
 * @since 1.4.0
 */
public class InsteonHubBindingLogUtil {

	public static void logCommunicationFailure(Logger logger,
			InsteonHubProxy proxy, Throwable t) {
		logger.error(
				"Communication error with Insteon Hub @"
						+ proxy.getConnectionString(), t);
	}

	public static void logCommunicationFailure(Logger logger,
			InsteonHubProxy proxy, String device, Throwable t) {
		logger.warn(
				"Cannot communicate with Insteon Hub @"
						+ proxy.getConnectionString() + " (device:" + device
						+ ")", t);
	}
}
