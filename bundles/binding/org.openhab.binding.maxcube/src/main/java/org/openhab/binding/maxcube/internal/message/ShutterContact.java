/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.maxcube.internal.message;

import java.util.Calendar;

import org.openhab.core.library.types.OpenClosedType;

/**
 * MAX!Cube Shutter contact device.
 * 
 * @author Andreas Heil (info@aheil.de)
 * @since 1.4.0
 */
public class ShutterContact extends Device {

	private OpenClosedType shutterState = null;
	private boolean linkError;
	private boolean panelLocked;
	private boolean gatewayOk;
	private boolean error;
	private boolean valid;

	public ShutterContact(Configuration c) {
		super(c);
	}

	public void setShutterState(OpenClosedType shutterState) {
		this.shutterState = shutterState;
	}

	public OpenClosedType getShutterState() {
		return shutterState;
	}

	@Override
	public DeviceType getType() {
		return DeviceType.ShutterContact;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Calendar getLastUpdate() {
		// TODO Auto-generated method stub
		return null;
	}

	void setLinkError(boolean linkError) {
		this.linkError = linkError;
	}

	void setPanelLocked(boolean panelLock) {
		this.panelLocked = panelLock;
	}

	void setGatewayOk(boolean gatewayOk) {
		this.gatewayOk = gatewayOk;

	}

	void setError(boolean error) {
		this.error = error;
	}

	void setValid(boolean valid) {
		this.valid = valid;
	}
}
