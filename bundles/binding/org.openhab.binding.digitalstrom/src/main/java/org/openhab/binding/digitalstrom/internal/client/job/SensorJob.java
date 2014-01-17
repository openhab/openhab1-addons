/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.digitalstrom.internal.client.job;

import org.openhab.binding.digitalstrom.internal.client.DigitalSTROMAPI;
import org.openhab.binding.digitalstrom.internal.client.entity.DSID;

/**
 * @author Alexander Betker
 * @author Alex Maier
 * @since 1.3.0
 */
public interface SensorJob {
	public DSID getDsid();
	public void execute(DigitalSTROMAPI digitalSTROM, String token);
	
}
