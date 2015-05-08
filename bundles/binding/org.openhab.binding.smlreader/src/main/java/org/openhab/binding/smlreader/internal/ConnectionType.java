/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

 package org.openhab.binding.smlreader.internal;

/**
 * @author Mathias Gilhuber
 * @since 1.7.0
 */
public enum ConnectionType {
	/**
	 * no connection type specified - only for intialization purposes
	 */
	notConfigured,
	
	/**
	 * The SML device is connected via USB/COM port
	 * */
	serial,

	/**
	 * The SML device is connected via TCP/IP
	 * */
	tsap
}
