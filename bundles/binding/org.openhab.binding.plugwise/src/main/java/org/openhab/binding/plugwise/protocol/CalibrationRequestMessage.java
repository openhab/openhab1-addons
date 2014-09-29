/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.plugwise.protocol;

/**
 * Circle Calibration request
 *
 * @author Karel Goderis
 * @since 1.1.0
 */
public class CalibrationRequestMessage extends Message {

	public CalibrationRequestMessage(String MAC, String payLoad) {
		super(MAC, payLoad);
		type = MessageType.DEVICE_CALIBRATION_REQUEST;
	}

	@Override
	protected String payLoadToHexString() {
		return "";
	}

	@Override
	protected void parsePayLoad() {
	}

	protected String sequenceNumberToHexString() {
		return "";
	}
	
}
