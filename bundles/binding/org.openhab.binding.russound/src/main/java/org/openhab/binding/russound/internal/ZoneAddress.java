/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.russound.internal;

public class ZoneAddress extends RussoundBinding {

	private int mController;
	private int mZone;

	public ZoneAddress(int controller, int zone) {
		mController = controller;
		mZone = zone;
	}

	public int getController() {
		return mController;
	}

	public int getZone() {
		return mZone;
	}

	@Override
	public int hashCode() {

		return mZone + 17 * mController;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ZoneAddress) {
			ZoneAddress addy = (ZoneAddress) obj;
			return addy.mZone == this.mZone
					&& addy.mController == this.mController;
		} else
			return false;
	}

	public String toString() {
		return new StringBuilder("Zone Addr-").append("controller: ")
				.append(mController).append(", zone: ").append(mZone)
				.toString();
	}
}
