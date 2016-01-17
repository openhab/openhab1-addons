/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.connectsdk.internal;

import org.openhab.core.binding.BindingConfig;

/**
 * A helper class holding binding specific item configuration.
 * 
 * @author Sebastian Prehn
 * @since 1.8.0
 */
public class ConnectSDKBindingConfig implements BindingConfig {
	/**
	 * IP or DNS name of device.
	 */
	public String device;

	/**
	 * Device class. See bridges for possible values. e.g. VolumeControl
	 */
	public String clazz;
	/**
	 * Property. See bridges for possilbe values. e.g. up
	 */
	public String property;	
}