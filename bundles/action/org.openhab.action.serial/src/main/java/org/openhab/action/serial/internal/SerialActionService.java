/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.action.serial.internal;

import org.openhab.core.scriptengine.action.ActionService;
	

/**
 * This class registers an OSGi service for the Serial action.
 * 
 * @author Johannes Engelke <info@johannes-engelke.de>
 * @since 1.6.0
 */
public class SerialActionService implements ActionService {	
	public SerialActionService() {
	}
	
	public void activate() {
	}
	
	public void deactivate() {
		// deallocate Resources here that are no longer needed and 
		// should be reset when activating this binding again
	}

	@Override
	public String getActionClassName() {
		return Serial.class.getCanonicalName();
	}

	@Override
	public Class<?> getActionClass() {
		return Serial.class;
	}
}
