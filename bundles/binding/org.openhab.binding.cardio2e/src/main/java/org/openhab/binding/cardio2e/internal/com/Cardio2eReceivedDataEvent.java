/**
 * Copyright (c) 2010-2020 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.binding.cardio2e.internal.com;

import java.util.EventObject;

/**
 * Cardio2e Received Data Event class.
 * 
 * @author Manuel Alberto Guerrero Díaz
 * @Since 1.11.0
 */

public class Cardio2eReceivedDataEvent extends EventObject {
	private static final long serialVersionUID = 1L;
	private String receivedData = null;

	public Cardio2eReceivedDataEvent(Object source, String receivedData) {
		super(source);
		this.receivedData = receivedData;
	}

	public String getReceivedData() {
		return receivedData;
	}
}