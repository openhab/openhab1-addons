/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.zwave.internal.config;

import org.openhab.binding.zwave.internal.HexToIntegerConverter;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;

/**
 * Implements a commandclass for the XML product database
 * @author Chris Jackson
 * @since 1.4.0
 *
 */
public class ZWaveDbCommandClass {
	@XStreamAlias("id")
	@XStreamConverter(HexToIntegerConverter.class)
	public Integer Id;

	// Endpoint number - if we want to set these options for a specific endpoint
	public Integer endpoint;

	// If we want to remove this class, set to true
	public Boolean remove;

	// IF we want to add this class if it doesn't exist, set to true
	public Boolean add;

	// Sets whether or not get is supported by ZWaveGetCommandClass
	public Boolean isGetSupported;
	
	// Configuration options for meter class
	public Boolean meterCanReset;
	public String meterScale;
	public String meterType;
}
