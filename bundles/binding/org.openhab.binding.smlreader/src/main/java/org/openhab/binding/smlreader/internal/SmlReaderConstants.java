/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.smlreader.internal;

import java.util.regex.Pattern;

/**
 * Constants used in SmlReaderBinding
 * 
 * @author Mathias Gilhuber
 * @since 1.7.0
 */
public final class SmlReaderConstants {

	/** {@link Pattern} which matches a binding configuration */ 
	public static final Pattern BINDING_PATTERN = Pattern.compile("(device)=(.*),(obis)=([0-9]{1,3}-[0-9]{1,3}:[0-9]{1,3}.[0-9]{1,3}.[0-9]{1})(#.*)?");

	/** {@link Pattern} which matches a binding configuration with transformation arguments*/ 
	public static final Pattern BINDING_PATTERN_TRANSFORM = Pattern.compile("(device)=(.*),(obis)=([0-9]{1,3}-[0-9]{1,3}:[0-9]{1,3}.[0-9]{1,3}.[0-9]{1})(#.*)?,(transform)=(JS|XSLT|REGEX)\\((.*)\\)");
	
	/** {@link Pattern} which matches a SmlReaderBinding config */ 
	public static final Pattern CONFIGURATION_PATTERN = Pattern.compile("(.*).serialPort");
	
	/** */
	public static final String CONFIGURATION_KEY_SERIALPORT = "serialPort";

	/** Represents the default serial port if no port is specified in configuration.*/
	public static final String DEFAULT_SERIAL_PORT = "COM1";
	
	/** Obis format*/
	public static final String OBIS_FORMAT = "%d-%d:%d.%d.%d";
}
