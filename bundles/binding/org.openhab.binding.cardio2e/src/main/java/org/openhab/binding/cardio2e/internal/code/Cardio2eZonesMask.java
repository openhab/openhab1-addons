/**
 * Copyright (c) 2010-2017, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.openhab.binding.cardio2e.internal.code;

import java.util.Arrays;

/**
 * Cardio2eZonesMask structured data model
 * 
 * @author Manuel Alberto Guerrero Díaz
 * @Since 1.11.0
 */

public class Cardio2eZonesMask {
	public static final Cardio2eZoneStates CARDIO2E_DEFAULT_NO_DETECTION_ZONE_STATE = Cardio2eZoneStates.NORMAL;
	private Cardio2eZoneStates zonesMask[];

	public Cardio2eZonesMask() {
		zonesMask = new Cardio2eZoneStates[Cardio2eTransaction.CARDIO2E_MAX_SECURITY_ZONE_NUMBER];
		Arrays.fill(zonesMask, CARDIO2E_DEFAULT_NO_DETECTION_ZONE_STATE); // Inits
																			// zones
																			// mask
	}

	public Cardio2eZonesMask(Cardio2eZoneStates zonesMask[]) {
		setZonesMask(zonesMask);
	}

	public Cardio2eZoneStates[] getZonesMask() {
		return zonesMask;
	}

	public void setZonesMask(Cardio2eZoneStates zonesMask[]) {
		if (zonesMask != null) { // Checks no null
			if (zonesMask.length == Cardio2eTransaction.CARDIO2E_MAX_SECURITY_ZONE_NUMBER) { // Checks
																								// array
																								// length
				boolean hasNull = false;
				for (Cardio2eZoneStates s : zonesMask) { // Checks no null array
															// elements
					if (s == null)
						hasNull = true;
				}
				if (!hasNull) { // Data is correct, so we stores it
					this.zonesMask = zonesMask;
				} else {
					throw new IllegalArgumentException(
							"zonesMask contains null elements");
				}
			} else {
				throw new IllegalArgumentException("invalid zonesMask length '"
						+ zonesMask.length + "'");
			}
		} else {
			throw new IllegalArgumentException("zonesMask is null");
		}
	}

	public Cardio2eZoneStates getZoneMask(byte zoneNumber) {
		Cardio2eZoneStates zoneNormalStateMask = null;
		if ((zoneNumber >= Cardio2eTransaction.CARDIO2E_MIN_OBJECT_NUMBER)
				&& (zoneNumber <= Cardio2eTransaction.CARDIO2E_MAX_SECURITY_ZONE_NUMBER)) {
			zoneNormalStateMask = zonesMask[(zoneNumber - 1)];
		} else {
			throw new IllegalArgumentException("invalid zoneNumber '"
					+ zoneNumber + "'");
		}
		return zoneNormalStateMask;
	}

	public void setZoneMask(byte zoneNumber, Cardio2eZoneStates zoneMask) {
		if ((zoneNumber >= Cardio2eTransaction.CARDIO2E_MIN_OBJECT_NUMBER)
				&& (zoneNumber <= Cardio2eTransaction.CARDIO2E_MAX_SECURITY_ZONE_NUMBER)) {
			if (zoneMask != null) { // Checks no null
				this.zonesMask[(zoneNumber - 1)] = zoneMask;
			} else {
				throw new IllegalArgumentException("zoneMask is null");
			}
		} else {
			throw new IllegalArgumentException("invalid zoneNumber '"
					+ zoneNumber + "'");
		}
	}
}
