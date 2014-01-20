/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.digitalstrom.internal.config;

/**
 * @author 	Alexander Betker
 * @since 1.3.0
 */
public enum ConsumptionConfig {
	
	ACTIVE_POWER,		// (default) possible for dsid and dsmid
	OUTPUT_CURRENT,		// only for devices (dsid)
	ELECTRIC_METER		// only for meters (dsmid)

}
